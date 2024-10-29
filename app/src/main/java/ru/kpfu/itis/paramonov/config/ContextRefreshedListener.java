package ru.kpfu.itis.paramonov.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.configuration.time.LogTime;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.service.impl.KudaGoApiServiceImpl;

import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final KudaGoApiServiceImpl kudaGoApiService;

    private final DataSource<Integer, CategoryDto> categoryDataSource;

    private final DataSource<String, CityDto> cityDataSource;

    private final KudaGoApiConfigurationProperties kudaGoConfig;

    @Qualifier("kudago_api_data_initializer")
    @Autowired
    private ExecutorService dataInitializer;

    @Qualifier("kudago_api_data_scheduler")
    @Autowired
    private ScheduledExecutorService dataScheduler;

    @Override
    @LogTime
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var duration = kudaGoConfig.getExecutorsConfig().getDataSchedulerDuration();
        dataScheduler.scheduleAtFixedRate(this::getData, 0, duration.getSeconds(), TimeUnit.SECONDS);
    }

    @LogTime
    private void getData() {
        log.info("Begin gathering data from KudaGo API");
        var countDownLatch = new CountDownLatch(2);
        var start = System.currentTimeMillis();

        dataInitializer.submit(() -> {
            var cities = kudaGoApiService.getAllCities()
                    .block();
            cities.forEach(dto -> {
                cityDataSource.add(dto.getSlug(), dto);
                log.info("City was added: {}", dto);
            });
            log.info("City data source is initialized");
            countDownLatch.countDown();
        });

        dataInitializer.submit(() -> {
            var categories = kudaGoApiService.getAllCategories()
                    .block();
            categories.forEach(dto -> {
                categoryDataSource.add(dto.getId(), dto);
                log.info("Category was added: {}", dto);
            });
            log.info("Category data source is initialized");
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
            var finish = System.currentTimeMillis();
            log.info("Finished gathering data from KudaGo API, took {} ms", finish - start);
        } catch (InterruptedException e) {
            log.error("Data initializer thread was interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
