package ru.kpfu.itis.paramonov.dto.request;

import lombok.Getter;

@Getter
public class CreateCategoryRequestDto {

    private String slug;

    private String name;
}
