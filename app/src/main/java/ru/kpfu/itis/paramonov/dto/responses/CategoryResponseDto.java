package ru.kpfu.itis.paramonov.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import ru.kpfu.itis.paramonov.dto.CategoryDto;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {

    @JsonProperty("category")
    private CategoryDto category;

}
