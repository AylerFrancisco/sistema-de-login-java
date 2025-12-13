/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import configSQLite.SQLiteConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Notificacao;

/**
 *
 * @author Ayler
 */
public class NotificacaoRepository {

    
     public boolean salvar(Notificacao n) {
        String sql = "INSERT INTO notificacao (idRemetente, idDestinatario, mensagem, dataEnvio, lida) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, n.getIdRemetente());
            stmt.setInt(2, n.getIdDestinatario());
            stmt.setString(3, n.getMensagem());
            stmt.setString(4, n.getDataEnvio());
            stmt.setInt(5, n.getLida());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar notificação: " + e.getMessage());
        }
    
}
 
     public boolean marcarComoLida(int idNotificacao) {
        String sql = "UPDATE notificacao SET lida = 1 WHERE id = ?";

        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idNotificacao);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao marcar como lida: " + e.getMessage());
        }
    }
     
     
    
}
