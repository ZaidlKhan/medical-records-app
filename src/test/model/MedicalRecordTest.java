package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordTest {

    private MedicalRecord mr;

    @BeforeEach
    void runBefore(){
        mr = new MedicalRecord(2017, 4, 6, "Patient has acute illness");
    }

    @Test
    void testConstructor() {
        assertEquals(mr.getYear(), 2017);
        assertEquals(mr.getMonth(), 4);
        assertEquals(mr.getDay(), 6);
        assertEquals(mr.getDoctorNotes(), "Patient has acute illness");
        assertEquals(mr.getPrescriptions().size(), 0);
        assertEquals(mr.getSymptoms().size(), 0);
    }

    @Test
    void testAddSymptoms() {
        assertEquals(mr.getSymptoms().size(), 0);
        mr.addSymptoms("Cough");
        assertEquals(mr.getSymptoms().size(), 1);
    }

    @Test
    void testAddMultipleSymptoms() {
        assertEquals(mr.getSymptoms().size(), 0);
        mr.addSymptoms("Cough");
        mr.addSymptoms("Muscle Pain");
        mr.addSymptoms("Soar Throat");
        assertEquals(mr.getSymptoms().size(), 3);
    }

    @Test
    void testAddPrescription() {
        assertEquals(mr.getPrescriptions().size(), 0);
        mr.addPrescription(false, "Robaxin");
        mr.addPrescription(true, "Parafon Forte");
        mr.addPrescription(false, "Norflex");
        assertEquals(mr.getPrescriptions().size(), 3);
    }

    @Test
    void testSearchPrescription() {
        assertEquals(mr.getPrescriptions().size(), 0);
        mr.addPrescription(false, "Robaxin");
        mr.addPrescription(true, "Parafon Forte");
        mr.addPrescription(false, "Norflex");
        Prescription p = mr.searchPrescription("Norflex");
        assertEquals(p.getMedication(), "Norflex");
    }

    @Test
    void testSearchPrescriptionNotThere() {
        assertEquals(mr.getPrescriptions().size(), 0);
        mr.addPrescription(false, "Robaxin");
        mr.addPrescription(true, "Parafon Forte");
        mr.addPrescription(false, "Norflex");
        assertEquals(mr.searchPrescription("ibuprofen"), null);
    }
}
