package service;

import java.util.ArrayList;
import java.util.List;
import model.Notificacao;
import model.Usuario;
import repository.IEnviarNotificacaoRepository;
import repository.EnviarNotificacaoSQLite;
import repository.UsuarioRepositorySQLite;


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

    public ResultadoEnvio enviarMultiplos(
            Integer idRemetente,
            List<Integer> destinatarios,
            String mensagem) throws Exception {

        if (idRemetente == null) {
            throw new IllegalArgumentException("Remetente inválido");
        }

        if (destinatarios == null || destinatarios.isEmpty()) {
            throw new IllegalArgumentException("Nenhum destinatário informado");
        }

        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensagem vazia");
        }

        List<Integer> naoAutorizados = new ArrayList<>();
        List<Notificacao> notificacoes = new ArrayList<>();

        //VALIDA TODOS PRIMEIRO
        for (Integer idDest : destinatarios) {
            Usuario u = usuarioRepository.buscarPorId(idDest);

            if (u == null || u.getAutorizado() == null || u.getAutorizado() != 1) {
                naoAutorizados.add(idDest);
            }
        }

        //SE EXISTIR PELO MENOS 1 NÃO AUTORIZADO → ABORTA TUDO
        if (!naoAutorizados.isEmpty()) {
            return new ResultadoEnvio(false, naoAutorizados);
        }

        //TODOS AUTORIZADOS → CRIA NOTIFICAÇÕES
        for (Integer idDest : destinatarios) {
            notificacoes.add(new Notificacao(idRemetente, idDest, mensagem));
        }

        boolean sucesso = repo.enviar(notificacoes);
        return new ResultadoEnvio(sucesso, List.of());
    }

    public static class ResultadoEnvio {

        private final boolean sucesso;
        private final List<Integer> destinatariosInvalidos;

        public ResultadoEnvio(boolean sucesso, List<Integer> destinatariosInvalidos) {
            this.sucesso = sucesso;
            this.destinatariosInvalidos = destinatariosInvalidos;
        }

        public boolean isSucesso() {
            return sucesso;
        }

        public List<Integer> getDestinatariosInvalidos() {
            return destinatariosInvalidos;
        }
    }
}
