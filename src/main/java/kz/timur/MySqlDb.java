package kz.timur;

import java.sql.*;
import java.util.ResourceBundle;

public class MySqlDb {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static ResourceBundle rb = ResourceBundle.getBundle("botconfig");

    public static String ReadDB(String tablename) throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    rb.getString("MySqlDbUrl"),
                    rb.getString("DbUserName"),
                    rb.getString("DbPassword")
            );
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        resultSet = statement.executeQuery("SELECT * FROM " + tablename + " ORDER BY id DESC LIMIT 1");
        int date = 0;
        double value = 0.0;
        if (resultSet.next()) {
            date = resultSet.getInt("timestamp");
            value = resultSet.getDouble("value");
        }
        resultSet.close();
        statement.close();
        connection.close();
        System.out.println("Таблица выведена");
        return Currency.SetCurrency(value, date);
    }
}
