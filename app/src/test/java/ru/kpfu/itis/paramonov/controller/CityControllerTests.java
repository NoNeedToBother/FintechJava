package ru.kpfu.itis.paramonov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kpfu.itis.paramonov.entity.Place;
import ru.kpfu.itis.paramonov.repository.PlaceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        placeRepository.deleteAll();
    }

    @Test
    public void createPlace_successfully_test() throws Exception {
        //Arrange
        String json = """
                {
                    "name": "a",
                    "slug": "a"
                }""";
        //Act, Assert
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place.name").value("a"))
                .andExpect(jsonPath("$.place.slug").value("a"));
    }

    @Test
    public void getPlace_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        //Act, Assert
        mockMvc.perform(get("/api/v1/locations/" + place.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place.name").value("a"))
                .andExpect(jsonPath("$.place.slug").value("a"));
    }

    @Test
    public void getPlace_notFound_test() throws Exception {
        //Act, Assert
        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Place not found"));
    }

    @Test
    public void deletePlace_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        //Act, Assert
        mockMvc.perform(delete("/api/v1/locations/" + place.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place.name").value("a"))
                .andExpect(jsonPath("$.place.slug").value("a"));
        Optional<Place> deleted = placeRepository.findById(place.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    public void deletePlace_notFound_test() throws Exception {
        //Act, Assert
        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Place not found"));
    }

    @Test
    public void updatePlace_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        String json = """
                {
                    "name": "b"
                }
                """;
        //Act, Assert
        mockMvc.perform(put("/api/v1/locations/" + place.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place.name").value("b"))
                .andExpect(jsonPath("$.place.slug").value(place.getSlug()));
        Optional<Place> databasePlace = placeRepository.findById(place.getId());
        assertAll(
                () -> assertTrue(databasePlace.isPresent()),
                () -> assertEquals("a", databasePlace.get().getSlug()),
                () -> assertEquals("b", databasePlace.get().getName())
        );
    }

    @Test
    public void updatePlace_notFound_test() throws Exception {
        //Arrange
        String json = """
                {
                    "name": "b"
                }""";
        //Act, Assert
        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Place not found"));
    }
}
