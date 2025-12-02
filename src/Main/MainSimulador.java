package Main;

import java.sql.SQLException;
import java.util.List;

import DAO.*;
import Class.*;

public class MainSimulador {
    
    private static String hashPassword(String password) {
        return "HASH_" + password + "_SALT";
    }

    public static void main(String[] args) {
        System.out.println("--- INICIANDO SIMULAÇÃO DE FLUXO ERP ---");
        
        clsLogin loginDao = new clsLogin();
        clsProduto produtoDao = new clsProduto();
        clsEstoque estoqueDao = new clsEstoque();
        clsVenda vendaDao = new clsVenda();
        clsItensVenda itensVendaDao = new clsItensVenda();
        clsHistoricoVenda historicoDao = new clsHistoricoVenda();

        int idAdmin = 0;
        int idProduto = 0;
        int idVenda = 0;

        try {
            System.out.println("\n== 1. CADASTRANDO ADMIN ==");
            Login admin = new Login("sysadmin", hashPassword("securepwd"), 99);
            loginDao.inserir(admin);
            idAdmin = admin.getId();
            System.out.println("Login ID gerado: " + idAdmin);

            System.out.println("\n== 2. CADASTRO DE PRODUTO E ENTRADA DE ESTOQUE ==");
            
            Produto novoProd = new Produto(idAdmin, "NOTE_DELL_001", "Notebook Dell Vostro", 2500.00, 3999.99);
            produtoDao.inserir(novoProd);
            idProduto = novoProd.getId();
            System.out.println("Produto ID gerado: " + idProduto);

            Estoque estoque = new Estoque(idProduto, 50.0f, "UN"); 
            estoqueDao.inserir(estoque);
            System.out.println("Estoque inicial de 50 UN registrado.");

            System.out.println("\n== 3. SIMULANDO UMA VENDA ==");
            
            double precoVenda = novoProd.getPrecoVenda();
            float qtdVendida = 2.0f;
            double totalItem = qtdVendida * precoVenda;

            Venda venda = new Venda(totalItem, "PENDENTE");
            idVenda = vendaDao.inserir(venda); 
            
            ItensVenda item = new ItensVenda(idVenda, idProduto, qtdVendida, precoVenda);
            itensVendaDao.inserir(item);
            
            Estoque estoqueAtual = estoqueDao.buscarPorIdProduto(idProduto);
            float novaQtd = estoqueAtual.getQuantidade() - qtdVendida;
            estoqueDao.atualizarQuantidade(idProduto, novaQtd);
            
            venda.setTotalVenda(totalItem); 
            venda.setStatus("PROCESSADA");
            vendaDao.atualizar(venda);
            
            HistoricoVenda historico = new HistoricoVenda(idVenda, idAdmin, venda.getStatus());
            historicoDao.inserir(historico);
            
            System.out.println("Venda " + idVenda + " processada e estoque baixado.");
            
            System.out.println("\n== 4. VERIFICAÇÃO FINAL (READ) ==");
            
            Estoque finalEstoque = estoqueDao.buscarPorIdProduto(idProduto);
            System.out.println("Estoque final do produto ID " + idProduto + ": " + finalEstoque.getQuantidade());

            List<ItensVenda> itens = itensVendaDao.listarPorVenda(idVenda);
            System.out.println("Itens vendidos na Venda " + idVenda + ": " + itens.size() + " item(s)");

            List<HistoricoVenda> historicoList = historicoDao.listarPorVenda(idVenda);
            System.out.println("Histórico da Venda " + idVenda + ": " + historicoList.size() + " registro(s)");


            System.out.println("\n== 5. LIMPEZA (DELETE) - OPICIONAL ==");
            
            // historicoDao.deletar(historico.getId()); 
            // itensVendaDao.deletar(idVenda, idProduto); 
            // vendaDao.deletar(idVenda); 
            // estoqueDao.deletar(idProduto);
            // produtoDao.deletar(idProduto);
            // loginDao.deletar(idAdmin);
            
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro fatal durante a simulação:");
            e.printStackTrace();
        } finally {
            System.out.println("\n--- SIMULAÇÃO FINALIZADA ---");
        }
    }
}
