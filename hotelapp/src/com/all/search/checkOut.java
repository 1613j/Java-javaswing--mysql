package com.all.search;

import com.database.helper.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class checkOut extends JFrame {
    private JTable table;

    public checkOut() {
        setTitle("入住管理");
        setSize(500, 300);
        setLocationRelativeTo(null); // 将窗口居中显示
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("订房编号");
        model.addColumn("客户编号");
        model.addColumn("房间编号");
        model.addColumn("订房日期");
        model.addColumn("入住情况");

        // 创建表格并添加到界面
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 创建确定退房按钮
        JButton confirmButton = new JButton("确定退房");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String bookNumber = (String) table.getValueAt(selectedRow, 0);
                    String cNumber = (String) table.getValueAt(selectedRow, 1);
                    String rNumber = (String) table.getValueAt(selectedRow, 2);
                    updateCheckinStatus(bookNumber);
                    updateRoomState(rNumber);
                    insertCheckinData(bookNumber, cNumber, rNumber);
                    JOptionPane.showMessageDialog(checkOut.this, "退房成功！");
                    refreshPage();
                } else {
                    JOptionPane.showMessageDialog(checkOut.this, "请选择一个信息");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);

        // 从数据库加载数据
        loadDataFromDatabase(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            checkOut checkOut = new checkOut();
            checkOut.setVisible(true);
        });
    }

    // 从数据库加载数据的方法
    private void loadDataFromDatabase(DefaultTableModel model) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT * FROM bookroom where checkin='已入住'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{
                            resultSet.getString("bookNumber"),
                            resultSet.getString("cNumber"),
                            resultSet.getString("rNumber"),
                            resultSet.getString("bookdate"),
                            resultSet.getString("checkin")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 更新退房状态的方法
    private void updateCheckinStatus(String bookNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String updateQuery = "UPDATE bookroom SET checkin = '已退房' WHERE bookNumber = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, bookNumber);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 更新房间状态的方法
    private void updateRoomState(String rNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String updateQuery = "UPDATE roomstate SET state = '已退房' WHERE rNumber = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, rNumber);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 插入退房数据的方法
    private void insertCheckinData(String bookNumber, String cNumber, String rNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String insertQuery = "INSERT INTO checkout (outNumber, cNumber, rNumber, outDate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, bookNumber); // 假设bookNumber是outNumber
                insertStatement.setString(2, cNumber);
                insertStatement.setString(3, rNumber);
                insertStatement.setDate(4, new Date(System.currentTimeMillis())); // 使用当前日期作为退房日期
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // 刷新页面的方法
    private void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 清空表格数据
        loadDataFromDatabase(model); // 重新加载数据
    }
}
