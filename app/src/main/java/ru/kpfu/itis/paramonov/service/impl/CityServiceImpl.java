package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.memento.impl.CityMementoCaretaker;
import ru.kpfu.itis.paramonov.service.CityService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private DataSource<String, CityDto> categoryDatasource;

    private CityMementoCaretaker categoryMementoCaretaker;

    @Override
    public CityDto get(String slug) {
        return categoryDatasource.get(slug);
    }

    @Override
    public Collection<CityDto> getAll() {
        return categoryDatasource.getAll();
    }

    @Override
    public CityDto add(String slug, String name) {
        var categoryDto = new CityDto(slug, name);
        var success = categoryDatasource.add(slug, categoryDto);
        if (success) {
            var saved = categoryDatasource.get(slug);
            categoryMementoCaretaker.add(saved.getSlug(), saved.createMemento());
            return saved;
        } else {
            return null;
        }
    }

    @Override
    public CityDto remove(String slug) {
        return categoryDatasource.remove(slug);
    }

    @Override
    public CityDto update(String slug, String name) {
        var cityDto = get(slug);
        CityDto result;
        if (cityDto != null) {
            String resultName = cityDto.getName();
            if (name != null) {
                resultName = name;
            }
            result = new CityDto(slug, resultName);
        } else {
            result = new CityDto(slug, name);
        }
        var saved = categoryDatasource.update(slug, result);
        categoryMementoCaretaker.add(saved.getSlug(), saved.createMemento());
        return saved;
    }
}

