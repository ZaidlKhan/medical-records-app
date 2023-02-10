package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient testPatient;

    @BeforeEach
    void runBefore() {
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
        testPatient.addMedicalRecord(1995, 7, 4, "Patient has been sick for a week");
        assertEquals(testPatient.getMedicalRecord().size(), 1);
    }

    @Test
    void testAddMulipleMedicalRecords() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(1995, 7, 4, "Patient has been sick for a week");
        testPatient.addMedicalRecord(2000, 9, 3, "Patient has never had this before");
        assertEquals(testPatient.getMedicalRecord().size(), 2);
    }

    @Test
    void testFilterMedicalRecordbyYear() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(1995, 7, 4, "Patient has been sick for a week");
        testPatient.addMedicalRecord(2000, 9, 3, "Patient has never had this before");
        testPatient.addMedicalRecord(2000, 12, 6, "Patient has had this before");
        ArrayList<MedicalRecord> filterList = testPatient.filterMedicalRecordsYear(2000);
        assertEquals(filterList.size(), 2);
    }

    @Test
    void testFilterMedicalRecordEmpty() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        ArrayList<MedicalRecord> filterList = testPatient.filterMedicalRecordsYear(1990);
        assertEquals(filterList.size(), 0);
    }

    @Test
    void testFilterMedicalRecordNoneFound() {
        assertEquals(testPatient.getMedicalRecord().size(), 0);
        testPatient.addMedicalRecord(1995, 7, 4, "Patient has been sick for a week");
        testPatient.addMedicalRecord(2000, 9, 3, "Patient has never had this before");
        testPatient.addMedicalRecord(2000, 12, 6, "Patient has had this before");
        ArrayList<MedicalRecord> filterList = testPatient.filterMedicalRecordsYear(2010);
        assertEquals(filterList.size(), 0);
    }


}