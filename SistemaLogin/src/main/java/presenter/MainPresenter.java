/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import configSQLite.SQLiteInicilizarBanco;
import java.util.List;
import model.Usuario;
import repository.UsuarioRepositorySQLite;

/**
 *
 * @author Ayler
 */
public class MainPresenter {
    
     public static void main(String[] args) {
     
         // 1. Inicializar banco de dados
        new SQLiteInicilizarBanco().inicializar();

        // 2. Instanciar repository
        UsuarioRepositorySQLite usuarioRepo = new UsuarioRepositorySQLite();

        // 3. Verificar se existe usuário cadastrado
         List<Usuario> usuario = usuarioRepo.listarTodos();

        // 4. Fluxo de inicialização
        if (usuario.isEmpty()) {
            // *** PRIMEIRA EXECUÇÃO DO SISTEMA ***
//            CadastroInicialView view = new CadastroInicialView();
//            CadastroInicialPresenter presenter = new CadastroInicialPresenter(view, usuarioRepo);
           new PrimeiroAcessoPresenter(UsuarioRepositorySQLite.getInstance());

//            view.setVisible(true);   // abre a tela
        } else {
            // *** USUÁRIO JÁ EXISTE → VAI PARA LOGIN ***
            //LoginView view = new LoginView();
             new LoginPresenter(UsuarioRepositorySQLite.getInstance());
    }
    
}
}