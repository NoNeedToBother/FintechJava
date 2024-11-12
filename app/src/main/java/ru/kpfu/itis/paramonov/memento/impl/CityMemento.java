package ru.kpfu.itis.paramonov.memento.impl;

import lombok.AllArgsConstructor;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.memento.Memento;

@AllArgsConstructor
public class CityMemento implements Memento<CityDto> {

    private CityDto state;

    @Override
    public CityDto getState() {
        return state;
    }

    @Override
    public void setState(CityDto state) {
        this.state = state;
    }
}