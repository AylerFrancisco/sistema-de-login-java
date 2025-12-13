package presenter;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import model.Notificacao;
import repository.UsuarioRepositorySQLite;
import service.NotificacaoLidaService;
import view.NotificacoesLidasViewSwing;

public class NotificacoesLidasPresenter {

    private final NotificacoesLidasViewSwing view;
    private final NotificacaoLidaService service;
    private final UsuarioRepositorySQLite usuarioRepo;
    private final Integer idUsuarioLogado;
    private final JFrame frame;

    public NotificacoesLidasPresenter(JFrame parent, Integer idUsuarioLogado) {

        this.idUsuarioLogado = idUsuarioLogado;
        this.service = new NotificacaoLidaService();
        this.usuarioRepo = UsuarioRepositorySQLite.getInstance();

        view = new NotificacoesLidasViewSwing();

        // Cria um frame para hospedar o painel
        frame = new JFrame("Notificações Lidas");
        frame.setContentPane(view);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        carregarTabela();

        frame.setVisible(true);
    }

    private void carregarTabela() {

        List<Notificacao> lista = service.listarLidas(idUsuarioLogado);

        DefaultTableModel model = (DefaultTableModel) view.getTabela().getModel();
        model.setRowCount(0);

        for (Notificacao n : lista) {

            String remetente = usuarioRepo.buscarPorId(n.getIdRemetente()).getUsuario();

            model.addRow(new Object[]{
                    remetente,
                    n.getMensagem(),
                    n.getDataEnvio()
            });
        }
    }
}
