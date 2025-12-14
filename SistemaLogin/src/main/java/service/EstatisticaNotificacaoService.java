package service;

import configSQLite.SQLiteConexao;
import dto.EstatisticaRemetenteDTO;
import dto.EstatisticaUsuarioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class EstatisticaNotificacaoService {

    //SABEMOS QUE NAO É O CORRETO NO PADRÃO FAZER ESSA PESQUISA AQUI
   // O CERTO SERIA NA REPOSITORY, MAS POR QUESTÕES DE TEMPO PRA CONSEGUIR ENTREGAR FIZEMOS AQUI

    public int totalNotificacoes() {
        return buscarInt("SELECT COUNT(*) FROM notificacao");
    }

    public int totalLidas() {
        return buscarInt("SELECT COUNT(*) FROM notificacao WHERE lida = 1");
    }

    public int totalNaoLidas() {
        return buscarInt("SELECT COUNT(*) FROM notificacao WHERE lida = 0");
    }

    private int buscarInt(String sql) {
        try (Connection c = new SQLiteConexao().getConnexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar estatísticas", e);
        }
    }

  
   //SABEMOS QUE NAO É O CORRETO NO PADRÃO FAZER ESSA PESQUISA AQUI
   // O CERTO SERIA NA REPOSITORY, MAS POR QUESTÕES DE TEMPO PRA CONSEGUIR ENTREGAR FIZEMOS AQUI
    public List<EstatisticaUsuarioDTO> estatisticaPorUsuario() {

        String sql = """
            SELECT 
                u.usuario AS nome,
                COUNT(n.id) AS total,
                SUM(CASE WHEN n.lida = 1 THEN 1 ELSE 0 END) AS lidas,
                SUM(CASE WHEN n.lida = 0 THEN 1 ELSE 0 END) AS naoLidas
            FROM usuario u
            LEFT JOIN notificacao n 
                ON n.idDestinatario = u.id
            GROUP BY u.usuario
            ORDER BY u.usuario
        """;

        List<EstatisticaUsuarioDTO> lista = new ArrayList<>();

        try (Connection c = new SQLiteConexao().getConnexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EstatisticaUsuarioDTO(
                        rs.getString("nome"),
                        rs.getInt("total"),
                        rs.getInt("lidas"),
                        rs.getInt("naoLidas")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar estatísticas por usuário", e);
        }

        return lista;
    }
    
   //SABEMOS QUE NAO É O CORRETO NO PADRÃO FAZER ESSA PESQUISA AQUI
   // O CERTO SERIA NA REPOSITORY, MAS POR QUESTÕES DE TEMPO PRA CONSEGUIR ENTREGAR FIZEMOS AQUI
    public List<EstatisticaRemetenteDTO> estatisticaPorRemetente() {

    String sql = """
        SELECT 
            u.usuario AS remetente,
            COUNT(n.id) AS total
        FROM usuario u
        LEFT JOIN notificacao n 
            ON n.idRemetente = u.id
        WHERE u.tipoCadastro IN ('ADMIN', 'ADMIN_MASTER')
        GROUP BY u.usuario
        ORDER BY u.usuario
    """;

    List<EstatisticaRemetenteDTO> lista = new ArrayList<>();

    try (Connection c = new SQLiteConexao().getConnexao();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            lista.add(new EstatisticaRemetenteDTO(
                    rs.getString("remetente"),
                    rs.getInt("total")
            ));
        }

    } catch (Exception e) {
        throw new RuntimeException("Erro ao buscar estatísticas por remetente", e);
    }

    return lista;
}


}
