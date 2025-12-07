/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import adapter.PasswordValidator;
import adapter.PasswordValidatorAdapter;
import exception.UsuarioJaCadastradoException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import repository.IUsuarioRepository;
import service.CadastroService;
import state.CadastroState;
import state.CadastroSucessoState;
import state.CadastroUsuarioSucessoState;
import view.CadastroUsuarioViewSwing;

/**
 *
 * @author lukian.borges
 */
public class CadastroPresenter {
    
    
     private final CadastroUsuarioViewSwing cadastroView;
    private final CadastroService cadastroService;
    private PasswordValidator passwordValidator;
    private CadastroState atualEstado;
    private final IUsuarioRepository usuarioRepository;

    public CadastroPresenter(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cadastroView = new CadastroUsuarioViewSwing();
        this.cadastroService = new CadastroService(usuarioRepository);
        this.passwordValidator = new PasswordValidatorAdapter();
        configurar();
    }
    
       public void configurar() throws IllegalArgumentException {

        cadastroView.getBtnCadastrar().addActionListener((java.awt.event.ActionEvent e) -> {
            String nome = cadastroView.getTxtCadastroNome().getText();
            String senha = new String(cadastroView.getPsswSenhaCadastro().getPassword());
            String confirmarSenha = new String(cadastroView.getPsswSenhaCadastro().getPassword());
            String tipoCadastro = "USER";
            Integer autorizado = 0;
            String dataCadastro = LocalDate.now().toString();
            

            if (!cadastroService.isPasswordSame(senha, confirmarSenha)) {
                JOptionPane.showMessageDialog(cadastroView, "As senhas não coincidem. Por favor, tente novamente.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                boolean isCriado = cadastroService.cadastrarUsuario(nome, senha, confirmarSenha, tipoCadastro, autorizado, dataCadastro, passwordValidator);

                if (isCriado) {
                    JOptionPane.showMessageDialog(cadastroView,
                            "Solicitação feita com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    changeState(new CadastroUsuarioSucessoState(this));
                    atualEstado.handle();
                } else {
                    JOptionPane.showMessageDialog(cadastroView,
                            "Erro ao criar usuário",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(cadastroView,
                        ex.getMessage(),
                        "Erro de Validação da Senha",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioJaCadastradoException ex) {
                JOptionPane.showMessageDialog(cadastroView,
                        ex.getMessage(),
                        "Usuario já cadastrado no sistema",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


    }
       
    public void exibir() {
        cadastroView.setVisible(true);
    }
       
     public void alterarEstado(CadastroState novoEstado) {
        this.atualEstado = novoEstado;
    }

    public CadastroUsuarioViewSwing getCadastroView() {
        return cadastroView;
    }

    public CadastroService getCadastroService() {
        return cadastroService;
    }

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    public void changeState(CadastroState state) {
        this.atualEstado = state;
    }

    public IUsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }    
    
}
