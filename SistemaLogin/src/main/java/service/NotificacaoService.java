package service;

import java.util.List;
import model.Notificacao;
import repository.EnviarNotificacaoSQLite;

/**
 * Serviço para listar e atualizar notificações.
 */
public class NotificacaoService {

    private final EnviarNotificacaoSQLite repo;

    public NotificacaoService() {
        this.repo = EnviarNotificacaoSQLite.getInstance();
    }

    public List<Notificacao> listarNaoLidas(Integer idUsuario) {
        if (idUsuario == null) 
            throw new IllegalArgumentException("Usuário inválido");

        return repo.listarNaoLidas(idUsuario);
    }

    /**
     * Marca várias notificações como lidas.
     * @param ids lista de IDs das notificações
     * @return true se nenhuma falha ocorrer
     */
    public boolean marcarComoLidas(List<Integer> ids) {
        if (ids == null || ids.isEmpty())
            return false;

        return repo.marcarVariasComoLidas(ids);
    }
}
