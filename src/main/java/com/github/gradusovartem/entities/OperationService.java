package com.github.gradusovartem.entities;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализует интерфейс Service
 */
public class OperationService implements Service {
    private final AtomicInteger id = new AtomicInteger(0);
    private Dao<Operation> operationDao = SingleOperationDao.getInstance();

    /**
     * Метод реализует получение данных по id, вызывая слой Dao
     * @param id - параметр Integer
     * @return возвращает объект класса Operation
     */
    @Override
    public Operation get(int id) {
        Operation currentOperation = operationDao.get(id);
        if (currentOperation != null)
            return currentOperation;
        return null;
    }

    /**
     * Метод реализует получение всех данных, вызывая слой Dao
     * @return коллекцию объектов класса Operation
     */
    @Override
    public Collection<Operation> getAll() {
        return operationDao.getAll();
    }

    /**
     * Метод реализует добавление новой операции, вызывая слой Dao
     * @param operation - параметр Operation
     * @return возвращает объект класса Operation
     */
    @Override
    public Operation add(Operation operation){
        if (operation.getOperation() == null) {
            return null;
        }

        switch (operation.getOperation()) {
            case "+":
            case "-":
            case "*":
            case "/":
                break;
            default:
                return null;
        }

        int result = calculation(operation);
        // int id_ = generateUniqueID();
        // operation.setId(id_);
        operation.setResult(result);
        Operation currentOperation = new Operation(operation);
        if (operationDao.add(currentOperation)) {
            currentOperation = operationDao.get(currentOperation.getId());
            return currentOperation;
        }
        return null;
    }

    /**
     * Метод реализует обновление операции по id, вызывая слой Dao
     * @param id - параметр Integer
     * @param comment - параметр String
     * @return возвращает объект класса Operation
     */
    @Override
    public Operation update(int id, String comment) {
        Operation currentOperation = operationDao.get(id);
        if (currentOperation == null) {
            return null;
        }
        if (operationDao.update(id, comment)) {
            currentOperation = operationDao.get(id);
            return currentOperation;
        }
        return null;
    }

    /**
     * Метод реализует удаление операции по id, вызывая слой Dao
     * @param id - параметр Integer
     * @return возвращает объект класса Operation
     */
    @Override
    public Operation delete(int id) {
        Operation currentOperation = operationDao.get(id);
        if (currentOperation == null) {
            return null;
        }
        if (operationDao.delete(id)) {
            return currentOperation;
        }
        return null;
    }

    /**
     * Метод реализует вычисление результата
     * @param operation
     * @return возвращает объект Integer
     */
    private int calculation(Operation operation) {
        int result = 0;
        switch (operation.getOperation()) {
            case "+":
                result = operation.getOper_1() + operation.getOper_2();
                break;
            case "-":
                result = operation.getOper_1() - operation.getOper_2();
                break;
            case "*":
                result = operation.getOper_1() * operation.getOper_2();
                break;
            case "/":
                result = operation.getOper_1() / operation.getOper_2();
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * Метод реализует создание уникального id
     * @return возвращает объект Integer
     */
    private int generateUniqueID() {
        return id.incrementAndGet();
    }
}
