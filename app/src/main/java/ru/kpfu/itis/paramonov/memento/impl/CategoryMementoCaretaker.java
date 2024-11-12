package ru.kpfu.itis.paramonov.memento.impl;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.memento.Caretaker;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CategoryMementoCaretaker implements Caretaker<Integer, CategoryMemento> {

    private final ConcurrentHashMap<Integer, LinkedList<CategoryMemento>> categoryMementos = new ConcurrentHashMap<>();

    @Override
    public void add(Integer id, CategoryMemento memento) {
        var mementos = categoryMementos.get(id);
        if (mementos != null) {
            mementos.addLast(memento);
        } else {
            var list = new LinkedList<CategoryMemento>();
            list.addLast(memento);
            categoryMementos.put(id, list);
        }
    }

    @Override
    public List<CategoryMemento> getHistory(Integer id) {
        return categoryMementos.get(id);
    }
}
