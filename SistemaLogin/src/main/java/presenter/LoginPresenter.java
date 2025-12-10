/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author Ayler
 */
import java.util.Optional;
import javax.swing.JOptionPane;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import service.LoginService;
import view.LoginViewSwing;
import view.AdminHomeViewSwing;
import view.UserHomeViewSwing;


public class LoginPresenter {

    private final LoginViewSwing loginView;
    private AdminHomeViewSwing adminHomeViewSwing;
    //private final IUsuarioRepository iUsuarioRepository;
    private UserHomePresenter userHomePresenter;
    private AdminHomePresenter adminHomePresenter;
    private CadastroPresenter cadastroPresenter;
    private final LoginService loginService;
    private Usuario usuarioGlobal;

      public LoginPresenter(IUsuarioRepository usuarioRepository) {
        this.loginView = new LoginViewSwing();
        this.cadastroPresenter = new CadastroPresenter(usuarioRepository);
        this.loginService = new LoginService(usuarioRepository);

        configurar();
    }

    public void configurar() {
        loginView.getBtnLogin().addActionListener((java.awt.event.ActionEvent e) -> {
            String user = loginView.getTxtUsernameField().getText();
            String senha = new String(loginView.getPsswSenhaField().getPassword());

            try {
                Optional<Usuario> usuario = loginService.autenticar(user, senha);
                this.usuarioGlobal = usuario.get();
                if (usuario.isPresent()) {
                    loginView.dispose();

                    if (this.usuarioGlobal != null) {
                        if ("ADMIN_MASTER".equals(this.usuarioGlobal.getTipoCadastro())) {
                            this.adminHomePresenter = new AdminHomePresenter(
                                    usuario.get(),
                                    UsuarioRepositorySQLite.getInstance()
                            );
                        } else if ("USER".equals(this.usuarioGlobal.getTipoCadastro())) {
                            this.userHomePresenter = new UserHomePresenter(
                                    usuario.get(),
                                    UsuarioRepositorySQLite.getInstance()
                            );
                        }

                    }
                    
                
                


                } else {
                    JOptionPane.showMessageDialog(null, "Login falhou! Verifique suas credenciais.", "Erro de Login", JOptionPane.ERROR_MESSAGE);

                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }

        });

        loginView.getBtnSolicitarAcesso().addActionListener((java.awt.event.ActionEvent e) -> {
            loginView.dispose();
            cadastroPresenter.exibir();


        });
        
        loginView.setVisible(true);
    }

 
}
