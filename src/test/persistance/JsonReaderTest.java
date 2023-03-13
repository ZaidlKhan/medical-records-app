package persistance;

import model.MediRecords;
import model.MedicalRecord;
import model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.File;
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

    @Test
    void testAddMedicalRecordJson() {
        Patient p = new Patient("john", 1, 1, 1);
        String[] symptomsList1 = {"cold", "cough"};
        String[] presciptionList1 = {"tylonol", "ibo"};
        MedicalRecord testmr1 = new MedicalRecord("2023-02-13", symptomsList1,
                presciptionList1, "Patient is ill");
        String[] symptomlist2 = {"dizzy"};
        String[] presciptionList2 = {"tylonol", "ibo"};
        MedicalRecord testmr2 = new MedicalRecord("2020-04-18", symptomlist2,
                presciptionList2, "not feeling well");
        p.addMedicalRecord(testmr2);
        p.addMedicalRecord(testmr1);

        JSONObject json1 = new JSONObject(testmr1.toJson());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("medicalRecords", jsonArray);

        assertEquals(2, p.getMedicalRecord().size());
        assertEquals(testmr1, p.getMedicalRecord().get(1));
        assertEquals(testmr2, p.getMedicalRecord().get(0));
    }

    @Test
    void testMakeStringArray() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("day");
        jsonArray.put("night");
        jsonArray.put("afternoon");

        String[] expectedArray = {"day", "night", "afternoon"};
        String[] actualArray = JsonReader.makeStringArray(jsonArray);

        assertArrayEquals(expectedArray, actualArray);

    }

    @Test
    void testReadFile() throws IOException {
        try {
            String source = "./data/testReaderEmptyMediRecords.json";
            JSONObject expected = new JSONObject("{\"registeredDoctors\":[]}");
            JsonReader jsonReader = new JsonReader(source);
            String actualContent = jsonReader.readFile(source);
            assertEquals(expected.toString(), actualContent);
        } catch (IOException e) {
            System.out.println("caught IO Expcetion");
        }
    }

    @Test
    void testReadFileHandlesException() throws IOException {
        JsonReader reader = new JsonReader(".data/testReaderEmptyMediRecords.json");
        File tempFile = File.createTempFile("testTempFile", ".json");

        tempFile.delete();

        try {
            String result = reader.readFile(tempFile.getPath());
            fail("Throw an IOException to be thrown");
        } catch (IOException e) {
            // pass
        }
    }

}


