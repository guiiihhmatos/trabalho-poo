/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import web.app.AppListener;

public class Emprestimo {
    
    public Emprestimo(){}
    
    private long rowId;
    private Long idUsuario;
    private Long idLivro;
    private Integer data_emprestimo;
    
    
    public static String getCreateStatement(){
        return "CREATE TABLE IF NOT EXISTS emprestimo("
                +"idUsuario BIGINT NOT NULL,"
                +"idLivro BIGINT NOT NULL,"
                +"data_emprestimo datetime NOT NULL,"
                +"Foreign key(idLivro) REFERENCES livro(rowId),"
                +"Foreign key(idUsuario) REFERENCES users(rowId)"
                +");";      
    }

    public Emprestimo(long rowId, Long idUsuario, Long idLivro, Integer data_emprestimo) {
        this.rowId = rowId;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.data_emprestimo = data_emprestimo;
    }
     
     public static ArrayList<Emprestimo> getAllEmprestimo() throws Exception{
        
        ArrayList<Emprestimo> list = new ArrayList<>();
        Connection conexao = AppListener.getConnection();
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from emprestimo ");
        
        while(rs.next())
        {
            long rowId = rs.getLong("rowid");
            long idUsuario = rs.getLong("idUsuario");
            long idLivro = rs.getLong("idLivro");
            int data_emprestimo = rs.getInt("data_emprestimo");
            
            list.add(new Emprestimo(rowId, idUsuario, idLivro, data_emprestimo));
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return list;
    }
     
     
    public static boolean insertEmprestimo(Long idUsuario, Long idLivro, Integer data_emprestimo) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "INSERT INTO emprestimo(idUsuario, idLivro, data_emprestimo)"
                + "VALUES (?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setLong(1, idUsuario);
        stmt.setLong(2, idLivro);
        stmt.setInt(3, data_emprestimo);
        int rowsAffected = stmt.executeUpdate();
            
        conexao.close();
        stmt.close();
        
        return rowsAffected > 0;
    }
    
    
}