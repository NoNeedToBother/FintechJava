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
public class KudaGoEventResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private String price;

    @JsonProperty("is_free")
    private Boolean isFree;

    @JsonProperty("dates")
    private List<KudaGoEventDateResponseDto> dates;
}
