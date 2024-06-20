package com.github.gradusovartem.entities;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class C3p0DataSource {
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/database.properties");
            props.load(in);
            in.close();
            String driver = props.getProperty("jdbc.driver");
            if (driver != null) {
                Class.forName(driver);

                cpds.setDriverClass(driver);
                cpds.setJdbcUrl(props.getProperty("jdbc.url"));
                cpds.setUser(props.getProperty("jdbc.username"));
                cpds.setPassword(props.getProperty("jdbc.password"));

                cpds.setMaxStatements(Integer.parseInt(props.getProperty("jdbc.maxStatements"))); // Устанавливает максимальное количество предварительно подготовленных заявлений (PreparedStatement), которые пул будет хранить
                cpds.setMaxStatementsPerConnection(Integer.parseInt(props.getProperty("jdbc.maxStatementsPerConnection"))); // Определяет максимальное количество заявлений на одно соединение в пуле
                cpds.setMinPoolSize(Integer.parseInt(props.getProperty("jdbc.minPoolSize"))); // Устанавливает минимальное количество соединений, которые пул будет поддерживать открытыми
                cpds.setAcquireIncrement(Integer.parseInt(props.getProperty("jdbc.acquireIncrement"))); // Задает количество соединений, которые пул добавит каждый раз, когда ему нужно увеличить количество соединений
                cpds.setMaxPoolSize(Integer.parseInt(props.getProperty("jdbc.maxPoolSize"))); // Определяет максимальное количество соединений, которые пул может открыть
                cpds.setMaxIdleTime(Integer.parseInt(props.getProperty("jdbc.maxIdleTime"))); //  Устанавливает максимальное время простоя соединения в пуле в секундах. Соединения, которые простаивают дольше 30 секунд, будут закрыты, чтобы освободить ресурсы
            }
        } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
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

    public C3p0DataSource() throws FileNotFoundException {}
}
