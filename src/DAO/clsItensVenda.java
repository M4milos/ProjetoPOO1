package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Class.ItensVenda;
import Conexao.Conexao;

public class clsItensVenda {
    
    public void inserir(ItensVenda item) {
        String sql = "INSERT INTO Itens_Venda (id_venda, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getIdVenda());
            stmt.setInt(2, item.getIdProduto());
            stmt.setFloat(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());

            stmt.executeUpdate();
            System.out.println("Item inserido na Venda ID " + item.getIdVenda() + " (Produto ID: " + item.getIdProduto() + ")");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Item de Venda: " + e.getMessage());
        }
    }
    
    public List<ItensVenda> listarPorVenda(int idVenda) {
        String sql = "SELECT * FROM Itens_Venda WHERE id_venda = ?";
        List<ItensVenda> itens = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItensVenda item = new ItensVenda(
                        rs.getInt("id_venda"),
                        rs.getInt("id_produto"),
                        rs.getFloat("quantidade"),
                        rs.getDouble("preco_unitario")
                    );
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Itens de Venda: " + e.getMessage());
        }
        return itens;
    }

    public void atualizar(ItensVenda item) {
        String sql = "UPDATE Itens_Venda SET quantidade = ?, preco_unitario = ? WHERE id_venda = ? AND id_produto = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, item.getQuantidade());
            stmt.setDouble(2, item.getPrecoUnitario());
            stmt.setInt(3, item.getIdVenda());   
            stmt.setInt(4, item.getIdProduto()); 

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Item da Venda " + item.getIdVenda() + " atualizado.");
            } else {
                System.out.println("Item não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Item de Venda: " + e.getMessage());
        }
    }
    
    public void deletar(int idVenda, int idProduto) {
        String sql = "DELETE FROM Itens_Venda WHERE id_venda = ? AND id_produto = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);
            stmt.setInt(2, idProduto);
            
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Item do Produto ID " + idProduto + " removido da Venda ID " + idVenda + ".");
            } else {
                System.out.println("Item não encontrado para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar Item de Venda: " + e.getMessage());
        }
    }

    public List<ItensVenda> buscarItensPorVenda(int idVenda) throws SQLException {
        String sql = "SELECT id_produto, quantidade, preco_unitario FROM Itens_Venda WHERE id_venda = ?";
        
        List<ItensVenda> itens = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	ItensVenda item = new ItensVenda(
                        idVenda,
                        rs.getInt("id_produto"),
                        rs.getFloat("quantidade"),
                        rs.getDouble("preco_unitario")
                    );
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens para Venda ID " + idVenda + ": " + e.getMessage());
            throw e; 
        }
        return itens;
    }
}