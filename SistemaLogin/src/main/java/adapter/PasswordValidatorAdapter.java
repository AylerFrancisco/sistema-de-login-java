/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adapter;

import com.pss.senha.validacao.ValidadorSenha;
import java.util.List;

/**
 *
 * @author lukian.borges
 */
public class PasswordValidatorAdapter implements PasswordValidator {
    
    private final ValidadorSenha validadorSenha;

    public PasswordValidatorAdapter() {
        this.validadorSenha = new ValidadorSenha();
    }

    @Override
    public void validarSenha(String senha) {
        List<String> erros = validadorSenha.validar(senha);
        if (!erros.isEmpty()) {
            String mensagemErro = String.join("\n", erros);
            throw new IllegalArgumentException("Senha inválida:\n" + mensagemErro);
        }
    }
    
}
