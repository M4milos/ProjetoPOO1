package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Class.HistoricoVenda;
import Conexao.Conexao;

public class clsHistoricoVenda {
	
    public void inserir(HistoricoVenda historico) {
        String sql = "INSERT INTO Historico_Venda (id_venda, id_login, novo_status) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, historico.getIdVenda());
            stmt.setInt(2, historico.getIdLogin());
            stmt.setString(3, historico.getNovoStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historico.setId(rs.getInt(1));
                }
            }
            System.out.println("Histórico registrado com sucesso! ID: " + historico.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao registrar Histórico de Venda: " + e.getMessage());
        }
    }

    public List<HistoricoVenda> listarPorVenda(int idVenda) {
        String sql = "SELECT * FROM Historico_Venda WHERE id_venda = ? ORDER BY data_registro ASC";
        List<HistoricoVenda> historicos = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HistoricoVenda h = new HistoricoVenda(
                        rs.getInt("id"),
                        rs.getInt("id_venda"),
                        rs.getInt("id_login"),
                        rs.getTimestamp("data_registro"),
                        rs.getString("novo_status")
                    );
                    historicos.add(h);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Histórico de Venda: " + e.getMessage());
        }
        return historicos;
    }
}
