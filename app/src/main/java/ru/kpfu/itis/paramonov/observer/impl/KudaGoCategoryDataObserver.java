package ru.kpfu.itis.paramonov.observer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.data.DataSource;
import ru.kpfu.itis.paramonov.dto.CategoryDto;
import ru.kpfu.itis.paramonov.memento.impl.CategoryMementoCaretaker;
import ru.kpfu.itis.paramonov.observer.Observer;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoCategoryDataObserver implements Observer {

    private final DataSource<Integer, CategoryDto> categoryDataSource;

    private final CategoryMementoCaretaker categoryMementoCaretaker;

    @Override
    public <P> void handle(P payload) {
        if (supports(payload)) {
            ((Collection<CategoryDto>) payload).forEach(dto -> {
                categoryDataSource.add(dto.getId(), dto);
                categoryMementoCaretaker.add(dto.getId(), dto.createMemento());
                log.info("Category was added: {}", dto);
            });
        }
    }

    @Override
    public <P> boolean supports(P payload) {
        return payload instanceof Collection<?>
                && ((Collection<?>) payload).stream().allMatch(CategoryDto.class::isInstance);
    }
}
