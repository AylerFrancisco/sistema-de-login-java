/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import adapter.PasswordValidator;
import adapter.PasswordValidatorAdapter;
import javax.swing.JDialog;
import javax.swing.JFrame;
import model.Usuario;
import repository.UsuarioRepositorySQLite;
import service.AlterarSenhaService;
import view.AlterarSenhaViewSwing;
import javax.swing.JOptionPane;


/**
 *
 * @author lukian.borges
 */
public class AlterarSenhaPresenter {

    private final JDialog dialog;
    private final AlterarSenhaViewSwing view;
    private final AlterarSenhaService service;
    private final PasswordValidator passwordValidator;
    private final Usuario usuarioLogado;

    public AlterarSenhaPresenter(JFrame parent, Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;
        this.view = new AlterarSenhaViewSwing();
        this.service = new AlterarSenhaService(UsuarioRepositorySQLite.getInstance());
        this.passwordValidator = new PasswordValidatorAdapter();

        this.dialog = new JDialog(parent, "Alterar Senha", true);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        configurarAcoes();

        dialog.setVisible(true);
    }

    private void configurarAcoes() {

        view.getBtnAlterarSenhaAlterarSenha1().addActionListener(e -> {

            String senhaAtual = new String(view.getPsswAlterarSenhaAtual().getPassword());
            String novaSenha = new String(view.getPsswAlterarSenhaNova().getPassword());
            String confirma = new String(view.getPsswAlterarSenhaConfirma().getPassword());

            if (senhaAtual.isBlank() || novaSenha.isBlank() || confirma.isBlank()) {
                JOptionPane.showMessageDialog(view,
                        "Preencha todos os campos.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {

                service.alterarSenha(
                        usuarioLogado,
                        senhaAtual,
                        novaSenha,
                        confirma,
                        passwordValidator
                );

                JOptionPane.showMessageDialog(view,
                        "Senha alterada com sucesso!");

                dialog.dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view,
                        ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Erro ao alterar senha: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}