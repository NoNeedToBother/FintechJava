package ru.kpfu.itis.paramonov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDto {

    private Integer id;

    private String slug;

    private String name;

}
