package com.hotel.app;

import com.database.helper.DatabaseHelper;
import com.hotel.book.RoomBooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame {

    public static String enteredUsername;  // 用于存储用户输入的用户名
    private JTextField usernameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JButton loginButton; // 登录按钮

    public static void main(String[] args) {
        LoginPage login = new LoginPage();
        login.LoginPage(); // 创建并显示登录界面
    }

    public void LoginPage() {
        setTitle("登录"); // 设置窗口标题

        usernameField = new JTextField(20); // 创建用户名输入框
        passwordField = new JPasswordField(20); // 创建密码输入框
        loginButton = new JButton("登录"); // 创建登录按钮

        // 创建面板
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        // 设置组件间距
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("用户名:"), c); // 添加用户名标签
        c.gridx = 1;
        panel.add(usernameField, c); // 添加用户名输入框

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("密码:"), c); // 添加密码标签
        c.gridx = 1;
        panel.add(passwordField, c); // 添加密码输入框

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2; // 设置登录按钮跨两列
        panel.add(loginButton, c); // 添加登录按钮

        add(panel);

        passwordField.setText("password"); // 设置密码输入框的默认文本
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enteredUsername = usernameField.getText();  // 将用户输入的用户名存储在成员变量中

                if (checkLogin()) {
                    JOptionPane.showMessageDialog(null, "登录成功"); // 显示登录成功提示框
                } else {
                    JOptionPane.showMessageDialog(null, "登录失败"); // 显示登录失败提示框
                }
            }
        });
        //另一种需要用户输入密码并判断是否等于123456的方法
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                enteredUsername = usernameField.getText();  // 将用户输入的用户名存储在成员变量中
//                String password = new String(passwordField.getPassword()); // 获取密码输入框中的密码
//                if (password.equals("123456")) { // 检查输入的密码是否为预设的密码
//                    if (checkLogin(enteredUsername, password)) {
//                        JOptionPane.showMessageDialog(null, "登录成功"); // 显示登录成功提示框
//                    } else {
//                        JOptionPane.showMessageDialog(null, "登录失败"); // 显示登录失败提示框
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "密码错误"); // 显示密码错误提示框
//                }
//            }
//        });

        setSize(500, 300); // 设置窗口大小
        setLocationRelativeTo(null); // 将窗口位置设置为屏幕中央
        setVisible(true); // 设置窗口可见
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭时的操作
    }

    // 检查登录信息的方法，当获取数据库连接成功且查询有结果返回true
    private boolean checkLogin() {
        Connection connection = DatabaseHelper.getConnection(); // 获取数据库连接
        if (connection != null) {
            try {
                String query = "SELECT * FROM client WHERE cNumber ='" + enteredUsername + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    // 如果用户名和密码正确则登录成功
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            runRoomBook();  // 调用方法启动 roombooking.java
                            dispose();  // 关闭当前登录页面
                        }
                    });
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;  // 如果用户名和密码错误则登录失败
    }

    // 运行roombook.java文件的方法
    private void runRoomBook() {
        RoomBooking.main(new String[]{enteredUsername}); // 运行roombook内的主函数，
        // 并将enteredUsername作为参数传递进入roombook内
    }
}
