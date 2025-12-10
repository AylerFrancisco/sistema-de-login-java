package presenter;

import adapter.PasswordValidator;
import adapter.PasswordValidatorAdapter;
import exception.UsuarioJaCadastradoException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Usuario;
import repository.IUsuarioRepository;
import repository.UsuarioRepositorySQLite;
import service.CadastroService;
import state.CadastroState;
import view.UsuarioCadastroHomeViewSwing;

public class UsuarioCadastroHomePresenter {

    private final UsuarioCadastroHomeViewSwing view;
    private UsuarioRepositorySQLite repository;
    private final CadastroService cadastroService;
    private PasswordValidator passwordValidator;
    private Usuario usuarioSelecionado = null;
    private CadastroState atualEstado;
    private JFrame parent;

    public UsuarioCadastroHomePresenter(UsuarioRepositorySQLite repository,
            CadastroService cadastroService, JFrame parent) {
      
        this.parent = parent;
        
        this.repository = repository != null ? repository : UsuarioRepositorySQLite.getInstance();
        this.cadastroService = cadastroService;
        this.passwordValidator = new PasswordValidatorAdapter();

        this.view = new UsuarioCadastroHomeViewSwing();
        
 
        this.view.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        
        this.view.setAlwaysOnTop(false);
        this.view.toFront();

        inicializarView();
        configurarAcoes();
        carregarUsuarios();
        
    

this.view.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

// posicione RELATIVE to parent e NÃO sobrescreva depois
this.view.setLocationRelativeTo(parent);

// Force na frente APÓS pack e APÓS setVisible
this.view.pack();
this.view.setAlwaysOnTop(true);   // tenta garantir que fique acima
this.view.setVisible(true);

// garantir foco
this.view.toFront();
this.view.requestFocus();
    }

