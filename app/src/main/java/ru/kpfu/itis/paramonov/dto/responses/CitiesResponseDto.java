package ru.kpfu.itis.paramonov.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import ru.kpfu.itis.paramonov.dto.CityDto;

import java.util.Collection;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CitiesResponseDto {

    @JsonProperty("cities")
    private Collection<CityDto> cities;

}
