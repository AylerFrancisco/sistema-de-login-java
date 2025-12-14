/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adapter;

import adapter.LogAdapter;
import br.ufes.log.CSVLogFormatter;
import br.ufes.log.FileLogWriter;
import br.ufes.log.JSONLogFormatter;
import br.ufes.log.LogFormatter;
import br.ufes.log.LogWriter;
import io.github.cdimascio.dotenv.Dotenv;
import service.ConfiguracaoService;

public class LogAdapterImpl implements LogAdapter {

    private final String logPath =
            Dotenv.configure().load().get("LOG_FILE_PATH");

    private final ConfiguracaoService configuracaoService =
            new ConfiguracaoService();
    
      public LogAdapterImpl() {
      
    }

    @Override
    public void log(String operation, String name, String user) {

        String tipoLog = configuracaoService.obterTipoLog(); // 🔥 DO BANCO

        LogFormatter formatter;
        String extensao;

        if ("CSV".equalsIgnoreCase(tipoLog)) {
            formatter = new CSVLogFormatter();
            extensao = "csv";
        } else if ("JSONL".equalsIgnoreCase(tipoLog)) {
            formatter = new JSONLogFormatter();
            extensao = "jsonl";
        } else {
            throw new IllegalArgumentException("Tipo de log inválido: " + tipoLog);
        }

        LogWriter writer =
                new FileLogWriter(logPath + "." + extensao);

        String formatted = formatter.format(operation, name, user);
        writer.write(formatted);
    }
}