    private void inicializarView() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Usuário", "Tipo", "Autorizado", "Data Cadastro"}
        );

        view.getTblListaUsuario().setModel(model);

        view.getTblListaUsuario().getColumnModel().getColumn(0).setMinWidth(0);
        view.getTblListaUsuario().getColumnModel().getColumn(0).setMaxWidth(0);

        view.getTblListaUsuario().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        view.getTblListaUsuario().getColumnModel().getColumn(0).setMinWidth(0);
        view.getTblListaUsuario().getColumnModel().getColumn(0).setMaxWidth(0);

        setFormularioEnabled(false);

        view.getBtnEditar().setEnabled(false);
        view.getBtnExcluir().setEnabled(false);
    }

    private void configurarAcoes() {

        view.getTblListaUsuario().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }

            int viewRow = view.getTblListaUsuario().getSelectedRow();
            if (viewRow == -1) {
                usuarioSelecionado = null;
                view.getBtnEditar().setEnabled(false);
                view.getBtnExcluir().setEnabled(false);
                return;
            }

            int modelRow = view.getTblListaUsuario().convertRowIndexToModel(viewRow);
            Object idObj = view.getTblListaUsuario().getModel().getValueAt(modelRow, 0);

            if (idObj == null) {
                usuarioSelecionado = null;
                view.getBtnEditar().setEnabled(false);
                view.getBtnExcluir().setEnabled(false);
                return;
            }

            int id = Integer.parseInt(idObj.toString());
            usuarioSelecionado = repository.buscarPorId(id);

            view.getBtnEditar().setEnabled(true);
            view.getBtnExcluir().setEnabled(true);
        });

        // NOVO
        view.getBtnNovo().addActionListener(e -> {
            usuarioSelecionado = null;
            limparFormulario();
            setFormularioEnabled(true);
            view.getTxtUsuario().requestFocus();
        });

        view.getBtnEditar().addActionListener(e -> {
            if (usuarioSelecionado == null) {
                JOptionPane.showMessageDialog(view, "Selecione um usuário para editar.");
                return;
            }

            setFormularioEnabled(true);
            preencherFormulario(usuarioSelecionado);

            view.getPsswSenha().setText("");
            view.getPsswConfirma().setText("");
        });

        // REMOVER
        view.getBtnExcluir().addActionListener(e -> {
            int linhaView = view.getTblListaUsuario().getSelectedRow();
            if (linhaView == -1) {
                JOptionPane.showMessageDialog(view, "Selecione um usuário para remover.");
                return;
            }

            int linhaModel = view.getTblListaUsuario().convertRowIndexToModel(linhaView);
            Object idObj = view.getTblListaUsuario().getModel().getValueAt(linhaModel, 0);
            if (idObj == null) {
                JOptionPane.showMessageDialog(view, "Não foi possível obter o ID do usuário selecionado.");
                return;
            }

            int id = Integer.parseInt(idObj.toString());
            Usuario toRemove = repository.buscarPorId(id);
            if (toRemove == null) {
                JOptionPane.showMessageDialog(view, "Usuário não encontrado no repositório.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view,
                    "Tem certeza que deseja remover este usuário?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = repository.remover(id);
                if (!ok) {
                    JOptionPane.showMessageDialog(view, "Erro ao remover usuário.");
                    return;
                }
                carregarUsuarios();
                limparFormulario();
                usuarioSelecionado = null;
                setFormularioEnabled(false);
            }
        });

        view.getBtnSalvar().addActionListener(e -> {

            System.out.println("DEBUG_SALVAR -> usuarioSelecionado = " + usuarioSelecionado);

            String usuario = view.getTxtUsuario().getText().trim();
            String senha = new String(view.getPsswSenha().getPassword());
            String confirmarSenha = new String(view.getPsswConfirma().getPassword());
            String tipoCadastro = view.getRadAdmin().isSelected() ? "ADMIN" : "USER";
            Integer autorizado = view.getRadSimAut().isSelected() ? 1 : 0;
            String dataCadastro = java.time.LocalDate.now().toString();

            if (usuario.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Preencha o usuário.");
                return;
            }

            if (usuarioSelecionado == null) {

                try {

                    if (!senha.equals(confirmarSenha)) {
                        JOptionPane.showMessageDialog(view, "A senha e a confirmação não coincidem.");
                        return;
                    }

                    boolean criado = cadastroService.cadastrarUsuario(
                            usuario, senha, confirmarSenha,
                            tipoCadastro, autorizado, dataCadastro,
                            passwordValidator
                    );

                    if (criado) {
                        JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso!");
                        carregarUsuarios();
                        limparFormulario();
                        setFormularioEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(view, "Erro ao cadastrar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (UsuarioJaCadastradoException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Usuário já existe", JOptionPane.ERROR_MESSAGE);

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Senha inválida", JOptionPane.ERROR_MESSAGE);
                }

                return;
            }

            try {

                if (usuarioSelecionado == null) {
                    JOptionPane.showMessageDialog(view, "Erro interno: nenhum usuário para editar.");
                    return;
                }

                System.out.println("DEBUG -> editando id=" + usuarioSelecionado.getId());

                // ======== VALIDAR SENHA ========
                String senhaFinal;

                if (senha.isBlank() && confirmarSenha.isBlank()) {
                    // manter senha atual
                    senhaFinal = usuarioSelecionado.getSenha();
                } else {
                    // precisa validar igual
                    if (!senha.equals(confirmarSenha)) {
                        JOptionPane.showMessageDialog(view,
                                "A senha e a confirmação não coincidem.");
                        return;
                    }

                    // validar regras da senha
                    try {
                        passwordValidator.validarSenha(senha);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(view,
                                ex.getMessage(),
                                "Senha inválida",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    senhaFinal = senha;
                }

                usuarioSelecionado.setUsuario(usuario);
                usuarioSelecionado.setSenha(senhaFinal);
                usuarioSelecionado.setTipoCadastro(tipoCadastro);
                usuarioSelecionado.setAutorizado(autorizado);

                if (usuarioSelecionado.getDataCadastro() == null
                        || usuarioSelecionado.getDataCadastro().isBlank()) {
                    usuarioSelecionado.setDataCadastro(dataCadastro);
                }

                System.out.println("DEBUG -> antes update: id=" + usuarioSelecionado.getId()
                        + " usuario=" + usuarioSelecionado.getUsuario()
                        + " tipo=" + usuarioSelecionado.getTipoCadastro()
                        + " aut=" + usuarioSelecionado.getAutorizado()
                        + " senhaEmpty?=" + (senhaFinal == null || senhaFinal.isEmpty()));

                boolean atualizado = repository.atualizar(usuarioSelecionado);

                if (!atualizado) {
                    JOptionPane.showMessageDialog(view,
                            "Nenhuma linha foi atualizada (ID não encontrado).",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(view, "Usuário atualizado com sucesso!");

                carregarUsuarios();
                limparFormulario();
                setFormularioEnabled(false);
                usuarioSelecionado = null;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Erro ao atualizar usuário: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        });

    }

    private void carregarUsuarioSelecionado() {
        int linhaView = view.getTblListaUsuario().getSelectedRow();
        if (linhaView == -1) {
            return;
        }

        int linhaModel = view.getTblListaUsuario().convertRowIndexToModel(linhaView);

        Object idObj = view.getTblListaUsuario().getModel().getValueAt(linhaModel, 0);
        int id = Integer.parseInt(idObj.toString());

        usuarioSelecionado = repository.buscarPorId(id);
    }

    private void salvarUsuario() {
        String usuario = view.getTxtUsuario().getText().trim();
        String senha = new String(view.getPsswSenha().getPassword());
        String confirmarSenha = new String(view.getPsswConfirma().getPassword());
        String tipoCadastro = view.getRadAdmin().isSelected() ? "ADMIN" : "USER";
        Integer autorizado = view.getRadSimAut().isSelected() ? 1 : 0;
        String dataCadastro = java.time.LocalDate.now().toString();

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Preencha o usuário.");
            return;
        }

        // NOVO USUÁRIO
        if (usuarioSelecionado == null) {
            try {
                if (!senha.equals(confirmarSenha)) {
                    JOptionPane.showMessageDialog(view, "Senha e confirmação não coincidem.");
                    return;
                }

                boolean criado = cadastroService.cadastrarUsuario(
                        usuario, senha, confirmarSenha,
                        tipoCadastro, autorizado, dataCadastro,
                        passwordValidator
                );

                if (criado) {
                    JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso!");
                    carregarUsuarios();
                    limparFormulario();
                    setFormularioEnabled(false);
                }

            } catch (UsuarioJaCadastradoException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage(), "Senha inválida", JOptionPane.ERROR_MESSAGE);
            }

            return;
        }

        try {
            String senhaFinal;

            if (!senha.isBlank()) {
                if (!senha.equals(confirmarSenha)) {
                    JOptionPane.showMessageDialog(view, "Senha e confirmação não coincidem.");
                    return;
                }

                try {
                    passwordValidator.validarSenha(senha);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Senha inválida", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                senhaFinal = senha;
            } else {
                senhaFinal = usuarioSelecionado.getSenha();
            }

            usuarioSelecionado.setUsuario(usuario);
            usuarioSelecionado.setSenha(senhaFinal);
            usuarioSelecionado.setTipoCadastro(tipoCadastro);
            usuarioSelecionado.setAutorizado(autorizado);
            usuarioSelecionado.setDataCadastro(usuarioSelecionado.getDataCadastro());

            repository.atualizar(usuarioSelecionado);

            JOptionPane.showMessageDialog(view, "Usuário atualizado com sucesso!");
            carregarUsuarios();
            limparFormulario();
            setFormularioEnabled(false);
            usuarioSelecionado = null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao atualizar usuário: " + ex.getMessage());
        }
    }

    private void preencherFormulario(Usuario u) {
        if (u == null) {
            return;
        }

        view.getTxtUsuario().setText(u.getUsuario());

        view.getPsswSenha().setText("");
        view.getPsswConfirma().setText("");

        if ("ADMIN".equalsIgnoreCase(u.getTipoCadastro())) {
            view.getRadAdmin().setSelected(true);
            view.getRadUsuario().setSelected(false);
        } else {
            view.getRadUsuario().setSelected(true);
            view.getRadAdmin().setSelected(false);
        }

        if (u.getAutorizado() != null && u.getAutorizado() == 1) {
            view.getRadSimAut().setSelected(true);
            view.getRadNaoAut().setSelected(false);
        } else {
            view.getRadNaoAut().setSelected(true);
            view.getRadSimAut().setSelected(false);
        }
    }

    private void limparFormulario() {
        view.getTxtUsuario().setText("");
        view.getPsswSenha().setText("");
        view.getPsswConfirma().setText("");
        view.getRadUsuario().setSelected(true);
        view.getRadNaoAut().setSelected(true);
    }

    private void setFormularioEnabled(boolean enabled) {
        view.getTxtUsuario().setEnabled(enabled);
        view.getPsswSenha().setEnabled(enabled);
        view.getPsswConfirma().setEnabled(enabled);
        view.getRadAdmin().setEnabled(enabled);
        view.getRadUsuario().setEnabled(enabled);
        view.getRadSimAut().setEnabled(enabled);
        view.getRadNaoAut().setEnabled(enabled);
        view.getBtnSalvar().setEnabled(enabled);
    }

    private void carregarUsuarios() {
        List<Usuario> lista = repository.listarTodos();
        DefaultTableModel model = (DefaultTableModel) view.getTblListaUsuario().getModel();
        model.setRowCount(0);

        for (Usuario u : lista) {
            model.addRow(new Object[]{
                u.getId(),
                u.getUsuario(),
                u.getTipoCadastro(),
                u.getAutorizado() == 1 ? "Sim" : "Não",
                u.getDataCadastro()
            });
        }
    }

    public void changeState(CadastroState state) {
        this.atualEstado = state;
    }

    public CadastroState getAtualEstado() {
        return atualEstado;
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public PasswordValidator getPasswordValidator() {
        return passwordValidator;
    }

    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    public void setAtualEstado(CadastroState atualEstado) {
        this.atualEstado = atualEstado;
    }

    public IUsuarioRepository getUsuarioRepository() {
        return repository;
    }

    public UsuarioRepositorySQLite getRepository() {
        return repository;
    }

    public void setRepository(UsuarioRepositorySQLite repository) {
        this.repository = repository;
    }

}
