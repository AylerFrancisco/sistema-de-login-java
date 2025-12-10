/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import adapter.PasswordValidator;
import exception.UsuarioJaCadastradoException;
import java.util.Optional;
import model.Usuario;
import repository.IUsuarioRepository;

/**
 *
 * @author lukian.borges
 */
public class PrimeiroAcessoService {
    
    private final IUsuarioRepository usuarioRepository;
    
      public PrimeiroAcessoService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
      
      public boolean cadastrarUsuario(String usuario1, String senha, String confirmarSenha, String tipoCadastro, Integer autorizado, String dataCadastro, PasswordValidator passwordValidator) throws UsuarioJaCadastradoException{

        passwordValidator.validarSenha(senha);
//        Usuario usuario = usuarioRepository.buscarPorUsuario(usuario1);
//        if (usuario != null) {
//            throw new UsuarioJaCadastradoException("Usuario já cadastrado");
//        }
        Usuario newUser = new Usuario(usuario1, senha, tipoCadastro, autorizado, dataCadastro);

        return usuarioRepository.criar(newUser);
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
     
    public boolean isPasswordSame(String senha, String confirmarSenha) {
        return senha.equals(confirmarSenha);
    }
      
}
