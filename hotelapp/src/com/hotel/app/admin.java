package com.hotel.app;

import com.all.search.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class admin extends JFrame {
    public admin() {
        setTitle("酒店住房管理系统"); // 设置窗口标题
        setSize(500, 300); // 设置窗口大小
        setLocationRelativeTo(null); // 将窗口位置设置为屏幕中央
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭时结束所有程序

        setLayout(new GridLayout(5, 3)); // 使用5行3列的网格布局

        // 创建并添加"删除用户"按钮
        JButton deleteClientBtn = new JButton("删除用户");
        deleteClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete_client.main(new String[]{});
            }
        });

        // 创建并添加"删除房间"按钮
        JButton deleteRoomBtn = new JButton("删除房间");
        deleteRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete_room.main(new String[]{});
            }
        });

        // 创建并添加"删除负责人"按钮
        JButton deleteLeaderBtn = new JButton("删除负责人");
        deleteLeaderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete_leader.main(new String[]{});
            }
        });

        // 创建并添加"添加用户"按钮
        JButton addClientBtn = new JButton("添加用户");
        addClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_client.main(new String[]{});
            }
        });

        // 创建并添加"添加房间"按钮
        JButton addRoomBtn = new JButton("添加房间");
        addRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_room.main(new String[]{});
            }
        });

        // 创建并添加"添加负责人"按钮
        JButton addLeaderBtn = new JButton("添加负责人");
        addLeaderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_leader.main(new String[]{});
            }
        });

        // 创建并添加"入住管理"按钮
        JButton checkInBtn = new JButton("入住管理");
        checkInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIn.main(new String[]{});
            }
        });

        // 创建并添加"退房管理"按钮
        JButton checkOutBtn = new JButton("退房管理");
        checkOutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkOut.main(new String[]{});
            }
        });

        // 创建并添加"换房管理"按钮
        JButton changeRoomBtn = new JButton("换房管理");
        changeRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeRoom.main(new String[]{});
            }
        });

        // 创建并添加"用户信息管理"按钮
        JButton alterClientBtn = new JButton("用户信息管理");
        alterClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alter_client.main(new String[]{});
            }
        });

        // 创建并添加"房间信息管理"按钮
        JButton alterRoomBtn = new JButton("房间信息管理");
        alterRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alter_room.main(new String[]{});
            }
        });

        // 创建并添加"负责人信息管理"按钮
        JButton alterLeaderBtn = new JButton("负责人信息管理");
        alterLeaderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alter_leader.main(new String[]{});
            }
        });
        // 创建并添加"所有房间信息"按钮
        JButton allRoomBtn = new JButton("所有房间信息");
        allRoomBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allRoom.main(new String[]{});
            }
        });

        // 将按钮添加到窗口中
        add(deleteClientBtn);
        add(deleteRoomBtn);
        add(deleteLeaderBtn);
        add(addClientBtn);
        add(addRoomBtn);
        add(addLeaderBtn);
        add(checkInBtn);
        add(checkOutBtn);
        add(changeRoomBtn);
        add(alterClientBtn);
        add(alterRoomBtn);
        add(alterLeaderBtn);
        add(allRoomBtn);

        setVisible(true); // 设置窗口可见
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new admin(); // 创建并显示管理员界面
            }
        });
    }
}
