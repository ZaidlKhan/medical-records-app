package model;

import java.util.ArrayList;

public class Patient {

    private String name;                                      // name of patient
    private int age;                                          // age of patient in years
    private int weight;                                       // weight of patient in kg
    private int height;                                       // height of patient in cm
    private ArrayList<MedicalRecord> medicalRecords;          // date of past visits

    public Patient(String name, int age, int weight, int height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.medicalRecords = new ArrayList<MedicalRecord>();
    }

    // MODIFIES: this
    // EFFECTS: adds a new medical record to the list of
    //          medical records the patient already has
    public void addMedicalRecord(MedicalRecord mr) {
        this.medicalRecords.add(mr);
    }

    // REQUIRES: this.medicalRecords.size() > 0
    // EFFECTS: returns the medical record with the given date
    //          returns null if it is not found
    public MedicalRecord searchMedicalRecord(String date) {
        for (MedicalRecord mr : medicalRecords) {
            if (mr.getDate().equals(date)) {
                return mr;
            }
        }
        return null;
    }


    // MODIFIES: this
    // EFFECTS: sets the name of the object
    public void setName(String name) {
        this.name = name;
    }


    // MODIFIES: this
    // EFFECTS: sets the age of the object
    public void setAge(int age) {
        this.age = age;
    }

    // MODIFIES: this
    // EFFECTS: Sets the weight of the object
    public void setWeight(int weight) {
        this.weight = weight;
    }

    // MODIFIES: this
    // EFFECTS: Sets the height of the object
    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getHeight() {
        return this.height;
    }

    public ArrayList<MedicalRecord> getMedicalRecord() {
        return this.medicalRecords;
    }

}
