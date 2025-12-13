/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import javax.swing.SwingUtilities;
import model.Usuario;
import observer.UsuarioObserver;
import repository.UsuarioRepositorySQLite;
import service.CadastroService;
import view.AdminHomeViewSwing;

/**
 *
 * @author lukian.borges
 */
public class AdminHomePresenter implements UsuarioObserver {

    private final AdminHomeViewSwing view;

    // private final List<WindowCommand> windowCommands = new ArrayList<>();
    private Usuario usuarioLogado;

    private UsuarioRepositorySQLite usuarioRepository;

    public AdminHomePresenter(Usuario usuarioLogado,
            UsuarioRepositorySQLite usuarioRepositorySQLite) {

        // Inicializando as variáveis
        this.view = new AdminHomeViewSwing();
        this.usuarioRepository = usuarioRepositorySQLite != null ? usuarioRepositorySQLite : UsuarioRepositorySQLite.getInstance();;
        this.usuarioLogado = usuarioLogado;;

        // Registrando observadores
        //this.repository.addObserver(this);
        //this.usuarioRepository.addUsuarioObserver(this);
        // Inicializando a view
//        this.view.setUsuarioLogado(usuarioLogado);
//        GlobalWindowManager.initialize(view);
//
//        // Inicializando os comandos e outras configurações
//        this.comandos = inicializarComandos();
//        inicializarEExecutarWindowCommands();
        // Tornando a view visível
        view.setVisible(true);
        configurarAcoes();
    }

    private void configurarAcoes() {

    // =========================
    // USUÁRIO -> CADASTRAR
    // =========================
    view.getMnuCadastrar().addActionListener(e -> {

        CadastroService cadastroService =
                new CadastroService(usuarioRepository);

        new UsuarioCadastroHomePresenter(
                usuarioRepository,
                cadastroService,
                view
        );
    });

    // =========================
    // USUÁRIO -> AUTORIZAR
    // =========================
    view.getMnuAutorizar().addActionListener(e -> {

        new ControleAcessoPresente(
                usuarioRepository,
                view
        );
    });
        
         // SAIR -> LOGOUT
        view.getMnuLogout()
                .addActionListener(e -> logout());
        
 view.getItemCadastrarNotificacao().addActionListener(e -> {
    new EnviarNotificacaoPresenter(view, usuarioLogado.getId());
});



    }
    
       private void logout() {
        view.dispose(); // fecha a Home
        new LoginPresenter(UsuarioRepositorySQLite.getInstance());
    }

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
            //view.setUsuarioLogado(usuarioAtualizado);
        }
    }

    @Override
    public void onUsuariosUpdated(List<Usuario> usuarios) {
        updateUsuarios(usuarios);
    }

}
