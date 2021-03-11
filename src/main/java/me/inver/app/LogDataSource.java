package me.inver.app;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class LogDataSource {

    private static LogDataSource logDataSource = new LogDataSource();

    public static final String ENVIRONMENT = "environment";
    private Properties prop;
    public String environment;
    private HikariDataSource hikariDataSource;

    private LogDataSource() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName("eisltier2LogPool");
            hikariConfig.setUsername("root");
            hikariConfig.setPassword("");
            hikariConfig.setDriverClassName("");
            hikariConfig.setConnectionTestQuery("SELECT 1");
            hikariConfig.setDataSourceClassName("com.mysql.jdbc.Driver");

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new NullPointerException(String.format("DataSourceClassName did not load. (DataSourceClassName='%s')", "com.mysql.jdbc.Driver"));
            }

            hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/logg?useSSL=false");
            this.hikariDataSource = new HikariDataSource(hikariConfig);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws java.sql.SQLException {
        DataSource returnItem = logDataSource.hikariDataSource;
        return returnItem.getConnection();
    }
}