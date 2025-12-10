/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import adapter.PasswordValidator;
import exception.UsuarioJaCadastradoException;
import model.Usuario;
import repository.IUsuarioRepository;

/**
 *
 * @author lukian.borges
 */
public class CadastroService {
    
    private final IUsuarioRepository usuarioRepository;
    
     public CadastroService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean cadastrarUsuario(String user, String senha, String confirmarSenha, String tipoCadastro, Integer autorizado, String dataCadastro, PasswordValidator passwordValidator) throws UsuarioJaCadastradoException{

        passwordValidator.validarSenha(senha);
        Usuario usuario = usuarioRepository.buscarPorUsuario(user);
        if (usuario != null) {
            throw new UsuarioJaCadastradoException("Usuario já cadastrado");
        }
        Usuario newUser = new Usuario(user, senha, tipoCadastro, autorizado, dataCadastro);

        return usuarioRepository.criar(newUser);
    }

    public boolean isPasswordSame(String senha, String confirmarSenha) {
        return senha.equals(confirmarSenha);
    }
    
    
}
