package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class PrescriptionTest {

    private Prescription testP;

    @BeforeEach
    void runBefore() {
        testP = new Prescription(false, "Norflex");
    }

    @Test
    void testConstructor() {
        assertFalse(testP.getStatus());
        assertEquals(testP.getMedication(), "Norflex");
    }

    @Test
    void testSendToPharmacy() {
        assertFalse(testP.getStatus());
        testP.sendToPharmacy();
        assertTrue(testP.getStatus());
    }

    @Test
    void testSendToPharmacyAlreadySent() {
        testP.sendToPharmacy();
        assertTrue(testP.getStatus());
    }

}
