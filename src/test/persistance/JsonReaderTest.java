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
    public void testAddMedicalRecordJson() {
        Patient patient = new Patient("john", 1, 1, 1);
        JSONObject medicalRecordJson = new JSONObject();
        medicalRecordJson.put("date", "2023-03-24");
        medicalRecordJson.put("symptoms", new JSONArray(new String[]{"headache", "fever"}));
        medicalRecordJson.put("prescriptions", new JSONArray(new String[]{"Ibuprofen", "Paracetamol"}));
        medicalRecordJson.put("doctorNotes", "Patient needs rest and medication");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("medicalRecords", new JSONArray(new JSONObject[]{medicalRecordJson}));

        JsonReader.addMedicalRecordJson(patient, jsonObject);

        assertEquals(1, patient.getMedicalRecord().size());
        MedicalRecord addedMedicalRecord = patient.getMedicalRecord().get(0);
        assertEquals("2023-03-24", addedMedicalRecord.getDate());
        assertArrayEquals(new String[]{"headache", "fever"}, addedMedicalRecord.getSymptoms());
        assertArrayEquals(new String[]{"Ibuprofen", "Paracetamol"}, addedMedicalRecord.getPrescriptions());
        assertEquals("Patient needs rest and medication", addedMedicalRecord.getDoctorNotes());
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


