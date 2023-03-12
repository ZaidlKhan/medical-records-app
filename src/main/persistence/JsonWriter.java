package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import model.MediRecords;
import org.json.JSONObject;

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    public JsonWriter(String destination) {
        this.destination = destination;
    }

    public void open() throws FileNotFoundException {
        this.writer = new PrintWriter(new File(this.destination));
    }

    public void write(MediRecords mr) {
        JSONObject json = mr.toJson();
        this.saveToFile(json.toString(4));
    }

    public void close() {
        this.writer.close();
    }

    private void saveToFile(String json) {
        this.writer.print(json);
    }


}
