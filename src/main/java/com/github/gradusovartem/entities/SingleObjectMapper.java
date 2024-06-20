package com.github.gradusovartem.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализует Singleton для objectMapper
 */
public class SingleObjectMapper {
    private static final ObjectMapper SINGLE_OBJECT_MAPPER = new ObjectMapper();

    /**
     * Конструктор SingleObjectMapper
     */
    private SingleObjectMapper () {}

    /**
     * Метод создает объект класса ObjectMapper и задает его параметры
     * @return возвращет объект класса ObjectMapper
     */
    public static ObjectMapper getInstance() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        SINGLE_OBJECT_MAPPER.registerModule(new JavaTimeModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter)));
        return SINGLE_OBJECT_MAPPER;
    }
}
