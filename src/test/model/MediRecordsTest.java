package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MediRecordsTest {

    private MediRecords testMedirecords;

    @BeforeEach
    void runBefore() {
        testMedirecords = new MediRecords();
    }

    @Test
    void testConstructor() {
        assertEquals(testMedirecords.getRegisteredDoctor().size(),0);
    }

    @Test
    void testAddDoctor() {
        Doctor d = new Doctor("john", "123");
        assertEquals(testMedirecords.getRegisteredDoctor().size(),0);
        testMedirecords.addDoctor(d);
        assertEquals(testMedirecords.getRegisteredDoctor().size(), 1);
    }

    @Test
    void testSearchDoctorIsThere() {
        Doctor d1 = new Doctor("john", "123");
        Doctor d2 = new Doctor("rammus", "123");
        assertEquals(testMedirecords.getRegisteredDoctor().size(),0);
        testMedirecords.addDoctor(d1);
        testMedirecords.addDoctor(d2);
        assertEquals(testMedirecords.getRegisteredDoctor().size(), 2);
        assertEquals(testMedirecords.searchDoctor("john"), d1);
    }

    @Test
    void testSearchDoctorIsNotThere() {
        Doctor d1 = new Doctor("john", "123");
        Doctor d2 = new Doctor("rammus", "123");
        assertEquals(testMedirecords.getRegisteredDoctor().size(),0);
        testMedirecords.addDoctor(d1);
        testMedirecords.addDoctor(d2);
        assertEquals(testMedirecords.getRegisteredDoctor().size(), 2);
        assertNull(testMedirecords.searchDoctor("joe"));
    }

    @Test
    void testSetLoading() {
        assertFalse(testMedirecords.getLoadingModel());
        testMedirecords.setLoadingModel(true);
        assertTrue(testMedirecords.getLoadingModel());
    }


}
