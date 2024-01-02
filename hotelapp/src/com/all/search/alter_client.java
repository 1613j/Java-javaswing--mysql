package com.all.search;

import com.database.helper.DatabaseHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class alter_client extends JFrame {
    private JTable table;
    private JButton editButton;

    private String selectedcNumber;

    public alter_client() {
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
        refreshTable(model); // 从数据库加载数据
        table = new JTable(model);
        // 添加选择监听器
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedcNumber = (String) table.getValueAt(selectedRow, 0);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 创建编辑用户按钮
        editButton = new JButton("编辑用户");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedcNumber != null) {
                    editUser(selectedcNumber);
                } else {
                    JOptionPane.showMessageDialog(alter_client.this, "请选择一个用户");
                }
            }
        });
        add(editButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                alter_client userInterface = new alter_client();
                userInterface.setVisible(true);
            }
        });
    }

    // 编辑用户信息的方法
    private void editUser(String selectedcNumber) {
        JFrame editFrame = new JFrame("编辑用户信息");
        editFrame.setSize(300, 200);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("新的姓名:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel ageLabel = new JLabel("新的年龄:");
        JTextField ageField = new JTextField();
        panel.add(ageLabel);
        panel.add(ageField);

        JLabel sexLabel = new JLabel("新的性别:");
        JTextField sexField = new JTextField();
        panel.add(sexLabel);
        panel.add(sexField);

        JLabel telLabel = new JLabel("新的电话:");
        JTextField telField = new JTextField();
        panel.add(telLabel);
        panel.add(telField);
//创建确定按钮
        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = nameField.getText();
                String newAge = ageField.getText();
                String newSex = sexField.getText();
                String newTel = telField.getText();

                if (newName.equals("") || newAge.equals("") || newSex.equals("") || newTel.equals("")) {
                    JOptionPane.showMessageDialog(editFrame, "输入信息不完整");
                } else {
                    updateUserInfo(selectedcNumber, newName, newAge, newSex, newTel);
                    JOptionPane.showMessageDialog(editFrame, "用户信息修改成功");
                    editFrame.dispose();
                    refreshTable((DefaultTableModel) table.getModel());
                }
            }
        });
        panel.add(confirmButton);
        editFrame.add(panel);
        editFrame.setVisible(true);
    }

    // 更新用户信息的方法
    private void updateUserInfo(String selectedcNumber, String newName, String newAge, String newSex, String newTel) {
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "UPDATE client SET cName=?, cAge=?, cSex=?, cTel=? WHERE cNumber=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newAge);
                preparedStatement.setString(3, newSex);
                preparedStatement.setString(4, newTel);
                preparedStatement.setString(5, selectedcNumber);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 从数据库加载数据的方法
    private void refreshTable(DefaultTableModel model) {
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                model.setRowCount(0); // 清空表格内容
                String query = "SELECT * FROM client";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String cNumber = resultSet.getString("cNumber");
                    String cName = resultSet.getString("cName");
                    String cAge = resultSet.getString("cAge");
                    String cSex = resultSet.getString("cSex");
                    String cTel = resultSet.getString("cTel");
                    model.addRow(new Object[]{cNumber, cName, cAge, cSex, cTel});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
