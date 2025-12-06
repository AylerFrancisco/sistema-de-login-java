/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repository;

import java.util.List;
import model.Usuario;

/**
 *
 * @author Ayler
 */
public interface IUsuarioRepository {
    
     boolean criar(Usuario usuario);

    Usuario consultar(String nome);

    void atualizar(Usuario usuario);

    boolean deletar(String email);

    List<Usuario> listarTodos();

    Usuario buscarPorUsuario(String usuario1);
    
}
