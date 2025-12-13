/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Notificacao;
import repository.IEnviarNotificacaoRepository;
import repository.UsuarioRepositorySQLite;
import service.NotificacaoService;
import view.NotificacoesViewSwing;

/**
 *
 * @author lukian.borges
 */
public class NotificacaoEmAbertoPresenter {
    
  private final NotificacoesViewSwing view;
    private final NotificacaoService service;
    private final UsuarioRepositorySQLite usuarioRepo;
    private final Integer idUsuarioLogado;

    public NotificacaoEmAbertoPresenter(JFrame parent, Integer idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        this.service = new NotificacaoService();
        this.usuarioRepo = UsuarioRepositorySQLite.getInstance();

        view = new NotificacoesViewSwing();
        view.setLocationRelativeTo(parent);

        carregarTabela();
        configurarAcoes();

        view.setVisible(true);
    }

    private void carregarTabela() {
        List<Notificacao> lista = service.listarNaoLidas(idUsuarioLogado);

        DefaultTableModel model = (DefaultTableModel) view.getjTable1().getModel();
        model.setRowCount(0);

        for (Notificacao n : lista) {
            String remetente = usuarioRepo.buscarPorId(n.getIdRemetente()).getUsuario();
            model.addRow(new Object[]{
                false,
                remetente,
                n.getMensagem(),
                n.getDataEnvio(),
                n.getId()
            });
        }
    }

    private void configurarAcoes() {
        view.getBtnMarcaLida().addActionListener(e -> marcarComoLida());
    }

    private void marcarComoLida() {
        DefaultTableModel model = (DefaultTableModel) view.getjTable1().getModel();
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean marcado = (Boolean) model.getValueAt(i, 0);
            if (marcado != null && marcado) {
                ids.add((Integer) model.getValueAt(i, 4));
            }
        }

        if (ids.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Selecione ao menos uma notificação.");
            return;
        }

        service.marcarComoLidas(ids);

        carregarTabela();
    }
}
