package service;

import java.util.List;
import model.Notificacao;
import repository.EnviarNotificacaoSQLite;

public class NotificacaoLidaService {

    private final EnviarNotificacaoSQLite repo;

    public NotificacaoLidaService() {
        this.repo = EnviarNotificacaoSQLite.getInstance();
    }

    public List<Notificacao> listarLidas(Integer idUsuario) {
        if (idUsuario == null)
            throw new IllegalArgumentException("Usuário inválido");

        return repo.listarLidas(idUsuario);
    }
}
