/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configSQLite;

import config.Conexao;
import config.InicializarBanco;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author lukian.borges
 */
public class SQLiteInicilizarBanco implements InicializarBanco {
    
    
    private final Conexao conexaoBanco = new SQLiteConexao();

    @Override
    public void inicializar() {
        try (Connection conexao = conexaoBanco.getConnexao();
             Statement stmt = conexao.createStatement()) {

         stmt.execute(
    "CREATE TABLE IF NOT EXISTS usuario ("
    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
    + "usuario VARCHAR(255) NOT NULL UNIQUE, "
    + "senha VARCHAR(255) NOT NULL, "
    + "tipoUsuario VARCHAR(50) NOT NULL, "
    + "autorizado INTEGER NOT NULL, "
    + "dataCadastro TEXT NOT NULL)"
);

  stmt.execute(
    "CREATE TABLE IF NOT EXISTS notificacao ("
    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
    + "idRemetente INTEGER NOT NULL, "
    + "idDestinatario INTEGER NOT NULL, "
    + "mensagem TEXT NOT NULL, "
    + "dataEnvio TEXT NOT NULL, "
    + "lida INTEGER NOT NULL DEFAULT 0, "
    + "FOREIGN KEY(idRemetente) REFERENCES usuario(id), "
    + "FOREIGN KEY(idDestinatario) REFERENCES usuario(id))"
);
  
  stmt.execute(
    "CREATE TABLE IF NOT EXISTS configuracao_log ("
    + "chave VARCHAR(100) PRIMARY KEY, "
    + "valor VARCHAR(100) NOT NULL)"
);

  stmt.execute(
    "INSERT OR IGNORE INTO configuracao_log (chave, valor) "
    + "VALUES ('tipoLog', 'CSV')"
);


        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar o banco: " + e.getMessage());
        }
    }
    
    
}
