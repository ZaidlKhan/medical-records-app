package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.MediRecords;
import model.MedicalRecord;
import model.Patient;
import model.Doctor;
import org.json.JSONArray;
import org.json.JSONObject;

// Represents a reader that reads mediRecords from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads medirecords from file and returns it;
    // throws IOException if an error occurs from read data from file
    public MediRecords read() throws IOException {
        String jsonData = this.readFile(this.source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return this.parseMediRecord(jsonObject);
    }

    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8);

        try {
            stream.forEach(contentBuilder::append);
        } catch (Throwable var7) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if (stream != null) {
            stream.close();
        }
        return contentBuilder.toString();
    }

    private MediRecords parseMediRecord(JSONObject jsonObject) {
        MediRecords mr = new MediRecords();
        this.addDoctors(mr, jsonObject);
        return mr;
    }

    private void addDoctors(MediRecords mr, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("registeredDoctors");

        for (Object json: jsonArray) {
            JSONObject nextDoctor = (JSONObject) json;
            this.addDoctor(mr, nextDoctor);
        }
    }

    private void addDoctor(MediRecords mr, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        Doctor d = new Doctor(name, password);
        addPatients(d, jsonObject);
        mr.addDoctor(d);

    }

    private void addPatients(Doctor d, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("patients");

        for (Object json : jsonArray) {
            JSONObject nextPatient = (JSONObject) json;
            this.addPatient(d, nextPatient);
        }
    }

    private void addPatient(Doctor d, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int age = jsonObject.getInt("age");
        int weight = jsonObject.getInt("weight");
        int height = jsonObject.getInt("height");
        Patient patient = new Patient(name, age, weight, height);
        addMedicalRecordJson(patient, jsonObject);
        d.addPatient(patient);
    }

    private void addMedicalRecordJson(Patient p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("medicalRecords");

        for (Object json: jsonArray) {
            JSONObject nextMedicalRecord = (JSONObject) json;
            this.addMedicalRecord(p, nextMedicalRecord);
        }
    }

    private void addMedicalRecord(Patient p, JSONObject jsonObject) {
        String date = jsonObject.getString("date");
        JSONArray symptoms = jsonObject.getJSONArray("symptoms");
        String[] stringSymptoms = makeStringArray(symptoms);
        JSONArray prescription = jsonObject.getJSONArray("prescriptions");
        String[] stringPrecription = makeStringArray(prescription);
        String doctorsNote = jsonObject.getString("doctorNotes");

        MedicalRecord mr = new MedicalRecord(date, stringSymptoms,
                stringPrecription, doctorsNote);
        p.addMedicalRecord(mr);
    }

    private String[] makeStringArray(JSONArray array) {
        String[] stringArray = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            stringArray[i] = array.getString(i);
        }
        return stringArray;
    }

}
