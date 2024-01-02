package com.all.search;

import com.database.helper.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.UUID;

public class changeRoom extends JFrame {
    private JTable table;

    public changeRoom() {
        setTitle("更换房间");
        setSize(500, 300);
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        // 创建表格
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("订房编号");
        model.addColumn("客户编号");
        model.addColumn("房间编号");
        model.addColumn("订房日期");
        model.addColumn("入住情况");
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 创建确定按钮
        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String bookNumber = (String) model.getValueAt(selectedRow, 0);
                    String cNumber = (String) model.getValueAt(selectedRow, 1);
                    String rNumber = (String) model.getValueAt(selectedRow, 2);
                    updateCheckinStatus(bookNumber);
                    updateRoomState(rNumber);
                    insertCheckoutData(bookNumber, cNumber, rNumber);
                    openChangeRoomDialog();
//                    refreshPage();
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);

        // 从数据库加载数据
        loadDataFromDatabase(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            changeRoom bookRoomViewer = new changeRoom();
            bookRoomViewer.setVisible(true);
        });
    }

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
//将oldroom的roomstate更改为已退房的方法
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

    private void insertCheckoutData(String bookNumber, String cNumber, String rNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String insertQuery = "INSERT INTO checkout (outNumber, cNumber, rNumber, outDate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, bookNumber); // Assuming bookNumber is the outNumber
                insertStatement.setString(2, cNumber);
                insertStatement.setString(3, rNumber);
                insertStatement.setDate(4, new Date(System.currentTimeMillis())); // Use current date as outDate
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void openChangeRoomDialog() {
        JFrame changeRoomDialog = new JFrame("Change Room");
        changeRoomDialog.setSize(500, 300);
        changeRoomDialog.setLocationRelativeTo(null); // 居中显示

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("换房理由:"));
        JTextField reasonField = new JTextField();
        panel.add(reasonField);
        panel.add(new JLabel("新房间:"));
        JTextField newRoomField = new JTextField();
        panel.add(newRoomField);
        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reason = reasonField.getText();
                String newRoom = newRoomField.getText();
                insertChangeRoomData(reason, newRoom);
                refreshPage();
                changeRoomDialog.dispose();

                // 弹出换房成功的消息
                JOptionPane.showMessageDialog(null, "换房成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(confirmButton);
        changeRoomDialog.add(panel);
        changeRoomDialog.setVisible(true);
    }

    private void insertChangeRoomData(String reason, String newRoom) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String changeNumber = generateChangeNumber();
                String cNumber = (String) table.getValueAt(table.getSelectedRow(), 1);
                String rNumber = newRoom;
                Date currentDate = new Date(System.currentTimeMillis());//te() is a method to get current date

                // 插入换房记录
                String insertChangeQuery = "INSERT INTO changeroom (changeNumber, cNumber, oldroom, reason, newroom) VALUES (?, ?, ?, ?, ?)";
                String insertChangeQuery2 = "UPDATE bookroom SET rNumber=? where cNumber=? AND rNumber=?";
                String insertChangeQuery3 = "UPDATE bookroom SET checkin='已入住' where cNumber=? AND rNumber=?";
                String insertChangeQuery4 = "UPDATE bookroom SET bookNumber=?  where cNumber=? AND rNumber=?";
                PreparedStatement insertChangeStatement = connection.prepareStatement(insertChangeQuery);
                PreparedStatement insertChangeStatement2 = connection.prepareStatement(insertChangeQuery2);
                PreparedStatement insertChangeStatement3 = connection.prepareStatement(insertChangeQuery3);
                PreparedStatement insertChangeStatement4 = connection.prepareStatement(insertChangeQuery4);
                insertChangeStatement.setString(1, changeNumber);
                insertChangeStatement.setString(2, cNumber);
                insertChangeStatement.setString(3, (String) table.getValueAt(table.getSelectedRow(), 2));
                insertChangeStatement.setString(4, reason);
                insertChangeStatement.setString(5, newRoom);
                insertChangeStatement2.setString(1, newRoom);
                insertChangeStatement2.setString(2, cNumber);
                insertChangeStatement2.setString(3, (String) table.getValueAt(table.getSelectedRow(), 2));
                insertChangeStatement3.setString(1, cNumber);
                insertChangeStatement3.setString(2, newRoom);
                insertChangeStatement4.setString(1, changeNumber);
                insertChangeStatement4.setString(2, cNumber);
                insertChangeStatement4.setString(3, newRoom);
                insertChangeStatement.executeUpdate();
                insertChangeStatement2.executeUpdate();
                insertChangeStatement3.executeUpdate();
                insertChangeStatement4.executeUpdate();

                // 更新roomstate表中对应房间的状态为已入住
                updateRoomStateToOccupied(newRoom);

                // 在checkin表内插入新数据
                insertCheckinData(changeNumber, cNumber, rNumber, currentDate);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertCheckinData(String inNumber, String cNumber, String rNumber, Date checkDate) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String insertQuery = "INSERT INTO checkin (inNumber, cNumber, rNumber, checkDate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, inNumber);
                insertStatement.setString(2, cNumber);
                insertStatement.setString(3, rNumber);
                insertStatement.setDate(4, checkDate);
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//将roomstate表内相对应的房间设置为已入住
    private void updateRoomStateToOccupied(String rNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String updateQuery = "UPDATE roomstate SET state = '已入住' WHERE rNumber = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, rNumber);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//随机生成换房编号的方法
    private String generateChangeNumber() {
        UUID uuid = UUID.randomUUID();
        String randomBookingNumber = uuid.toString().replace("-", "").substring(5, 11);
        return "CR" + randomBookingNumber;
    }
//刷新页面的方法
    private void refreshPage() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 清空表格内容
        loadDataFromDatabase(model);
        table.setModel(model); // 重置表格模型
    }
}