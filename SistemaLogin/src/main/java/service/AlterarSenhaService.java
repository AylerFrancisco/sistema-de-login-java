/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import adapter.PasswordValidator;
import model.Usuario;
import repository.UsuarioRepositorySQLite;

/**
 *
 * @author lukian.borges
 */
public class AlterarSenhaService {

    private final UsuarioRepositorySQLite repository;

    public AlterarSenhaService(UsuarioRepositorySQLite repository) {
        this.repository = repository;
    }

    public void alterarSenha(
            Usuario usuarioLogado,
            String senhaAtual,
            String novaSenha,
            String confirmaSenha,
            PasswordValidator validator
    ) {

       
        if (!usuarioLogado.getSenha().equals(senhaAtual)) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

      
        if (!novaSenha.equals(confirmaSenha)) {
            throw new IllegalArgumentException("Nova senha e confirmação não coincidem.");
        }

       
        validator.validarSenha(novaSenha);

     
        usuarioLogado.setSenha(novaSenha);

        boolean atualizado = repository.atualizar(usuarioLogado);

        if (!atualizado) {
            throw new RuntimeException("Erro ao atualizar a senha.");
        }
    }
}
