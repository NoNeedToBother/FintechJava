package ru.kpfu.itis.paramonov.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.service.CategoryService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetAll() throws Exception {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        CategoryDto categoryDto2 = new CategoryDto(2, "b", "B");
        when(categoryService.getAll()).thenReturn(List.of(categoryDto1, categoryDto2));

        //Act
        mockMvc.perform(get("/api/v1/places/categories"))
                //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories.length()").value(2))
                .andExpect(jsonPath("$.categories[0].id").value(categoryDto1.getId()))
                .andExpect(jsonPath("$.categories[0].slug").value(categoryDto1.getSlug()))
                .andExpect(jsonPath("$.categories[0].name").value(categoryDto1.getName()))
                .andExpect(jsonPath("$.categories[1].id").value(categoryDto2.getId()))
                .andExpect(jsonPath("$.categories[1].slug").value(categoryDto2.getSlug()))
                .andExpect(jsonPath("$.categories[1].name").value(categoryDto2.getName()));
    }

    @Test
    public void testGet() throws Exception {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        CategoryDto categoryDto2 = new CategoryDto(2, "b", "B");
        when(categoryService.get(1)).thenReturn(categoryDto1);
        when(categoryService.get(2)).thenReturn(categoryDto2);

        //Act
        ResultActions result1 = mockMvc.perform(get("/api/v1/places/categories/1"));
        ResultActions result2 = mockMvc.perform(get("/api/v1/places/categories/2"));
        //Assert
        result1.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category.id").value(categoryDto1.getId()))
                .andExpect(jsonPath("$.category.slug").value(categoryDto1.getSlug()))
                .andExpect(jsonPath("$.category.name").value(categoryDto1.getName()));
        result2.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category.id").value(categoryDto2.getId()))
                .andExpect(jsonPath("$.category.slug").value(categoryDto2.getSlug()))
                .andExpect(jsonPath("$.category.name").value(categoryDto2.getName()));
    }

    @Test
    public void testAdd() throws Exception {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        when(categoryService.add(1, "a", "A")).thenReturn(categoryDto1);

        //Act
        ResultActions result1 = mockMvc.perform(post("/api/v1/places/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"slug\":\"a\",\"name\":\"A\"}"));
        //Assert
        result1.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category.id").value(categoryDto1.getId()))
                .andExpect(jsonPath("$.category.slug").value(categoryDto1.getSlug()))
                .andExpect(jsonPath("$.category.name").value(categoryDto1.getName()));
    }

    @Test
    public void testDelete() throws Exception {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        when(categoryService.remove(1)).thenReturn(categoryDto1);

        //Act
        ResultActions result1 = mockMvc.perform(delete("/api/v1/places/categories/1"));
        //Assert
        result1.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category.id").value(categoryDto1.getId()))
                .andExpect(jsonPath("$.category.slug").value(categoryDto1.getSlug()))
                .andExpect(jsonPath("$.category.name").value(categoryDto1.getName()));
    }

    @Test
    public void testPut() throws Exception {
        //Arrange
        CategoryDto categoryDto1 = new CategoryDto(1, "a", "A");
        CategoryDto categoryDto2 = new CategoryDto(1, "b", "B");
        when(categoryService.update(1, "a", "A")).thenReturn(categoryDto1);
        when(categoryService.update(1, "b", "B")).thenReturn(categoryDto2);

        //Act
        ResultActions result1 = mockMvc.perform(put("/api/v1/places/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"slug\":\"a\",\"name\":\"A\"}"));
        ResultActions result2 = mockMvc.perform(put("/api/v1/places/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"slug\":\"b\",\"name\":\"B\"}"));
        //Assert
        result1.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category.id").value(categoryDto1.getId()))
                .andExpect(jsonPath("$.category.slug").value(categoryDto1.getSlug()))
                .andExpect(jsonPath("$.category.name").value(categoryDto1.getName()));

        result2.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.category.id").value(categoryDto2.getId()))
                .andExpect(jsonPath("$.category.slug").value(categoryDto2.getSlug()))
                .andExpect(jsonPath("$.category.name").value(categoryDto2.getName()));
    }
}
