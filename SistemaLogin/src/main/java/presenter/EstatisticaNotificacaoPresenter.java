package presenter;

import dto.EstatisticaRemetenteDTO;
import dto.EstatisticaUsuarioDTO;
import java.awt.Dialog;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import service.EstatisticaNotificacaoService;
import view.EstatisticasNotificacaoViewSwing;


/**
 *
 * @author luluzinho da pomerana
 */

public class EstatisticaNotificacaoPresenter {

    private final JDialog dialog;
    private final EstatisticasNotificacaoViewSwing view;
    private final EstatisticaNotificacaoService service;

    public EstatisticaNotificacaoPresenter(JFrame parent) {

        this.service = new EstatisticaNotificacaoService();
        this.view = new EstatisticasNotificacaoViewSwing();

        this.dialog = new JDialog(parent, "Estatísticas de Notificações", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        carregarDados();

        dialog.setVisible(true);
    }

    private void carregarDados() {

        // TOTAIS
        view.getTxtTotal().setText(String.valueOf(service.totalNotificacoes()));
        view.getTxtLidas().setText(String.valueOf(service.totalLidas()));
        view.getTxtNaoLidas().setText(String.valueOf(service.totalNaoLidas()));

        // TABELA
        configurarTabela(service.estatisticaPorUsuario());
        
         // TABELA POR REMETENTE
    configurarTabelaRemetente(service.estatisticaPorRemetente());
    }
    
    private void configurarTabelaRemetente(List<EstatisticaRemetenteDTO> dados) {

    DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Remetente", "Total Enviadas"}, 0
    );

    for (EstatisticaRemetenteDTO dto : dados) {
        model.addRow(new Object[]{
                dto.getNomeRemetente(),
                dto.getTotalEnviadas()
        });
    }

    view.getTblTabelaQtdRemet().setModel(model);
}


    private void configurarTabela(List<EstatisticaUsuarioDTO> dados) {

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Destinatário", "Total", "Lidas", "Não Lidas"}, 0
        );

        for (EstatisticaUsuarioDTO dto : dados) {
            model.addRow(new Object[]{
                    dto.getNomeUsuario(),
                    dto.getTotal(),
                    dto.getLidas(),
                    dto.getNaoLidas()
            });
        }

        view.getTblTabelaQtdNot().setModel(model);
    }
}
