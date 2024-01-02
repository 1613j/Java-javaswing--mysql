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

public class add_room extends JFrame {
    private JTable table;
    private JTextField rNumberField, rTypeField, rPriceField, lNumberField, lTelField;

    public add_room() {
        setTitle("所有房间界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create input fields
        rNumberField = new JTextField(10);
        rTypeField = new JTextField(10);
        rPriceField = new JTextField(10);
        lNumberField = new JTextField(10);
        lTelField = new JTextField(10);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 5));
        inputPanel.add(new JLabel("新房间编号"));
        inputPanel.add(new JLabel("新房间类型"));
        inputPanel.add(new JLabel("新房间价格"));
        inputPanel.add(new JLabel("新房间负责人"));
        inputPanel.add(new JLabel("新房间电话"));
        inputPanel.add(rNumberField);
        inputPanel.add(rTypeField);
        inputPanel.add(rPriceField);
        inputPanel.add(lNumberField);
        inputPanel.add(lTelField);

        // Add input panel to the frame
        add(inputPanel, BorderLayout.NORTH);

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("房间编号");
        model.addColumn("房间类型");
        model.addColumn("房间价格");
        model.addColumn("负责人编号");
        model.addColumn("房间电话");

        // Create table and add to the interface
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create add client button
        refreshPage(); // 添加完成后刷新当前页面
        JButton addButton = new JButton("添加房间");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cNumber = rNumberField.getText();
                String cName = rTypeField.getText();
                String cAge = rPriceField.getText();
                String cSex = lNumberField.getText();
                String cTel = lTelField.getText();

                if (!cNumber.isEmpty() && !cName.isEmpty() && !cAge.isEmpty() && !cSex.isEmpty() && !cTel.isEmpty()) {
                    addClient(cNumber, cName, cAge, cSex, cTel);
                    JOptionPane.showMessageDialog(add_room.this, "添加完成！");
                    refreshPage(); // 添加完成后刷新当前页面
                } else {
                    JOptionPane.showMessageDialog(add_room.this, "请填写完整信息");
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

    private void addClient(String rNumber, String rType, String rPrice, String lNumber, String lTel) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query = "INSERT INTO rooms (rNumber, rType, rPrice, lNumber, lTel) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, rNumber);
                preparedStatement.setString(2, rType);
                preparedStatement.setString(3, rPrice);
                preparedStatement.setString(4, lNumber);
                preparedStatement.setString(5, lTel);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the table content
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
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
        table.setModel(model); // Reset the table model
    }

    public static void main (String[]args){
        SwingUtilities.invokeLater(() -> {
            add_room addroom = new add_room();
            addroom.setVisible(true);
        });
    }
}