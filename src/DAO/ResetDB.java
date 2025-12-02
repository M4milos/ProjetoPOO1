package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Conexao.Conexao;

public class ResetDB {
	public void resetarTodasAsTabelas() {
        String[] tabelas = {
            "Historico_Venda", 
            "Itens_Venda", 
            "Venda", 
            "Estoque", 
            "Produto", 
            "Login"
        };
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;"); 

            for (String tabela : tabelas) {
                String sqlTruncate = "TRUNCATE TABLE " + tabela;
                stmt.executeUpdate(sqlTruncate);
            }

            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;"); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
