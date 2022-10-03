import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    private String log = "productNum, amount\n";

    public void log(int productNum, int amount) throws NullPointerException {
        log += String.format(productNum + ", " + amount + "\n");
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (FileWriter fileWriter = new FileWriter(txtFile, true)) {
            fileWriter.write(log);
        }
    }
}