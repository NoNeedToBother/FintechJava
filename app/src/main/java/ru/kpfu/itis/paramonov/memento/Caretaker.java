package ru.kpfu.itis.paramonov.memento;

import java.util.List;

public interface Caretaker<ID, M extends Memento<?>> {

    void add(ID id, M memento);

    List<M> getHistory(ID id);
}
