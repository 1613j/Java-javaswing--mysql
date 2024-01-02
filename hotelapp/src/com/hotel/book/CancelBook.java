package com.hotel.book;

import com.database.helper.DatabaseHelper;
import com.hotel.app.LoginPage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelBook extends JFrame {
    private JTable table; // 表格
    private JButton confirmButton; // 确认按钮
    private String selectedRNumber; // 保存用户选择的房间编号
    private String enteredUsername; // 保存用户登录的用户名

    // 构造函数
    public CancelBook(String username) {
        this.enteredUsername = username;  // 保存用户登录的用户名
        setTitle("查询房间"); // 设置窗口标题
        setSize(500, 300); // 设置窗口大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置窗口关闭操作为销毁窗口
        setLocationRelativeTo(null); // 将窗口位置设置为屏幕中央

        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("预约编号");
        model.addColumn("用户编号");
        model.addColumn("房间编号");
        model.addColumn("预约日期");
        model.addColumn("入住情况");

        // 从数据库获取数据并添加到表格模型
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM bookroom where cNumber='" + enteredUsername + "' AND checkin='未入住'";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("bookNumber"), resultSet.getString("cNumber"),
                            resultSet.getString("rNumber"), resultSet.getString("bookdate"),
                            resultSet.getString("checkin")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 创建表格并添加到界面
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedRNumber = (String) table.getValueAt(selectedRow, 2); // 保存用户选择的房间编号
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER); // 将表格添加到界面的中间位置

        // 创建取消预约按钮
        confirmButton = new JButton("取消预约");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRNumber != null) {
                    delete(selectedRNumber); // 调用删除预约的方法
                    updateRoomState(selectedRNumber); // 调用更新房间状态的方法
                    JOptionPane.showMessageDialog(CancelBook.this, selectedRNumber + "取消成功"); // 显示取消成功提示框
                    refreshPage(); // 在取消预约后刷新页面
                } else {
                    JOptionPane.showMessageDialog(CancelBook.this, "请选择一个房间"); // 如果未选择房间，显示提示框
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH); // 将取消预约按钮添加到界面的底部
    }

    // 主方法
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String username = LoginPage.enteredUsername; // 获取用户登录的用户名
                CancelBook cancelBooking = new CancelBook(username); // 创建取消预约界面
                cancelBooking.setVisible(true); // 设置取消预约界面可见
            }
        });
    }

    // 刷新页面的方法
    public void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 清空表格内容
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM bookroom where cNumber='" + enteredUsername + "' AND checkin='未入住'";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("bookNumber"),
                            resultSet.getString("cNumber"),
                            resultSet.getString("rNumber"),
                            resultSet.getString("bookdate"),
                            resultSet.getString("checkin")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(model); // 重新设置表格模型
    }

    // 删除预约的方法
    public void delete(String rNumber) {
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "DELETE FROM bookroom WHERE rNumber=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, rNumber);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新房间状态的方法
    public void updateRoomState(String rNumber) {
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "UPDATE roomstate SET state='否' WHERE rNumber=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, rNumber);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}