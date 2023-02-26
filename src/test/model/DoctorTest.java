package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class DoctorTest {

    private Doctor testDoctor;
    private Patient testPatient1;
    private Patient testPatient2;

    @BeforeEach
    void runBefore() {
        testPatient1 = new Patient("Walter White", 60, 70, 180);
        testPatient2 = new Patient("Jesse Pinkman", 25, 55, 175);
        testDoctor = new Doctor("Gregor");
    }

    @Test
    void testConstructor() {
        assertEquals(testDoctor.getName(), "Gregor");
        assertEquals(testDoctor.getPatients().size(), 0);
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

}
