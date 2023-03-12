package persistance;

import model.Doctor;
import model.MediRecords;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.print.Doc;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
        MediRecords mr = new MediRecords();
        JsonWriter writer = new JsonWriter("\"./data/my\\0illegal:fileName.json\"");
        writer.open();
        fail("IOException was expected");
    } catch (IOException e) {
        // pass
        }
    }

    @Test
    void testWriterEmptyMediRecords() {
        try {
            MediRecords mr = new MediRecords();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMediRecords.json");
            writer.open();
            writer.write(mr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMediRecords.json");
            mr = reader.read();
            assertEquals(mr.getRegisteredDoctor().size(), 0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralMediRecords() {
        try {
            MediRecords mr = new MediRecords();
            mr.addDoctor(new Doctor("john", "cena"));
            mr.addDoctor(new Doctor("ryan", "lau"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMediRecords.json");
            writer.open();
            writer.write(mr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMediRecords.json");
            mr = reader.read();
            assertEquals(mr.getRegisteredDoctor().size(), 2);
            List<Doctor> doctors = mr.getRegisteredDoctor();
            checkDoctor("ryan", "lau", 0, doctors.get(1));
            checkDoctor("john", "cena", 0, doctors.get(0));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
