package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.memento.impl.CategoryMementoCaretaker;
import ru.kpfu.itis.paramonov.service.CategoryService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private DataSource<Integer, CategoryDto> categoryDatasource;

    private CategoryMementoCaretaker categoryMementoCaretaker;

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
        var categoryDto = new CategoryDto(id, slug, name);
        var success = categoryDatasource.add(id, categoryDto);
        if (success) {
            var saved = categoryDatasource.get(id);
            categoryMementoCaretaker.add(saved.getId(), saved.createMemento());
            return saved;
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
        var categoryDto = get(id);
        CategoryDto result;
        if (categoryDto != null) {
            String resultSlug = categoryDto.getSlug();
            String resultName = categoryDto.getName();
            if (slug != null) {
                resultSlug = slug;
            }
            if (name != null) {
                resultName = name;
            }
            result = new CategoryDto(id, resultSlug, resultName);
        } else {
            result = new CategoryDto(id, slug, name);
        }
        var saved = categoryDatasource.update(id, result);
        categoryMementoCaretaker.add(saved.getId(), saved.createMemento());
        return saved;
    }
}
