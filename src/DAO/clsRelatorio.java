package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Conexao.Conexao;

public class clsRelatorio {

    public List<String> verificarEstoqueCritico(float limiteMinimo) {
        String sql = 
            "SELECT P.codigo, P.descricao, E.quantidade, E.unidade " +
            "FROM Estoque E " +
            "JOIN Produto P ON E.id_produto = P.id " +
            "WHERE E.quantidade <= ? " +
            "ORDER BY E.quantidade ASC";

        List<String> criticos = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, limiteMinimo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String item = String.format(
                        "CÓD: %s | DESC: %s | QTD ATUAL: %.2f %s",
                        rs.getString("codigo"),
                        rs.getString("descricao"),
                        rs.getFloat("quantidade"),
                        rs.getString("unidade")
                    );
                    criticos.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar Relatório de Estoque Crítico: " + e.getMessage());
        }
        return criticos;
    }

    public List<String> listarProdutosMaisVendidos(int limite) {
        String sql = 
            "SELECT P.descricao, SUM(I.quantidade) AS total_vendido " +
            "FROM Itens_Venda I " +
            "JOIN Produto P ON I.id_produto = P.id " +
            "GROUP BY P.descricao " +
            "ORDER BY total_vendido DESC " +
            "LIMIT ?";

        List<String> maisVendidos = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limite);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String item = String.format(
                        "DESC: %s | QTD VENDIDA: %.2f",
                        rs.getString("descricao"),
                        rs.getDouble("total_vendido")
                    );
                    maisVendidos.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar Relatório de Mais Vendidos: " + e.getMessage());
        }
        return maisVendidos;
    }


    public List<String> calcularMargemLucro() {
        String sql = 
            "SELECT descricao, preco_compra, preco_venda, " +
            "((preco_venda - preco_compra) / preco_venda) * 100 AS margem_percentual " +
            "FROM Produto " +
            "WHERE preco_compra IS NOT NULL AND preco_venda > 0 " +
            "ORDER BY margem_percentual DESC";

        List<String> margens = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String item = String.format(
                    "DESC: %s | Compra: R$%.2f | Venda: R$%.2f | MARGEM: %.2f%%",
                    rs.getString("descricao"),
                    rs.getDouble("preco_compra"),
                    rs.getDouble("preco_venda"),
                    rs.getDouble("margem_percentual")
                );
                margens.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao calcular Margem de Lucro: " + e.getMessage());
        }
        return margens;
    }
}