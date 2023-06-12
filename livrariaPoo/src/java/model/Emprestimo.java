/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import web.app.AppListener;

public class Emprestimo {

    private long rowId;
    private Long idUsuario;
    private Long idLivro;
    private Date dataEmprestimo;
    private String dataFormatada;    
    private Date dataDevolucao;
    private String dataDevFormatada;
    private String titulo;
    private String login;
    private String name;
    private String isbn;
 
    public static String getCreateStatement(){
        return "CREATE TABLE IF NOT EXISTS emprestimo("
                +"idUsuario BIGINT NOT NULL,"
                +"idLivro BIGINT NOT NULL,"
                +"data_emprestimo datetime NOT NULL,"
                +"data_devolucao datetime,"
                +"Foreign key(idLivro) REFERENCES livro(rowId),"
                +"Foreign key(idUsuario) REFERENCES users(rowId)"
                +");";      
    }    

    public Emprestimo(long rowId, Long idUsuario, Long idLivro, Date dataEmprestimo, 
            String titulo, String login, String name, String isbn, Date dataDevolucao) {
        this.rowId = rowId;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.dataFormatada = sdf.format(dataEmprestimo);        
        this.titulo = titulo;
        this.login = login;
        this.name = name;
        this.isbn = isbn;
        this.dataDevolucao = dataDevolucao;
        if(dataDevolucao!=null)
            this.dataDevFormatada = sdf.format(dataDevolucao);        
        else
            this.dataDevFormatada = "";
    }
     
     public static ArrayList<Emprestimo> getEmprestimo(Long pIdUsuario) throws Exception{
        
        ArrayList<Emprestimo> list = new ArrayList<>();
        Connection conexao = AppListener.getConnection();
        Statement stmt = conexao.createStatement();
        String query = "SELECT e.rowid, e.*, l.*, u.* from emprestimo e "
                + "JOIN livro l ON e.idLivro = l.rowid "
                + "JOIN users u ON e.IdUsuario = u.rowid " ;
        if(pIdUsuario!=null){
            query += "Where u.rowid = " + pIdUsuario +
                     " and e.data_devolucao is null";
        }
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next())
        {
            long rowId = rs.getLong("rowid");
            long idUsuario = rs.getLong("idUsuario");
            long idLivro = rs.getLong("idLivro");
            String titulo = rs.getString("titulo");
            Date dataEmprestimo = new Date(rs.getDate("data_emprestimo").getTime());
            Date dataDevolucao = null;
            if(rs.getDate("data_devolucao")!=null){
                dataDevolucao = new Date(rs.getDate("data_devolucao").getTime());
            }
            String login = rs.getString("login");
            String name = rs.getString("name");
            String isbn = rs.getString("isbn");
            list.add(new Emprestimo(rowId, idUsuario, idLivro, dataEmprestimo, titulo, login, name, isbn, dataDevolucao));
        }
        
        rs.close();
        stmt.close();
        conexao.close();
        
        return list;
    }
     
    public static boolean devolverEmprestimo(String rowId, LocalDate dataDevolucao) throws Exception {
        Connection conexao = AppListener.getConnection();
        String sql = "UPDATE emprestimo SET data_devolucao = ? WHERE rowid = ?";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        // Define o valor do primeiro parâmetro como uma String                
        stmt.setDate(1,java.sql.Date.valueOf(dataDevolucao));
        stmt.setLong(2, Long.parseLong(rowId));

        // Executa o comando UPDATE
        int rowsAffected = stmt.executeUpdate();
        
        conexao.close();
        stmt.close();
        return rowsAffected > 0;
    } 
    public static boolean insertEmprestimo(Long idUsuario, Long idLivro, LocalDate dataEmprestimo) throws Exception{
        
        Connection conexao = AppListener.getConnection();
        String sql = "INSERT INTO emprestimo(idUsuario, idLivro, data_emprestimo) "
                + "VALUES (?,?,?)";
        
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setLong(1, idUsuario);
        stmt.setLong(2, idLivro);
        stmt.setDate(3, java.sql.Date.valueOf(dataEmprestimo));
        int rowsAffected = stmt.executeUpdate();
            
        conexao.close();
        stmt.close();
        
        return rowsAffected > 0;
    }
    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date data_emprestimo) {
        this.dataEmprestimo = data_emprestimo;
    }    

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }    
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }    

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }    

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getDataDevFormatada() {
        return dataDevFormatada;
    }

    public void setDataDevFormatada(String dataDevFormatada) {
        this.dataDevFormatada = dataDevFormatada;
    }
    
}
