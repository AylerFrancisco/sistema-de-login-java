package service;

import java.util.ArrayList;
import java.util.List;
import model.Notificacao;
import model.Usuario;
import repository.IEnviarNotificacaoRepository;
import repository.EnviarNotificacaoSQLite;
import repository.UsuarioRepositorySQLite;

/**
 * Serviço que valida destinatários e utiliza o repositório para persistir.
 */
public class EnviarNotificacaoService {

    private final IEnviarNotificacaoRepository repo;
    private final UsuarioRepositorySQLite usuarioRepository; // para validação

    public EnviarNotificacaoService() {
        this.repo = EnviarNotificacaoSQLite.getInstance();
        this.usuarioRepository = UsuarioRepositorySQLite.getInstance();
    }

    public EnviarNotificacaoService(IEnviarNotificacaoRepository repo, UsuarioRepositorySQLite usuarioRepository) {
        this.repo = repo;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Valida lista de ids de destinatários e envia notificações.
     * Retorna lista de ids inválidos (não autorizados ou inexistentes).
     *
     * @param idRemetente id do admin remetente
     * @param destinatarios lista de ids destinatarios
     * @param mensagem corpo da mensagem
     * @return resultado contendo sucesso e lista de destinatários invalidos (vazia se nenhum)
     * @throws Exception se ocorrer erro de persistência
     */
    public ResultadoEnvio enviarMultiplos(Integer idRemetente, List<Integer> destinatarios, String mensagem) throws Exception {
        if (idRemetente == null) throw new IllegalArgumentException("Remetente inválido");
        if (destinatarios == null || destinatarios.isEmpty()) throw new IllegalArgumentException("Nenhum destinatário informado");
        if (mensagem == null || mensagem.trim().isEmpty()) throw new IllegalArgumentException("Mensagem vazia");

        List<Integer> invalidos = new ArrayList<>();
        List<Notificacao> paraEnviar = new ArrayList<>();

        for (Integer idDest : destinatarios) {
            Usuario u = usuarioRepository.buscarPorId(idDest);
            if (u == null || u.getAutorizado() == null || u.getAutorizado() != 1) {
                invalidos.add(idDest);
            } else {
                Notificacao n = new Notificacao(idRemetente, idDest, mensagem);
                paraEnviar.add(n);
            }
        }

        if (paraEnviar.isEmpty()) {
            // nenhum destinatário válido
            return new ResultadoEnvio(false, invalidos);
        }

        // tenta enviar todos em transação (repo lança exception em caso de falha)
        boolean ok = repo.enviar(paraEnviar);
        return new ResultadoEnvio(ok, invalidos);
    }

    public static class ResultadoEnvio {
        private final boolean sucesso;
        private final List<Integer> destinatariosInvalidos;

        public ResultadoEnvio(boolean sucesso, List<Integer> destinatariosInvalidos) {
            this.sucesso = sucesso;
            this.destinatariosInvalidos = destinatariosInvalidos;
        }

        public boolean isSucesso() { return sucesso; }
        public List<Integer> getDestinatariosInvalidos() { return destinatariosInvalidos; }
    }
}
