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
    public boolean add(String slug, CityDto cityDto) {
        return cityDatasource.add(slug, cityDto);
    }

    @Override
    public CityDto remove(String slug) {
        return cityDatasource.remove(slug);
    }

    @Override
    public CityDto update(String slug, CityDto cityDto) {
        return cityDatasource.update(slug, cityDto);
    }
}
