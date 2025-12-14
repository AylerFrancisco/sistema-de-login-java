package presenter;

import java.awt.Dialog;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Notificacao;
import model.Usuario;
import repository.UsuarioRepositorySQLite;
import service.EnviarNotificacaoService;
import view.EnviarNotificacaoViewSwing;


public class EnviarNotificacaoPresenter {

    private final JDialog dialog;
    private final EnviarNotificacaoViewSwing painel;
    private final EnviarNotificacaoService service;
    private final UsuarioRepositorySQLite usuarioRepository;
    private final Integer remetenteId;

    public EnviarNotificacaoPresenter(JFrame parent, Integer remetenteId) {
        this.remetenteId = remetenteId;
        this.usuarioRepository = UsuarioRepositorySQLite.getInstance();
        this.service = new EnviarNotificacaoService();

        painel = new EnviarNotificacaoViewSwing();

        dialog = new JDialog(parent, "Enviar Notificação", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(painel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        carregarUsuarios();
        configurarAcoes();

        dialog.setVisible(true);
    }

    private void carregarUsuarios() {
        List<Usuario> todos = usuarioRepository.listarTodos();
        painel.setUsuarios(todos); 
    }

    private void configurarAcoes() {
        painel.getBtnEnviar().addActionListener(e -> {
            List<Integer> selecionados = painel.getUsuariosSelecionados();
            if (selecionados.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Selecione pelo menos um usuário.");
                return;
            }

            String mensagem = painel.getMensagem();
            if (mensagem.isBlank()) {
                JOptionPane.showMessageDialog(dialog, "Digite uma mensagem.");
                return;
            }

            try {
                EnviarNotificacaoService.ResultadoEnvio res
                        = service.enviarMultiplos(remetenteId, selecionados, mensagem);

                if (!res.isSucesso()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Só é permitido enviar notificação para usuários autorizados.\n"
                            + "Usuários não autorizados selecionados: " + res.getDestinatariosInvalidos(),
                            "Envio bloqueado",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                JOptionPane.showMessageDialog(dialog, "Notificações enviadas com sucesso!");
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        });
    }

}
