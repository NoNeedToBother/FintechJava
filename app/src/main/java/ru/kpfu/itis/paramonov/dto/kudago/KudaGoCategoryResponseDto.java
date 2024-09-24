package ru.kpfu.itis.paramonov.dto.kudago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KudaGoCategoryResponseDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("name")
    private String name;

}
