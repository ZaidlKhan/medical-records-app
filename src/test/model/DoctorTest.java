package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DoctorTest {

    private Doctor testDoctor;
    private Patient testPatient1;
    private Patient testPatient2;

    @BeforeEach
    void runBefore() {
        testPatient1 = new Patient("Walter White", 60, 70, 180);
        testPatient2 = new Patient("Jesse Pinkman", 25, 55, 175);
        testDoctor = new Doctor("Gregor", "CPSC110");
    }

    @Test
    void testConstructor() {
        assertEquals(testDoctor.getName(), "Gregor");
        assertEquals(testDoctor.getPatients().size(), 0);
        assertEquals(testDoctor.getPassword(), "CPSC110");
    }

    @Test
    void testSetPasswordTrue() {
        testDoctor.setPassword("CPSC");
        assertTrue(testDoctor.checkPassword("CPSC"));
    }

    @Test
    void testSetPasswordFalse() {
        testDoctor.setPassword("CPSC");
        assertFalse(testDoctor.checkPassword("NotCPSC"));
    }
    @Test
    void testAddPatient() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient(testPatient1);
        assertEquals(testDoctor.getPatients().size(), 1);
    }

    @Test
    void testAddSeveralPatient() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient(testPatient1);
        testDoctor.addPatient(testPatient2);
        assertEquals(testDoctor.getPatients().size(), 2);
    }

    @Test
    void testRemovePatient() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient(testPatient1);
        assertEquals(testDoctor.getPatients().size(), 1);
        testDoctor.removePatient("Walter White");
        assertEquals(testDoctor.getPatients().size(), 0);
    }

    @Test
    void testRemovePatientNotInList() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient(testPatient1);
        assertEquals(testDoctor.getPatients().size(), 1);
        testDoctor.removePatient("Rick");
        assertEquals(testDoctor.getPatients().size(), 1);
    }

    @Test
    void testSearchPatient() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient(testPatient1);
        testDoctor.addPatient(testPatient2);
        Patient p = testDoctor.searchPatient("Jesse Pinkman");
        assertEquals(p.getName(), "Jesse Pinkman");
        assertEquals(p.getAge(), 25);
        assertEquals(p.getWeight(), 55);
        assertEquals(p.getHeight(), 175);
        assertEquals(p.getMedicalRecord().size(), 0);
    }

    @Test
    void testSearchPatientNotInList() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient(testPatient1);
        testDoctor.addPatient(testPatient2);
        assertNull(testDoctor.searchPatient("Rick"));
    }

    @Test
    void testPatientsToJson() {
        testDoctor.addPatient(testPatient1);
        assertEquals(testDoctor.getPatients().size(), 1);

        ArrayList<MedicalRecord> medicalRecordList = testPatient1.getMedicalRecord();

        JSONObject expectedPatient = new JSONObject();
        expectedPatient.put("name" , "Walter White");
        expectedPatient.put("age", 60);
        expectedPatient.put("weight", 70);
        expectedPatient.put("height", 180);
        expectedPatient.put("medicalRecords", medicalRecordList);

        JSONArray expectedArray = new JSONArray();
        expectedArray.put(expectedPatient);

        assertEquals(expectedArray.toString(), testDoctor.patientsToJson().toString());

    }

}
