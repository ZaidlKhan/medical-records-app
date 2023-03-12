package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a Medical record with the currentDate, a list of symptoms,
// a list of prescriptions, and a doctorsnote
public class MedicalRecord implements Writable {

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


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date", this.date);
        json.put("prescriptions", this.prescriptions);
        json.put("symptoms", this.symptoms);
        json.put("doctorNotes", this.doctorNotes);
        return json;
    }

}
