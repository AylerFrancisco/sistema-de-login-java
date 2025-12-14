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
        this.repository = repository;

        
        this.view = new UsuariosPendentesViewSwing();

        this.view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

     
        carregarTabela();
        configurarAcoes();

   
        this.view.setLocationRelativeTo(parent);

    
        this.view.pack();
        this.view.setVisible(true);
    }

    private void carregarTabela() {
        List<Usuario> usuarios = repository.listarTodos();

        DefaultTableModel model = (DefaultTableModel) view.getjTable1().getModel();
        model.setRowCount(0);

        for (Usuario u : usuarios) {
            String status = u.getAutorizado() == 1 ? "Autorizado" : "Aguardando Autorização";
            model.addRow(new Object[]{u.getUsuario(), status, u.getId()});
        }

    
        view.getjTable1().getColumnModel().getColumn(2).setMinWidth(0);
        view.getjTable1().getColumnModel().getColumn(2).setMaxWidth(0);
        view.getjTable1().getColumnModel().getColumn(2).setWidth(0);

    }

    private int getUsuarioSelecionadoId() {
        int row = view.getjTable1().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário!");
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

            JOptionPane.showMessageDialog(
                    view,
                    "Usuário autorizado!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            carregarTabela();
        });

        view.getBtnExcluir().addActionListener(e -> {
            int id = getUsuarioSelecionadoId();
            if (id == -1) {
                return;
            }

            repository.excluir(id);

            JOptionPane.showMessageDialog(view, "Usuário excluído!");
            carregarTabela();
        });
    }
}
