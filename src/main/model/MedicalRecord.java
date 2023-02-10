package model;

import javax.swing.plaf.synth.SynthMenuBarUI;
import java.util.ArrayList;

public class MedicalRecord {

    private int year;                             // year medical record was created
    private int month;                            // month medical record was created
    private int day;                              // day medical record was created
    private ArrayList<String> symptoms;           // symptoms of patient
    private ArrayList<Prescription> prescriptions;        // a list of medication the patient is taking
    private String doctorNotes;                   // any additional doctors notes

    public MedicalRecord(int year, int month, int day, String doctorNotes) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.symptoms = new ArrayList<String>();
        this.prescriptions = new ArrayList<Prescription>();
        this.doctorNotes = doctorNotes;
    }


    // MODIFIES: this
    // EFFECTS: adds the given symptom to the list of symptoms
    public void addSymptoms(String sypmtom) {
        symptoms.add(sypmtom);
    }

    // MODIFIES: this
    // EFFECTS: adds the given prescription to the list of medications
    public void addPrescription(boolean sentToPharmacy, String medication) {
        Prescription p = new Prescription(sentToPharmacy, medication);
        this.prescriptions.add(p);
    }

    // REQUIRES: prescription.size > 0
    // EFFECTS: returns the Prescription with the given name of medication,
    //          if not found return null
    public Prescription searchPrescription(String medication) {
        for (Prescription p : prescriptions) {
            if (p.getMedication().equals(medication)) {
                return p;
            }
        }
        return null;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return this.prescriptions;
    }

    public ArrayList<String> getSymptoms() {
        return this.symptoms;
    }

    public String getDoctorNotes() {
        return this.doctorNotes;
    }


}
