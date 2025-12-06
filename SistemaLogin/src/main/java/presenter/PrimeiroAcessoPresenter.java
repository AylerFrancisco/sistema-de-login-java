/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author Ayler
 */
import java.time.LocalDate;
import java.util.Arrays;
import javax.swing.JOptionPane;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import view.AdminHomeViewSwing;
import view.PrimeiroAcessoViewSwing;

public class PrimeiroAcessoPresenter {

    private final PrimeiroAcessoViewSwing view;
    private final IUsuarioRepository usuarioRepository;

    public PrimeiroAcessoPresenter(PrimeiroAcessoViewSwing view) {
        this(view, new UsuarioRepositorySQLite());
    }

    public PrimeiroAcessoPresenter(PrimeiroAcessoViewSwing view,
                                   IUsuarioRepository usuarioRepository) {
        this.view = view;
        this.usuarioRepository = usuarioRepository;
        configurarListeners();
    }

    private void configurarListeners() {
        view.getBtnCadastrar().addActionListener(e -> cadastrarPrimeiroAdmin());
        view.getRootPane().setDefaultButton(view.getBtnCadastrar());
    }

    private void cadastrarPrimeiroAdmin() {
        String nome = view.getTxtnome().getText().trim();
        String username = view.getTxtusername().getText().trim();
        char[] senhaArr = view.getPsswsenha().getPassword();
        char[] senhaConfArr = view.getPsswsenhaConfirma().getPassword();
        String senha = new String(senhaArr);
        String senhaConfirma = new String(senhaConfArr);

        // validações simples
        if (nome.isEmpty() || username.isEmpty() || senha.isEmpty() || senhaConfirma.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Preencha todos os campos.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE
            );
            limparSenha(senhaArr, senhaConfArr);
            return;
        }

        if (!senha.equals(senhaConfirma)) {
            JOptionPane.showMessageDialog(
                    view,
                    "As senhas não conferem.",
                    "Senha",
                    JOptionPane.ERROR_MESSAGE
            );
            limparSenha(senhaArr, senhaConfArr);
            return;
        }

        try {
            // segurança extra: se já existir usuário, não deveria estar no fluxo de primeiro acesso
            if (!usuarioRepository.listarTodos().isEmpty()) {
                JOptionPane.showMessageDialog(
                        view,
                        "Já existe usuário cadastrado. Primeiro acesso não é mais permitido.",
                        "Primeiro acesso",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Usuario admin = new Usuario();
            admin.setUsuario(username);
            admin.setSenha(senha);
            admin.setTipoCadastro("admin");      // administrador inicial
            admin.setAutorizado(1);              // já autorizado
            admin.setDataCadastro(LocalDate.now().toString());

            boolean criado = usuarioRepository.criar(admin);

            if (criado) {
                JOptionPane.showMessageDialog(
                        view,
                        "Administrador criado com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );
                abrirHomeAdmin(admin);
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        view,
                        "Não foi possível criar o administrador.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    view,
                    "Erro ao criar administrador: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            limparSenha(senhaArr, senhaConfArr);
        }
    }

    private void abrirHomeAdmin(Usuario admin) {
        AdminHomeViewSwing tela = new AdminHomeViewSwing();
        // aqui depois você pode usar o admin para preencher o rodapé
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    private void limparSenha(char[]... arrays) {
        for (char[] arr : arrays) {
            if (arr != null) {
                Arrays.fill(arr, '\0');
            }
        }
    }
}
