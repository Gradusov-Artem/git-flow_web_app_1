package com.github.gradusovartem.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализует интерфейс Dao
 */
public class OperationDao implements Dao<Operation>{
    // private Model model = Model.getInstance();
    private Map<Integer, Operation> modelOper;
    public OperationDao() {
        modelOper = new HashMap<>();
    }

    /**
     * Метод реализует получение данных по id
     * @param id - параметр Integer
     * @return возвращает объект класса Operation
     */
    @Override
    public Operation get(int id) {
        return getOperationById(id);
    }

    /**
     * Метод реализует получение всех данных, которые находятся в коллекции
     * @return возвращает коллекцию объектов Operation
     */
    @Override
    public Collection<Operation> getAll() {
        return getValues();
    }

    /**
     * Метод реализует добавление новой операции
     * @param operation - параметр Operation
     * @return возвращает булево значение
     */
    @Override
    public boolean add(Operation operation) {
        addOperation(operation);
        return true;
    }

    /**
     * Метод реализует обновление операции
     * @param id - параметр Integer
     * @param comment - параметр Integer
     * @return возвращает булево значение
     */
    @Override
    public boolean update(int id, String comment) {
        // operation.setComment(comment);
        Operation operation = getOperationById(id);
        if (operation != null) {
            operation.setComment(comment);
            return true;
        }
        return false;
    }

    /**
     * Метод реализует удаление операции
     * @param id - параметр Integer
     * @return возвращает булево значение
     */
    @Override
    public boolean delete(int id) {
        Operation operation = getOperationById(id);
        if (operation != null) {
            removeById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод добавляет операцию в коллекцию
     * @param operation
     */
    public void addOperation(Operation operation) {
        modelOper.put(operation.getId(), operation);
    }

    /**
     * Метод реализует получение операции из коллекции по id
     * @param id
     * @return возвращает объект класса Operation
     */
    public Operation getOperationById(int id) {
        Operation operationById = modelOper.get(id);
        return operationById;
    }

    /**
     * Метод реализует удаление операции из коллекции по id
     * @param id
     * @return возвращает объект класса Operation
     */
    public Operation removeById(int id) {
        Operation operationById = getOperationById(id);
        modelOper.remove(id, getOperationById(id));
        return operationById;
    }

    /**
     * Метод реализует получение всех данных в коллекции
     * @return возвращает коллекцию объектов Operation
     */
    public Collection<Operation> getValues() {
        Collection<Operation> values = modelOper.values();
        return values;
    }
}
