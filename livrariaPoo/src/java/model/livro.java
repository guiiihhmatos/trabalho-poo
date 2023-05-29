/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.ArrayList;
import java.sql.*;
import web.AppListener;

public class Livro {
    
    private long rowId;
    private String titulo;
    private String autor;
    private String editora;
    private String ano_publicacao;
    private String isbn;
    private String descricao;
    private String disponibilidade;

    public Livro(long rowId, String titulo, String autor, String editora, String ano_publicacao, String isbn, String descricao, String disponibilidade) {
        this.rowId = rowId;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ano_publicacao = ano_publicacao;
        this.isbn = isbn;
        this.descricao = descricao;
        this.disponibilidade = disponibilidade;
    }
    
    public static String getCreateStatement(){
        return "CREATE TABLE IF NOT EXISTS livro("
                +"titulo VARCHAR(50) NOT NULL,"
                +"autor VARCHAR(100) NOT NULL,"
                +"editora VARCHAR(40) NOT NULL,"
                +"ano_publicacao datetime NOT NULL,"
                +"isbn VARCHAR(13) UNIQUE NOT NULL,"
                +"descricao VARCHAR(150) NOT NULL,"
                +"disponibilidade VARCHAR(40) NOT NULL"
                +");";      
    }
    
     public static ArrayList<Livro> getLivros() throws Exception{
        
        ArrayList<Livro> list = new ArrayList<>();
        Connection conexao = AppListener.getConnection();
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from users");
        
        while(rs.next())
        {
            long rowId = rs.getLong("rowid");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String editora = rs.getString("editora");
            String ano_publicacao = rs.getString("ano_publicacao");
            String isbn = rs.getString("isbn");
            String descricao = rs.getString("descricao");
            String disponibilidade = rs.getString("disponibilidade");
            
            
            list.add(new Livro(rowId, titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade));
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return list;
    }
    
    public static Livro getLivro(String isbn) throws Exception{
        
        Livro livro = null;
        Connection conexao = AppListener.getConnection();
        String sql = "SELECT rowid, * from livro WHERE isbn=?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            long rowId = rs.getLong("rowid");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String editora = rs.getString("editora");
            String ano_publicacao = rs.getString("ano_publicacao");
            String descricao = rs.getString("descricao");
            String disponibilidade = rs.getString("disponibilidade");
            
            livro = new Livro(rowId, titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade);
            
            
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return livro;
    }
    
    public static void insertLivro(String titulo, String autor, String editora, String ano_publicacao, String isbn, String descricao, String disponibilidade) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "INSERT INTO users(titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade)"
                + "VALUES (?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, titulo);
        stmt.setString(2, autor);
        stmt.setString(3, editora);
        stmt.setString(4, ano_publicacao);
        stmt.setString(5, isbn);
        stmt.setString(6, descricao);
        stmt.setString(7, disponibilidade);
        
        stmt.execute();
        conexao.close();
        stmt.close();
        
    }
    
    public static void updateLivro(String titulo, String autor, String editora, String ano_publicacao, String isbn, String descricao, String disponibilidade) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "UPDATE users SET titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade WHERE isbn=?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, titulo);
        stmt.setString(2, autor);
        stmt.setString(3, editora);
        stmt.setString(4, ano_publicacao);
        stmt.setString(5, descricao);
        stmt.setString(6, disponibilidade);
        stmt.setString(7, isbn);
        
        stmt.execute();
        conexao.close();
        stmt.close();
        
    }
    
    public static void deleteLivro(long rowId) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "DELETE FROM livro WHERE rowid=?";
        
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getAno_publicacao() {
        return ano_publicacao;
    }

    public void setAno_publicacao(String ano_publicacao) {
        this.ano_publicacao = ano_publicacao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
    
    
}
