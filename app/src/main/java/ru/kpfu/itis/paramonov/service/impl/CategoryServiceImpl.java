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
    public CategoryDto add(Integer id, String slug, String name) {
        CategoryDto categoryDto = new CategoryDto(id, slug, name);
        boolean success = categoryDatasource.add(id, categoryDto);
        if (success) {
            return categoryDatasource.get(id);
        } else {
            return null;
        }
    }

    @Override
    public CategoryDto remove(Integer id) {
        return categoryDatasource.remove(id);
    }

    @Override
    public CategoryDto update(Integer id, String slug, String name) {
        CategoryDto categoryDto = get(id);
        if (categoryDto != null) {
            String resultSlug = categoryDto.getSlug();
            String resultName = categoryDto.getName();
            if (slug != null) {
                resultSlug = slug;
            }
            if (name != null) {
                resultName = name;
            }
            CategoryDto result = new CategoryDto(id, resultSlug, resultName);
            return categoryDatasource.update(id, result);
        } else {
            CategoryDto result = new CategoryDto(id, slug, name);
            return categoryDatasource.update(id, result);
        }
    }
}
