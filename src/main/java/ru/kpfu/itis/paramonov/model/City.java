package ru.kpfu.itis.paramonov.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("coords")
    private Coordinates coordinates;
}
