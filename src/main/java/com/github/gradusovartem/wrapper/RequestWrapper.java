package com.github.gradusovartem.wrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * class JsonRequestWrapper возвращает json данные в request
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;

    /**
     * constructor JsonRequestWrapper
     * @param request
     * @param body
     */
    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = new byte[request.getContentLength()];
        request.getInputStream().read(body, 0, body.length);
    }

    /**
     * метод getReader позволяет считывать данные из тела запроса
     * @return
     * @throws IOException
     */
    /* @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new StringReader(json.toString()));
    } */

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private int index = 0;

            @Override
            public int read() throws IOException {
                if (index >= body.length) {
                    return -1;
                }
                return body[index++];
            }
        };
    }
}

