/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.Optional;
import model.Usuario;
import repository.IUsuarioRepository;

/**
 *
 * @author lukian.borges
 */
public class LoginService {
    
     private IUsuarioRepository usuarioRepository;

    public LoginService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

   public Optional<Usuario> autenticar(String user, String senha) throws RuntimeException{
    // Busca o usuário pelo e-mail e retorna um Optional com o resultado
    Optional<Usuario> usuario = Optional.ofNullable(usuarioRepository.buscarPorUsuario(user));

    // Verifica se o usuário existe e a senha é válida
    if (usuario.isEmpty() || !usuario.get().getSenha().equals(senha)) {
        throw new RuntimeException("Usuário ou senha inválidos");
    }
    

    // Retorna o Optional com o usuário
    return usuario;
}

    
}
