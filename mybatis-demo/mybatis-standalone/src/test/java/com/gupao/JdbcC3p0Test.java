package com.gupao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class JdbcC3p0Test {

    private final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/gupao";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    @Before
    public void before() throws PropertyVetoException, SQLException {
        //创建连接池数据源 ComboPooledDataSource
        ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setDriverClass(DRIVER_CLASS_NAME);
        pooledDataSource.setJdbcUrl(JDBC_URL);
        pooledDataSource.setUser(USERNAME);
        pooledDataSource.setPassword(PASSWORD);
        //从连接池中获取连接
        connection = pooledDataSource.getConnection();
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
        preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Map<String, Object> resultMap =new HashMap<String, Object>();

            resultMap.put("id", resultSet.getInt(1));
            resultMap.put("name", resultSet.getString("emp_name"));
            resultMap.put("gender", resultSet.getString("gender"));
            resultMap.put("emial", resultSet.getString("email"));
            resultMap.put("did", resultSet.getInt("d_id"));

            System.out.println("map:" + resultMap.toString());
        }
    }


}
