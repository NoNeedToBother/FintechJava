package ru.kpfu.itis.paramonov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kpfu.itis.paramonov.list.impl.CustomLinkedListImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListImplTest {

    private CustomLinkedListImpl<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new CustomLinkedListImpl<>();
    }

    @Test
    public void testAddGetAndContains() {
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertFalse(list.contains(1));
        list.add(1);
        assertTrue(list.contains(1));
        assertEquals(1, list.get(0));
        list.add(2);
        assertEquals(2, list.get(1));
        assertTrue(list.contains(2));
    }

    @Test
    public void testRemove() {
        list.add(0);
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(2));
        list.add(2);
        Integer removed = list.remove(2);
        assertEquals(2, removed);
        assertFalse(list.contains(2));
        list.remove(0);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void testAddAll() {
        list.add(100);
        List<Integer> from = List.of(1, 2, 3, 4, 5);
        list.addAll(from);
        assertEquals(from.size() + 1, list.size());
        assertEquals(list.get(0), 100);
        assertEquals(list.get(list.size() - 1), 5);
    }
}
