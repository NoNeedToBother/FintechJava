package ru.kpfu.itis.paramonov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.configuration.time.LogTime;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.request.CreateCategoryRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateCategoryRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.CategoriesResponseDto;
import ru.kpfu.itis.paramonov.dto.responses.CategoryResponseDto;
import ru.kpfu.itis.paramonov.service.CategoryService;

import java.util.Collection;

@RequestMapping("/api/v1/places/categories")
@AllArgsConstructor
@RestController
@LogTime
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoriesResponseDto> getAll() {
        Collection<CategoryDto> categoryDtos = categoryService.getAll();
        return new ResponseEntity<>(new CategoriesResponseDto(categoryDtos), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> get(
            @PathVariable("id") Integer id
    ) {
        CategoryDto categoryDto = categoryService.get(id);
        return new ResponseEntity<>(new CategoryResponseDto(categoryDto), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> post(
            @RequestBody CreateCategoryRequestDto createCategoryRequestDto,
            @PathVariable("id") Integer id
    ) {
        CategoryDto categoryDto = categoryService.add(
                id, createCategoryRequestDto.getSlug(), createCategoryRequestDto.getName()
        );
        return new ResponseEntity<>(new CategoryResponseDto(categoryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> put(
            @PathVariable("id") Integer id,
            @RequestBody UpdateCategoryRequestDto updateCategoryRequestDto
    ) {
        CategoryDto categoryDto = categoryService.update(
                id, updateCategoryRequestDto.getSlug(), updateCategoryRequestDto.getName()
        );

        return new ResponseEntity<>(new CategoryResponseDto(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> delete(
            @PathVariable("id") Integer id
    ) {
        CategoryDto categoryDto = categoryService.remove(id);
        return new ResponseEntity<>(new CategoryResponseDto(categoryDto), HttpStatus.OK);
    }
}
