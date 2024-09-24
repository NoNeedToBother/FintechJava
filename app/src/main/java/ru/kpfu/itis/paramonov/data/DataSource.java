package ru.kpfu.itis.paramonov.data;

import ru.kpfu.itis.paramonov.exception.IdExistsException;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class DataSource<ID, V> {

    private ConcurrentHashMap<ID, V> values = new ConcurrentHashMap<>();

    public V get(ID id) {
        if (id == null) {
            return null;
        }
        return values.get(id);
    }

    public Collection<V> getAll() {
        return values.values();
    }

    public boolean add(ID id, V value) {
        if (values.containsKey(id)) {
            throw new IdExistsException("Value with this id already exists");
        }
        try {
            values.put(id, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public V remove(ID id) {
        if (id == null) {
            return null;
        }
        return values.remove(id);
    }

    public V update(ID id, V value) {
        if (id == null) {
            return null;
        }
        values.put(id, value);
        return values.get(id);
    }

}