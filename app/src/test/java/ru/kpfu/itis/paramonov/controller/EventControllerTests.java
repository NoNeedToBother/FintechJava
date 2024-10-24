package ru.kpfu.itis.paramonov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.kpfu.itis.paramonov.entity.Event;
import ru.kpfu.itis.paramonov.entity.Place;
import ru.kpfu.itis.paramonov.repository.EventRepository;
import ru.kpfu.itis.paramonov.repository.PlaceRepository;

import java.sql.Timestamp;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class EventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        eventRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    public void createEvent_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        String json = "{" +
                "    \"description\": \"a\",\n" +
                "    \"name\": \"a\",\n" +
                "    \"price\": 1.0,\n" +
                "    \"date\": \"2000-01-01 12:00:00\",\n" +
                "    \"placeId\":" + place.getId() + "\n" +
                "}";
        //Act, Assert
        mockMvc.perform(post("/api/v1/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("a"))
                .andExpect(jsonPath("$.description").value("a"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.place.id").value(place.getId()))
                .andExpect(jsonPath("$.date").value("2000-01-01T12:00:00"));
    }

    @Test
    public void createEvent_placeDoesNotExist_test() throws Exception {
        //Arrange
        String json = """
                {
                    "price": 1.0,
                    "placeId": 1,
                    "name": "a",
                    "description": "a",
                    "date": "2000-01-01 12:00:00"
                }
                """;
        //Act, Assert
        mockMvc.perform(post("/api/v1/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Place id does not exist"));
    }

    @Test
    public void getEvent_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        Event event = Event.builder()
                .price(1.0)
                .place(place)
                .name("a")
                .description("a")
                .date(Timestamp.valueOf("2000-01-01 12:00:00"))
                .build();
        eventRepository.save(event);
        //Act, Assert
        mockMvc.perform(get("/api/v1/event/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("a"))
                .andExpect(jsonPath("$.description").value("a"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.place.id").value(place.getId()))
                .andExpect(jsonPath("$.id").value(event.getId()))
                .andExpect(jsonPath("$.date").value("2000-01-01T12:00:00"));
    }

    @Test
    public void getEvent_notFound_test() throws Exception {
        //Act, Assert
        mockMvc.perform(get("/api/v1/event/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Event was not found"));
    }

    @Test
    public void deleteEvent_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        Event event = Event.builder()
                .price(1.0)
                .place(place)
                .name("a")
                .description("a")
                .date(Timestamp.valueOf("2000-01-01 12:00:00"))
                .build();
        event = eventRepository.save(event);
        //Act, Assert
        mockMvc.perform(delete("/api/v1/event/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("a"))
                .andExpect(jsonPath("$.description").value("a"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.place.id").value(place.getId()))
                .andExpect(jsonPath("$.id").value(event.getId()))
                .andExpect(jsonPath("$.date").value("2000-01-01T12:00:00"));
        Optional<Event> deleted = eventRepository.findById(event.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    public void deleteEvent_notFound_test() throws Exception {
        //Act, Assert
        mockMvc.perform(delete("/api/v1/event/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Event was not found"));
    }

    @Test
    public void updateEvent_successfully_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        Event previous = Event.builder()
                .name("a")
                .description("a")
                .price(1.0)
                .date(Timestamp.valueOf("2000-01-01 12:00:00"))
                .place(place)
                .build();
        previous = eventRepository.save(previous);
        String json = """
                {
                    "name": "b",
                    "price": 2.0
                }
                """;
        //Act, Assert
        mockMvc.perform(put("/api/v1/event/" + previous.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("b"))
                .andExpect(jsonPath("$.description").value(previous.getDescription()))
                .andExpect(jsonPath("$.price").value(2.0))
                .andExpect(jsonPath("$.place.id").value(place.getId()))
                .andExpect(jsonPath("$.id").value(previous.getId()))
                .andExpect(jsonPath("$.date").value("2000-01-01T12:00:00"));
        Optional<Event> databaseEvent = eventRepository.findById(previous.getId());
        assertAll(
                () -> assertTrue(databaseEvent.isPresent()),
                () -> assertEquals(2.0, databaseEvent.get().getPrice()),
                () -> assertEquals("b", databaseEvent.get().getName())
        );
    }

    @Test
    public void updateEvent_placeDoesNotExist_test() throws Exception {
        //Arrange
        Place place = Place.builder()
                .name("a")
                .slug("a")
                .build();
        place = placeRepository.save(place);
        Event previous = Event.builder()
                .name("a")
                .description("a")
                .price(1.0)
                .date(Timestamp.valueOf("2000-01-01 12:00:00"))
                .place(place)
                .build();
        previous = eventRepository.save(previous);
        String json = """
                {
                    "price": 1.0,
                    "placeId": 2,
                    "name": "a"
                }
                """;
        //Act, Assert
        mockMvc.perform(put("/api/v1/event/" + previous.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Place id does not exist"));
    }

    @Test
    public void updateEvent_notFound_test() throws Exception {
        //Arrange
        String json = """
                {
                    "description": "a",
                    "name": "a",
                    "price": 1.0,
                    "date": "2000-01-01 12:00:00",
                    "placeId":1
                }""";
        //Act, Assert
        mockMvc.perform(put("/api/v1/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Event was not found"));
    }
}
