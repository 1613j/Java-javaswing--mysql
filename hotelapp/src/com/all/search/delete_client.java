package com.all.search;

import com.database.helper.DatabaseHelper;

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

public class delete_client extends JFrame {
    private JTable table;
    private String selectedcNumber; // 保存选择的 cNumber
    private JButton confirmButton;

    public delete_client() {
        setTitle("所有用户界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("用户编号");
        model.addColumn("用户姓名");
        model.addColumn("用户年龄");
        model.addColumn("用户性别");
        model.addColumn("用户电话");

        // 从数据库获取数据并添加到表格模型
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM client";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("cNumber"),
                            resultSet.getString("cName"),
                            resultSet.getString("cAge"),
                            resultSet.getString("cSex"),
                            resultSet.getString("cTel")});
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
                        selectedcNumber = (String) table.getValueAt(selectedRow, 0); // 保存选择的 cNumber
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 创建删除用户按钮
        confirmButton = new JButton("删除用户");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedcNumber != null) {
                    delete(selectedcNumber);
                    JOptionPane.showMessageDialog(delete_client.this, selectedcNumber + "删除成功");
                    refreshPage(); // 在点击删除后刷新页面
                } else {
                    JOptionPane.showMessageDialog(delete_client.this, "请选择一个用户");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);
    }

    // 主方法
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delete_client members = new delete_client();
                members.setVisible(true);
            }
        });
    }

    // 删除用户的方法
    private void delete(String selectedcNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query1 = "DELETE FROM client where cNumber = '" + selectedcNumber + "'";
                String query2 = "DELETE FROM bookroom where cNumber = '" + selectedcNumber + "'";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement2.executeUpdate();
                preparedStatement1.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 刷新页面的方法
    public void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 清空表格内容
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM client";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("cNumber"),
                            resultSet.getString("cName"),
                            resultSet.getString("cAge"),
                            resultSet.getString("cSex"),
                            resultSet.getString("cTel")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(model); // 重新设置表格模型
    }
}
