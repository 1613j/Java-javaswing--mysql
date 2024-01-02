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

public class delete_room extends JFrame {
    private JTable table;
    private String selectedrNumber; // 保存选择的 cNumber
    private JButton confirmButton;

    public delete_room() {
        setTitle("所有房间界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("房间编号");
        model.addColumn("房间类型");
        model.addColumn("房间价格");
        model.addColumn("负责人编号");
        model.addColumn("负责人电话");

        // 从数据库获取数据并添加到表格模型
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

        // 创建表格并添加到界面
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedrNumber = (String) table.getValueAt(selectedRow, 0); // 保存选择的 cNumber
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        // 创建删除房间按钮
        confirmButton = new JButton("删除房间");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedrNumber != null) {
                    delete(selectedrNumber);
                    JOptionPane.showMessageDialog(delete_room.this, "房间" + selectedrNumber + "删除成功");
                    refreshPage(); // 在点击删除后刷新页面
                } else {
                    JOptionPane.showMessageDialog(delete_room.this, "请选择一个房间");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);
    }



    private void delete(String selectedrNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query1 = "DELETE FROM rooms where rNumber = '" + selectedrNumber + "'";
                String query2 = "DELETE FROM roomstate where rNumber = '" + selectedrNumber + "'";
                String query3 = "DELETE FROM bookroom where rNumber = '" + selectedrNumber + "'";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                preparedStatement2.executeUpdate();
                preparedStatement3.executeUpdate();
                preparedStatement1.executeUpdate();
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
        table.setModel(model); // 重新设置表格模型
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delete_room members = new delete_room();
                members.setVisible(true);
            }
        });
    }
}