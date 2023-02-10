package model;

import java.util.ArrayList;

public class Doctor {

    private String name;
    private ArrayList<Patient> patients;


    public Doctor(String name) {
        this.name = name;
        this.patients = new ArrayList<Patient>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Patient> getPatients() {
        return this.patients;
    }

    // MODIFIES: this
    // EFFECTS: Constructs a patient and adds them to the patient list of the
    //          Doctor
    public void addPatient(String name, int age, int weight, int height) {
        Patient p = new Patient(name, age, weight, height);
        this.patients.add(p);
    }

    // MODIFIES: this
    // EFFECTS: Removes the patient from the list by their name
    public void removePatient(String name) {
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            if (p.getName().equals(name)) {
                patients.remove(i);
                break;
            }
        }
    }

    // REQUIRES: patients.size > 0
    // EFFECTS: returns the details of the patient with the given name
    public Patient searchPatient(String name) {
        for (Patient p : patients) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
}