package ru.kpfu.itis.paramonov.memento.impl;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.memento.Caretaker;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CityMementoCaretaker implements Caretaker<String, CityMemento> {

    private final ConcurrentHashMap<String, LinkedList<CityMemento>> cityMementos = new ConcurrentHashMap<>();

    @Override
    public void add(String id, CityMemento memento) {
        var mementos = cityMementos.get(id);
        if (mementos != null) {
            mementos.addLast(memento);
        } else {
            var list = new LinkedList<CityMemento>();
            list.addLast(memento);
            cityMementos.put(id, list);
        }
    }

    @Override
    public List<CityMemento> getHistory(String slug) {
        return cityMementos.get(slug);
    }
}
