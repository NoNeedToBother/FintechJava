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
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.service.CityService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CityController.class)
public class CityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Test
    public void testGetAll() throws Exception {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        CityDto cityDto2 = new CityDto("b", "B");
        when(cityService.getAll()).thenReturn(List.of(cityDto1, cityDto2));

        //Act
        mockMvc.perform(get("/api/v1/locations"))
        //Assert
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cities").isArray())
                .andExpect(jsonPath("$.cities.length()").value(2))
                .andExpect(jsonPath("$.cities[0].slug").value(cityDto1.getSlug()))
                .andExpect(jsonPath("$.cities[0].name").value(cityDto1.getName()))
                .andExpect(jsonPath("$.cities[1].slug").value(cityDto2.getSlug()))
                .andExpect(jsonPath("$.cities[1].name").value(cityDto2.getName()));
    }

    @Test
    public void testGet() throws Exception {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        CityDto cityDto2 = new CityDto("b", "B");
        when(cityService.get("a")).thenReturn(cityDto1);
        when(cityService.get("b")).thenReturn(cityDto2);

        //Act
        ResultActions result1 = mockMvc.perform(get("/api/v1/locations/a"));
        ResultActions result2 = mockMvc.perform(get("/api/v1/locations/b"));
        //Assert
        result1.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city.slug").value(cityDto1.getSlug()))
                .andExpect(jsonPath("$.city.name").value(cityDto1.getName()));
        result2.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city.slug").value(cityDto2.getSlug()))
                .andExpect(jsonPath("$.city.name").value(cityDto2.getName()));
    }

    @Test
    public void testAdd() throws Exception {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        when(cityService.add("a", "A")).thenReturn(cityDto1);

        //Act
        ResultActions result1 = mockMvc.perform(post("/api/v1/locations/a")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\"}"));
        //Assert
        result1.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city.slug").value(cityDto1.getSlug()))
                .andExpect(jsonPath("$.city.name").value(cityDto1.getName()));
    }

    @Test
    public void testDelete() throws Exception {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        when(cityService.remove("a")).thenReturn(cityDto1);

        //Act
        ResultActions result1 = mockMvc.perform(delete("/api/v1/locations/a"));
        //Assert
        result1.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city.slug").value(cityDto1.getSlug()))
                .andExpect(jsonPath("$.city.name").value(cityDto1.getName()));
    }

    @Test
    public void testPut() throws Exception {
        //Arrange
        CityDto cityDto1 = new CityDto("a", "A");
        CityDto cityDto2 = new CityDto("a", "B");
        when(cityService.update("a", "A")).thenReturn(cityDto1);
        when(cityService.update("a", "B")).thenReturn(cityDto2);

        //Act
        ResultActions result1 = mockMvc.perform(put("/api/v1/locations/a")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\"}"));
        ResultActions result2 = mockMvc.perform(put("/api/v1/locations/a")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"B\"}"));
        //Assert
        result1.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city.slug").value(cityDto1.getSlug()))
                .andExpect(jsonPath("$.city.name").value(cityDto1.getName()));

        result2.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city.slug").value(cityDto2.getSlug()))
                .andExpect(jsonPath("$.city.name").value(cityDto2.getName()));
    }
}
