package web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Date;
import model.Livro;
import model.User;

@WebListener
public class AppListener implements ServletContextListener{
    
    public static final String CLASS_NAME = "org.sqlite.JDBC";
    public static final String URL = "jdbc:sqlite:livraria.db";
    public static String initializelog = "";
    public static Exception exception = null;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try{
            Connection conexao = AppListener.getConnection();
            Statement stmt = conexao.createStatement();
            
            initializelog += new Date() + ": Initializing database creation";
            initializelog += "Creating User table if not exists...";
            stmt.execute(User.getCreateStatement());
            if(User.getUsers().isEmpty())
            {
                initializelog += "Adding default users...";
                User.insertUser("admin", "Administrador", "ADMIN", "1234");
                initializelog += "Admin added";
                
                User.insertUser("guilherme", "Guilherme Matos Santana", "USER", "1234");
                initializelog += "Guilherme added";
                
                User.insertUser("raquel", "Raquel Facchini Batista Franco", "USER", "12345");
                initializelog += "Raquel added";
            }
            initializelog += "done;";
            
            initializelog += "Creating Livro table if not exists...";
            stmt.execute(Livro.getCreateStatement());
            initializelog += "done;";
            
            stmt.close();
            conexao.close();
        }
        catch(Exception ex){
            
            initializelog += "Erro: " + ex.getMessage();
            exception = ex;
        }
    }
    
    public static String getMd5Hash(String text) throws NoSuchAlgorithmException{
        
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(text.getBytes(), 0, text.length());
        
        return new BigInteger(1, m.digest()).toString();
    }
    
    public static Connection getConnection() throws Exception {
        Class.forName(CLASS_NAME);
        return DriverManager.getConnection(URL);
    }
    
}
