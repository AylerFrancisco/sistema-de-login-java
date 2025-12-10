/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import adapter.PasswordValidator;
import adapter.PasswordValidatorAdapter;
import exception.UsuarioJaCadastradoException;
import java.time.LocalDate;
import java.util.Optional;
import javax.swing.JOptionPane;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import service.PrimeiroAcessoService;
import state.CadastroState;
import state.CadastroSucessoState;
import view.PrimeiroAcessoViewSwing;

/**
 *
 * @author lukian.borges
 */
public class PrimeiroAcessoPresenter {
 
    private PrimeiroAcessoViewSwing primeiroAcessoViewSwing;
    private PrimeiroAcessoService primeiroAcessoService;
    private PasswordValidator passwordValidator;
    private CadastroState atualEstado;
    private final IUsuarioRepository usuarioRepository;
    private AdminHomePresenter adminHomePresenter;
    
    public PrimeiroAcessoPresenter(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.primeiroAcessoViewSwing = new PrimeiroAcessoViewSwing();
        
        this.passwordValidator = new PasswordValidatorAdapter();
         this.primeiroAcessoService = new PrimeiroAcessoService(usuarioRepository); // <-- FALTAVA!!
      
        configurar();

        //loginView.getJBtnCadastrar().addActionListener(e -> acessarTelaCadastro());
    }

    public void configurar() {
        primeiroAcessoViewSwing.getBtnCadastrar().addActionListener((java.awt.event.ActionEvent e) -> {
            String usuario = primeiroAcessoViewSwing.getTxtusername().getText();
            String senha = new String(primeiroAcessoViewSwing.getPsswsenha().getPassword());
            String confirmarSenha = new String(primeiroAcessoViewSwing.getPsswsenhaConfirma().getPassword());
            String tipoCadastro = "ADMIN_MASTER";
            Integer autorizado = 1;
            String dataCadastro = LocalDate.now().toString();

            if (usuario.trim().isEmpty()) {
                JOptionPane.showMessageDialog(primeiroAcessoViewSwing,
                        "O campo usuário não pode estar vazio.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!primeiroAcessoService.isPasswordSame(senha, confirmarSenha)) {
                JOptionPane.showMessageDialog(primeiroAcessoViewSwing, "As senhas não coincidem. Por favor, tente novamente.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }



            try {
                boolean isCriado = primeiroAcessoService.cadastrarUsuario(usuario, senha, confirmarSenha, tipoCadastro, autorizado, dataCadastro, passwordValidator);

                if (isCriado) {
                    JOptionPane.showMessageDialog(primeiroAcessoViewSwing,
                            "Usuário criado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
//                    changeState(new CadastroSucessoState(this));
//                    atualEstado.handle();
                  
                     Optional<Usuario> usuario1 = primeiroAcessoService.autenticar(usuario, senha);
                if (usuario1.isPresent()) {
                    primeiroAcessoViewSwing.dispose();

                    this.adminHomePresenter = new AdminHomePresenter(
                            usuario1.get(),
                            UsuarioRepositorySQLite.getInstance()
                    );

                }
                    
                } else {
                    JOptionPane.showMessageDialog(primeiroAcessoViewSwing,
                            "Erro ao criar usuário",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }

               
                

          

                
                
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(primeiroAcessoViewSwing,
                        ex.getMessage(),
                        "Erro de Validação da Senha",
                        JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioJaCadastradoException ex) {
                JOptionPane.showMessageDialog(primeiroAcessoViewSwing,
                        ex.getMessage(),
                        "Email já cadastrado no sistema",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

//        loginView.getJBtnCadastrar().addActionListener((java.awt.event.ActionEvent e) -> {
//            loginView.dispose();
//            cadastroPresenter.exibir();
//
//        });
       
      primeiroAcessoViewSwing.setVisible(true);
        
         
    }

    public PrimeiroAcessoViewSwing getPrimeiroAcessoViewSwing() {
        return primeiroAcessoViewSwing;
    }

    public void setPrimeiroAcessoViewSwing(PrimeiroAcessoViewSwing primeiroAcessoViewSwing) {
        this.primeiroAcessoViewSwing = primeiroAcessoViewSwing;
    }

    public PrimeiroAcessoService getPrimeiroAcessoService() {
        return primeiroAcessoService;
    }

    public void setPrimeiroAcessoService(PrimeiroAcessoService primeiroAcessoService) {
        this.primeiroAcessoService = primeiroAcessoService;
    }

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    public CadastroState getAtualEstado() {
        return atualEstado;
    }

    public void setAtualEstado(CadastroState atualEstado) {
        this.atualEstado = atualEstado;
    }
     
     
     public void changeState(CadastroState state) {
        this.atualEstado = state;
    }

    public IUsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
}
