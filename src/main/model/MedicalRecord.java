package model;

import java.util.ArrayList;

public class MedicalRecord {

    private final String date;                             // day medical record was created
    private final String[] symptoms;                       // symptoms of patient
    private final String[] prescriptions;                  // a list of medication the patient is taking
    private final String doctorNotes;                      // any additional doctors notes

    public MedicalRecord(String date, String[] symptoms, String[] prescriptions, String doctorNotes) {
        this.date = date;
        this.symptoms = symptoms;
        this.prescriptions = prescriptions;
        this.doctorNotes = doctorNotes;
    }

    public String getDate() {
        return this.date;
    }

    public String[] getPrescriptions() {
        return this.prescriptions;
    }

    public String[] getSymptoms() {
        return this.symptoms;
    }

    public String getDoctorNotes() {
        return this.doctorNotes;
    }


}
