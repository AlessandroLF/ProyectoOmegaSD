/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sandro
 */
public class BdConnection {
    
    boolean connected;
    Connection con;
    Statement query;
    
    public BdConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/Usuarios", "chiruper", "chiruperadmin");
            connected = true;
            query = con.createStatement();
        }catch(SQLException e){
            connected = false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BdConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int signIn(String uname, String pwd, String email){
        int rs;
        try {
            query = con.createStatement();
            rs = query.executeUpdate("INSERT INTO USERS VALUES ('" + uname + "', '" + pwd + "', '" + email + "')");
            con.commit();
        } catch (SQLException ex) {
            return 0;
        }
        return rs;
    }
    
    public String logIn(String uname){
        try {
            query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT * FROM USERS where username='" + uname + "'");
            String p = "";
            while(rs.next()){
                p = rs.getString("pasword");
            }
            if(!p.equals("")){
                return p;
            }
        } catch (SQLException ex) {
            return "catch";
        }
        return "nada";
    }
    
    public void closeCon(){
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(BdConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> getFollowers(String uname){
        ArrayList<String> ls = new ArrayList();
        try {
            query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT seguidor FROM FOLLOWS where seguido='" + uname + "'");
            while(rs.next()){
                ls.add(rs.getString("seguidor"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BdConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
    
    public ArrayList<String> getFollowing(String uname){
        ArrayList<String> ls = new ArrayList();
        try {
            query = con.createStatement();
            ResultSet rs = query.executeQuery("SELECT seguido FROM FOLLOWS where seguidor='" + uname + "'");
            while(rs.next()){
                ls.add(rs.getString("seguido"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BdConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
    
}
