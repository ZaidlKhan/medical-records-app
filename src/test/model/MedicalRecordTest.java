package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordTest {

    private MedicalRecord mr;

    @BeforeEach
    void runBefore(){
        String[] symptoms = {"cough", "joint pain", "sore throat"};
        String[] prescriptions = {"Acenocoumarol", "Abiraterone acetate"};
        mr = new MedicalRecord("2017-02-13", symptoms, prescriptions, "Patient has acute illness");
    }

    @Test
    void testConstructor() {
        assertEquals(mr.getDate(),"2017-02-13");
        assertEquals(mr.getDoctorNotes(), "Patient has acute illness");
        assertEquals(mr.getPrescriptions().length, 2);
        assertEquals(mr.getSymptoms().length, 3);
    }

    @Test
    void medicalRecordToJson() {
        JSONObject expectedMedicalRecord = new JSONObject();
        String[] symptomList = mr.getSymptoms();
        String[] prescriptionList = mr.getPrescriptions();
        expectedMedicalRecord.put("date", "2017-02-13");
        expectedMedicalRecord.put("symptoms", symptomList);
        expectedMedicalRecord.put("doctorNotes", "Patient has acute illness");
        expectedMedicalRecord.put("prescriptions", prescriptionList);

        JSONObject actualMr = mr.toJson();
        assertEquals(expectedMedicalRecord.toString(), actualMr.toString());
    }

}
