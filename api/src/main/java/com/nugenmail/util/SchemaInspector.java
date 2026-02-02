package com.nugenmail.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class SchemaInspector {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.0.45:3306/tims?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
        String user = "mailadm";
        String pass = "secret";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection successful!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT config_name FROM mail_config WHERE config_name LIKE '%INIT%' OR config_name LIKE '%MENU%' OR config_name LIKE '%PAGE%'");

            System.out.println("Relevant keys in mail_config:");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
