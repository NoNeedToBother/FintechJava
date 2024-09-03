package ru.kpfu.itis.paramonov;

import lombok.extern.slf4j.Slf4j;
import ru.kpfu.itis.paramonov.model.City;
import ru.kpfu.itis.paramonov.parser.JsonToObjectParser;
import ru.kpfu.itis.paramonov.parser.XmlToStringParser;
import ru.kpfu.itis.paramonov.util.FileUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
public class Main {

    public static void main(String[] args) {
        String firstFileName = "/city.json";
        String secondFileName = "/city-error.json";

        processFile(firstFileName);
        processFile(secondFileName);
    }

    private static void processFile(String fileName) {
        try {
            File file = new File(Main.class.getResource(fileName).toURI());
            Optional<String> content = FileUtil.read(file);

            if (content.isEmpty()) {
                log.warn("Failed to proceed file {}", fileName);
                return;
            }

            JsonToObjectParser<City> jsonParser = new JsonToObjectParser<>();
            Optional<City> city = jsonParser.parse(content.get(), City.class);

            if (city.isEmpty()) {
                log.warn("Failed to proceed file {}", fileName);
                return;
            }

            XmlToStringParser xmlParser = new XmlToStringParser();
            Optional<String> xml = xmlParser.toXml(city.get());

            if (xml.isEmpty()) {
                log.warn("Failed to proceed file {}", fileName);
                return;
            }

            File xmlFile = new File("xml.txt");
            boolean success = FileUtil.write(xml.get(), xmlFile);
            if (success) log.info("File {} was successfully proceeded", fileName);
            else log.warn("Failed to write result xml:\n{}\ninto file", xml.get());
        } catch (URISyntaxException e) {
            log.error("Cannot find file: {}", fileName);
        }
    }

}
