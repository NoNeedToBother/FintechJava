package ru.itis.kpfu.paramonov.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.exception.IdExistsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatasourceTests {

    private DataSource<Integer, Integer> dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = new DataSource<>();
    }

    @Test
    public void testAdd_success() {
        //Arrange
        Integer value1 = 1;
        Integer value2 = 2;
        //Act
        boolean result1 = dataSource.add(1, value1);
        boolean result2 = dataSource.add(2, value2);

        //Assert
        assertAll(
                () -> assertTrue(result1),
                () -> assertTrue(result2),
                () -> assertEquals(dataSource.get(1), value1),
                () -> assertEquals(dataSource.get(2), value2)
        );
    }

    @Test
    public void testAdd_fail() {
        //Arrange
        Integer value1 = 1;
        Integer value2 = 2;
        Integer failValue = 100500;
        //Act
        boolean result1 = dataSource.add(null, value1);
        dataSource.add(2, value2);

        //Assert
        assertFalse(result1);
        Exception ex = assertThrows(IdExistsException.class, () -> dataSource.add(2, failValue));
        assertEquals("Value with this id already exists", ex.getMessage());
    }

    @Test
    public void testGet_success() {
        //Arrange
        Integer value1 = 1;
        Integer value2 = 2;
        Integer value3 = 3;
        dataSource.add(1, value1);
        dataSource.add(2, value2);
        dataSource.add(3, value3);

        //Act, Assert
        assertAll(
                () -> assertEquals(value1, dataSource.get(1)),
                () -> assertEquals(value2, dataSource.get(2)),
                () -> assertEquals(value3, dataSource.get(3))
        );
    }

    @Test
    public void testGet_fail() {
        //Act
        Integer result1 = dataSource.get(null);
        Integer result2 = dataSource.get(1);

        //Assert
        assertAll(
                () -> assertNull(result1),
                () -> assertNull(result2)
        );
    }

    @Test
    public void testGetAll() {
        //Arrange
        Collection<Integer> values1 = List.of(1, 2, 3, 4);
        values1.forEach(value -> dataSource.add(value, value));

        //Act
        Collection<Integer> collection1 = new ArrayList<>(dataSource.getAll());
        dataSource.add(5, 5);
        Collection<Integer> values2 = new ArrayList<>(values1);
        values2.add(5);
        Collection<Integer> collection2 = new ArrayList<>(dataSource.getAll());

        //Assert
        assertAll(
                () -> assertIterableEquals(values1, collection1),
                () -> assertIterableEquals(values2, collection2)
        );
    }

    @Test
    public void testRemove_success() {
        //Arrange
        Integer value1 = 1;
        Integer value2 = 2;
        dataSource.add(1, value1);
        dataSource.add(2, value2);

        //Act
        Integer size1 = dataSource.getAll().size();
        Integer removed1 = dataSource.remove(1);
        Integer size2 = dataSource.getAll().size();
        Integer removed2 = dataSource.remove(2);
        Integer size3 = dataSource.getAll().size();

        //Assert
        assertAll(
                () -> assertEquals(2, size1),
                () -> assertEquals(1, size2),
                () -> assertEquals(0, size3),
                () -> assertEquals(value1, removed1),
                () -> assertEquals(value2, removed2)
        );
    }

    @Test
    public void testRemove_fail() {
        //Arrange
        Integer value1 = 1;
        dataSource.add(1, value1);

        //Act
        Integer size1 = dataSource.getAll().size();
        Integer removed1 = dataSource.remove(null);
        Integer removed2 = dataSource.remove(2);
        Integer size2 = dataSource.getAll().size();

        //Assert
        assertAll(
                () -> assertEquals(1, size1),
                () -> assertEquals(size1, size2),
                () -> assertNull(removed1),
                () -> assertNull(removed2)
        );
    }

    @Test
    public void testUpdate_success() {
        //Arrange
        Integer value1 = 1;
        dataSource.update(1, value1);
        Integer size1 = dataSource.getAll().size();

        //Act
        Integer updated1 = dataSource.update(1, 2);
        Integer size2 = dataSource.getAll().size();
        dataSource.update(2, 2);
        Integer size3 = dataSource.getAll().size();


        //Assert
        assertAll(
                () -> assertEquals(size1, size2),
                () -> assertNotEquals(value1, updated1),
                () -> assertNotEquals(size2, size3),
                () -> assertEquals(updated1, dataSource.get(1))
        );
    }

    @Test
    public void testUpdate_fail() {
        //Act
        Integer updated = dataSource.update(null, null);

        //Assert
        assertNull(updated);
    }
}
