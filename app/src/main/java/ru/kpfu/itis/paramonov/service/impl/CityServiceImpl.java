package ru.kpfu.itis.paramonov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.service.CityService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private DataSource<String, CityDto> cityDatasource;

    @Override
    public CityDto get(String id) {
        return cityDatasource.get(id);
    }

    @Override
    public Collection<CityDto> getAll() {
        return cityDatasource.getAll();
    }

    @Override
    public CityDto add(String slug, String name) {
        CityDto cityDto = new CityDto(slug, name);
        boolean success = cityDatasource.add(slug, cityDto);
        if (success) {
            return get(slug);
        } else {
            return null;
        }
    }

    @Override
    public CityDto remove(String slug) {
        return cityDatasource.remove(slug);
    }

    @Override
    public CityDto update(String slug, String name) {
        CityDto cityDto = get(slug);
        if (cityDto != null) {
            String resultName = cityDto.getName();
            if (name != null) {
                resultName = name;
            }
            CityDto result = new CityDto(slug, resultName);
            return cityDatasource.update(slug, result);
        } else {
            CityDto result = new CityDto(slug, name);
            return cityDatasource.update(slug, result);
        }
    }
}
