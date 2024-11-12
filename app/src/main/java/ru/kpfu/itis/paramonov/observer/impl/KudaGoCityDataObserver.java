package ru.kpfu.itis.paramonov.observer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CityDto;
import ru.kpfu.itis.paramonov.memento.impl.CityMementoCaretaker;
import ru.kpfu.itis.paramonov.observer.Observer;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoCityDataObserver implements Observer {

    private final DataSource<String, CityDto> cityDataSource;
    private final CityMementoCaretaker cityMementoCaretaker;

    @Override
    public <P> void handle(P payload) {
        if (supports(payload)) {
            ((Collection<CityDto>) payload).forEach(dto -> {
                cityDataSource.add(dto.getSlug(), dto);
                cityMementoCaretaker.add(dto.getSlug(), dto.createMemento());
                log.info("City was added: {}", dto);
            });
        } else {
            throw new IllegalStateException(
                    "Unsupported payload: " + payload.toString() + "of class: " + payload.getClass()
            );
        }
    }

    @Override
    public <P> boolean supports(P payload) {
        return payload instanceof Collection<?>
                && ((Collection<?>) payload).stream().allMatch(CityDto.class::isInstance);
    }
}
