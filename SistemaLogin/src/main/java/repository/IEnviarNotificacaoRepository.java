package repository;

import java.util.List;
import model.Notificacao;

/**
 * Interface do repositório de notificações
 */
public interface IEnviarNotificacaoRepository {

    boolean enviar(List<Notificacao> notificacoes) throws Exception;

    List<Notificacao> listarPorDestinatario(int idDestinatario) throws Exception;

    List<Notificacao> listarTodos() throws Exception;

    boolean remover(int idNotificacao) throws Exception;

    boolean atualizar(Notificacao notificacao) throws Exception;
    
    boolean salvar(Notificacao n) throws Exception;
    
    boolean marcarComoLida(int idNotificacao) throws Exception;
    
    
}
