package presenter;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Usuario;
import repository.UsuarioRepositorySQLite;
import view.UsuariosPendentesViewSwing;

public class ControleAcessoPresente {

    private UsuariosPendentesViewSwing view;
    private final UsuarioRepositorySQLite repository;
    private JFrame parent;

    public ControleAcessoPresente(UsuarioRepositorySQLite repository, JFrame parent) {
        this.parent = parent;
        this.view = new UsuariosPendentesViewSwing();
        this.repository = repository;

        
        this.view = new UsuariosPendentesViewSwing();
        
 
        this.view.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        
        this.view.setAlwaysOnTop(false);
        this.view.toFront();
        
        carregarTabela();
        configurarAcoes();


        this.view.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        this.view.setLocationRelativeTo(parent);


        this.view.pack();
        this.view.setAlwaysOnTop(true);  
        this.view.setVisible(true);


        this.view.toFront();
        this.view.requestFocus();
    }

    private void carregarTabela() {
        List<Usuario> usuarios = repository.listarTodos();

        DefaultTableModel model = (DefaultTableModel) view.getjTable1().getModel();
        model.setRowCount(0);

        for (Usuario u : usuarios) {
            String status = u.getAutorizado() == 1 ? "Autorizado" : "Aguardando Autorização";
            model.addRow(new Object[]{u.getUsuario(), status, u.getId()});
        }

        // Esconder coluna ID (3ª)
        view.getjTable1().getColumnModel().getColumn(2).setMinWidth(0);
        view.getjTable1().getColumnModel().getColumn(2).setMaxWidth(0);
        view.getjTable1().getColumnModel().getColumn(2).setWidth(0);

    }

    private int getUsuarioSelecionadoId() {
        int row = view.getjTable1().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário!");
            return -1;
        }

        return (int) view.getjTable1().getValueAt(row, 2);
    }

    private void configurarAcoes() {

        view.getBtnAutorizar().addActionListener(e -> {
            int id = getUsuarioSelecionadoId();
            if (id == -1) {
                return;
            }

            repository.autorizar(id);  // precisa criar esse método abaixo

            JOptionPane.showMessageDialog(null, "Usuário autorizado!");
            carregarTabela();
        });

        view.getBtnExcluir().addActionListener(e -> {
            int id = getUsuarioSelecionadoId();
            if (id == -1) {
                return;
            }

            repository.excluir(id);

            JOptionPane.showMessageDialog(null, "Usuário excluído!");
            carregarTabela();
        });
    }
}
