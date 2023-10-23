package com.github.reginald231;

import com.mysql.cj.protocol.Resultset;
import java.sql.*;
import java.util.HashMap;

public class DBManager {

    FeudManager fm;
    private Connection conn;
    private final String DB_URL = "";
    private final String USER = "";
    private final String PASS = "";

    public DBManager(FeudManager fm) {
        try{
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            this.conn = conn;
            this.fm = fm;

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void start(){

        try(PreparedStatement stmt = this.conn.prepareStatement("CREATE TABLE IF NOT EXISTS `LEADERBOARD`" +
                    " ( `ID` INT NOT NULL ," +
                    " `TEAMNAME` VARCHAR(255) NOT NULL DEFAULT 'ANON' ," +
                    " `TEAMSIZE` INT NOT NULL DEFAULT '5' ," +
                    " `SCORE` INT NOT NULL " +
                    ", `DATE` DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP," +
                    " PRIMARY KEY (`ID`)) ENGINE = InnoDB;")
        ){
            ResultSet rs =  stmt.executeQuery();

            System.out.println("Leaderboard created.");
            rs.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try{
            this.conn.close();
        }catch (SQLException se){
           se.printStackTrace();
        }
    }

    private void enterScore(String teamName, int size, int score) {
        String sql = "INSERT INTO LEADERBOARD (TEAMNAME, TEAMSIZE, SCORE) VALUES (?,?,?)";

        try(PreparedStatement ps = this.conn.prepareStatement(sql)){

            ps.setString(1, teamName);
            ps.setInt(2, size);
            ps.setInt(3, score);

            ResultSet rs =  ps.executeQuery();
            rs.close();


        }catch (SQLException se){
            se.printStackTrace();
        }

    }

    public void getScores(String teamName, int limit) {
        String sql = "SELECT SCORES, DATE FROM TEAMNAME WHERE TEAMNAME = ?" +
                "ORDER BY DATE DESC" +
                "LIMIT ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, teamName);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();
            HashMap hm = new HashMap();


            while(rs.next()){
                System.out.println(rs.next());
                hm.put(rs.getInt("SCORE"), rs.getDate("DATE"));
            }
            System.out.println(hm);
            rs.close();

        }catch (SQLException se){
            se.printStackTrace();
        }
    }


}
