package Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Conexao.Conexao;
import DAO.*;
import Class.*;

public class VendaService {

    private final clsVenda vendaDao = new clsVenda();
    private final clsItensVenda itensVendaDao = new clsItensVenda();
    private final clsEstoque estoqueDao = new clsEstoque();
    private final clsHistoricoVenda hostoricoDao = new clsHistoricoVenda();

    public int registrarNovaVenda(List<ItensVenda> itensVenda, double totalVenda, int idLogin) throws SQLException {                         
        Connection conn = null;
        int idVendaGerado = -1;
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);
            Venda novaVenda = new Venda(idLogin, totalVenda, "EM APROVAÇÃO"); 

            idVendaGerado = vendaDao.inserir(novaVenda);

            if (idVendaGerado <= 0) {
                throw new SQLException("Falha ao gerar o ID da venda.");
            }

            for (ItensVenda item : itensVenda) {

                itensVendaDao.inserir(new ItensVenda(
                    idVendaGerado, 
                    item.getIdProduto(), 
                    item.getQuantidade(), 
                    item.getPrecoUnitario()
                ));
            }

            conn.commit(); 
            return idVendaGerado;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignore) {  }
                Conexao.fechar(conn);
            }
        }
    }

    public void aprovarVendaTransacional(int idVendaAprovar, int idUsuarioLogado) throws Exception {
                                            
        Connection conn = null;
        String NOVO_STATUS = "APROVADA";
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);
            List<ItensVenda> itensVenda = itensVendaDao.buscarItensPorVenda(idVendaAprovar); 

            if (itensVenda.isEmpty()) {
                throw new Exception("Venda ID " + idVendaAprovar + " não possui itens ou não existe.");
            }

            for (ItensVenda item : itensVenda) {
                boolean sucessoEstoque = estoqueDao.decrementarQuantidade(
                    item.getIdProduto(), 
                    item.getQuantidade()
                );
                
                if (!sucessoEstoque) {
                    throw new SQLException("Falha na baixa de estoque para o Produto ID: " + item.getIdProduto());
                }
            }

            vendaDao.atualizarStatus(idVendaAprovar, NOVO_STATUS);

            HistoricoVenda historico = new HistoricoVenda(
                idVendaAprovar, 
                idUsuarioLogado, 
                NOVO_STATUS
            );
            hostoricoDao.inserir(historico);
            conn.commit(); 

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception("Falha na transação de aprovação. Causa: " + e.getMessage(), e); 
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignore) {  }
                Conexao.fechar(conn);
            }
        }
    }
}