/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.kmnos.mekup.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author tnbrito
 */
public class AutenticacaoDAO {
    //2 - preparando conexao com banco de dados
    private final String SCHEMA = "mekup";
    private final String CAMINHO = "jdbc:mysql://localhost/"+SCHEMA;
    private final String USUARIO_BD = "root";
    private final String SENHA_BD = "";
    
    //QUERYS==========================================================
    private final String CADASTRAR_LOGIN_SENHA = "INSERT INTO dados (usuario,senha) VALUES (?,MD5(?))";
    private final String VERIFICAR_LOGIN_SENHA = "SELECT * FROM dados WHERE usuario = (?) AND senha = MD5(?)";
    private final String ALTERAR_SENHA_LOGIN = "";
        private final String ALTERAR_SENHA = "UPDATE dados SET senha=MD5(?) WHERE usuario = (?)";
    //QUERYS==========================================================
    
    private static Connection conexao = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;
    
    //construtor ->ALT+INSERT

    public AutenticacaoDAO() throws ClassNotFoundException {
        //registrar o drive JDBC
        Class.forName("com.mysql.jdbc.Driver");
    }
    
    //o metodo abaixo vai retornar um arraylist de objetos do tipo Dados
    public void cadastrarNovoLoginESenha(String login, String senha) throws SQLException{
        conectarBanco();
        
        //2 - preparar a query
        String query = CADASTRAR_LOGIN_SENHA;
        stmt = conexao.prepareStatement(query);
        
        //INSERT INTO dados (usuario,senha) VALUES (?,MD5(?));
        stmt.setString(1, login);
        stmt.setString(2, senha);
                
        //3 - executar a query
        stmt.execute();
        JOptionPane.showMessageDialog(null, "Usuário " + login + " Cadastrado!");
                      
        fecharBanco();
    }
    
    public boolean verificarLoginESenha(String login, String senha) throws SQLException{
        conectarBanco();
        Dados dados = new Dados();
        
        String query = VERIFICAR_LOGIN_SENHA;
        stmt = conexao.prepareStatement(query);
        
        //SELECT * FROM dados WHERE usuario = (?) AND senha = MD5(?)
        stmt.setString(1, login);
        stmt.setString(2, senha);
        
        dados.login = "";
        dados.senha = "";
        
        rs = stmt.executeQuery();
        
        while(rs.next()){
            dados.id = rs.getInt("id");
            dados.login = rs.getString("usuario");
            dados.senha = rs.getString("senha");
        }
        if (dados.login.equals("") && dados.senha.equals("")) {
            JOptionPane.showMessageDialog(null, "Usuario e/ou senha inválidos");
            
        }else{
            JOptionPane.showMessageDialog(null, "Usuario Validado!");
            return false;
        }
        
        fecharBanco();
        
        return true;
    }
    
    public void alterarSenhaDadoLoginESenha(String login, String senha, String novaSenha) throws SQLException{
        conectarBanco();
        Dados dados = new Dados();
        
        String query1 = VERIFICAR_LOGIN_SENHA;
        stmt = conexao.prepareStatement(query1);
        
        //SELECT * FROM dados WHERE usuario = (?) AND senha = MD5(?)
        stmt.setString(1, login);
        stmt.setString(2, senha);
        
        dados.login = "";
        dados.senha = "";
        
        rs = stmt.executeQuery();
        
        while(rs.next()){
            dados.id = rs.getInt("id");
            dados.login = rs.getString("usuario");
            dados.senha = rs.getString("senha");
        }
        if (dados.login.equals("") && dados.senha.equals("")) {
            JOptionPane.showMessageDialog(null, "Usuario e/ou senha inválidos");
            
        }else{
            String query = ALTERAR_SENHA;
            stmt = conexao.prepareStatement(query);
            
            //UPDATE dados SET senha=MD5(?) WHERE usuario = (?)
            stmt.setString(1, novaSenha);
            stmt.setString(2, login);
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Senha alterada com sucesso!");
            
        }
        
        fecharBanco();
    }
    
    
    private void conectarBanco() throws SQLException{
        //1 - conexao com banco de dados
        conexao = DriverManager.getConnection(CAMINHO, USUARIO_BD, SENHA_BD); 
    }
    
    private void fecharBanco() throws SQLException{
         //4 - fechar o BD
        stmt.close();
        conexao.close();
    }
    
}
