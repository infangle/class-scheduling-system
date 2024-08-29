        package com.aastu.scheduler.utility;
        import java.sql.*;

        public class DatabaseUtility {

                private static final String URL = "jdbc:mysql://localhost:3306/schedules";
                private static final String USER = "root";
                private static final String PASSWORD = "Deshet";

                public static Connection getConnection() throws SQLException {

                    return DriverManager.getConnection(URL, USER, PASSWORD);
                }


        }
