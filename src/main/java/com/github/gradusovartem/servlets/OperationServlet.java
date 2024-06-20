package com.github.gradusovartem.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.gradusovartem.entities.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * class OperationServlet привязан к url: /operations
 */
public class OperationServlet extends HttpServlet {
    private final Logger logger = LogManager.getFormatterLogger(getClass());
    private static Service service = new OperationService();
    ObjectMapper objectMapper = SingleObjectMapper.getInstance();

    @Override
    public void destroy() {
        try {
            ConnectionPool.shutdown();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.destroy();
    }

    /**
     * Метод вызывается при обращении к url: /operations и возвращает объект (объекты) класса Operation
     *
     * @param request  - запрос передаваемый в метод
     * @param response - ответ отправляемый из метода
     * throws IOException
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Задание формата вывода
        response.setContentType("application/json");
        // Считывание path
        String pathInfo = request.getPathInfo();
        String id = null;

        try {
            // Проверка на пустой path и вывод всех элементов
            if (pathInfo == null) {
                String json = objectMapper.writeValueAsString(service.getAll());
                decorateResponse(response, json, 200);
                return;
            }

            // Разделение path и считывание 2 части
            String[] parts = pathInfo.split("/");
            id = parts[1];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            decorateResponse(response, "You didn't enter id", 400);
            return;
        }
        catch (NumberFormatException e) {
            decorateResponse(response, "Id should be Integer type, change it please", 400);
            return;
        }
        catch (Exception e) {
            decorateResponse(response, "Something went wrong, please try again", 400);
            return;
        }
        Operation operation = service.get(Integer.parseInt(id));

        // Если operation равно null, вывести сообщение и установить статус
        if (operation == null) {
            decorateResponse(response, "There is no such operation", 400);
            return;
        }
        // Используем ObjectMapper для преобразования operation в JSON
        String json = objectMapper.writeValueAsString(operation);
        // Вывод ответа json
        decorateResponse(response, json, 200);
    }

    /**
     * Метод вызывается при обращении к url: /operations и возвращает созданный объект
     *
     * @param request  - запрос передаваемый в метод
     * @param response - ответ отправляемый из метода
     *                 throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Задание формата вывода
        response.setContentType("application/json");

        Operation data = null;

        try {
            // Считываем данные из тела запроса
            data = objectMapper.readValue(request.getInputStream(), Operation.class); // считывание в data оставить в сервлете или убрать на отдельный уровень
        }
        // Проверка на правильно указанные данные
        catch (NullPointerException e) {
            decorateResponse(response, "Please check, whether the input data is correct and meet all requirement:\ncomment - String type\noper_1 - Integer type\noper_2 - Integer type" +
                            "\noperation - String type", 400);
            return;
        }
        // Обработка исключение при вводе неправильных данных
        catch (JsonParseException e) {
            decorateResponse(response, "Please check, whether the input data is correct and meet all requirement:\ncomment - String type\noper_1 - Integer type\noper_2 - Integer type" +
                    "\noperation - String type", 400);
            return;
        }
        // Проверка на правильно введенный тип данных
        catch (InvalidFormatException e) {
            decorateResponse(response, "Some issues with format. Please check the input data.", 400);
            return;
        }
        // Общая проверка исключений
        catch (Exception e) {
            decorateResponse(response, e.getClass().getCanonicalName() + "\nIncorrect values.", 400);
            return;
        }

        // Добавление операции
        Operation operation = service.add(data);
        logger.info("Object with id: " + data.getId() + " created");

        // Вывод результата
        if (operation == null) {
            decorateResponse(response, "The operation can't be created. Please, check the input data", 400);
            return;
        }
        String json = objectMapper.writeValueAsString(operation);
        // Вывод ответа json
        decorateResponse(response, json, 200);
    }

    /**
     * Метод вызывается при обращении к url /operations и обновляет комментарий в объекте Operation
     *
     * @param request
     * @param response
     * @throws IOException
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Задание формата вывода
        response.setContentType("application/json");

        Operation data = null;
        // Считывание path
        String pathInfo = request.getPathInfo();
        String id = null;

        try {
            // Проверка path
            if (pathInfo == null) {
                response.setStatus(400);
                return;
            }

            // Разделение path и считывание 2 части
            String[] parts = pathInfo.split("/");
            id = parts[1];

            // Считывание операции
            data = objectMapper.readValue(request.getInputStream(), Operation.class);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            decorateResponse(response, "You didn't enter id", 400);
            return;
        }
        catch (NumberFormatException e) {
            decorateResponse(response, "Id should be Integer type, change it please", 400);
            return;
        }
        catch (Exception e) {
            decorateResponse(response, "Something went wrong, please try again", 400);
            return;
        }
        String comment = data.getComment();

        Operation operation = service.update(Integer.parseInt(id), comment);

        // Вывод обновленного комментария
        if (operation == null) {
            decorateResponse(response, "There is no such operation", 400);
            return;
        }

        String json = objectMapper.writeValueAsString(operation);
        // Вывод ответа json
        decorateResponse(response, json, 200);
    }

    /**
     * Метод вызывается при обращении к url /operations и удаляет объект класса Operation
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Задание формата вывода
        response.setContentType("application/json");
        // Считывание path
        String pathInfo = request.getPathInfo();
        String id = null;

        try {
            // Проверка на пустой path
            if (pathInfo == null) {
                response.setStatus(400);
                return;
            }

            // Разделение path на две части и считывание id
            String[] parts = pathInfo.split("/");
            id = parts[1];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            decorateResponse(response, "You didn't enter id", 400);
            return;
        }
        catch (NumberFormatException e) {
            decorateResponse(response, "Id should be Integer type, change it please", 400);
            return;
        }
        catch (Exception e) {
            decorateResponse(response, "Something went wrong, please try again", 400);
            return;
        }
        Operation operation = service.delete(Integer.parseInt(id));

        // Проверка существование операции
        if (operation == null) {
            decorateResponse(response, "There is no such operation", 400);
            return;
        }
        // Используем ObjectMapper для преобразования operationById в JSON
        String json = objectMapper.writeValueAsString(operation);
        // Вывод ответа json
        decorateResponse(response, json, 200);
    }

    /**
     * Метод выводит заданное сообщение и устанавливает статус ответа
     * @param response
     * @param message
     * @param status
     * @throws IOException
     */
    private void decorateResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.getWriter().println(message);
        response.setStatus(status);
    }
}
