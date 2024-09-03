package ru.kpfu.itis.paramonov.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class JsonToObjectParser<T> {

    public Optional<T> parse(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            log.error("Error when processing json:\n{}, caused by", json, e);
            return Optional.empty();
        }
    }
}
