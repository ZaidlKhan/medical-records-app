package ui;

import model.Doctor;
import model.MedicalRecord;
import model.Patient;

import java.util.*;

public class MedicalRecordsApp {

    private Scanner scan;
    private Doctor doctor;

    // EFFECTS: runs the Medical Records application
    public MedicalRecordsApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {

        boolean keepGoing = true;
        scan = new Scanner(System.in);
        doctor = new Doctor("x");

        while (keepGoing) {

            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. View Patients");
            System.out.println("4. Search Patients");
            System.out.println("5. Exit");
            System.out.print("Enter Your Choice: ");
            int choice = scan.nextInt();
            if (choice == 5) {
                System.out.println("Exiting Application");
                keepGoing = false;
            }
            processCommandMain(choice);
        }
        System.out.println("Goodbye!");
    }

    private void processCommandMain(int command) {
        if (command == 1) {
            addPatient();
        } else if (command == 2) {
            removePatient();
        } else if (command == 3) {
            viewPatient();
        } else if (command == 4) {
            searchPatient();
        }
    }


    private void addPatient() {

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

        System.out.println("Patient has been successfully added!\n");
        doctor.addPatient(p);
    }

    private void removePatient() {
        System.out.print("Enter Patient Name: ");
        scan.nextLine();
        String name = scan.nextLine();
        if (doctor.removePatient(name)) {
            System.out.println("Patient " + name + " has been successfully removed!");
            System.out.println("\n");
        } else {
            System.out.println("Patient " + name + " cannot be found.");
            System.out.println("\n");
        }
    }

    private void viewPatient() {
        if (doctor.getPatients().size() == 0) {
            System.out.println("No Patients In Directory");
        }
        if (doctor.getPatients().size() != 0) {
            System.out.println("Patients: ");
            for (int i = 0; i < doctor.getPatients().size(); i++) {
                System.out.println(doctor.getPatients().get(i).getName());
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

    private void searchPatient() {
        System.out.print("Enter Patient Name: ");
        scan.nextLine();
        String name = scan.nextLine();
        Patient p = doctor.searchPatient(name);
        if (doctor.getPatients().contains(p)) {
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

        if (!doctor.getPatients().contains(p)) {
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
}


