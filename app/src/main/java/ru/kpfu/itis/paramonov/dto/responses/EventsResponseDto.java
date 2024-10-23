package ru.kpfu.itis.paramonov.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import ru.kpfu.itis.paramonov.dto.EventDto;

import java.util.List;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsResponseDto {

    @JsonProperty("events")
    private List<EventDto> events;
}
