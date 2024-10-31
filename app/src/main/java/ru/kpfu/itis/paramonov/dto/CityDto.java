package ru.kpfu.itis.paramonov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kpfu.itis.paramonov.memento.Originator;
import ru.kpfu.itis.paramonov.memento.impl.CityMemento;

@Data
@AllArgsConstructor
public class CityDto implements Originator<CityMemento> {

    private String slug;

    private String name;

    @Override
    public void setMemento(CityMemento memento) {
        if (memento.getState().getSlug().equals(slug)) {
            name = memento.getState().getName();
        } else {
            throw new IllegalStateException("Attempt to restore state with wrong id: " + memento.getState().getSlug()
            + ", should be " + slug);
        }
    }

    @Override
    public CityMemento createMemento() {
        return new CityMemento(this);
    }
}
