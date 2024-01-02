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

public class alter_leader extends JFrame {
    private JTable table;
    private JButton editButton;

    private String selectedlNumber;

    public alter_leader() {
        setTitle("所有用户界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("负责人编号");
        model.addColumn("负责人姓名");
        model.addColumn("负责人性别");
        model.addColumn("负责人电话");
        refreshTable(model);
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedlNumber = (String) table.getValueAt(selectedRow, 0);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        editButton = new JButton("编辑负责人");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedlNumber != null) {
                    editUser(selectedlNumber);
                } else {
                    JOptionPane.showMessageDialog(alter_leader.this, "请选择一个用户");
                }
            }
        });
        add(editButton, BorderLayout.SOUTH);
    }

    private void editUser(String selectedlNumber) {
        JFrame editFrame = new JFrame("编辑负责人信息");
        editFrame.setSize(300, 200);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("新的姓名:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);


        JLabel sexLabel = new JLabel("新的性别:");
        JTextField sexField = new JTextField();
        panel.add(sexLabel);
        panel.add(sexField);

        JLabel telLabel = new JLabel("新的电话:");
        JTextField telField = new JTextField();
        panel.add(telLabel);
        panel.add(telField);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = nameField.getText();
                String newSex = sexField.getText();
                String newTel = telField.getText();

                if (newName.equals("") ||newSex.equals("") || newTel.equals("")) {
                    JOptionPane.showMessageDialog(editFrame, "输入信息不完整");
                } else {
                    updateUserInfo(selectedlNumber, newName,  newSex, newTel);
                    JOptionPane.showMessageDialog(editFrame, "负责人信息修改成功");
                    editFrame.dispose();
                    refreshTable((DefaultTableModel) table.getModel());
                }
            }
        });
        panel.add(confirmButton);
        editFrame.add(panel);
        editFrame.setVisible(true);
    }

    private void updateUserInfo(String selectedlNumber, String newName,  String newSex, String newTel) {
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "UPDATE leader SET lName=?,lSex=?, lTel=? WHERE lNumber=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newSex);
                preparedStatement.setString(3, newTel);
                preparedStatement.setString(4, selectedlNumber);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                model.setRowCount(0); // 清空表格内容
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
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                alter_leader userInterface = new alter_leader();
                userInterface.setVisible(true);
            }
        });
    }
}
