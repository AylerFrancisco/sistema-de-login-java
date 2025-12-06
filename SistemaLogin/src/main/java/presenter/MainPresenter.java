/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import config.InicializarBanco;
import configSQLite.SQLiteInicilizarBanco;
import java.util.List;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import view.LoginViewSwing;
import view.PrimeiroAcessoViewSwing;

/**
 * Controla o fluxo inicial da aplicação:
 *  - inicializa o banco
 *  - verifica se já existe usuário cadastrado
 *  - decide entre PrimeiroAcesso ou Login.
 */
public class MainPresenter {

    private final InicializarBanco inicializador;
    private final IUsuarioRepository usuarioRepository;

    public MainPresenter() {
        this.inicializador = new SQLiteInicilizarBanco();
        this.usuarioRepository = new UsuarioRepositorySQLite();
    }

    public void iniciarAplicacao() {
        // cria tabelas se não existirem
        inicializador.inicializar();

        if (temUsuariosCadastrados()) {
            abrirTelaLogin();
        } else {
            abrirTelaPrimeiroAcesso();
        }
    }

    private boolean temUsuariosCadastrados() {
        List<Usuario> usuarios = usuarioRepository.listarTodos();
        return usuarios != null && !usuarios.isEmpty();
    }

    private void abrirTelaLogin() {
        java.awt.EventQueue.invokeLater(() -> {
            LoginViewSwing view = new LoginViewSwing();
            new LoginPresenter(view);
            view.setLocationRelativeTo(null);
            view.setVisible(true);
        });
    }

    private void abrirTelaPrimeiroAcesso() {
        java.awt.EventQueue.invokeLater(() -> {
            PrimeiroAcessoViewSwing view = new PrimeiroAcessoViewSwing();
            new PrimeiroAcessoPresenter(view); // ⬅ Presenter específico para primeiro acesso
            view.setLocationRelativeTo(null);
            view.setVisible(true);
        });
    }
}
