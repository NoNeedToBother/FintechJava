package ru.kpfu.itis.paramonov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.service.impl.CityServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests {

    private CityService cityService;

    @Mock
    private DataSource<String, CityDto> cityDataSource;

    @BeforeEach
    public void setUp() {
        cityService = new CityServiceImpl(cityDataSource);
    }

    @Test
    public void testGet_success() {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        CityDto cityDto2 = new CityDto("b", "B");
        when(cityDataSource.add("a", cityDto1)).thenReturn(true);
        when(cityDataSource.get("a")).thenReturn(cityDto1);
        when(cityDataSource.add("b", cityDto2)).thenReturn(true);
        when(cityDataSource.get("b")).thenReturn(cityDto2);
        //Act
        cityService.add("a", "A");
        cityService.add("b", "B");
        CityDto gotten1 = cityService.get("a");
        CityDto gotten2 = cityService.get("b");

        //Assert
        assertAll(
                () -> assertEquals(cityDto1, gotten1),
                () -> assertEquals(cityDto2, gotten2)
        );
        verify(cityDataSource, times(2)).get("a");
        verify(cityDataSource, times(2)).get("b");
    }

    @Test
    public void testGet_fail() {
        //Arrange
        when(cityDataSource.get(isNull())).thenReturn(null);
        when(cityDataSource.get(anyString())).thenReturn(null);
        //Act
        CityDto gotten1 = cityService.get("a");
        CityDto gotten2 = cityService.get(null);

        //Assert
        assertAll(
                () -> assertNull(gotten1),
                () -> assertNull(gotten2)
        );
    }

    @Test
    public void testAdd_success() {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        CityDto cityDto2 = new CityDto("b", "B");
        when(cityDataSource.add("a", cityDto1)).thenReturn(true);
        when(cityDataSource.get("a")).thenReturn(cityDto1);
        when(cityDataSource.add("b", cityDto2)).thenReturn(true);
        when(cityDataSource.get("b")).thenReturn(cityDto2);

        //Act
        CityDto cityDto1Added = cityService.add("a", "A");
        CityDto cityDto2Added = cityService.add("b", "B");

        //Assert
        verify(cityDataSource).add("a", cityDto1);
        verify(cityDataSource).add("b", cityDto2);

        assertAll(
                () -> assertEquals(cityDto1, cityDto1Added),
                () -> assertEquals(cityDto2, cityDto2Added),
                () -> assertEquals(cityDto1Added, cityService.get("a")),
                () -> assertEquals(cityDto2Added, cityService.get("b"))
        );
    }

    @Test
    public void testAdd_fail() {
        // Arrange
        when(cityDataSource.add(isNull(), any())).thenReturn(false);

        //Act, Assert
        assertNull(cityService.add(null, ""));
    }

    @Test
    public void testGetAll() {
        //Arrange
        Collection<CityDto> datasourceCities = List.of(
                new CityDto("a", "A"),
                new CityDto("b", "B")
        );
        when(cityDataSource.getAll()).thenReturn(datasourceCities);
        //Act
        Collection<CityDto> result = cityService.getAll();
        //Assert
        assertIterableEquals(datasourceCities, result);
        verify(cityDataSource).getAll();
    }

    @Test
    public void testRemove_success() {
        //Arrange
        CityDto cityDto = new CityDto("a", "A");
        when(cityDataSource.remove("a")).thenReturn(cityDto);
        //Act
        CityDto result = cityService.remove("a");
        //Assert
        assertEquals(cityDto, result);
    }

    @Test
    public void testRemove_fail() {
        //Arrange
        when(cityDataSource.remove(anyString())).thenReturn(null);
        //Act
        CityDto result = cityService.remove("a");
        //Assert
        assertNull(result);
    }

    @Test
    public void testUpdate_success() {
        //Arrange
        CityDto cityDto = new CityDto("a", "A");
        AtomicBoolean wasAdded = new AtomicBoolean(false);
        doAnswer(invocationOnMock -> {
            if (wasAdded.get()) {
                return cityDto;
            } else {
                return null;
            }
        }).when(cityDataSource).get("a");

        doAnswer(invocationOnMock -> {
            if (!wasAdded.get()) {
                wasAdded.set(true);
            }
            return cityDto;
        }).when(cityDataSource).update(eq("a"), eq(cityDto));

        CityDto updatedCityDto = new CityDto("a", "C");
        when(cityDataSource.update(eq("a"), eq(updatedCityDto))).thenReturn(updatedCityDto);
        //Act
        CityDto added = cityService.update("a", "A");
        CityDto updated = cityService.update("a", "C");
        //Assert
        assertAll(
                () -> assertNotEquals(added, updated),
                () -> assertEquals(cityDto, added),
                () -> assertEquals(updatedCityDto, updated)
        );
        verify(cityDataSource).update(eq("a"), eq(cityDto));
        verify(cityDataSource).update(eq("a"), eq(updatedCityDto));
    }

    @Test
    public void testUpdate_fail() {
        //Arrange
        when(cityDataSource.update(isNull(), any())).thenReturn(null);
        //Act
        CityDto updated = cityService.update(null, "");
        //Assert
        assertNull(updated);
        verify(cityDataSource).update(isNull(), any());
    }
}

