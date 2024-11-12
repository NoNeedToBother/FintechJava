package ru.kpfu.itis.paramonov.memento.impl;

import lombok.AllArgsConstructor;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.memento.Memento;

@AllArgsConstructor
public class CategoryMemento implements Memento<CategoryDto> {

    private CategoryDto state;

    @Override
    public CategoryDto getState() {
        return state;
    }

    @Override
    public void setState(CategoryDto state) {
        this.state = state;
    }
}
