package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

// Represent the MediRecords app that has a list of registeredDoctors
public class MediRecords {

    private ArrayList<Doctor> registeredDoctors;
    private boolean isLoading;

    public MediRecords() {
        this.registeredDoctors = new ArrayList<Doctor>();
        this.isLoading = false;
    }

    public ArrayList<Doctor> getRegisteredDoctor() {
        return this.registeredDoctors;
    }

    // MODIFIES: this
    // EFFECTS: adds the doctor to the registeredDoctors
    public void addDoctor(Doctor d) {
        this.registeredDoctors.add(d);
        if (isLoading) {
            EventLog.getInstance().logEvent(new Event(d.getName() + " added to MediRecords"));
        }
    }

    // REQUIRES: registeredDoctors.size > 0
    // EFFECTS: returns the details of the doctor with the given name
    public Doctor searchDoctor(String name) {
        for (Doctor d : registeredDoctors) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("registeredDoctors", this.doctorsToJson());
        return json;
    }

    public JSONArray doctorsToJson() {
        JSONArray jsonArray = new JSONArray();
        Iterator var2 = this.registeredDoctors.iterator();

        while (var2.hasNext()) {
            Doctor d = (Doctor)var2.next();
            jsonArray.put(d.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: sets the loading status to allows for events to be logged if true
    public void setLoadingModel(boolean isLoading) {
        this.isLoading = isLoading;
    }

    // EFFECTS: returns the loading status
    public boolean getLoadingModel() {
        return this.isLoading;
    }

}
