package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represent a Doctor with a name, password and patients
public class Doctor implements Writable {

    private String name;
    private String password;
    private ArrayList<Patient> patients;


    public Doctor(String name, String password) {
        this.name = name;
        this.password = password;
        this.patients = new ArrayList<Patient>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Patient> getPatients() {
        return this.patients;
    }

    public String getPassword() {
        return this.password;
    }

    // MODIFIES: this
    // EFFECTS: changes the password to the given password
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    // EFFECTS: return true if the password is correct
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // MODIFIES: this
    // EFFECTS: Constructs a patient and adds them to the patient list of the
    //          Doctor
    public void addPatient(Patient p) {
        this.patients.add(p);
    }

    // MODIFIES: this
    // EFFECTS: Removes the patient from the list by their name
    public boolean removePatient(String name) {
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            if (p.getName().equals(name)) {
                patients.remove(i);
                return true;
            }
        }
        return false;
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("password", this.password);
        json.put("patients", this.patientsToJson());
        return json;
    }

    // EFFECTS: returns things in this mediRecordsapp as jsonArray
    JSONArray patientsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Patient p : this.patients) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }
}