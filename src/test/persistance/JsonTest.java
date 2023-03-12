package persistance;
import model.Doctor;
import model.Patient;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JsonTest {
    protected void checkDoctor(String name, String password,
                               int patientsSize, Doctor d) {
        assertEquals(name, d.getName());
        assertEquals(patientsSize, d.getPatients().size());
        assertEquals(password, d.getPassword());
    }
}
