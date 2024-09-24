package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.service.CategoryService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private DataSource<Integer, CategoryDto> categoryDatasource;

    @Override
    public CategoryDto get(Integer id) {
        return categoryDatasource.get(id);
    }

    @Override
    public Collection<CategoryDto> getAll() {
        return categoryDatasource.getAll();
    }

    @Override
    public boolean add(Integer id, CategoryDto categoryDto) {
        return categoryDatasource.add(id, categoryDto);
    }

    @Override
    public CategoryDto remove(Integer id) {
        return categoryDatasource.remove(id);
    }

    @Override
    public CategoryDto update(Integer id, CategoryDto categoryDto) {
        return categoryDatasource.update(id, categoryDto);
    }
}
