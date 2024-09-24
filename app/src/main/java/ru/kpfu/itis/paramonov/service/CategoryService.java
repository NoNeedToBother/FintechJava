package ru.kpfu.itis.paramonov.service;

import ru.kpfu.itis.paramonov.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {

    CategoryDto get(Integer id);

    Collection<CategoryDto> getAll();

    boolean add(Integer id, CategoryDto categoryDto);

    CategoryDto remove(Integer id);

    CategoryDto update(Integer id, CategoryDto categoryDto);
}
