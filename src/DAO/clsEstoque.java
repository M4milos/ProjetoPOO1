package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Class.Estoque;
import Conexao.Conexao;

public class clsEstoque {

    public void inserir(Estoque estoque) {
        String sql = "INSERT INTO Estoque (id_produto, quantidade, unidade) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, estoque.getIdProduto());
            stmt.setFloat(2, estoque.getQuantidade());
            stmt.setString(3, estoque.getUnidade());

            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    estoque.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir registro de Estoque: " + e.getMessage());
        }
    }

    public Estoque buscarPorIdProduto(int idProduto) {
        String sql = "SELECT * FROM Estoque WHERE id_produto = ?";
        Estoque estoque = null;
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProduto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estoque = new Estoque(
                        rs.getInt("id"),
                        rs.getInt("id_produto"),
                        rs.getFloat("quantidade"),
                        rs.getString("unidade"),
                        rs.getTimestamp("data_ult_movimento")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Estoque por Produto: " + e.getMessage());
        }
        return estoque;
    }
    
    public List<Estoque> listarTodos() {
        String sql = "SELECT * FROM Estoque ORDER BY id_produto";
        List<Estoque> estoques = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Estoque e = new Estoque(
                    rs.getInt("id"),
                    rs.getInt("id_produto"),
                    rs.getFloat("quantidade"),
                    rs.getString("unidade"),
                    rs.getTimestamp("data_ult_movimento")
                );
                estoques.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar registros de Estoque: " + e.getMessage());
        }
        return estoques;
    }
    
    public void atualizarQuantidade(int idProduto, float novaQuantidade) {
        String sql = "UPDATE Estoque SET quantidade = ? WHERE id_produto = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, novaQuantidade);
            stmt.setInt(2, idProduto);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar quantidade de Estoque: " + e.getMessage());
        }
    }
    
    public boolean decrementarQuantidade(int idProduto, float quantidadeVendida) {
        String sql = "UPDATE Estoque SET quantidade = quantidade - ? WHERE id_produto = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, quantidadeVendida);
            stmt.setInt(2, idProduto);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao decrementar estoque do produto ID " + idProduto + ": " + e.getMessage());
            return false;
        }
    }
    
    
}