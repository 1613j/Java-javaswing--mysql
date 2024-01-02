package com.all.search;

import com.database.helper.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class add_client extends JFrame {
    private JTable table;
    private JTextField cNumberField, cNameField, cAgeField, cSexField, cTelField;

    public add_client() {
        setTitle("所有用户界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建输入字段
        cNumberField = new JTextField(10);
        cNameField = new JTextField(10);
        cAgeField = new JTextField(10);
        cSexField = new JTextField(10);
        cTelField = new JTextField(10);

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 5));
        inputPanel.add(new JLabel("新用户编号"));
        inputPanel.add(new JLabel("新用户名字"));
        inputPanel.add(new JLabel("新用户年龄"));
        inputPanel.add(new JLabel("新用户性别"));
        inputPanel.add(new JLabel("新用户电话"));
        inputPanel.add(cNumberField);
        inputPanel.add(cNameField);
        inputPanel.add(cAgeField);
        inputPanel.add(cSexField);
        inputPanel.add(cTelField);

        // 将输入面板添加到窗体中
        add(inputPanel, BorderLayout.NORTH);

        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("用户编号");
        model.addColumn("用户姓名");
        model.addColumn("用户年龄");
        model.addColumn("用户性别");
        model.addColumn("用户电话");

        // 创建表格并添加到界面中
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 创建添加用户按钮
        refreshPage(); // 添加完成后刷新当前页面
        JButton addButton = new JButton("添加用户");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cNumber = cNumberField.getText();
                String cName = cNameField.getText();
                String cAge = cAgeField.getText();
                String cSex = cSexField.getText();
                String cTel = cTelField.getText();

                if (!cNumber.isEmpty() && !cName.isEmpty() && !cAge.isEmpty() && !cSex.isEmpty() && !cTel.isEmpty()) {
                    addClient(cNumber, cName, cAge, cSex, cTel);
                    JOptionPane.showMessageDialog(add_client.this, "添加完成！");
                    refreshPage(); // 添加完成后刷新当前页面
                } else {
                    JOptionPane.showMessageDialog(add_client.this, "请填写完整信息");
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // 在事件分发线程中创建用户界面
        SwingUtilities.invokeLater(() -> {
            add_client addClient = new add_client();
            addClient.setVisible(true);
        });
    }

    // 添加用户信息的方法
    private void addClient(String cNumber, String cName, String cAge, String cSex, String cTel) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query = "INSERT INTO client (cNumber, cName, cAge, cSex, cTel) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, cNumber);
                preparedStatement.setString(2, cName);
                preparedStatement.setString(3, cAge);
                preparedStatement.setString(4, cSex);
                preparedStatement.setString(5, cTel);
                preparedStatement.executeUpdate();
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
        table.setModel(model); // 重置表格模型
    }
}
