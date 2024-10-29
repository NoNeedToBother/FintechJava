package ru.kpfu.itis.paramonov.dto.kudago.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class KudaGoEventDateResponseDto {

    @JsonProperty("start")
    private long start;

    @JsonProperty("end")
    private long end;
}
