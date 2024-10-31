package ru.kpfu.itis.paramonov.memento;

public interface Memento<S> {

    S getState();

    void setState(S state);
}
