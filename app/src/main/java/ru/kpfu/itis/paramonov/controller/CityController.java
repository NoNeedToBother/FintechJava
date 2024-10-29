package ru.kpfu.itis.paramonov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.configuration.time.LogTime;
import ru.kpfu.itis.paramonov.dto.PlaceDto;
import ru.kpfu.itis.paramonov.dto.request.CreateCityRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateCityRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.PlacesResponseDto;
import ru.kpfu.itis.paramonov.dto.responses.PlaceResponseDto;
import ru.kpfu.itis.paramonov.service.PlaceService;

import java.util.Collection;

@RequestMapping("/api/v1/locations")
@AllArgsConstructor
@RestController
@LogTime
public class CityController {

    private PlaceService placeService;

    @GetMapping
    public ResponseEntity<PlacesResponseDto> getAll() {
        Collection<PlaceDto> cityDtos = placeService.getAll();
        return new ResponseEntity<>(new PlacesResponseDto(cityDtos), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> get(
            @PathVariable("id") Long id
    ) {
        PlaceDto cityDto = placeService.get(id);
        return new ResponseEntity<>(
                new PlaceResponseDto(cityDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaceResponseDto> post(
            @RequestBody CreateCityRequestDto createCityRequestDto
    ) {
        PlaceDto cityDto = placeService.add(createCityRequestDto.getSlug(), createCityRequestDto.getName());
        return new ResponseEntity<>(new PlaceResponseDto(cityDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> put(
            @PathVariable("id") Long id,
            @RequestBody UpdateCityRequestDto updateCityRequestDto
    ) {
        PlaceDto cityDto = placeService.update(id, updateCityRequestDto.getSlug(), updateCityRequestDto.getName());
        return new ResponseEntity<>(new PlaceResponseDto(cityDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> delete(
            @PathVariable("id") Long id
    ) {
        PlaceDto cityDto = placeService.remove(id);
        return new ResponseEntity<>(new PlaceResponseDto(cityDto), HttpStatus.OK);
    }
}