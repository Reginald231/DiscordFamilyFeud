package com.github.reginald231;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.util.HashMap;

public class DBManager {
    private Connection conn;
    private Dotenv config;
    private String DB_URL;
    private String USER;
    private String PASS;

    public DBManager() {
        try {
            this.DB_URL = config.get("DB_URL");
            this.USER = config.get("DB_USER");
            this.PASS = config.get("DB_PASS");
            Connection conn = DriverManager.getConnection(
                this.DB_URL,
                this.USER,
                this.PASS
            );
            this.conn = conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try (
            PreparedStatement stmt = this.conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `LEADERBOARD`" +
                    " ( `ID` INT NOT NULL ," +
                    " `TEAMNAME` VARCHAR(255) NOT NULL DEFAULT 'ANON' ," +
                    " `TEAMSIZE` INT NOT NULL DEFAULT '5' ," +
                    " `SCORE` INT NOT NULL " +
                    ", `DATE` DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP," +
                    " PRIMARY KEY (`ID`)) ENGINE = InnoDB;"
                )
        ) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("Leaderboard created.");
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private void enterScore(String teamName, int size, int score) {
        String sql =
            "INSERT INTO LEADERBOARD (TEAMNAME, TEAMSIZE, SCORE) VALUES (?,?,?)";

        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, teamName);
            ps.setInt(2, size);
            ps.setInt(3, score);

            ResultSet rs = ps.executeQuery();
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public HashMap getScores(String teamName, int limit) {
        String sql =
            "SELECT SCORE, DATE FROM LEADERBOARD WHERE TEAMNAME = ? " +
            "ORDER BY DATE DESC " +
            "LIMIT ?";

        HashMap hm = new HashMap();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teamName);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.next());
                hm.put(rs.getInt("SCORE"), rs.getDate("DATE"));
            }
            System.out.println(hm);
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return hm;
    }
}
