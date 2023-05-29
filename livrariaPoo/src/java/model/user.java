/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.sql.*;
import web.AppListener;

public class User {
    
    private long rowId;
    private String login;
    private String name;
    private String role;
    private String passwordHash;

    public User(long rowId, String login, String name, String role, String passwordHash) {
        this.rowId = rowId;
        this.name = name;
        this.login = login;
        this.role = role;
        this.passwordHash = passwordHash;
    }
    
    public static String getCreateStatement(){
        return "CREATE TABLE IF NOT EXISTS users("
                +"login VARCHAR(50) UNIQUE NOT NULL,"
                +"name VARCHAR(200) NOT NULL,"
                +"role VARCHAR(20) NOT NULL,"
                +"password_hash VARCHAR(50) NOT NULL"
                +");";      
    }
    
    public static ArrayList<User> getUsers() throws Exception{
        
        ArrayList<User> list = new ArrayList<>();
        Connection conexao = AppListener.getConnection();
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from users");
        
        while(rs.next())
        {
            long rowId = rs.getLong("rowid");
            String login = rs.getString("login");
            String name = rs.getString("name");
            String role = rs.getString("role");
            String passwordHash = rs.getString("password_hash");
            
            list.add(new User(rowId, name, login, role, passwordHash));
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return list;
    }
    
    public static User getUser(String login, String password) throws Exception{
        
        User user = null;
        Connection conexao = AppListener.getConnection();
        String sql = "SELECT rowid, * from users WHERE login=? AND password_hash=?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, login);
        stmt.setString(2, AppListener.getMd5Hash(password));
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            long rowId = rs.getLong("rowid");
            String name = rs.getString("name");
            String role = rs.getString("role");
            String passwordHash = rs.getString("password_hash");
            
            user = new User(rowId, login, name, role, passwordHash);
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return user;
    }
    
    public static void insertUser(String login, String name, String role, String password) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "INSERT INTO users(login, name, role, password_hash)"
                + "VALUES (?,?,?,?)";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, login);
        stmt.setString(2, name);
        stmt.setString(3, role);
        stmt.setString(4, AppListener.getMd5Hash(password));
        
        stmt.execute();
        conexao.close();
        stmt.close();
        
    }
    
    public static void updateUser(String login, String name, String role, String password) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "UPDATE users SET name=?, role=?, password_hash=? WHERE login=?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, name);
        stmt.setString(2, role);
        stmt.setString(3, AppListener.getMd5Hash(password));
        stmt.setString(4, login);
        
        stmt.execute();
        conexao.close();
        stmt.close();
        
    }
    
    public static void deleteUser(long rowId) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "DELETE FROM users WHERE rowid=?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setLong(1, rowId);
        stmt.execute();
        conexao.close();
        stmt.close();
    }

    public long getId() {
        return rowId;
    }

    public void setId(long rowId) {
        this.rowId = rowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    
}
