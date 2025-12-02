package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Class.Login;
import Class.Produto;
import Conexao.Conexao;

public class clsProduto {
	
	public void inserir(Produto produto) {
        String sql = "INSERT INTO Produto (id_login, codigo, descricao, preco_compra, preco_venda) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, produto.getIdLogin());
            stmt.setString(2, produto.getCodigo());
            stmt.setString(3, produto.getDescricao());
            stmt.setDouble(4, produto.getPrecoCompra());
            stmt.setDouble(5, produto.getPrecoVenda());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        } finally {
            Conexao.fechar(conn);
        }
    }

    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM Produto";
        List<Produto> produtos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Produto p = new Produto(
                    rs.getInt("id"),
                    rs.getInt("id_login"),
                    rs.getString("codigo"),
                    rs.getString("descricao"),
                    rs.getDouble("preco_compra"),
                    rs.getDouble("preco_venda")
                );
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {  }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {  }
            Conexao.fechar(conn);
        }
        return produtos;
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE Produto SET codigo = ?, descricao = ?, preco_compra = ?, preco_venda = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoCompra());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getId()); 
            
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto ID " + produto.getId() + " atualizado.");
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            Conexao.fechar(conn);
        }
    }

    public void deletar(int idProduto) {
        String sql = "DELETE FROM Produto WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idProduto);
            
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto ID " + idProduto + " deletado com sucesso.");
            } else {
                System.out.println("Produto ID " + idProduto + " não encontrado para deleção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
        } finally {
            Conexao.fechar(conn);
        }
    }

	public Produto buscarPorId(int idProduto) {
		String sql = "SELECT * FROM Produto WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto prod = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idProduto);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	prod = new Produto(
                    rs.getInt("id"),
                    rs.getInt("id_login"),
                    rs.getString("codigo"),
                    rs.getString("descricao"),
                    rs.getDouble("preco_compra"),
                    rs.getDouble("preco_venda")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
        return prod;
	}

	public Produto buscarPorCodigo(String codigo) {
		String sql = "SELECT * FROM Produto WHERE codigo = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto prod = null;

        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	prod = new Produto(
                    rs.getInt("id"),
                    rs.getInt("id_login"),
                    rs.getString("codigo"),
                    rs.getString("descricao"),
                    rs.getDouble("preco_compra"),
                    rs.getDouble("preco_venda")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            Conexao.fechar(conn);
        }
        return prod;
	}
}
