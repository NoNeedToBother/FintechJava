package ru.kpfu.itis.paramonov.commands.kudago;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.observer.Observable;
import ru.kpfu.itis.paramonov.observer.Observer;
import ru.kpfu.itis.paramonov.service.KudaGoApiService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoCommandReceiver implements Observable {

    private final KudaGoApiService kudaGoApiService;

    private final List<Observer> observers = new ArrayList<>();

    public void getCities() {
        var cities = kudaGoApiService.getAllCities()
                .block();
        notifyObservers(cities);
        log.info("City data source is initialized");
    }

    public void getCategories() {
        var categories = kudaGoApiService.getAllCategories()
                .block();
        notifyObservers(categories);
        log.info("Category data source is initialized");
    }

    @Override
    public <P> void notifyObservers(P payload) {
        observers.forEach(observer -> {
            if (observer.supports(payload)) {
                observer.handle(payload);
            }
        });
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.add(observer);
    }
}
