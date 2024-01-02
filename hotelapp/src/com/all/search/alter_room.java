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

public class alter_room extends JFrame {
    private JTable table;
    private JButton editButton;

    private String selectedrNumber;

    public alter_room() {
        setTitle("所有房间界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("房间编号");
        model.addColumn("房间类型");
        model.addColumn("房间价格");
        model.addColumn("负责人编号");
        model.addColumn("负责人电话");
        refreshTable(model);
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedrNumber = (String) table.getValueAt(selectedRow, 0);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        editButton = new JButton("编辑房间");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedrNumber != null) {
                    editUser(selectedrNumber);
                } else {
                    JOptionPane.showMessageDialog(alter_room.this, "请选择一个用户");
                }
            }
        });
        add(editButton, BorderLayout.SOUTH);
    }

    private void editUser(String selectedrNumber) {
        JFrame editFrame = new JFrame("编辑房间信息");
        editFrame.setSize(300, 200);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel typeLabel = new JLabel("新的房间类型:");
        JTextField typeField = new JTextField();
        panel.add(typeLabel);
        panel.add(typeField);

        JLabel priceLabel = new JLabel("新的房间价格:");
        JTextField priceField = new JTextField();
        panel.add(priceLabel);
        panel.add(priceField);

        JLabel numberLabel = new JLabel("新的负责人编号:");
        JTextField numberField = new JTextField();
        panel.add(numberLabel);
        panel.add(numberField);

        JLabel telLabel = new JLabel("新的负责人电话:");
        JTextField telField = new JTextField();
        panel.add(telLabel);
        panel.add(telField);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newType = typeField.getText();
                String newPrice = priceField.getText();
                String newNumber = numberField.getText();
                String newTel = telField.getText();

                if (newType.equals("") || newPrice.equals("") || newNumber.equals("") || newTel.equals("")) {
                    JOptionPane.showMessageDialog(editFrame, "输入信息不完整");
                } else {
                    updateUserInfo(selectedrNumber, newType, newPrice, newNumber, newTel);
                    JOptionPane.showMessageDialog(editFrame, "房间信息修改成功");
                    editFrame.dispose();
                    refreshTable((DefaultTableModel) table.getModel());
                }
            }
        });
        panel.add(confirmButton);
        editFrame.add(panel);
        editFrame.setVisible(true);
    }

    private void updateUserInfo(String selectedrNumber, String newType, String newPrice, String newNumber, String newTel) {
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "UPDATE rooms SET rType=?, rPrice=?, lNumber=?, lTel=? WHERE rNumber=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, newType);
                preparedStatement.setString(2, newPrice);
                preparedStatement.setString(3, newNumber);
                preparedStatement.setString(4, newTel);
                preparedStatement.setString(5, selectedrNumber);
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
                String query = "SELECT * FROM rooms";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("rNumber"),
                            resultSet.getString("rType"),
                            resultSet.getString("rPrice"),
                            resultSet.getString("lNumber"),
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
                alter_room userInterface = new alter_room();
                userInterface.setVisible(true);
            }
        });
    }
}
