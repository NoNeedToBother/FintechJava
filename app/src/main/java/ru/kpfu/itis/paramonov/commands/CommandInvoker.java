package ru.kpfu.itis.paramonov.commands;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandInvoker {

    private final Map<String, Command> commands = new HashMap<>();

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public void execute(String commandName) {
        var command = commands.get(commandName);
        if (command == null) {
            throw new IllegalStateException("No command found for name: " + commandName);
        } else {
            command.execute();
        }
    }
}
