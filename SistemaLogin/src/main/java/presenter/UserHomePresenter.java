package presenter;

import java.util.List;
import javax.swing.SwingUtilities;
import model.Usuario;
import observer.UsuarioObserver;
import repository.UsuarioRepositorySQLite;
import view.UserHomeViewSwing;

public class UserHomePresenter implements UsuarioObserver {

    private final UserHomeViewSwing view;
    private Usuario usuarioLogado;
    private final UsuarioRepositorySQLite usuarioRepository;

    public UserHomePresenter(Usuario usuarioLogado,
            UsuarioRepositorySQLite usuarioRepositorySQLite) {

        this.view = new UserHomeViewSwing();
        this.usuarioRepository = (usuarioRepositorySQLite != null)
                ? usuarioRepositorySQLite
                : UsuarioRepositorySQLite.getInstance();

        this.usuarioLogado = usuarioLogado;

        configurarAcoesMenu();

        view.setVisible(true);
        configurarUsuarioLogado();
    }

    private void configurarUsuarioLogado() {
        if (usuarioLogado == null) {
            return;
        }

        String tipoFormatado;

        switch (usuarioLogado.getTipoCadastro()) {
            case "ADMIN_MASTER":
                tipoFormatado = "ADMINISTRADOR MASTER";
                break;
            case "ADMIN":
                tipoFormatado = "ADMINISTRADOR";
                break;
            case "USER":
                tipoFormatado = "USUÁRIO";
                break;
            default:
                tipoFormatado = usuarioLogado.getTipoCadastro();
        }

        String texto = "Usuário logado: "
                + usuarioLogado.getUsuario()
                + " | Perfil: "
                + tipoFormatado;

        view.getTxtUserLog().setText(texto);
        view.getTxtUserLog().setEditable(false);
    }

 
    private void configurarAcoesMenu() {

        // NOTIFICAÇÕES -> CAIXA DE ENTRADA
        view.getMenuCaixaEntrada()
                .addActionListener(e -> abrirCaixaEntrada());

        // NOTIFICAÇÕES -> LIDAS
        view.getMenuLidas()
                .addActionListener(e -> abrirNotificacoesLidas());

        // ALTERAR SENHA
        view.getMnuAlterar()
                .addActionListener(e -> abrirAlterarSenha());

        // SAIR -> LOGOUT
        view.getMnuLogout()
                .addActionListener(e -> logout());
    }

    private void abrirCaixaEntrada() {
        new NotificacaoEmAbertoPresenter(view, usuarioLogado.getId());
    }

    private void abrirNotificacoesLidas() {
        new NotificacoesLidasPresenter(view, usuarioLogado.getId());
    }
    
    private void abrirAlterarSenha(){
        new AlterarSenhaPresenter(view, usuarioLogado);
    }


    private void logout() {
        view.dispose();
        new LoginPresenter(UsuarioRepositorySQLite.getInstance());
    }


    //      OBSERVER

    private void updateUsuarios(List<Usuario> usuarios) {
        SwingUtilities.invokeLater(() -> atualizarInformacoesUsuarioLogado(usuarios));
    }

    private void atualizarInformacoesUsuarioLogado(List<Usuario> usuarios) {
        Usuario usuarioAtualizado = usuarios.stream()
                .filter(u -> u.getId() == usuarioLogado.getId())
                .findFirst()
                .orElse(null);

        if (usuarioAtualizado != null) {
            usuarioLogado = usuarioAtualizado;
        }
    }

    @Override
    public void onUsuariosUpdated(List<Usuario> usuarios) {
        updateUsuarios(usuarios);
    }
}
