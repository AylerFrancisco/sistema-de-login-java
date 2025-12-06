/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

/**
 *
 * @author Ayler
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.swing.JOptionPane;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import view.CadastroUsuarioViewSwing;
import view.LoginViewSwing;

public class RegistroPresenter {

    private final CadastroUsuarioViewSwing view;
    private final IUsuarioRepository usuarioRepository;

    // Construtor padrão usado pela aplicação
    public RegistroPresenter(CadastroUsuarioViewSwing view) {
        this(view, new UsuarioRepositorySQLite());
    }

    // Construtor com injeção de dependência (para testes)
    public RegistroPresenter(CadastroUsuarioViewSwing view,
                             IUsuarioRepository usuarioRepository) {
        this.view = view;
        this.usuarioRepository = usuarioRepository;
        configurarListeners();
    }

    private void configurarListeners() {
        // Botão Cadastrar
        view.getBtnCadastrar().addActionListener(e -> cadastrarUsuario());
        // Se quiser, pode colocar o Enter como padrão
        view.getRootPane().setDefaultButton(view.getBtnCadastrar());
    }

    private void cadastrarUsuario() {
        String nome     = view.getTxtCadastroNome().getText().trim();
        String username = view.getTxtCadastroUsername().getText().trim();
        char[] senhaArr = view.getPsswSenhaCadastro().getPassword();
        String senha    = new String(senhaArr);

        // Validações simples
        if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "Preencha Nome, Username e Senha.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE
            );
            limparSenha(senhaArr);
            return;
        }

        try {
            // Verifica se já existe usuário com esse username
            Usuario existente = usuarioRepository.consultar(username);
            if (existente != null) {
                JOptionPane.showMessageDialog(
                        view,
                        "Já existe um usuário com esse username.",
                        "Usuário já cadastrado",
                        JOptionPane.ERROR_MESSAGE
                );
                limparSenha(senhaArr);
                return;
            }

            // Monta o objeto Usuario
            Usuario novo = new Usuario();
            novo.setUsuario(username);
            novo.setSenha(senha);

            // Aqui você pode ajustar o tipo conforme sua regra:
            // "usuario", "comum", etc.
            novo.setTipoCadastro("usuario");

            // Novo cadastro começa SEM autorização do admin
            novo.setAutorizado(0);

            // Data de cadastro (ex.: 2025-03-10 14:32)
            String dataCadastro = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            novo.setDataCadastro(dataCadastro);

            // Persiste no banco
            usuarioRepository.criar(novo);

            JOptionPane.showMessageDialog(
                    view,
                    "Usuário cadastrado com sucesso!\nAguarde autorização do administrador.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Após cadastro, volta para tela de login
            irParaTelaLogin();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Erro ao cadastrar usuário: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            limparSenha(senhaArr);
        }
    }

    private void irParaTelaLogin() {
        view.dispose();
        LoginViewSwing telaLogin = new LoginViewSwing();
        new LoginPresenter(telaLogin); // liga o presenter da tela de login
        telaLogin.setLocationRelativeTo(null);
        telaLogin.setVisible(true);
    }

    private void limparSenha(char[] senhaArr) {
        if (senhaArr != null) {
            Arrays.fill(senhaArr, '\0');
        }
    }
}