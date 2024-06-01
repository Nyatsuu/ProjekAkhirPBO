package com.projekakhir;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    Connection con;

    public DB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dbpizza", "root", "");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
