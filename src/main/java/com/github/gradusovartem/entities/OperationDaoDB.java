package com.github.gradusovartem.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;

/**
 * Класс реализует слой Dao для доступа к базе данных
 */
public class OperationDaoDB implements Dao {
    private static final String url = "jdbc:postgresql://localhost:5432/OperationDB?user=postgres&password=7719150Artik";
    private static String getStatement = "SELECT * FROM operations1 WHERE id = ?";
    private static String getAllStatement = "SELECT * FROM operations1";
    private static String addStatement = "INSERT INTO operations1(comment, dt_operation, oper_1, oper_2, operation, result) VALUES(?, ?, ?, ?, ?, ?)";
    private static String updateStatement = "UPDATE operations1 SET comment = ? WHERE id = ?";
    private static String deleteStatement = "DELETE FROM operations1 WHERE id = ?";
    ObjectMapper objectMapper = SingleObjectMapper.getInstance();
    ConnectionPool pool;

    {
        try {
            pool = ConnectionPool.create(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод реализует подключение к базе данных и получение объекта по id
     * @param id - параметр Integer
     * @return возвращает объект класса Operation или null
     */
    @Override
    public Operation get(int id) {
        Connection conn = pool.getConnection();

        /* if(1 == 1){
            System.out.println("TEST");
            throw new RuntimeException("TEST");
        } */
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(getStatement);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            JSONObject json = null;
            Operation operation = null;

            if (rs.next()) {
                // json = convert(rs);
                operation = convert(rs);
            }

            if(rs != null)
                rs.close();
            if(stmt != null)
                stmt.close();

            // rs.close();
            // stmt.close();
            // System.out.println(json.toString());
            System.out.println(pool.getSize());
            // Operation operation = objectMapper.readValue(json.toString(), Operation.class);
            conn.commit();
            return operation;
        } catch (SQLException | JSONException | NullPointerException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }

        return null;
    }

    /**
     * Метод реализует подключение к базе данных и получение всех данных, которые находятся в базе данных
     * @return возвращает коллекцию элементов Operation или null
     */
    @Override
    public Collection getAll() {
        Connection conn = pool.getConnection();
        int count = 0;
        try {
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM operations1");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

            stmt = conn.prepareStatement(getAllStatement);
            rs = stmt.executeQuery();
            conn.commit();
            JSONArray jsonArray = new JSONArray();
            Operation[] operations = new Operation[count];
            int i = 0;

            while (rs.next()) {
                // JSONObject json = convert(rs);
                // jsonArray.put(json);
                operations[i] = convert(rs);
                i++;
            }

            if(rs != null)
                rs.close();
            if(stmt != null)
                stmt.close();
            // System.out.println(jsonArray.toString());
            // Operation[] operation = objectMapper.readValue(jsonArray.toString(), Operation[].class);
            return Arrays.asList(operations);
        } catch (SQLException | JSONException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
        return null;
    }

    /**
     * Метод реализует подключение к базе данных и добавление нового объекта в базу данных
     * @param t - параметр Operation
     * @return возвращает булево значение
     */
    @Override
    public boolean add(Operation t) {
        PreparedStatement stmt = null;
        Connection conn = pool.getConnection();
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(addStatement, PreparedStatement.RETURN_GENERATED_KEYS);
            // stmt.setInt(1, t.getId());
            stmt.setString(1, t.getComment());
            stmt.setObject(2, t.getDt_operation());
            stmt.setInt(3, t.getOper_1());
            stmt.setInt(4, t.getOper_2());
            stmt.setString(5, t.getOperation());
            stmt.setInt(6, t.getResult());
            stmt.executeUpdate();
            conn.commit();

            rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
                System.out.println("ID последней вставленной записи: " + id);
            }
            if (id == 0) {
                throw new IllegalArgumentException("CAN'T GET GENERATED KEYS");
            }
            t.setId(id);
            if(rs != null)
                rs.close();
            if(stmt != null)
                stmt.close();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            pool.releaseConnection(conn);
        }
    }

    /**
     * Метод реализует подключение к базе данных и обновление объекта по id
     * @param id - параметр Integer
     * @param comment - параметр Integer
     * @return возвращает булево значение
     */
    @Override
    public boolean update(int id, String comment) {
        Connection conn = pool.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(updateStatement);
            stmt.setString(1, comment);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            conn.commit();

            if(stmt != null)
                stmt.close();

            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            pool.releaseConnection(conn);
        }
    }

    /**
     * Метод реализует подключение к базе данных и удаление объекта по id
     * @param id - параметр Integer
     * @return возвращает будево значение
     */
    @Override
    public boolean delete(int id) {
        Connection conn = pool.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            conn.commit();

            if(stmt != null)
                stmt.close();

            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            pool.releaseConnection(conn);
        }
    }

    /**
     * Метод преобразует полученные данные в формат JSON
     * @param rs - ResultSet полученный из БД
     * @return возвращает объект класса JSONObject
     * @throws SQLException
     * @throws JSONException
     */
    /* public static JSONObject convert(ResultSet rs) throws SQLException, JSONException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();
        JSONObject obj = new JSONObject();

        for (int i = 1; i < numColumns + 1; i++) {
            String column_name = rsmd.getColumnName(i);

            if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                obj.put(column_name, rs.getInt(column_name));
            } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR || rsmd.getColumnType(i) == java.sql.Types.CHAR) {
                obj.put(column_name, rs.getString(column_name));
            } else {
                obj.put(column_name, LocalDateTime.parse(String.valueOf(rs.getObject(column_name)).replaceAll("\\s", "T")));
            }
        }
        return obj;
    } */

    public static Operation convert(ResultSet rs) throws SQLException, JSONException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();
        Operation operation = new Operation();
        operation.setId(rs.getInt("id"));
        operation.setComment(rs.getString("comment"));
        operation.setDt_operation(LocalDateTime.parse(String.valueOf(rs.getObject("dt_operation")).replaceAll("\\s", "T")));
        operation.setOper_1(rs.getInt("oper_1"));
        operation.setOper_2(rs.getInt("oper_2"));
        operation.setOperation(rs.getString("operation"));
        operation.setResult(rs.getInt("result"));

        /* for (int i = 1; i < numColumns + 1; i++) {
            String column_name = rsmd.getColumnName(i);

            if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                // obj.put(column_name, rs.getInt(column_name));
                operation.setMethod(i, rs.getInt(column_name));
            } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR || rsmd.getColumnType(i) == java.sql.Types.CHAR) {
                // obj.put(column_name, rs.getString(column_name));
                operation.setMethod(i, rs.getString(column_name));
            } else {
                // obj.put(column_name, LocalDateTime.parse(String.valueOf(rs.getObject(column_name)).replaceAll("\\s", "T")));
                operation.setMethod(i, LocalDateTime.parse(String.valueOf(rs.getObject(column_name)).replaceAll("\\s", "T")));
            }
        } */
        return operation;
    }
}
