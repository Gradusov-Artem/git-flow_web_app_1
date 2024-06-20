package com.github.gradusovartem.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * class Swagger привязан к url /openapi, возвращает yaml файл
 */
public class SwaggerServlet extends HttpServlet {

    /**
     * method doGet возвращает yaml файл
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/x-yaml");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String yaml = new String(Files.readAllBytes(Paths.get("src/main/resources/openapi.yaml")), StandardCharsets.UTF_8);

        // Выводим файл yaml
        out.println(yaml);
    }
}
