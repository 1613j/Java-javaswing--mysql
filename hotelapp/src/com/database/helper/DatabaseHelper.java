package com.database.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 创建用于建立和关闭数据库连接的方法。
 */
public class DatabaseHelper {
    // 数据库连接的JDBC URL
    private static final String url = "jdbc:mysql://localhost:3306/hotelapp";
    // 数据库用户名
    private static final String user = "root";
    // 数据库密码
    private static final String password = "root";
    // 用于保存数据库连接的Connection对象
    private static Connection connection;

    /**
     * 返回到数据库的连接。如果连接尚未建立，它会创建一个新的连接并返回。
     *
     * @return 数据库连接
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // 使用JDBC URL、用户名和密码建立新的连接
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                // 如果发生SQL异常，则打印堆栈跟踪
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 如果连接已打开，则关闭数据库连接。
     */
    public static void closeConnection() {
        try {
            if (connection != null) {
                // 关闭连接
                connection.close();
            }
        } catch (SQLException e) {
            // 如果发生SQL异常，则打印堆栈跟踪
            e.printStackTrace();
        }
    }
}
