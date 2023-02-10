package model;

public class Prescription {

    private boolean status;                // if prescription is sent to pharmacy
    private String medication;             // the name of the medication

    public Prescription(boolean sentToPharmacy, String medication) {
        this.status = false;
        this.medication = medication;
    }

    public void sendToPharmacy() {
        this.status = true;
    }

    public boolean getStatus() {
        return this.status;
    }

    public String getMedication() {
        return this.medication;
    }

}
