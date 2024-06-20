package com.github.gradusovartem.filters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.gradusovartem.entities.Operation;
import com.github.gradusovartem.entities.SingleObjectMapper;
import com.github.gradusovartem.wrapper.RequestWrapper;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * class DivisionByZeroFilter - фильтр, который проверяет деление на 0
 */
public class DivisionByZeroFilter implements Filter {

    ObjectMapper objectMapper = SingleObjectMapper.getInstance();

    /**
     * method init - вызывается при вызове фильтра
     * @param config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {

    }

    /**
     * method doFilter вызывается при попадании запроса в фильтр
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String httpMethod = httpRequest.getMethod();

        if (!("POST".equals(httpMethod))) {
            chain.doFilter(request, response);
            return;
        }
        Operation data = null;

        // Возвращение тела запроса
        RequestWrapper wrapperRequest = new RequestWrapper((HttpServletRequest) request);

        try {
            byte[] bytes = new byte[wrapperRequest.getContentLength()];
            wrapperRequest.getInputStream().read(bytes, 0, bytes.length);
            data = objectMapper.readValue(bytes, Operation.class);
        }
        // Проверка на правильно указанные данные
        catch (NullPointerException e) {
            decorateResponse((HttpServletResponse) response, "Please check, whether the input data is correct and meet all requirement:\ncomment - String type\noper_1 - Integer type\noper_2 - Integer type" +
                    "\noperation - String type", 400);
            return;
        }
        // Обработка исключение при вводе неправильных данных
        catch (JsonParseException e) {
            decorateResponse((HttpServletResponse) response, "Please check, whether the input data is correct and meet all requirement:\ncomment - String type\noper_1 - Integer type\noper_2 - Integer type" +
                    "\noperation - String type", 400);
            return;
        }
        // Проверка на правильно введенный тип данных
        catch (InvalidFormatException e) {
            decorateResponse((HttpServletResponse) response, "Some issues with format. Please check the input data.", 400);
            return;
        }
        // Общая проверка исключений
        catch (Exception e) {
            decorateResponse((HttpServletResponse) response, e.getClass().getCanonicalName() + "\nIncorrect values.", 400);
            return;
        }
        // Проверка, если в боди нет операции
        if (data.getOperation() == null) {
            decorateResponse((HttpServletResponse) response, "Please enter an operation", 400);
            return;
        }

        String operation;
        // Проверка, если в операции вводится не арифметический символ
        switch (data.getOperation()) {
            case "+":
            case "-":
            case "*":
            case "/":
                operation = data.getOperation();
                break;
            default:
                decorateResponse((HttpServletResponse) response, "Please enter an operation symbol", 400);
                return;

        }

        // Считывание operation и oper_2
        operation = data.getOperation();
        int oper_2 = data.getOper_2();

        // Проверка деления на ноль
        if (oper_2 == 0 && operation.equals("/")) {
            decorateResponse((HttpServletResponse) response, "Division by zero.", 400);
        }
        // Передача запроса по цепочке
        else {
            chain.doFilter(wrapperRequest, response);
        }
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

    /**
     * method destroy вызывается при завершении работы фильтра
     */
    public void destroy() {

    }
}
