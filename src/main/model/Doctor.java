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
    private boolean isLoading;


    public Doctor(String name, String password) {
        this.name = name;
        this.password = password;
        this.patients = new ArrayList<Patient>();
        this.isLoading = false;
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
        EventLog.getInstance().logEvent(new Event(this.getName() + " changed their password"));
    }

    //MODIFIES: this
    //EFFECTS: changes the name of the doctor
    public void setName(String newName) {
        this.name = newName;
        EventLog.getInstance().logEvent(new Event(this.getName() + " changed their name to " + newName));
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
        if (isLoading) {
            EventLog.getInstance().logEvent(new Event(this.getName() + " added to " + p.getName()
                    + " to their Patient list"));
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the patient from the list by their name
    public boolean removePatient(String name) {
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            if (p.getName().equals(name)) {
                patients.remove(i);
                EventLog.getInstance().logEvent(new Event(this.getName()
                        + " removed " + p.getName() + " from their Patient list"));
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