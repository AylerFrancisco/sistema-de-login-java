/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import adapter.LogAdapter;
import configSQLite.SQLiteConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Notificacao;

/**
 *
 * @author lukian.borges
 */
public class EnviarNotificacaoSQLite implements IEnviarNotificacaoRepository {
    
    private static EnviarNotificacaoSQLite instance;
     private LogAdapter logAdapter;
     
       public static synchronized EnviarNotificacaoSQLite getInstance() {
        if (instance == null) {
            instance = new EnviarNotificacaoSQLite();
        }
        return instance;
    }
    
    @Override
    public boolean enviar(List<Notificacao> notificacoes) {
        if (notificacoes == null || notificacoes.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO notificacao (idRemetente, idDestinatario, mensagem, dataEnvio, lida) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = new SQLiteConexao().getConnexao()) {

            conexao.setAutoCommit(false); // importante para batch

            try (PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {

                for (Notificacao n : notificacoes) {
                    preparedStatement.setInt(1, n.getIdRemetente());
                    preparedStatement.setInt(2, n.getIdDestinatario());
                    preparedStatement.setString(3, n.getMensagem());
                    preparedStatement.setString(4,
                            (n.getDataEnvio() == null ? LocalDateTime.now().toString() : n.getDataEnvio())
                    );
                    preparedStatement.setInt(5,
                            (n.getLida() == null ? 0 : n.getLida())
                    );

                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
                conexao.commit();
                return true;

            } catch (Exception e) {
                conexao.rollback();
                throw new RuntimeException("Erro ao enviar notificações: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro na conexão ao enviar notificações: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Notificacao> listarPorDestinatario(int idDestinatario) throws Exception {
        List<Notificacao> lista = new ArrayList<>();
        ;
        String sql = "SELECT id, idRemetente, idDestinatario, mensagem, dataEnvio, lida FROM notificacao WHERE idDestinatario = ? ORDER BY dataEnvio DESC";
        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idDestinatario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacao n = new Notificacao();
                    n.setId(rs.getInt("id"));
                    n.setIdRemetente(rs.getInt("idRemetente"));
                    n.setIdDestinatario(rs.getInt("idDestinatario"));
                    n.setMensagem(rs.getString("mensagem"));
                    n.setDataEnvio(rs.getString("dataEnvio"));
                    n.setLida(rs.getInt("lida"));
                    lista.add(n);
                }
            }
        }
        return lista;
    }

    @Override
    public List<Notificacao> listarTodos() throws Exception {
        List<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT id, idRemetente, idDestinatario, mensagem, dataEnvio, lida FROM notificacao ORDER BY dataEnvio DESC";
        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Notificacao n = new Notificacao();
                n.setId(rs.getInt("id"));
                n.setIdRemetente(rs.getInt("idRemetente"));
                n.setIdDestinatario(rs.getInt("idDestinatario"));
                n.setMensagem(rs.getString("mensagem"));
                n.setDataEnvio(rs.getString("dataEnvio"));
                n.setLida(rs.getInt("lida"));
                lista.add(n);
            }
        }
        return lista;
    }

    @Override
    public boolean remover(int idNotificacao) throws Exception {
        String sql = "DELETE FROM notificacao WHERE id = ?";
        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idNotificacao);
            int r = ps.executeUpdate();
            return r > 0;
        }
    }

@Override
public boolean atualizar(Notificacao notificacao) {
  //  if (notificacao == null || notificacao.getId() == null) return false;

    String sql = "UPDATE notificacao SET mensagem = ?, dataEnvio = ?, lida = ? WHERE id = ?";

    try (Connection conexao = new SQLiteConexao().getConnexao();
         PreparedStatement stmt = conexao.prepareStatement(sql)) {

        stmt.setString(1, notificacao.getMensagem());
        stmt.setString(2, notificacao.getDataEnvio());
        stmt.setInt(3, notificacao.getLida() == null ? 0 : notificacao.getLida());
        stmt.setInt(4, notificacao.getId());

        int linhasAfetadas = stmt.executeUpdate();

        if (linhasAfetadas > 0) {
            // mantém a lógica original (não havia reload de lista aqui)
            return true;
        }

        return false; // nenhuma linha alterada

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao atualizar notificação: " + e.getMessage());
    }
}

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
     

     
         public List<Notificacao> listarNaoLidas(int idDestinatario) {
        List<Notificacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM notificacao WHERE idDestinatario = ? AND lida = 0";

        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idDestinatario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapping(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notificações não lidas: " + e.getMessage());
        }

        return lista;
    }

    public List<Notificacao> listarLidas(int idDestinatario) {
        List<Notificacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM notificacao WHERE idDestinatario = ? AND lida = 1";

        try (Connection conexao = new SQLiteConexao().getConnexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idDestinatario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapping(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notificações lidas: " + e.getMessage());
        }

        return lista;
    }

    private Notificacao mapping(ResultSet rs) throws SQLException {
    Notificacao n = new Notificacao();
    n.setId(rs.getInt("id"));
    n.setIdRemetente(rs.getInt("idRemetente"));
    n.setIdDestinatario(rs.getInt("idDestinatario"));
    n.setMensagem(rs.getString("mensagem"));
    n.setDataEnvio(rs.getString("dataEnvio"));
    n.setLida(rs.getInt("lida"));
    return n;
}

    
        public boolean marcarVariasComoLidas(List<Integer> ids) {
    String sql = "UPDATE notificacao SET lida = 1 WHERE id = ?";

    try (Connection conexao = new SQLiteConexao().getConnexao();
         PreparedStatement stmt = conexao.prepareStatement(sql)) {

        for (Integer id : ids) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        return true;

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao marcar várias notificações como lidas: " + e.getMessage());
    }
}


}