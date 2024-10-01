package ru.kpfu.itis.paramonov.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.configuration.time.LogTime;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.service.impl.KudaGoApiServiceImpl;

@Component
@AllArgsConstructor
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private KudaGoApiServiceImpl kudaGoApiService;

    private DataSource<Integer, CategoryDto> categoryDataSource;

    private DataSource<String, CityDto> cityDataSource;

    @Override
    @LogTime
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Begin gathering data from KudaGo API");
        kudaGoApiService.getAllCategories()
                .doOnNext(response -> {
                    response.forEach(dto -> {
                        categoryDataSource.add(dto.getId(), dto);
                        log.info("Category was added: {}", dto);
                    });
                    log.info("Category data source is initialized");
                })
                .subscribe();

        kudaGoApiService.getAllCities()
                .doOnNext(response -> {
                    response.forEach(dto -> {
                        cityDataSource.add(dto.getSlug(), dto);
                        log.info("City was added: {}", dto);
                    });
                    log.info("City data source is initialized");
                })
                .subscribe();
    }
}
