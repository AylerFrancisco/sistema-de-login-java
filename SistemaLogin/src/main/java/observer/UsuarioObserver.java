/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import java.util.List;
import model.Usuario;

/**
 *
 * @author lukian.borges
 */
public interface UsuarioObserver {
    
    void onUsuariosUpdated(List<Usuario> usuarios);
    
}
