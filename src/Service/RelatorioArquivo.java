package Service; // Coloque esta classe no pacote Service ou Utilities

import DAO.clsRelatorio;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioArquivo {

    private final clsRelatorio relatoriosDao = new clsRelatorio();
    private static final String NOME_ARQUIVO_BASE = "Relatorio_Gerencial_ERP_";
    private static final String FORMATO_DATA = "yyyyMMdd_HHmmss";

    /**
     * Gera e salva um relatório completo com as principais análises do sistema.
     * O arquivo é salvo no diretório raiz do projeto.
     */
    public void gerarRelatorioCompleto() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMATO_DATA));
        String nomeArquivo = NOME_ARQUIVO_BASE + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            writer.println("===============================================================");
            writer.println("=== RELATÓRIO GERENCIAL CONSOLIDADO ERP DE VENDAS E ESTOQUE ===");
            writer.println("===============================================================");
            writer.println("Data de Geração: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println("Gerado por: Sistema Java ERP");
            writer.println("---------------------------------------------------------------");
            
            writer.println("\n--- 1. ESTOQUE CRÍTICO (PONTO DE RESSUPRIMENTO: Qtd <= 10) ---");
            List<String> criticos = relatoriosDao.verificarEstoqueCritico(10.0f);
            if (criticos.isEmpty()) {
                writer.println("Nenhum produto em nível crítico de estoque.");
            } else {
                criticos.forEach(writer::println);
            }
            writer.println("---------------------------------------------------------------");

            writer.println("\n--- 2. TOP 10 PRODUTOS MAIS VENDIDOS (Por Quantidade) ---");
            List<String> maisVendidos = relatoriosDao.listarProdutosMaisVendidos(10);
            if (maisVendidos.isEmpty()) {
                 writer.println("Nenhum registro de vendas encontrado.");
            } else {
                 maisVendidos.forEach(writer::println);
            }
            writer.println("---------------------------------------------------------------");
            
            writer.println("\n--- 3. MARGENS DE LUCRO POR PRODUTO (%) ---");
            List<String> margens = relatoriosDao.calcularMargemLucro();
            if (margens.isEmpty()) {
                writer.println("Nenhum produto com preço de compra e venda registrado.");
            } else {
                margens.forEach(writer::println);
            }
            writer.println("---------------------------------------------------------------");
            writer.println("\n=== FIM DO RELATÓRIO ===");

            System.out.println("✅ Relatório gerencial salvo com sucesso em: " + nomeArquivo);

        } catch (IOException e) {
            System.err.println("❌ Erro de I/O ao tentar salvar o arquivo de relatório: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado ao gerar dados para o relatório: " + e.getMessage());
        }
    }
}