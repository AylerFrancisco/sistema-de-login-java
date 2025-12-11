/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adapter;

import br.ufes.log.CSVLogFormatter;
import br.ufes.log.FileLogWriter;
import br.ufes.log.JSONLogFormatter;
import br.ufes.log.LogFormatter;
import br.ufes.log.LogWriter;
import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author lukian.borges
 */
public class LogAdapterImpl implements LogAdapter {
    
     private final LogFormatter formatter;
    private final LogWriter writer;
    private final String logType = Dotenv.configure().load().get("LOG_FILE_TYPE");
    private final String logPath = Dotenv.configure().load().get("LOG_FILE_PATH");

    LogAdapterImpl(Builder builder) {
        if (logType.equalsIgnoreCase("csv")) {
            this.formatter = new CSVLogFormatter();
        } else if (logType.equalsIgnoreCase("json")) {
            this.formatter = new JSONLogFormatter();
        } else {
            throw new IllegalArgumentException("Tipo de log inválido.");
        }
        this.writer = new FileLogWriter(logPath + "." + logType);
    }

    @Override
    public void log(String operation, String name, String user) {
        String formattedMessage = formatter.format(operation, name, user);
        writer.write(formattedMessage);
    }

    public static class Builder {
        public LogAdapterImpl build() {
            return new LogAdapterImpl(this);
        }
    }
    
    
}
