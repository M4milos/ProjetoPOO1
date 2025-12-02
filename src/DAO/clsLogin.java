package DAO;

import java.sql.*;
import java.sql.PreparedStatement;

import Class.Login;
import Conexao.Conexao;

public class clsLogin {
	
    public void inserir(Login login) {
        String sql = "INSERT INTO Login (username, pass_hash, permission) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, login.getUsername());
            stmt.setString(2, login.getPassHash()); 
            stmt.setInt(3, login.getPermission());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    login.setId(rs.getInt(1));
                }
            }
            System.out.println("Login inserido com sucesso! ID: " + login.getId());
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir Login: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
    }

    public Login buscarPorId(int id) {
        String sql = "SELECT * FROM Login WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Login login = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                login = new Login(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("pass_hash"),
                    rs.getInt("permission")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Login por ID: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
        return login;
    }
    
    public void atualizar(Login login) {
        String sql = "UPDATE Login SET username = ?, permission = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, login.getUsername());
            stmt.setInt(2, login.getPermission());
            stmt.setInt(3, login.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Login ID " + login.getId() + " atualizado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Login: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
    }
    
    
    public void deletar(int idLogin) {
        String sql = "DELETE FROM Login WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idLogin);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar Login: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
    }
    
    public void deletarPorNome(String nome) {
    	String sql = "SELECT * FROM Login WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Login login = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                login = new Login(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("pass_hash"),
                    rs.getInt("permission")
                );
            }
            
            if(login != null) {
            	deletar(login.getId());
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Login por ID: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
    }

    public Login buscarPorUsername(String user) {
        String sql = "SELECT * FROM Login WHERE username LIKE ?"; 
        Login login = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + user + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    login = new Login(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("pass_hash"),
                        rs.getInt("permission")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Login parcialmente: " + e.getMessage());
        }
        return login;
    }
	
	public Login Logar(String user, String senha) {
		String sql = "SELECT * FROM Login WHERE username = ? AND pass_hash = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Login login = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                login = new Login(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("pass_hash"),
                    rs.getInt("permission")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Login por username: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
        return login;
	}
	
}
