package ru.kpfu.itis.paramonov.list;

import java.util.List;

public interface CustomLinkedList<E> {

    void add(E elem);

    E get(int index);

    E remove(int index);

    boolean contains(E elem);

    void addAll(List<? extends E> from);

    int size();
}
