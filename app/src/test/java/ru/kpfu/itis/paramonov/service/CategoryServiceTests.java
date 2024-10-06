package ru.kpfu.itis.paramonov.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.service.impl.CategoryServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    private CategoryService categoryService;

    @Mock
    private DataSource<Integer, CategoryDto> categoryDataSource;

    @BeforeEach
    public void setUp() {
        categoryService = new CategoryServiceImpl(categoryDataSource);
    }

    @Test
    public void testGet_success() {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        CategoryDto categoryDto2 = new CategoryDto(2, "b", "B");
        when(categoryDataSource.add(1, categoryDto1)).thenReturn(true);
        when(categoryDataSource.get(1)).thenReturn(categoryDto1);
        when(categoryDataSource.add(2, categoryDto2)).thenReturn(true);
        when(categoryDataSource.get(2)).thenReturn(categoryDto2);
        //Act
        categoryService.add(1, "a", "A");
        categoryService.add(2, "b", "B");
        CategoryDto gotten1 = categoryService.get(1);
        CategoryDto gotten2 = categoryService.get(2);

        //Assert
        assertAll(
                () -> assertEquals(categoryDto1, gotten1),
                () -> assertEquals(categoryDto2, gotten2)
        );
        verify(categoryDataSource, times(2)).get(1);
        verify(categoryDataSource, times(2)).get(2);
    }

    @Test
    public void testGet_fail() {
        //Arrange
        when(categoryDataSource.get(isNull())).thenReturn(null);
        when(categoryDataSource.get(anyInt())).thenReturn(null);
        //Act
        CategoryDto gotten1 = categoryService.get(1);
        CategoryDto gotten2 = categoryService.get(null);

        //Assert
        assertAll(
                () -> assertNull(gotten1),
                () -> assertNull(gotten2)
        );
    }

    @Test
    public void testAdd_success() {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        CategoryDto categoryDto2 = new CategoryDto(2, "b", "B");
        when(categoryDataSource.add(1, categoryDto1)).thenReturn(true);
        when(categoryDataSource.get(1)).thenReturn(categoryDto1);
        when(categoryDataSource.add(2, categoryDto2)).thenReturn(true);
        when(categoryDataSource.get(2)).thenReturn(categoryDto2);

        //Act
        CategoryDto categoryDto1Added = categoryService.add(1, "a", "A");
        CategoryDto categoryDto2Added = categoryService.add(2, "b", "B");

        //Assert
        verify(categoryDataSource).add(1, categoryDto1);
        verify(categoryDataSource).add(2, categoryDto2);

        assertAll(
                () -> assertEquals(categoryDto1, categoryDto1Added),
                () -> assertEquals(categoryDto2, categoryDto2Added),
                () -> assertEquals(categoryDto1Added, categoryService.get(1)),
                () -> assertEquals(categoryDto2Added, categoryService.get(2))
        );
    }

    @Test
    public void testAdd_fail() {
        // Arrange
        when(categoryDataSource.add(isNull(), any())).thenReturn(false);

        //Act, Assert
        assertNull(categoryService.add(null, "", ""));
    }

    @Test
    public void testGetAll() {
        //Arrange
        Collection<CategoryDto> datasourceCategories = List.of(
                new CategoryDto(1, "a", "A"),
                new CategoryDto(2, "b", "B")
        );
        when(categoryDataSource.getAll()).thenReturn(datasourceCategories);
        //Act
        Collection<CategoryDto> result = categoryService.getAll();
        //Assert
        assertIterableEquals(datasourceCategories, result);
        verify(categoryDataSource).getAll();
    }

    @Test
    public void testRemove_success() {
        //Arrange
        CategoryDto categoryDto = new CategoryDto(1, "a", "A");
        when(categoryDataSource.remove(1)).thenReturn(categoryDto);
        //Act
        CategoryDto result1 = categoryService.remove(1);
        //Assert
        assertEquals(categoryDto, result1);
    }

    @Test
    public void testRemove_fail() {
        //Arrange
        when(categoryDataSource.remove(anyInt())).thenReturn(null);
        //Act
        CategoryDto result1 = categoryService.remove(1);
        //Assert
        assertNull(result1);
    }

    @Test
    public void testUpdate_success() {
        //Arrange
        CategoryDto categoryDto = new CategoryDto(1, "a", "A");
        AtomicBoolean wasAdded = new AtomicBoolean(false);
        doAnswer(invocationOnMock -> {
            if (wasAdded.get()) {
                return categoryDto;
            } else {
                return null;
            }
        }).when(categoryDataSource).get(1);

        doAnswer(invocationOnMock -> {
            if (!wasAdded.get()) {
                wasAdded.set(true);
            }
            return categoryDto;
        }).when(categoryDataSource).update(eq(1), eq(categoryDto));

        CategoryDto updatedCategoryDto = new CategoryDto(1, "c", "C");
        when(categoryDataSource.update(eq(1), eq(updatedCategoryDto))).thenReturn(updatedCategoryDto);
        //Act
        CategoryDto added = categoryService.update(1, "a", "A");
        CategoryDto updated = categoryService.update(1, "c", "C");
        //Assert
        assertAll(
                () -> assertNotEquals(added, updated),
                () -> assertEquals(categoryDto, added),
                () -> assertEquals(updatedCategoryDto, updated)
        );
        verify(categoryDataSource).update(eq(1), eq(categoryDto));
        verify(categoryDataSource).update(eq(1), eq(updatedCategoryDto));
    }

    @Test
    public void testUpdate_fail() {
        //Arrange
        when(categoryDataSource.update(isNull(), any())).thenReturn(null);
        //Act
        CategoryDto updated = categoryService.update(null, "", "");
        //Assert
        assertNull(updated);
        verify(categoryDataSource).update(isNull(), any());
    }
}
