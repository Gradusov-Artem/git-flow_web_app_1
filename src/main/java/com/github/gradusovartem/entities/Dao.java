package com.github.gradusovartem.entities;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Класс-шаблон для реализации CRUD-операций
 * @param <T>
 */
public interface Dao<T> {

    /**
     * Метод отвечает за получение данных по id
     * @param id - параметр Integer
     * @return возвращает объект класса Operation
     */
    Operation get(int id);

    /**
     * Метод отвечает за получение всех данных
     * @return возвращает коллекцию элементов
     */
    Collection<T> getAll();

    /**
     * Метод отвечает за добавление нового объекта
     * @param t - параметр Operation
     * @return возвращает булево значение
     */
    boolean add(Operation t);

    /**
     * Метод отвечает за обновление объекта
     * @param id - параметр Integer
     * @param comment - параметр Integer
     * @return возвращает булево значение
     */
    boolean update(int id, String comment);

    /**
     * Метод отвечает за удаление объекта
     * @param id - параметр Integer
     * @return возвращает булево значение
     */
    boolean delete(int id);
}
