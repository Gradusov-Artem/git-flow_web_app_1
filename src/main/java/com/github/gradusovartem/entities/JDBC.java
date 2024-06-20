package com.github.gradusovartem.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Реализует подключение к базе данных
 */
public class JDBC {
    Connection conn;

    /**
     * Устанавливает подключение к базе данных
     * @return возвращает новое подключение
     */
    public Connection getConnection() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/OperationDB?user=postgres&password=7719150Artik");
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
