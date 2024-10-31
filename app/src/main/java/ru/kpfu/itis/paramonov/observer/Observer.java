package ru.kpfu.itis.paramonov.observer;

public interface Observer {

    <P> void handle(P payload);

    <P> boolean supports(P payload);

}
