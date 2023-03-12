package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient testPatient;
    private MedicalRecord testmr1;
    private MedicalRecord testmr2;

    @BeforeEach
    void runBefore() {
        String[] symptoms1 = {"cough", "joint pain", "sore throat"};
        String[] prescriptions1 = {"Acenocoumarol", "Abiraterone acetate"};
        String[] symptoms2 = {"cold", "chills", "fatigue"};
        String[] prescriptions2 = {"Antihistamines"};
        testmr1 = new MedicalRecord("2017-02-13", symptoms1, prescriptions1, "Patient is otherwise healthy");
        testmr2 = new MedicalRecord("2018-09-19", symptoms2, prescriptions2, "Patient is otherwise healthy");
        testPatient = new Patient("Gregor", 45, 65, 180);
    }


    @Test
    void testConstructor() {
        assertEquals(testPatient.getName(), "Gregor");
        assertEquals(testPatient.getAge(), 45);
        assertEquals(testPatient.getWeight(), 65);
        assertEquals(testPatient.getHeight(), 180);
        assertEquals(testPatient.getMedicalRecord().size(), 0);
    }

    @Test
    void testSetName() {
        assertEquals(testPatient.getName(), "Gregor");
        testPatient.setName("Steve");
        assertEquals(testPatient.getName(), "Steve");
    }

    @Test
    void testSetAge() {
        assertEquals(testPatient.getAge(), 45);
        testPatient.setAge(50);
        assertEquals(testPatient.getAge(), 50);
    }

    @Test
    void testSetHeight() {
        assertEquals(testPatient.getHeight(), 180);
        testPatient.setHeight(190);
        assertEquals(testPatient.getHeight(), 190);
    }

    @Test
    void testSetWeight() {
        assertEquals(testPatient.getWeight(), 65);
        testPatient.setWeight(60);
        assertEquals(testPatient.getWeight(), 60);
    }

    @Test
    void testAddMedicalRecord() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(testmr1);
        assertEquals(testPatient.getMedicalRecord().size(), 1);
    }

    @Test
    void testAddMulipleMedicalRecords() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(testmr1);
        testPatient.addMedicalRecord(testmr2);
        assertEquals(testPatient.getMedicalRecord().size(), 2);
    }

    @Test
    void testSearchMedicalRecord() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(testmr1);
        testPatient.addMedicalRecord(testmr2);
        assertEquals(testPatient.getMedicalRecord().size(), 2);
        assertEquals(testPatient.searchMedicalRecord("2018-09-19"), testmr2);
    }

    @Test
    void testSearchMedicalRecordNotThere() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(testmr1);
        testPatient.addMedicalRecord(testmr2);
        assertEquals(testPatient.getMedicalRecord().size(), 2);
        assertNull(testPatient.searchMedicalRecord("2020-03-20"));
    }

    @Test
    void testToJson() {
        JSONObject json = new JSONObject();
        json.put("name", "Gregor");
        json.put("age", 45);
        json.put("weight", 65);
        json.put("height", 180);
        json.put("medicalRecords", new JSONArray());

        assertEquals(json.toString(), testPatient.toJson().toString());
    }

    @Test
    void testMedicalRecordToJson() {
        testPatient.addMedicalRecord(testmr1);

        String[] symptoms = testmr1.getSymptoms();
        String[] prescriptions = testmr1.getPrescriptions();;

        JSONObject expectedRecords = new JSONObject();
        expectedRecords.put("date", "2017-02-13");
        expectedRecords.put("symptoms", symptoms);
        expectedRecords.put("prescriptions", prescriptions);
        expectedRecords.put("doctorNotes", "Patient is otherwise healthy");

        JSONArray expectedArray = new JSONArray();
        expectedArray.put(expectedRecords);

        assertEquals(expectedArray.toString(), testPatient.medicalRecordToJson().toString());

    }



}