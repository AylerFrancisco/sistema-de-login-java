/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presenter;

import java.awt.Dialog;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import service.ConfiguracaoService;
import view.AdminHomeViewSwing;
import view.ConfiguracaoLogViewSwing;



public class ConfiguracaoPresenter {

    private final ConfiguracaoLogViewSwing view;
    private final ConfiguracaoService service;
    private final JDialog dialog;

    public ConfiguracaoPresenter(AdminHomeViewSwing parent) {

        this.view = new ConfiguracaoLogViewSwing();
        this.service = new ConfiguracaoService(); // ✅ INICIALIZADO

        this.dialog = new JDialog(
                parent,
                "Configuração de Log",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        configurarRadios();        // ✅
        carregarConfiguracao();    // ✅
        configurarAcoes();         // ✅

        dialog.setVisible(true);
    }

    private void configurarRadios() {
        ButtonGroup group = new ButtonGroup();
        group.add(view.getRadConfiguracaoLogCSV());
        group.add(view.getRadConfiguracaoLogJSONL());
    }

    private void carregarConfiguracao() {
        String tipo = service.obterTipoLog();

        if ("CSV".equalsIgnoreCase(tipo)) {
            view.getRadConfiguracaoLogCSV().setSelected(true);
        } else {
            view.getRadConfiguracaoLogJSONL().setSelected(true);
        }
    }

    private void configurarAcoes() {
        view.getBtnConfiguracaoLogSalvar().addActionListener(e -> {

            String tipoSelecionado;

            if (view.getRadConfiguracaoLogCSV().isSelected()) {
                tipoSelecionado = "CSV";
            } else {
                tipoSelecionado = "JSONL"; // ✅ consistente
            }

            service.salvarTipoLog(tipoSelecionado);

            JOptionPane.showMessageDialog(
                    dialog,
                    "Configuração salva com sucesso!\nNovo tipo de log: " + tipoSelecionado
            );

            dialog.dispose(); // opcional: fecha após salvar
        });
    }
}
