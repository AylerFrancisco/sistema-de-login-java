/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.util.List;
import javax.swing.SwingUtilities;
import model.Usuario;
import observer.UsuarioObserver;
import repository.UsuarioRepositorySQLite;
import view.UserHomeViewSwing;

/**
 *
 * @author lukian.borges
 */
public class UserHomePresenter implements UsuarioObserver {
    
     private final UserHomeViewSwing view;

   // private final List<WindowCommand> windowCommands = new ArrayList<>();
    private Usuario usuarioLogado;

    private UsuarioRepositorySQLite usuarioRepository;

    public UserHomePresenter(Usuario usuarioLogado,
                              UsuarioRepositorySQLite usuarioRepositorySQLite) {

        // Inicializando as variáveis
        this.view = new UserHomeViewSwing();
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
