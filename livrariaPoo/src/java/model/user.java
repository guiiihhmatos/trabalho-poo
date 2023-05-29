/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class user {
    
    private long id;
    private String name;
    private String login;
    private String role;
    private String passwordHash;

    public user(long id, String name, String login, String role, String passwordHash) {
        this.id = id;
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
                +"password_hash VARCHAR UNIQUE NOT NULL,"
                +")";
                
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
