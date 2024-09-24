package ru.kpfu.itis.paramonov.dto.kudago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KudaGoCityResponseDto {

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("name")
    private String name;

}
