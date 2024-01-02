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

public class add_leader extends JFrame {
    private JTable table;
    private JTextField lNumberField, lNameField,lSexField, lTelField;

    public add_leader() {
        setTitle("所有管理员界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create input fields
        lNumberField = new JTextField(10);
        lNameField = new JTextField(10);
        lSexField = new JTextField(10);
        lTelField = new JTextField(10);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 5));
        inputPanel.add(new JLabel("新负责人编号"));
        inputPanel.add(new JLabel("新负责人名字"));
        inputPanel.add(new JLabel("新负责人性别"));
        inputPanel.add(new JLabel("新负责人电话"));
        inputPanel.add(lNumberField);
        inputPanel.add(lNameField);
        inputPanel.add(lSexField);
        inputPanel.add(lTelField);

        // Add input panel to the frame
        add(inputPanel, BorderLayout.NORTH);

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("负责人编号");
        model.addColumn("负责人姓名");
        model.addColumn("负责人性别");
        model.addColumn("负责人电话");

        // Create table and add to the interface
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create add client button
        refreshPage(); // 添加完成后刷新当前页面
        JButton addButton = new JButton("添加用户");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lNumber = lNumberField.getText();
                String lName = lNameField.getText();
                String lSex = lSexField.getText();
                String lTel = lTelField.getText();

                if (!lNumber.isEmpty() && !lName.isEmpty() && !lSex.isEmpty() && !lTel.isEmpty()) {
                    add_leader(lNumber, lName, lSex, lTel);
                    JOptionPane.showMessageDialog(add_leader.this, "添加完成！");
                    refreshPage(); // 添加完成后刷新当前页面
                } else {
                    JOptionPane.showMessageDialog(add_leader.this, "请填写完整信息");
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

    private void add_leader(String lNumber, String lName, String lSex, String lTel) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query = "INSERT INTO leader (lNumber, lName, lSex, lTel) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, lNumber);
                preparedStatement.setString(2, lName);
                preparedStatement.setString(3, lSex);
                preparedStatement.setString(4, lTel);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 清除表格内容
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT * FROM leader";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{
                            resultSet.getString("lNumber"),
                            resultSet.getString("lName"),
                            resultSet.getString("lSex"),
                            resultSet.getString("lTel")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(model); // 重新加载表格模型
    }

    public static void main (String[]args){
        SwingUtilities.invokeLater(() -> {
            add_leader addleader = new add_leader();
            addleader.setVisible(true);
        });
    }
}