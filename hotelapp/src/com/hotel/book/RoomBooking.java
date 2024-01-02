package com.hotel.book;

import com.database.helper.DatabaseHelper;
import com.hotel.app.LoginPage;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RoomBooking extends JFrame {
    private JTable table;
    private JButton confirmButton;
    private String selectedRNumber; // 保存用户选择的 rNumber
    private String enteredUsername; // 保存用户登录的用户名


    public RoomBooking(String username) {
        this.enteredUsername = username;  // 保存用户登录的用户名
        setTitle("预定房间");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("房间号");
        model.addColumn("房间类型");
        model.addColumn("价格");

        // 从数据库获取数据并添加到表格模型
        try {
            Connection conn = DatabaseHelper.getConnection();
            if (conn != null) {
                String query = "SELECT rNumber, rType, rPrice FROM rooms";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getString("rNumber"),
                            resultSet.getString("rType"),
                            resultSet.getString("rPrice")});
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
                        selectedRNumber = (String) table.getValueAt(selectedRow, 0); // 保存用户选择的 rNumber
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 创建确定按钮
        confirmButton = new JButton("确定");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRNumber != null) {
                    updateRoomState(selectedRNumber); // 使用用户选择的 rNumber
                    bookUpdate(selectedRNumber, enteredUsername); // 添加预订信息到数据库的bookroom表
                    JOptionPane.showMessageDialog(RoomBooking.this, selectedRNumber + "预订成功");
                } else {
                    JOptionPane.showMessageDialog(RoomBooking.this, "请选择一个房间");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);
        // 创建查询预约按钮
        JButton queryButton = new JButton("查询预约");
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CancelBook.main(new String[]{});
            }
        });
        add(queryButton, BorderLayout.EAST);  // 将查询预约按钮添加到界面的右边
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String username = LoginPage.enteredUsername;
                RoomBooking roomBooking = new RoomBooking(username);
                roomBooking.setVisible(true);
            }
        });
    }

    private void updateRoomState(String selectedRNumber) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                String query = "UPDATE roomstate SET state = '已预订' WHERE rNumber = '" + selectedRNumber + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void bookUpdate(String selectedRNumber, String enteredUsername) {
        Connection connection = DatabaseHelper.getConnection();
        if (connection != null) {
            try {
                // 获取当前日期
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateStr = dateFormat.format(currentDate);

                String query = "INSERT INTO bookroom (bookNumber, cNumber, rNumber, bookdate, checkin) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, generateBookNumber()); // 生成预订号
                preparedStatement.setString(2, enteredUsername);
                preparedStatement.setString(3, selectedRNumber);
                preparedStatement.setString(4, currentDateStr);
                preparedStatement.setString(5, "未入住");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateBookNumber() {
        // 创建一个随机生成预定序号的方法
        UUID uuid = UUID.randomUUID();
        String randomBookingNumber = uuid.toString().replace("-", "").substring(5, 11);
        return "BOOK" + randomBookingNumber;
    }
}