/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import configSQLite.SQLiteConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author lukian.borges
 */
public class ConfiguracaoService {

    public String obterTipoLog() {
        String sql = "SELECT valor FROM configuracao_log WHERE chave = 'tipoLog'";

        try (Connection c = new SQLiteConexao().getConnexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("valor");
            }
            return "CSV"; // fallback seguro

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar tipo de log", e);
        }
    }

    public void salvarTipoLog(String tipo) {
        String sql = "UPDATE configuracao_log SET valor = ? WHERE chave = 'tipoLog'";

        try (Connection c = new SQLiteConexao().getConnexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tipo.toUpperCase());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar tipo de log", e);
        }
    }
}