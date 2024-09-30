package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {

    CategoryDto get(Integer id);

    Collection<CategoryDto> getAll();

    CategoryDto add(Integer id, String slug, String name);

    CategoryDto remove(Integer id);

    CategoryDto update(Integer id, String slug, String name);
}
