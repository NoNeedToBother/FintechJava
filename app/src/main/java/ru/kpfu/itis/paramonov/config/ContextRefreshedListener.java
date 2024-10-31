package ru.kpfu.itis.paramonov.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.commands.CommandInvoker;
import ru.kpfu.itis.paramonov.commands.kudago.GetCategoriesCommand;
import ru.kpfu.itis.paramonov.commands.kudago.GetCitiesCommand;
import ru.kpfu.itis.paramonov.commands.kudago.KudaGoCommandReceiver;
import ru.kpfu.itis.paramonov.configuration.time.LogTime;
import ru.kpfu.itis.paramonov.observer.impl.KudaGoCategoryDataObserver;
import ru.kpfu.itis.paramonov.observer.impl.KudaGoCityDataObserver;

import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final CommandInvoker commandInvoker;

    private final GetCategoriesCommand getCategoriesCommand;

    private final GetCitiesCommand getCitiesCommand;

    private final KudaGoApiConfigurationProperties kudaGoConfig;

    private final KudaGoCategoryDataObserver kudaGoCategoryDataObserver;

    private final KudaGoCityDataObserver kudaGoCityDataObserver;

    private final KudaGoCommandReceiver kudaGoCommandReceiver;

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
        commandInvoker.register("getCities", getCitiesCommand);
        commandInvoker.register("getCategories", getCategoriesCommand);
        kudaGoCommandReceiver.addObserver(kudaGoCategoryDataObserver);
        kudaGoCommandReceiver.addObserver(kudaGoCityDataObserver);

        dataScheduler.scheduleAtFixedRate(this::getData, 0, duration.getSeconds(), TimeUnit.SECONDS);
    }

    @LogTime
    private void getData() {
        log.info("Begin gathering data from KudaGo API");
        var countDownLatch = new CountDownLatch(2);

        var start = System.currentTimeMillis();

        dataInitializer.submit(() -> {
            commandInvoker.execute("getCities");
            countDownLatch.countDown();
        });

        dataInitializer.submit(() -> {
            commandInvoker.execute("getCategories");
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
