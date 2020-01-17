package com.gupao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcTest {

    private final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/gupao";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    @Before
    public void before() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_CLASS_NAME);
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
    }

    @After
    public void after() throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (statement != null) {
            statement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void test1() throws SQLException {
        String sql = "SELECT * FROM tbl_emp";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            final Integer id = resultSet.getInt("emp_id");
            final String name = resultSet.getString("emp_name");
            final String gender = resultSet.getString("gender");
            final String email = resultSet.getString("email");
            final String dId = resultSet.getString("d_id");
            Map<String, Object> emp = new LinkedHashMap<String, Object>() {{
                put("id", id);
                put("name", name);
                put("gender", gender);
                put("email", email);
                put("dId", dId);
            }};
            System.out.println("emp: " + emp.toString());
        }
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void addBatch() throws SQLException {
        Long startTime = System.currentTimeMillis();
        String sql = "INSERT INTO tbl_emp VALUES(?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 100; i < 10000; i++) {
            preparedStatement.setInt(1, 2 * i);
            preparedStatement.setString(2, String.valueOf(i + "emp_name"));
            preparedStatement.setString(3, "F");
            preparedStatement.setString(4, String.valueOf("email" + i));
            preparedStatement.setInt(5, i);

            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        Long endTime = System.currentTimeMillis();
//        cost time = 24241
        System.out.println("cost time = " + (endTime - startTime));
    }

    @Test
    public void addBatch2() throws SQLException {
        Long startTime = System.currentTimeMillis();
        String sql = "INSERT INTO tbl_emp VALUES(?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 100; i < 10000; i++) {
            preparedStatement.setInt(1, 2 * i);
            preparedStatement.setString(2, String.valueOf(i + "emp_name"));
            preparedStatement.setString(3, "M");
            preparedStatement.setString(4, String.valueOf("email" + i));
            preparedStatement.setInt(5, i);

            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
        connection.close();
        Long endTime = System.currentTimeMillis();
        // cost time = 35700
        System.out.println("cost time = " + (endTime - startTime));
    }

    @Test
    public void test3() throws SQLException {
        String sql = "DELETE  FROM tbl_emp WHERE emp_id > ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 11);
        preparedStatement.executeUpdate();
    }
}
