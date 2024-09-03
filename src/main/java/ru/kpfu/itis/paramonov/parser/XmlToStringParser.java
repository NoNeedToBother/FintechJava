package ru.kpfu.itis.paramonov.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class XmlToStringParser {

    public Optional<String> toXml(Object obj) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return Optional.of(xmlMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.error("Error when processing object: {}, caused by\n", obj.toString(), e);
            return Optional.empty();
        }
    }
}
