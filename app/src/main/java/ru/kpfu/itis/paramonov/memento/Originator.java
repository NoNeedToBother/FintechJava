package ru.kpfu.itis.paramonov.memento;

public interface Originator<M extends Memento<?>> {

    void setMemento(M memento);

    M createMemento();
}
