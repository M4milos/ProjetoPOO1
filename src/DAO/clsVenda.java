package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Class.Venda;
import Conexao.Conexao;

public class clsVenda {
    
    public int inserir(Venda venda) {
        String sql = "INSERT INTO Venda (id_login, data_venda, total_venda, status) VALUES (?, ?, ?, ?)";
        int idGerado = -1;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        	
        	Timestamp agora = new Timestamp(System.currentTimeMillis());
            
        	stmt.setInt(1, venda.getIdLogin());
            stmt.setTimestamp(2, agora); 
            stmt.setDouble(3, venda.getTotalVenda());
            stmt.setString(4, venda.getStatus());

            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    venda.setId(idGerado);
                }
            }
            System.out.println("Venda aberta com sucesso! ID: " + idGerado);

        } catch (SQLException e) {
            System.err.println("Erro ao abrir nova Venda: " + e.getMessage());
        }
        return idGerado;
    }

    public Venda buscarPorId(int idVenda) {
        String sql = "SELECT * FROM Venda WHERE id = ?";
        Venda venda = null;
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    venda = new Venda(
                        rs.getInt("id"),
                        rs.getInt("id_login"),
                        rs.getTimestamp("data_venda"),
                        rs.getDouble("total_venda"),
                        rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Venda por ID: " + e.getMessage());
        }
        return venda;
    }
    
    public void atualizar(Venda venda) {
        String sql = "UPDATE Venda SET total_venda = ?, status = ? WHERE id = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, venda.getTotalVenda());
            stmt.setString(2, venda.getStatus());
            stmt.setInt(3, venda.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Venda ID " + venda.getId() + " atualizada.");
            } else {
                System.out.println("Venda não encontrada.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Venda: " + e.getMessage());
        }
    }
    
    public void deletar(int idVenda) {
        String sql = "DELETE FROM Venda WHERE id = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);
            
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Venda ID " + idVenda + " deletada com sucesso.");
            } else {
                System.out.println("Venda ID " + idVenda + " não encontrada para deleção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar Venda: " + e.getMessage());
        }
    }

    public List<Venda> buscarPorStatus(String status) {
        String sql = "SELECT * FROM Venda WHERE status = ?";
        List<Venda> vendas = new ArrayList<>();
        
        String statusBusca = status.trim().toUpperCase(); 
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, statusBusca);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda(
                        rs.getInt("id"),
                        rs.getInt("id_login"),
                        rs.getTimestamp("data_venda"),
                        rs.getDouble("total_venda"),
                        rs.getString("status")
                    );
                    vendas.add(venda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Vendas por Status: " + e.getMessage());
        }
        return vendas;
    }

	public void atualizarStatus(int idvenda, String status) {
		String sql = "UPDATE Venda SET status = ? WHERE id = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idvenda);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Venda: " + e.getMessage());
        }
	}
}