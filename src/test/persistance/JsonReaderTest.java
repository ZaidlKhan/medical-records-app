package persistance;

import model.MediRecords;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader(".data/MediRecords.json");
        try {
            MediRecords mr = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyMediRecords() {
        JsonReader reader = new JsonReader(".data/MediRecords.json");

        try {
            MediRecords mr = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralMediRecords() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralMediRecords.json");
        try {
            MediRecords mr = reader.read();
            assertEquals(mr.getRegisteredDoctor().size(), 2);
            checkDoctor("zaid", "khan", 1, mr.getRegisteredDoctor().get(0));
            checkDoctor("Steve", "12345", 2, mr.getRegisteredDoctor().get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
