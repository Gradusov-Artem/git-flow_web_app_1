package com.github.gradusovartem.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private String url;
    private String user;
    private String password;
    private static List<Connection> connectionPool;
    private static List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;

    public ConnectionPool() throws SQLException {

    }

    public static ConnectionPool create(
            String url) throws SQLException {

        connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(createConnection(url));
        }
        return new ConnectionPool();
    }

    // standard constructors

    public Connection getConnection() {
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public static boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(
            String url)
            throws SQLException {
        return DriverManager.getConnection(url);
    }

    public String getSize() {
        return String.valueOf(connectionPool.size());
    }

    public static void shutdown() throws SQLException {
        usedConnections.forEach(ConnectionPool::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }
}
