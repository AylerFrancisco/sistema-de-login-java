package presenter;

import java.util.Arrays;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import view.AdminHomeViewSwing;
import view.LoginViewSwing;
import view.UserHomeViewSwing;
import view.CadastroUsuarioViewSwing;

public class LoginPresenter {

    private final LoginViewSwing view;
    private final IUsuarioRepository usuarioRepository;

    public LoginPresenter(LoginViewSwing view) {
        this(view, new UsuarioRepositorySQLite());
    }

    public LoginPresenter(LoginViewSwing view, IUsuarioRepository usuarioRepository) {
        this.view = view;
        this.usuarioRepository = usuarioRepository;
        configurarListeners();
    }

    private void configurarListeners() {
        // Botão LOGIN
        view.getBtnLogin().addActionListener(e -> fazerLogin());

        // Botão CADASTRAR
        view.getBtnCadastrarUsuario().addActionListener(e -> abrirTelaCadastro());

        // Enter = Login
        view.getRootPane().setDefaultButton(view.getBtnLogin());
    }

    private void fazerLogin() {
        String username = view.getTxtUsernameField().getText().trim();
        char[] senhaChars = view.getPsswSenhaField().getPassword();
        String senha = new String(senhaChars);

        if (username.isEmpty() || senha.isEmpty()) {
            view.getLblErro().setText("Informe usuário e senha.");
            limparSenha(senhaChars);
            return;
        }

        try {
            Usuario usuario = usuarioRepository.consultar(username);

            if (usuario == null) {
                view.getLblErro().setText("Usuário não encontrado.");
                limparSenha(senhaChars);
                return;
            }

            // só entra se estiver autorizado
            if (usuario.getAutorizado() == null || usuario.getAutorizado() == 0) {
                view.getLblErro().setText("Acesso não liberado. Aguarde autorização.");
                limparSenha(senhaChars);
                return;
            }

            if (!usuario.getSenha().equals(senha)) {
                view.getLblErro().setText("Senha incorreta.");
                limparSenha(senhaChars);
                return;
            }

            abrirTelaInicial(usuario);
            view.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.getLblErro().setText("Erro ao tentar autenticar.");
        } finally {
            limparSenha(senhaChars);
        }
    }

    private void abrirTelaCadastro() {
        CadastroUsuarioViewSwing telaCadastro = new CadastroUsuarioViewSwing();
        // Se quiser já ligar com o presenter responsável pelo cadastro:
        // new RegistroPresenter(telaCadastro);
        telaCadastro.setLocationRelativeTo(null);
        telaCadastro.setVisible(true);
    }

    private void abrirTelaInicial(Usuario usuario) {
        String tipo = usuario.getTipoCadastro();

        if (tipo != null && tipo.equalsIgnoreCase("administrador")) {
            AdminHomeViewSwing telaAdmin = new AdminHomeViewSwing();
            // aqui você pode injetar o usuário logado no rodapé, etc.
            telaAdmin.setLocationRelativeTo(null);
            telaAdmin.setVisible(true);
        } else {
            UserHomeViewSwing telaUser = new UserHomeViewSwing();
            // idem para usuário comum
            telaUser.setLocationRelativeTo(null);
            telaUser.setVisible(true);
        }
    }

    private void limparSenha(char[] senhaChars) {
        if (senhaChars != null) {
            Arrays.fill(senhaChars, '\0');
        }
    }
}
