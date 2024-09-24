package ru.kpfu.itis.paramonov.config;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.service.impl.KudaGoApiServiceImpl;

@Component
@AllArgsConstructor
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private KudaGoApiServiceImpl kudaGoApiService;

    private DataSource<Integer, CategoryDto> categoryDataSource;

    private DataSource<String, CityDto> cityDataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        kudaGoApiService.getAllCategories()
                .doOnNext(response ->
                        response.forEach(dto -> categoryDataSource.add(dto.getId(), dto)))
                .subscribe();

        kudaGoApiService.getAllCities()
                .doOnNext(response ->
                        response.forEach(dto -> cityDataSource.add(dto.getSlug(), dto)))
                .subscribe();
    }
}
