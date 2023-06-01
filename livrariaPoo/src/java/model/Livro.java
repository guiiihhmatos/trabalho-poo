package model;

import java.util.ArrayList;
import java.sql.*;
import web.AppListener;

public class Livro {
    
    private long rowId;
    private String titulo;
    private String autor;
    private String editora;
    private int ano_publicacao;
    private String isbn;
    private String descricao;
    private Boolean disponibilidade;

    public Livro(long rowId, String titulo, String autor, String editora, Integer ano_publicacao, String isbn, String descricao, Boolean disponibilidade) {
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
                +"descricao VARCHAR(300) NOT NULL,"
                +"disponibilidade boolean"
                +");";      
    }
    
     public static ArrayList<Livro> getLivros() throws Exception{
        
        ArrayList<Livro> list = new ArrayList<>();
        Connection conexao = AppListener.getConnection();
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from livro WHERE dispobilidade = true ");
        
        while(rs.next())
        {
            long rowId = rs.getLong("rowid");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String editora = rs.getString("editora");
            int ano_publicacao = rs.getInt("ano_publicacao");
            String isbn = rs.getString("isbn");
            String descricao = rs.getString("descricao");
            Boolean disponibilidade = rs.getBoolean("disponibilidade");
            
            
            list.add(new Livro(rowId, titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade));
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return list;
    }
     
     public static ArrayList<Livro> getAllLivros() throws Exception{
        
        ArrayList<Livro> list = new ArrayList<>();
        Connection conexao = AppListener.getConnection();
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from livro ");
        
        while(rs.next())
        {
            long rowId = rs.getLong("rowid");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String editora = rs.getString("editora");
            int ano_publicacao = rs.getInt("ano_publicacao");
            String isbn = rs.getString("isbn");
            String descricao = rs.getString("descricao");
            boolean disponibilidade = rs.getBoolean("disponibilidade");
            
            
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
            int ano_publicacao = rs.getInt("ano_publicacao");
            String descricao = rs.getString("descricao");
            Boolean disponibilidade = rs.getBoolean("disponibilidade");
            
            livro = new Livro(rowId, titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade);
            
            
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return livro;
    }
    
    public static void insertLivro(String titulo, String autor, String editora, int ano_publicacao, String isbn, String descricao, Boolean disponibilidade) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "INSERT INTO livro(titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade)"
                + "VALUES (?,?,?,?,?,?,?)";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, titulo);
        stmt.setString(2, autor);
        stmt.setString(3, editora);
        stmt.setInt(4, ano_publicacao);
        stmt.setString(5, isbn);
        stmt.setString(6, descricao);
        stmt.setBoolean(7, disponibilidade);
        
        stmt.execute();
        conexao.close();
        stmt.close();
        
    }
    
    public static void updateLivro(String titulo, String autor, String editora, int ano_publicacao, String isbn, String descricao, Boolean disponibilidade) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "UPDATE livro SET titulo = ?, autor = ?, editora = ?, ano_publicacao = ?, "
                + "isbn = ?, descricao = ?, disponibilidade = ? WHERE isbn = ?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        // Define o valor do primeiro parâmetro como uma String
                
        stmt.setString(1, titulo);
        stmt.setString(2, autor);
        stmt.setString(3, editora);
        stmt.setInt(4, ano_publicacao);
        stmt.setString(5, isbn);
        stmt.setString(6, descricao);
        stmt.setBoolean(7, disponibilidade);
        stmt.setString(8, isbn);
        
        // Executa o comando UPDATE
        int rowsAffected = stmt.executeUpdate();
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

    public int getAno_publicacao() {
        return ano_publicacao;
    }

    public void setAno_publicacao(int ano_publicacao) {
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

    public Boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }       
}