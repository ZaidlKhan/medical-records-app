package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a patient with their name, age, weight, height and list of
// medical Records
public class Patient implements Writable {

    private String name;                                      // name of patient
    private int age;                                          // age of patient in years
    private int weight;                                       // weight of patient in kg
    private int height;                                       // height of patient in cm
    private ArrayList<MedicalRecord> medicalRecords;          // date of past visits
    private boolean isLoading;

    public Patient(String name, int age, int weight, int height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.medicalRecords = new ArrayList<MedicalRecord>();
        this.isLoading = false;
    }

    // MODIFIES: this
    // EFFECTS: adds a new medical record to the list of
    //          medical records the patient already has
    public void addMedicalRecord(MedicalRecord mr) {
        this.medicalRecords.add(mr);
        if (isLoading) {
            EventLog.getInstance().logEvent(new Event("New Medical record added to " + this.getName()));
        }
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
        EventLog.getInstance().logEvent(new Event(this.getName() + " changed name to " + name));
    }

    // MODIFIES: this
    // EFFECTS: sets the age of the object
    public void setAge(int age) {
        this.age = age;
        EventLog.getInstance().logEvent(new Event(this.getName() + " changed age to " + age));
    }

    // MODIFIES: this
    // EFFECTS: Sets the weight of the object
    public void setWeight(int weight) {
        this.weight = weight;
        EventLog.getInstance().logEvent(new Event(this.getName() + " changed weight to " + weight));
    }

    // MODIFIES: this
    // EFFECTS: Sets the height of the object
    public void setHeight(int height) {
        this.height = height;
        EventLog.getInstance().logEvent(new Event(this.getName() + " changed height to " + height));
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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("age", this.age);
        json.put("weight", this.weight);
        json.put("height", this.height);
        json.put("medicalRecords", medicalRecordToJson());
        return json;
    }

    public JSONArray medicalRecordToJson() {
        JSONArray jsonArray = new JSONArray();

        for (MedicalRecord mr : medicalRecords) {
            jsonArray.put(mr.toJson());
        }

        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: sets the loading status to allows for events to be logged if true
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    // EFFECTS: returns the loading status
    public boolean getLoading() {
        return this.isLoading;
    }
}
