package ru.kpfu.itis.paramonov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.CityDto;

@Configuration
public class DataConfig {

    @Bean(name = "categoryDataSource")
    public DataSource<Integer, CategoryDto> categoryDataSource() {
        return new DataSource<>();
    }

    @Bean(name = "cityDataSource")
    public DataSource<String, CityDto> cityDataSource() {
        return new DataSource<>();
    }


}