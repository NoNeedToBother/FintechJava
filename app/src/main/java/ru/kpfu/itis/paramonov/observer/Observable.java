package ru.kpfu.itis.paramonov.observer;

public interface Observable {

    <P> void notifyObservers(P payload);

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

}
