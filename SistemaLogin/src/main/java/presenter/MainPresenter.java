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
     
         
        new SQLiteInicilizarBanco().inicializar();

  
        UsuarioRepositorySQLite usuarioRepo = new UsuarioRepositorySQLite();

     
         List<Usuario> usuario = usuarioRepo.listarTodos();

     
        if (usuario.isEmpty()) {
         
//            CadastroInicialView view = new CadastroInicialView();
//            CadastroInicialPresenter presenter = new CadastroInicialPresenter(view, usuarioRepo);
           new PrimeiroAcessoPresenter(UsuarioRepositorySQLite.getInstance());

//            view.setVisible(true);   // abre a tela
        } else {
       

             new LoginPresenter(UsuarioRepositorySQLite.getInstance());
    }
    
}
}