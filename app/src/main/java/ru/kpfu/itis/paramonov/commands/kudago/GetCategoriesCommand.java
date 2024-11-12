package ru.kpfu.itis.paramonov.commands.kudago;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.commands.Command;

@Component
@RequiredArgsConstructor
public class GetCategoriesCommand implements Command {

    private final KudaGoCommandReceiver kudaGoCommandReceiver;

    @Override
    public void execute() {
        kudaGoCommandReceiver.getCategories();
    }
}
