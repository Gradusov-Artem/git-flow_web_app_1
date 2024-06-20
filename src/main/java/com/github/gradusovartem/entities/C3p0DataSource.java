package com.github.gradusovartem.entities;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3p0DataSource {
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            cpds.setDriverClass("org.postgresql.Driver");
            cpds.setJdbcUrl("jdbc:postgresql://localhost:5432/OperationDB");
            cpds.setUser("postgres");
            cpds.setPassword("7719150Artik");

            cpds.setMaxStatements             (10); // Устанавливает максимальное количество предварительно подготовленных заявлений (PreparedStatement), которые пул будет хранить
            cpds.setMaxStatementsPerConnection(10); // Определяет максимальное количество заявлений на одно соединение в пуле
            cpds.setMinPoolSize               (5); // Устанавливает минимальное количество соединений, которые пул будет поддерживать открытыми
            cpds.setAcquireIncrement          (1); // Задает количество соединений, которые пул добавит каждый раз, когда ему нужно увеличить количество соединений
            cpds.setMaxPoolSize               (10); // Определяет максимальное количество соединений, которые пул может открыть
            cpds.setMaxIdleTime               (30); //  Устанавливает максимальное время простоя соединения в пуле в секундах. Соединения, которые простаивают дольше 30 секунд, будут закрыты, чтобы освободить ресурсы
        } catch (PropertyVetoException e) {

        }
    }

    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    public static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public C3p0DataSource(){}
}
