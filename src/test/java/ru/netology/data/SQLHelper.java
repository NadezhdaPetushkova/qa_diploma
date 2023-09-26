package ru.netology.data;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlHelper {

    private static String url = System.getProperty("db.url");
    private static String user = System.getProperty("db.user");
    private static String pass = System.getProperty("db.password");
    public static String sut = System.getProperty("sut.url");
    private static QueryRunner runner = new QueryRunner();

    private SqlHelper() {
    }

    @SneakyThrows
    private static Connection getConnect() {
        return DriverManager.getConnection(url, user, pass);
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var connection = getConnect();
        var SqlQuery = "SELECT status FROM payment_entity ORDER BY created DESC limit 1";
        return runner.query(connection, SqlQuery, new ScalarHandler<String>());
    }


    public static void expectedPaymentStatus(String expectedStatus) {
        assertEquals(expectedStatus, getPaymentStatus());
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var connection = getConnect();
        var SqlQuery = "SELECT status FROM credit_request_entity ORDER BY created DESC limit 1";
        return runner.query(connection, SqlQuery, new ScalarHandler<String>());
    }


    public static void expectedCreditStatus(String expectedStatus) {
        assertEquals(expectedStatus, getCreditStatus());
    }

        @SneakyThrows
        public static void cleanDatabase() {
            var connection = getConnect();
            runner.execute(connection, "DELETE FROM credit_request_entity");
            runner.execute(connection, "DELETE FROM payment_entity");
            runner.execute(connection, "DELETE FROM order_entity");
        }
}
