package ru.kpfu.itis.paramonov.dto.kudago.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class KudaGoEventsResponseDto {

    @JsonProperty("results")
    private List<KudaGoEventResponseDto> events;
}
