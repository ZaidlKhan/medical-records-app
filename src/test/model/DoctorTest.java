package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class DoctorTest {

    private Doctor testDoctor;

    @BeforeEach
    void runBefore() {
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
        testDoctor.addPatient("Steve", 40, 68,180);
        assertEquals(testDoctor.getPatients().size(), 1);
    }

    @Test
    void testRemovePatient() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient("Rick", 60, 60, 190);
        assertEquals(testDoctor.getPatients().size(), 1);
        testDoctor.removePatient("Rick");
        assertEquals(testDoctor.getPatients().size(), 0);
    }

    @Test
    void testRemovePatientNotInList() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient("Rick", 60, 60, 190);
        assertEquals(testDoctor.getPatients().size(), 1);
        testDoctor.removePatient("Morty");
        assertEquals(testDoctor.getPatients().size(), 1);
    }

    @Test
    void testSearchPatient() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient("Walter White", 60, 73, 182);
        testDoctor.addPatient("Jesse Pinkman", 25, 65, 175);
        Patient p = testDoctor.searchPatient("Jesse Pinkman");
        assertEquals(p.getName(), "Jesse Pinkman");
        assertEquals(p.getAge(), 25);
        assertEquals(p.getWeight(), 65);
        assertEquals(p.getHeight(), 175);
        assertEquals(p.getMedicalRecord().size(), 0);
    }

    @Test
    void testSearchPatientNotInList() {
        assertEquals(testDoctor.getPatients().size(), 0);
        testDoctor.addPatient("Walter White", 60, 73, 182);
        testDoctor.addPatient("Jesse Pinkman", 25, 65, 175);
        assertEquals(testDoctor.searchPatient("Rick"), null);
    }


}
