package com.nugenmail.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbCheck {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.0.45:3306/tims?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
        String user = "mailadm";
        String pass = "secret";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection successful!");

            String sql = "SELECT m.mail_uid " +
                    "FROM mail_user m, mail_user_info i, mail_domain d " +
                    "WHERE m.mail_uid = ? " +
                    "AND m.mail_domain_seq = (select mail_domain_seq from mail_domain where mail_domain = ?) " +
                    "AND i.mail_user_seq = m.mail_user_seq " +
                    "AND d.mail_domain_seq = m.mail_domain_seq " +
                    "AND m.user_type='mailUser'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "mailadm");
            pstmt.setString(2, "sogang.ac.kr");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("FULL QUERY User Found: " + rs.getString("mail_uid"));
            } else {
                System.out.println("FULL QUERY User NOT Found. Please check mail_user_info and mail_domain tables.");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
