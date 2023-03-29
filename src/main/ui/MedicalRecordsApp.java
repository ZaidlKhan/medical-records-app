package ui;

import model.Doctor;
import model.MediRecords;
import model.MedicalRecord;
import model.Patient;
import persistence.JsonReader;
import persistence.JsonWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// Represents the MediRecords Application
public class MedicalRecordsApp {

    private Scanner scan;
    private MediRecords mediRecords;
    private final JsonWriter mainJsonWriter;
    private final JsonReader mainJsonReader;

    // EFFECTS: runs the Medical Records application
    public MedicalRecordsApp() {

        this.mainJsonWriter = new JsonWriter("./data/MediRecords.json");
        this.mainJsonReader = new JsonReader("./data/MediRecords.json");

        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {

        boolean keepGoing = true;
        scan = new Scanner(System.in);
        mediRecords = new MediRecords();

        this.loadMediRecords();

        while (keepGoing) {
            System.out.println("Welcome to MediRecords");
            System.out.println("1. Log in");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter Your Choice: ");
            int choice = scan.nextInt();

            if (choice == 3) {
                keepGoing = false;
                System.out.println("Exiting Application");
                this.saveMediRecord();
            } else {
                processCommandMediRecords(choice);
            }
        }
        System.out.println("Goodbye!");
    }

    private void processCommandMediRecords(int command) {
        if (command == 1) {
            login();
        } else if (command == 2) {
            signup();
        }
    }

    private void login() {
        boolean pendingPassword = true;
        System.out.print("Enter Your Name: ");
        scan.nextLine();
        String doctorName = scan.nextLine();
        Doctor d = mediRecords.searchDoctor(doctorName);
        if (mediRecords.getRegisteredDoctor().contains(d)) {
            while (pendingPassword) {
                System.out.print("Enter Your Password: ");
                String pw = scan.nextLine();

                if (d.checkPassword(pw)) {
                    pendingPassword = false;
                    System.out.println("Load Previous Patients? [Y/N]: ");
                    String choice = scan.nextLine();
                    loadOption(choice, d);
                } else {
                    System.out.println("Incorrect Password!");
                }
            }
        } else {
            System.out.println("Doctor Not Found\n");
        }
    }

    private void signup() {
        System.out.print("Enter Your Name: ");
        scan.nextLine();
        String doctorName = scan.nextLine();

        System.out.print("Enter Your Password: ");
        String pw = scan.nextLine();

        Doctor d = new Doctor(doctorName, pw);
        mediRecords.addDoctor(d);

        mainMenu(d);
    }

    private void loadOption(String option, Doctor d) {
        if (option.equals("Y")) {
            mainMenu(d);
        } else if (option.equals("N")) {
            mediRecords.getRegisteredDoctor().remove(d);
            d = new Doctor(d.getName(), d.getPassword());
            mainMenu(d);
            mediRecords.addDoctor(d);
        }
    }

    private void mainMenu(Doctor d) {

        boolean keepGoing = true;
        while (keepGoing) {
            System.out.println("\nWelcome Dr. " + d.getName() + "!");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. View Patients");
            System.out.println("4. Search Patients");
            System.out.println("5. Log out");
            System.out.print("Enter Your Choice: ");
            int choice = scan.nextInt();
            if (choice == 5) {
                keepGoing = false;
                System.out.println("Logging out... \n");
            }
            processCommandMain(choice, d);
        }

    }

    private void processCommandMain(int command, Doctor d) {
        if (command == 1) {
            addPatient(d);
        } else if (command == 2) {
            removePatient(d);
        } else if (command == 3) {
            viewPatient(d);
        } else if (command == 4) {
            searchPatient(d);
        }
    }

    private void addPatient(Doctor d) {

        System.out.print("Enter Patient Name: ");
        scan.nextLine();
        String name = scan.nextLine();

        System.out.print("Enter Patient Age: ");
        int age = scan.nextInt();

        System.out.print("Enter Patient Height (cm): ");
        int height = scan.nextInt();

        System.out.print("Enter Patient Weight (kg): ");
        int weight = scan.nextInt();

        System.out.print("Add new Medical Record [Y/N]: ");
        String choice = scan.next();

        Patient p = new Patient(name, age, weight, height);

        if (choice.equals("Y")) {
            MedicalRecord mr = addNewMedicalRecord();
            p.addMedicalRecord(mr);
        }

        System.out.println("Save Patient to file? [Y/N]");
        String saveChoice = scan.next();

        if (saveChoice.equals("Y")) {
            d.addPatient(p);
        }
    }

    private void removePatient(Doctor d) {
        System.out.print("Enter Patient Name: ");
        scan.nextLine();
        String name = scan.nextLine();
        if (d.removePatient(name)) {
            System.out.println("Patient " + name + " has been successfully removed!");
            System.out.println("\n");
        } else {
            System.out.println("Patient " + name + " cannot be found.");
            System.out.println("\n");
        }
    }

    private void viewPatient(Doctor d) {
        if (d.getPatients().size() == 0) {
            System.out.println("No Patients In Directory");
        }
        if (d.getPatients().size() != 0) {
            System.out.println("Patients: ");
            for (int i = 0; i < d.getPatients().size(); i++) {
                System.out.println(d.getPatients().get(i).getName());
            }
        }

        System.out.println("Enter 1 to Return to Main Menu");
        scan.next();
    }

    // Resource from: https://www.javatpoint.com/java-get-current-date
    // https://stackoverflow.com/questions/6096845/adding-comma-separated-strings-to-an-arraylist-and-vice-versa
    private MedicalRecord addNewMedicalRecord() {
        String currentdate = String.valueOf(java.time.LocalDate.now());
        System.out.println("Current Date: " + currentdate);

        System.out.print("Enter symptoms (separated by comma): ");
        scan.nextLine();
        String[] symptoms = scan.nextLine().split(",");

        System.out.print("Enter prescriptions (separated by comma, hit enter if none): ");
        String[] prescriptions = scan.nextLine().split(", ");

        System.out.print("Additional comments: ");
        String comment = scan.nextLine();

        MedicalRecord mr = new MedicalRecord(currentdate, symptoms, prescriptions, comment);
        System.out.println("Medical Record has successfully been added!");
        return mr;
    }

    private void searchPatient(Doctor d) {
        System.out.print("Enter Patient Name: ");
        scan.nextLine();
        String name = scan.nextLine();
        Patient p = d.searchPatient(name);
        if (d.getPatients().contains(p)) {
            System.out.println("\n");
            printPatientDetails(p);

            printMedicalRecordsTab();
            int choice = scan.nextInt();
            if (choice == 1) {
                changePatient(p);
            } else if (choice == 2) {
                MedicalRecord mr = addNewMedicalRecord();
                p.addMedicalRecord(mr);
            } else if (choice == 3) {
                viewMedicalRecord(p);
            }
        }

        if (!d.getPatients().contains(p)) {
            System.out.println("Patient could not be found \n");
        }
    }

    private void printMedicalRecordsTab() {
        System.out.println("1. Change Patient Data");
        System.out.println("2. Add New Medical Record");
        System.out.println("3. View Medical Records");
        System.out.println("4. Return");
        System.out.print("Enter Your Choice: ");
    }

    private void printPatientDetails(Patient p) {
        System.out.println("Name: " + p.getName());
        System.out.println("Age: " + p.getAge());
        System.out.println("Height: " + p.getHeight());
        System.out.println("Weight: " + p.getWeight());
    }

    private void changePatient(Patient p) {
        System.out.println("\n");
        System.out.println("1. Change Name ");
        System.out.println("2. Change Age ");
        System.out.println("3. Change Height ");
        System.out.println("4. Change Weight ");
        System.out.println("5. Return ");
        System.out.print("Enter Your Choice: ");
        int choice = scan.nextInt();
        if (choice == 1) {
            changePatientName(p);
        }
        if (choice == 2) {
            changePatientAge(p);
        }
        if (choice == 3) {
            changePatientHeight(p);
        }
        if (choice == 4) {
            changePatientWeight(p);
        }
    }

    private void changePatientName(Patient p) {
        System.out.print("Enter New Name: ");
        scan.nextLine();
        String newName = scan.nextLine();
        p.setName(newName);
        System.out.println("Name has successfully been changed!\n");
    }

    private void changePatientAge(Patient p) {
        System.out.print("Enter New Age: ");
        int newAge = scan.nextInt();
        p.setAge(newAge);
        System.out.println("Age has successfully been changed! \n");
    }

    private void changePatientHeight(Patient p) {
        System.out.print("Enter New Height: ");
        int newHeight = scan.nextInt();
        p.setHeight(newHeight);
        System.out.println("Height has successfully been changed! \n");
    }

    private void changePatientWeight(Patient p) {
        System.out.print("Enter New Weight: ");
        int newWeight = scan.nextInt();
        p.setWeight(newWeight);
        System.out.println("Weight has successfully been changed! \n");
    }

    private void viewMedicalRecord(Patient p) {
        ArrayList<MedicalRecord> mrList = p.getMedicalRecord();
        System.out.println("\n");
        if (p.getMedicalRecord().size() == 0) {
            System.out.println("No Medical Records Available \n");
        } else {
            for (MedicalRecord mr : mrList) {
                System.out.println(mr.getDate() + "  ");
                searchMedicalRecord(p);
            }
        }
    }

    private void searchMedicalRecord(Patient p) {
        System.out.println("Enter Date: ");
        scan.nextLine();
        String searchdate = scan.nextLine();

        MedicalRecord mr1 = p.searchMedicalRecord(searchdate);
        if (p.getMedicalRecord().contains(mr1)) {
            System.out.println("Date: " + mr1.getDate());
            System.out.println("Symptoms: " + Arrays.toString(mr1.getSymptoms()));
            System.out.println("Prescriptions: " + Arrays.toString(mr1.getPrescriptions()));
            System.out.println("Doctors Note: " + mr1.getDoctorNotes());
            System.out.println("Enter 1 to Return to Main Menu");
            scan.next();
            System.out.println("\n");
        }

        if (!p.getMedicalRecord().contains(mr1)) {
            System.out.println("Medical Record could not be found");
            System.out.println("Enter 1 to Return to Main Menu");
            scan.next();
        }
    }

    private void saveMediRecord() {
        try {
            this.mainJsonWriter.open();
            this.mainJsonWriter.write(this.mediRecords);
            this.mainJsonWriter.close();
        } catch (FileNotFoundException var2) {
            System.out.println("Unable to save file!");
        }
    }

    private void loadMediRecords() {
        try {
            this.mediRecords = this.mainJsonReader.read();
        } catch (IOException var2) {
            System.out.println("Unable to read from file: ./data/Medirecords.json");
        }
    }
}




