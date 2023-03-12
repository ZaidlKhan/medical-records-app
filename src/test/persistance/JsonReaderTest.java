package persistance;

import java.util.*;
import model.MediRecords;
import model.MedicalRecord;
import model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonWriter;
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

    @Test
    void testAddMedicalRecordJson() {
        Patient p = new Patient("john", 1, 1, 1);
        String[] symptomsList = {"cold", "cough"};
        String[] presciptionList = {"tylonol", "ibo"};
        MedicalRecord testmr = new MedicalRecord("2023-02-13", symptomsList,
                presciptionList, "Patient is ill");
        p.addMedicalRecord(testmr);

        JSONObject json1 = new JSONObject(testmr.toJson());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("medicalRecords", jsonArray);

        assertEquals(1, p.getMedicalRecord().size());
        assertEquals(testmr, p.getMedicalRecord().get(0));
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

    public void addMedicalRecordJson(Patient p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("medicalRecords");

        for (Object json : jsonArray) {
            JSONObject nextMedicalRecord = (JSONObject) json;
            this.addMedicalRecord(p, nextMedicalRecord);
        }
    }

    public void addMedicalRecord(Patient p, JSONObject jsonObject) {
        String date = jsonObject.getString("date");
        JSONArray symptoms = jsonObject.getJSONArray("symptoms");
        String[] stringSymptoms = makeStringArray(symptoms);
        JSONArray prescription = jsonObject.getJSONArray("prescriptions");
        String[] stringPrecription = makeStringArray(prescription);
        String doctorsNote = jsonObject.getString("doctorNotes");

        MedicalRecord mr = new MedicalRecord(date, stringSymptoms,
                stringPrecription, doctorsNote);
        p.addMedicalRecord(mr);
    }

    public static String[] makeStringArray(JSONArray array) {
        String[] stringArray = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            stringArray[i] = array.getString(i);
        }
        return stringArray;
    }
}


