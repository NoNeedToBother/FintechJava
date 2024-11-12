package ru.kpfu.itis.paramonov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kpfu.itis.paramonov.memento.Originator;
import ru.kpfu.itis.paramonov.memento.impl.CategoryMemento;

@Data
@AllArgsConstructor
public class CategoryDto implements Originator<CategoryMemento> {

    private Integer id;

    private String slug;

    private String name;

    @Override
    public void setMemento(CategoryMemento memento) {
        if (memento.getState().getId().equals(id)) {
            slug = memento.getState().getSlug();
            name = memento.getState().getName();
        } else {
            throw new IllegalStateException("Attempt to restore state with wrong id: " + memento.getState().getId()
                    + ", should be: " + id);
        }
    }

    @Override
    public CategoryMemento createMemento() {
        return new CategoryMemento(this);
    }
}
