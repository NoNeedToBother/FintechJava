package ru.kpfu.itis.paramonov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.configuration.time.LogTime;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.dto.request.CreateCityRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateCityRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.CitiesResponseDto;
import ru.kpfu.itis.paramonov.dto.responses.CityResponseDto;
import ru.kpfu.itis.paramonov.service.CityService;

import java.util.Collection;

@RequestMapping("/api/v1/locations")
@AllArgsConstructor
@RestController
@LogTime
public class CityController {

    private CityService cityService;

    @GetMapping
    public ResponseEntity<CitiesResponseDto> getAll() {
        Collection<CityDto> cityDtos = cityService.getAll();
        return new ResponseEntity<>(new CitiesResponseDto(cityDtos), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDto> get(
            @PathVariable("id") String id
    ) {
        CityDto cityDto = cityService.get(id);
        return new ResponseEntity<>(
                new CityResponseDto(cityDto), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CityResponseDto> post(
            @RequestBody CreateCityRequestDto createCityRequestDto,
            @PathVariable("id") String id
    ) {
        CityDto cityDto = cityService.add(id, createCityRequestDto.getName());
        return new ResponseEntity<>(new CityResponseDto(cityDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDto> put(
            @PathVariable("id") String id,
            @RequestBody UpdateCityRequestDto updateCityRequestDto
    ) {
        CityDto cityDto = cityService.update(id, updateCityRequestDto.getName());
        return new ResponseEntity<>(new CityResponseDto(cityDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CityResponseDto> delete(
            @PathVariable("id") String id
    ) {
        CityDto cityDto = cityService.remove(id);
        return new ResponseEntity<>(new CityResponseDto(cityDto), HttpStatus.OK);
    }
}