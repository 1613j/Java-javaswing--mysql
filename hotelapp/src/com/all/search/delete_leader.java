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

public class delete_leader extends JFrame {
    private JTable table;
    private String selectedlNumber; // 保存选择的 lNumber
    private JButton confirmButton;

    public delete_leader() {
        setTitle("所有用户界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("负责人编号");
        model.addColumn("负责人姓名");
        model.addColumn("负责人性别");
        model.addColumn("负责人电话");

        // 从数据库获取数据并添加到表格模型
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM leader";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("lNumber"),
                            resultSet.getString("lName"),
                            resultSet.getString("lSex"),
                            resultSet.getString("lTel")});
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
                        selectedlNumber = (String) table.getValueAt(selectedRow, 0); // 保存选择的 lNumber
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        // 创建删除负责人按钮
        confirmButton = new JButton("删除负责人");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedlNumber != null) {
                    delete(selectedlNumber);
                    JOptionPane.showMessageDialog(delete_leader.this, selectedlNumber + "删除成功");
                    refreshPage(); // 在点击删除后刷新页面
                } else {
                    JOptionPane.showMessageDialog(delete_leader.this, "请选择一个负责人");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);
    }
    private void delete(String selectedlNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query1 = "UPDATE rooms SET lNumber = 'NULL' where lNumber = '" + selectedlNumber + "'";
                String query2 = "DELETE FROM leader where lNumber = '" + selectedlNumber + "'";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.executeUpdate();
                preparedStatement2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 清空表格内容
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM leader";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("lNumber"),
                            resultSet.getString("lName"),
                            resultSet.getString("lSex"),
                            resultSet.getString("lTel")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(model); // 重新设置表格模型
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delete_leader members = new delete_leader();
                members.setVisible(true);
            }
        });
    }
}