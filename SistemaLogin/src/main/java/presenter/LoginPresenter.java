/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author Ayler
 */
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import view.LoginViewSwing;
import view.AdminHomeViewSwing;
import view.UserHomeViewSwing;


public class LoginPresenter {

    private final LoginViewSwing view;
    private final IUsuarioRepository iUsuarioRepository;

    public LoginPresenter(LoginViewSwing view) {
        this.view = view;
        this.iUsuarioRepository = new UsuarioRepositorySQLite();

        configurarListeners();
    }

    private void configurarListeners() {
        view.getBtnLogin().addActionListener(e -> fazerLogin());
    }

    private void fazerLogin() {
        String username = view.getTxtUsernameField().getText();
        String senha = new String(view.getPsswSenhaField().getPassword());

        if (username.isEmpty() || senha.isEmpty()) {
            view.getLblErro().setText("Preencha usuário e senha.");
            return;
        }

        try {
            var usuario = iUsuarioRepository.consultar(username);

            if (usuario == null) {
                view.getLblErro().setText("Usuário não encontrado.");
                return;
            }

            if (!usuario.getSenha().equals(senha)) {
                view.getLblErro().setText("Senha incorreta.");
                return;
            }

            abrirTelaInicial(usuario.getTipoCadastro());  // admin ou user
            view.dispose();

        } catch (Exception ex) {
            view.getLblErro().setText("Erro ao tentar logar.");
            ex.printStackTrace();
        }
    }

    private void abrirTelaInicial(String tipo) {
        if ("admin".equalsIgnoreCase(tipo)) {
            new AdminHomeViewSwing().setVisible(true);
        } else {
            new UserHomeViewSwing().setVisible(true);
        }
    }
}
