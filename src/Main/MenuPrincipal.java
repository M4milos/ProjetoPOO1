package Main;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import DAO.*;
import Unidades.UnidadeMedida;
import Class.*;
import Service.*;

public class MenuPrincipal {
	private static Sessao sessaoAtual = null;
    private static final Scanner scanner = new Scanner(System.in);
    private static final clsLogin loginDao = new clsLogin();
    private static final clsEstoque EstoqueDao = new clsEstoque();
    private static final clsProduto ProdutoDao = new clsProduto();
    private static final clsVenda VendaDao = new clsVenda();
    private static final RelatorioArquivo Relatorio = new RelatorioArquivo();
    
    private static String hashPassword(String password) {
        return "HASH_" + password + "_SALT";
    }
    
    public static void main(String[] args) {
        
        System.out.println("=== Sistema ERP Inicializado ===");
        
        while (true) {
            if (sessaoAtual == null) {
                exibirMenuDeslogado();
            } else {
                exibirMenuLogado();
            }
        }    
    }
    
    private static void exibirMenuDeslogado() {
        System.out.println("\n----------------------------------");
        System.out.println("  MENU PRINCIPAL ");
        System.out.println("----------------------------------");
        System.out.println("1. Logar");
        System.out.println("2. Cadastrar Novo Usuário");
        System.out.println("3. Excluir Usuário Existente (Apenas para Testes)");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");

        if (scanner.hasNextInt()) {
            int opcao = scanner.nextInt();
            scanner.nextLine(); 
            
            try {
                switch (opcao) {
                    case 1:
                        realizarLogin();
                        break;
                    case 2:
                        cadastrarUsuario();
                        break;
                    case 3:
                        excluirUsuarioExistente();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema. Adeus!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.err.println("Erro na operação: " + e.getMessage());
            }
        } else {
            System.out.println("Entrada inválida. Digite um número.");
            scanner.nextLine(); 
        }
    }
    
    private static void exibirMenuLogado() {
        System.out.println("\n----------------------------------");
        System.out.printf("Bem-vindo, %s! (Nível: %d)%n", sessaoAtual.getUsername(), sessaoAtual.getPermissionLevel());
        System.out.println("----------------------------------");
        System.out.println("1. Cadastrar Novo Produto");
        System.out.println("2. Consultar Estoque (Inventário)");
        System.out.println("3. Registrar Nova Venda");
        System.out.println("4. Aprovar Vendas Pendentes");
        System.out.println("5. Gerar Relatórios Gerenciais");
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opção: ");

        if (scanner.hasNextInt()) {
            int opcao = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (opcao) {
                case 1:
                	cadastrarProduto();
                    break;
                case 2:
                	consultarEstoque();
                    break;
                case 3:
                    simularVenda();
                    break;
                case 4:
                	aprovarVendasPendentes();
                    break;
                case 5:
                	gerarArquivoRelatorio();
                    break;
                case 0:
                    sessaoAtual = null;
                    System.out.println("Logout realizado com sucesso.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } else {
            System.out.println("Entrada inválida. Digite um número.");
            scanner.nextLine(); 
        }
    }
    
    private static void realizarLogin() throws Exception {
        System.out.print("Username: ");
        String user = "João Guilherme";
        System.out.print("Senha (Pura): ");
        String senhaPura = "1234";
        
        System.out.println("Tentativa cadastro: " + user + " Senha: " + senhaPura );
        
        Login log = loginDao.Logar(user, hashPassword(senhaPura)); 
        if (log != null) {
             sessaoAtual = new Sessao(log.getId(), log.getUsername(), log.getPermission());
             System.out.println("✅ Login bem-sucedido!");
             return;
        }
        
        System.out.println("❌ Falha no login: Usuário ou senha inválidos.");
    }
    
    private static void cadastrarUsuario() {
    	isVendedorOuAdmin();
        System.out.print("Novo Username: ");
        String user = scanner.nextLine();
        System.out.print("Nova Senha (Pura): ");
        String senhaPura = scanner.nextLine();
        System.out.print("Nível de Permissão (ex: 1 ou 3): ");
        int permissao = scanner.nextInt();
        scanner.nextLine();

        String senhaHash = hashPassword(senhaPura);
        
        Login novo = new Login(user, senhaHash, permissao);
        loginDao.inserir(novo);
    }
    
    private static void excluirUsuarioExistente() {
    	isVendedorOuAdmin();
        System.out.print("Nome do Usuário a ser excluído: ");
        String nome = scanner.nextLine();
        Login log = loginDao.buscarPorUsername(nome);
        if(log != null) {
        	System.out.println("Usuário encontrado será deletado!");
        	loginDao.deletar(log.getId());
        }else {
        	System.out.println("Usuário não encontrado!");
        }
        
    }
    
    private static void consultarEstoque() {
        System.out.println("\n=== CONSULTA DE ESTOQUE (INVENTÁRIO) ===");
        
        List<Estoque> inventario = EstoqueDao.listarTodos();

        if (inventario.isEmpty()) {
            System.out.println("O inventário está vazio. Cadastre um produto e dê entrada no estoque.");
            return;
        }

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-10s | %-40s | %-10s | %-5s |%n", 
            "ID", "ID Produto", "Descrição", "Quantidade", "UN");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Estoque item : inventario) {
            Produto produto = ProdutoDao.buscarPorId(item.getIdProduto());
            String descricao = (produto != null) ? produto.getDescricao() : "PRODUTO NÃO ENCONTRADO";
            
            System.out.printf("| %-5d | %-10d | %-40s | %-10.2f | %-5s |%n", 
                item.getId(), 
                item.getIdProduto(), 
                descricao,
                item.getQuantidade(), 
                item.getUnidade());
        }
        System.out.println("-------------------------------------------------------------------------------------------");
    }
    
    private static void cadastrarProduto() {
    	isVendedorOuAdmin();
        
        System.out.println("\n=== CADASTRO DE NOVO PRODUTO ===");
        
        try {
            System.out.print("Código (Identificador Único): ");
            String codigo = scanner.nextLine().trim();

            if (ProdutoDao.buscarPorCodigo(codigo) != null) {
                System.out.println("❌ Erro: Já existe um produto com este código. Cadastro cancelado.");
                return;
            }

            System.out.print("Descrição do Produto: ");
            String descricao = scanner.nextLine().trim();

            System.out.print("Preço de Compra (Ex: 10,50): R$ ");
            double precoCompra = scanner.nextDouble();

            System.out.print("Preço de Venda (Ex: 15,99): R$ ");
            double precoVenda = scanner.nextDouble();
            
            
            String unidade = solicitarUnidade();
            
            if (unidade == null) {
                System.out.println("Cadastro de produto cancelado pelo usuário.");
                return;
            }
            
            System.out.print("Quantidade de unidades: ");
            float quantidade = scanner.nextFloat();
            
            scanner.nextLine();
            
            int idLogin = sessaoAtual.getIdUsuarioLogado(); 

            Produto novoProd = new Produto(idLogin, codigo, descricao, precoCompra, precoVenda);

            ProdutoDao.inserir(novoProd); 

            Estoque novoEstoque = new Estoque(novoProd.getId(), quantidade, unidade);
            
            EstoqueDao.inserir(novoEstoque);
            
            System.out.println("✅ Produto cadastrado com sucesso! ID Gerado: " + novoProd.getId());
            
            System.out.println("✅ Produto inicializou estoque!");

        } catch (java.util.InputMismatchException e) {
            System.err.println("❌ Erro de entrada: Por favor, insira números válidos para os preços.");
        } catch (Exception e) {
            System.err.println("❌ Falha durante o cadastro: " + e.getMessage());
        }
    }
    
    private static void simularVenda() {
        if (!isVendedorOuAdmin()) {
            return;
        }

        System.out.println("\n=== INICIAR NOVA VENDA ===");
        
        List<ItensVenda> itensCarrinho = new ArrayList<>(); 
        double totalVenda = 0.0;
        
        try {
            while (true) {
                System.out.print("\nDigite o Código/SKU do Produto (ou 'FIM' para finalizar): ");
                String codigo = scanner.nextLine().trim().toUpperCase();

                if (codigo.equals("FIM")) {
                    break;
                }

                Produto produto = ProdutoDao.buscarPorCodigo(codigo);
                if (produto == null) {
                    System.out.println("❌ Produto não encontrado. Tente novamente.");
                    continue;
                }
                
                Estoque estoque = EstoqueDao.buscarPorIdProduto(produto.getId());
                float estoqueAtual = (estoque != null) ? estoque.getQuantidade() : 0.0f;
                String unidade = (estoque != null) ? estoque.getUnidade() : "UN";

                System.out.printf("Produto: %s | Estoque Disponível: %.2f %s%n", produto.getDescricao(), estoqueAtual, unidade);

                System.out.print("Quantidade a vender: ");
                float quantidadeVenda = scanner.nextFloat();
                scanner.nextLine(); 

                if (quantidadeVenda <= 0) {
                    System.out.println("Quantidade deve ser maior que zero.");
                    continue;
                }
                
                if (quantidadeVenda > estoqueAtual) {
                    System.out.println("⚠️ Estoque insuficiente! Venda máxima: " + estoqueAtual + " " + unidade);
                    continue;
                }
                
                double subtotal = quantidadeVenda * produto.getPrecoVenda();
                totalVenda += subtotal;
                itensCarrinho.add(new ItensVenda(produto.getId(), quantidadeVenda, produto.getPrecoVenda()));
                
                if (EstoqueDao.decrementarQuantidade(produto.getId(), quantidadeVenda)) {
                    System.out.println("✅ Item adicionado. Estoque de " + produto.getDescricao() + " atualizado.");
                } else {
                    System.err.println("❌ Erro no DB: Falha ao atualizar estoque. Item removido da venda.");
                    totalVenda -= subtotal;
                    itensCarrinho.remove(itensCarrinho.size() - 1);
                }
            }
            
            if (itensCarrinho.isEmpty()) {
                System.out.println("Venda cancelada ou sem itens.");
                return;
            }
            
            int idLogin = sessaoAtual.getIdUsuarioLogado();
            
            System.out.println("\nTentando registrar venda no banco de dados...");
            VendaService VendaService = new VendaService();
            
            int idVendaGerado = VendaService.registrarNovaVenda(itensCarrinho, totalVenda, idLogin);
            
            System.out.println("=================================================");
            System.out.printf("✅ VENDA ID %d REGISTRADA COM SUCESSO!%n", idVendaGerado);
            System.out.printf("TOTAL FINAL: R$%.2f%n", totalVenda);

        } catch (SQLException e) {
            System.err.println("\n❌ TRANSAÇÃO CANCELADA! Falha no banco de dados.");
            System.err.println("Detalhes: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.err.println("❌ Erro de entrada: Por favor, insira um número válido.");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("❌ Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
    
	private static String solicitarUnidade() {
        UnidadeMedida[] unidades = UnidadeMedida.values();
        int opcao;

        while (true) {
            System.out.println("\n--- SELEÇÃO DE UNIDADE ---");
            
            for (int i = 0; i < unidades.length; i++) {
                System.out.printf("%d. %s (%s)%n", (i + 1), unidades[i].getNomeCompleto(), unidades[i].getSigla());
            }
            System.out.print("Selecione o número da unidade (ou 0 para cancelar): ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); 

                if (opcao == 0) {
                    return null; 
                }
                
                if (opcao > 0 && opcao <= unidades.length) {
                    UnidadeMedida unidadeSelecionada = unidades[opcao - 1];
                    System.out.printf("✅ Unidade selecionada: %s.%n", unidadeSelecionada.getSigla());
                    
                    return unidadeSelecionada.getSigla(); 
                } else {
                    System.out.println("Opção inválida. Selecione um número da lista.");
                }
            } else {
                System.out.println("Entrada inválida. Digite um número.");
                scanner.nextLine();
            }
        }
    }
    
	private static void aprovarVendasPendentes() {
	    if (!isVendedorOuAdmin()) {
	        return;
	    }

	    System.out.println("\n=== APROVAÇÃO DE VENDAS PENDENTES ===");
	    
	    try {
	        List<Venda> rascunhos = VendaDao.buscarPorStatus("EM APROVAÇÃO");
	        
	        if (rascunhos.isEmpty()) {
	            System.out.println("Nenhuma venda pendente de aprovação.");
	            return;
	        }

	        System.out.println("Vendas Pendentes Encontradas:");
	        for (Venda v : rascunhos) {
	            System.out.printf("ID: %d | Total: R$%.2f | Vendedor ID: %d%n", v.getId(), v.getTotalVenda(), v.getIdLogin());
	        }

	        System.out.print("Digite o ID da Venda para Aprovar (ou 0 para cancelar): ");
	        int idVendaAprovar = scanner.nextInt();
	        scanner.nextLine();

	        if (idVendaAprovar == 0) return;

	        System.out.println("Processando transação da Venda ID " + idVendaAprovar + "...");
	        
	        VendaService VendaService = new VendaService();
	        VendaService.aprovarVendaTransacional(idVendaAprovar, sessaoAtual.getIdUsuarioLogado());
	        
	        System.out.println("✅ VENDA APROVADA! Estoque baixado e status atualizado.");

	    } catch (SQLException e) {
	        System.err.println("\n❌ FALHA NA APROVAÇÃO: A transação foi desfeita (ROLLBACK).");
	        System.err.println("Detalhes: " + e.getMessage());
	    } catch (java.util.InputMismatchException e) {
	        System.err.println("❌ Entrada inválida. Digite um número inteiro.");
	        scanner.nextLine();
	    } catch (Exception e) {
	        System.err.println("❌ Ocorreu um erro: " + e.getMessage());
	    }
	}
	
	private static void gerarArquivoRelatorio() {
	    System.out.println("\n--- INICIANDO GERAÇÃO DE ARQUIVO ---");
	    Relatorio.gerarRelatorioCompleto();
	}
	
    private static boolean isVendedorOuAdmin() {
        if (sessaoAtual == null || sessaoAtual.getIdUsuarioLogado() <= 0) {
            System.out.println("⚠️ Necessário estar logado.");
            return false;
        }
        
        int permissao = sessaoAtual.getPermissionLevel(); 
        
        if (permissao == 1) {
            System.out.println("🚫 Permissão negada. Seu nível de acesso (1) não permite realizar esta ação.");
            return false;
        }
        
        return permissao == 2 || permissao == 3;
    }
    
    
}
