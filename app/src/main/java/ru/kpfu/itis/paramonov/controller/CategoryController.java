package ru.kpfu.itis.paramonov.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.dto.request.CreateCategoryRequestDto;
import ru.kpfu.itis.paramonov.dto.request.UpdateCategoryRequestDto;
import ru.kpfu.itis.paramonov.dto.responses.CategoriesResponseDto;
import ru.kpfu.itis.paramonov.dto.responses.CategoryResponseDto;
import ru.kpfu.itis.paramonov.exception.CreateEntityFailException;
import ru.kpfu.itis.paramonov.service.CategoryService;

import java.util.Collection;

@RequestMapping("/api/v1/places/categories")
@AllArgsConstructor
@RestController
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoriesResponseDto> getAll() {
        Collection<CategoryDto> categories = categoryService.getAll();
        return new ResponseEntity<>(new CategoriesResponseDto(categories), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> get(
            @PathVariable("id") Integer id
    ) {
        CategoryDto category = categoryService.get(id);
        return new ResponseEntity<>(
                new CategoryResponseDto(category), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> post(
            @RequestBody CreateCategoryRequestDto createCategoryRequestDto,
            @PathVariable("id") Integer id
    ) {
        CategoryDto categoryDto = new CategoryDto(
                id,
                createCategoryRequestDto.getSlug(),
                createCategoryRequestDto.getName()
        );
        boolean added = categoryService.add(id, categoryDto);
        if (added) {
            return new ResponseEntity<>(new CategoryResponseDto(categoryDto), HttpStatus.CREATED);
        } else throw new CreateEntityFailException("Failed to create category");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> put(
            @PathVariable("id") Integer id,
            @RequestBody UpdateCategoryRequestDto updateCategoryRequestDto
    ) {
        CategoryDto categoryDto = categoryService.get(id);
        if (categoryDto != null) {
            String slug = categoryDto.getSlug();
            String name = categoryDto.getName();
            if (updateCategoryRequestDto.getSlug() != null) {
                slug = updateCategoryRequestDto.getSlug();
            }
            if (updateCategoryRequestDto.getName() != null) {
                name = updateCategoryRequestDto.getName();
            }
            CategoryDto result = categoryService.update(id, new CategoryDto(id, slug, name));
            return new ResponseEntity<>(new CategoryResponseDto(result), HttpStatus.OK);
        } else {
            CategoryDto result = categoryService.update(id,
                    new CategoryDto(id, updateCategoryRequestDto.getSlug(), updateCategoryRequestDto.getName()));
            return new ResponseEntity<>(new CategoryResponseDto(result), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> delete(
            @PathVariable("id") Integer id
    ) {
        CategoryDto categoryDto = categoryService.remove(id);
        return new ResponseEntity<>(new CategoryResponseDto(categoryDto), HttpStatus.OK);
    }
}
