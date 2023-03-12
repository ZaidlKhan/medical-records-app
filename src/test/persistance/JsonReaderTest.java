package persistance;

import java.util.*;
import model.MediRecords;
import model.MedicalRecord;
import model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;
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
        String[] actualArray  = JsonReader.makeStringArray(jsonArray);

        assertArrayEquals(expectedArray, actualArray);

    }
}

