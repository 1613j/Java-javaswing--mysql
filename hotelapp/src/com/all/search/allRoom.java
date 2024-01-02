package com.all.search;

import com.database.helper.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class allRoom extends JFrame {

    public allRoom() {
        setTitle("房间总数查询");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建标签用于显示房间总数
        JLabel roomCountLabel = new JLabel("房间总数：");
        JLabel inCountLabel = new JLabel("入住房间总数：");
        JLabel bookCountLabel = new JLabel("预定房间总数：");
        JLabel outCountLabel = new JLabel("退房房间总数：");
        roomCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        outCountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 使用布局管理器来管理组件位置
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(roomCountLabel);
        panel.add(inCountLabel);
        panel.add(bookCountLabel);
        panel.add(outCountLabel);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(panel);

        // 从数据库获取房间总数并显示在标签上
        try {
            // 连接到数据库
            Connection conn = DatabaseHelper.getConnection();

            // 执行查询
            String query1 = "SELECT COUNT(*) FROM rooms";
            String query2 = "SELECT COUNT(*) FROM roomstate where state='已入住'";
            String query3 = "SELECT COUNT(*) FROM roomstate where state='已预订' ";
            String query4 = "SELECT COUNT(*) FROM roomstate where state='已退房' ";
            PreparedStatement preparedStatement1 = conn.prepareStatement(query1);
            PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
            PreparedStatement preparedStatement3 = conn.prepareStatement(query3);
            PreparedStatement preparedStatement4 = conn.prepareStatement(query4);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            ResultSet resultSet4 = preparedStatement4.executeQuery();
            resultSet1.next();
            resultSet2.next();
            resultSet3.next();
            resultSet4.next();
            int roomCount = resultSet1.getInt(1);
            int inCount = resultSet2.getInt(1);
            int bookCount = resultSet3.getInt(1);
            int outCount = resultSet4.getInt(1);

            // 在标签上显示房间总数
            roomCountLabel.setText("房间总数：" + roomCount);
            inCountLabel.setText("入住房间总数：" + inCount);
            bookCountLabel.setText("预订房间总数：" + bookCount);
            outCountLabel.setText("退房房间总数：" + outCount);
        } catch (SQLException e) {
            e.printStackTrace();
            roomCountLabel.setText("无法获取房间信息");
            roomCountLabel.setText("无法获取入住信息");
            roomCountLabel.setText("无法获取预定信息");
            roomCountLabel.setText("无法获取退房信息");
        }
    }

    // 主方法
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                allRoom roomCountGUI = new allRoom();
                roomCountGUI.setVisible(true);
            }
        });
    }
}
