/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configSQLite;

import config.Conexao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author lukian.borges
 */
public class SQLiteConexao implements Conexao {
    
    private static final String URL = "jdbc:sqlite:atividadeAvaliativaProjetos.db";

    static {
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar SQLite: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnexao() {
         try {
            return DriverManager.getConnection(URL); 
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco: " + e.getMessage());
        }

    }
    
    
}
