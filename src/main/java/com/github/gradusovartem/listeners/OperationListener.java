package com.github.gradusovartem.listeners;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * class OperationListener - слушатель для событий, который считает количество обращений к сервлету
 */

public class OperationListener implements ServletRequestListener {
    private static int requestCount = 0;

    /**
     * method requestDestroyed() вызывается при завершении обработки запроса
     * @param servletRequestEvent
     */
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    /**
     * method requestInitialized() вызывается при отправке запроса
     * @param servletRequestEvent
     */
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        String requestURL = request.getRequestURL().toString();
        try {
            URI uri = new URI(requestURL);
            String path = uri.getPath();
            if ("/operations".equals(path)) {
                requestCount++;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method getRequestCount() возвращает количество обращений к сервлету
     * @return
     */
    public static int getRequestCount() {
        return requestCount;
    }
}
