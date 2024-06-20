package com.github.gradusovartem.entities;

import java.util.Collection;

/**
 * Описывает поведение объекта на слое Service
 */
public interface Service {
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
    Collection<Operation> getAll();

    /**
     * Метод отвечает за добавление нового объекта
     * @param operation - параметр Operation
     * @return возвращает объект класса Operation
     */
    Operation add(Operation operation);

    /**
     * Метод отвечает за обновление объекта
     * @param id - параметр Integer
     * @param comment - параметр String
     * @return возвращает объект класса Operation
     */
    Operation update(int id, String comment);

    /**
     * Метод отвечает за удаление объекта
     * @param id - параметр Integer
     * @return возвращает объект класса Operation
     */
    Operation delete(int id);

}
