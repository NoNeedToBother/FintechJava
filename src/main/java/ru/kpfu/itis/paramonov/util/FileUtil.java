package ru.kpfu.itis.paramonov.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Optional;

@Slf4j
public class FileUtil {

    public static Optional<String> read(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
            log.debug("File {} was read, its content:\n{}", file.getName(), stringBuilder);
            return Optional.of(stringBuilder.toString());
        } catch (IOException e) {
            log.error("Error when reading file: {}, caused by:\n", file.getName(), e);
            return Optional.empty();
        }
    }

    public static boolean write(String content, File file) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            log.debug("Content:\n{}\nwas written into file {}", content, file.getName());
            return true;
        } catch (IOException e) {
            log.error("Error when writing into file {}, cause by:\n", file.getName(), e);
            return false;
        }
    }
}
