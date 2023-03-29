package ui;

import model.*;
import persistence.JsonWriter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents the mainMenu for this application
 **/
public class MainMenuUI extends JFrame {

    private JList<String> patientList;
    private DefaultListModel<String> patientListModel;
    private JPanel patientDataPanel;
    private GridBagConstraints gridBagConstraints;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField weightField;
    private JTextField heightField;
    private final JsonWriter mainJsonWriter;
    private final Doctor doctor;
    private JDialog medicalRecordWindow;
    private JTextArea symptomField;
    private JTextArea prescriptionField;
    private JTextArea doctorsNoteField;
    private final Color lightColor;
    private JLabel text;
    private JPanel infoPanel;
    private final JLabel logo;

    // General Layout used: https://www.javatpoint.com/java-gridbaglayout
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public MainMenuUI(MediRecords mr, Doctor d) {

        this.mainJsonWriter = new JsonWriter("./data/MediRecords.json");

        lightColor = new Color(187, 210, 246);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        doctor = d;

        ImageIcon logoIcon = new ImageIcon("data/image3.png");
        logo = new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(300, -1,
                Image.SCALE_SMOOTH)));

        setLayout(new BorderLayout());

        JPanel leftPanel = leftPanel(mr);
        add(leftPanel, BorderLayout.WEST);
        add(dashboardPanel(doctor), BorderLayout.NORTH);
        add(logo, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        loadPatients(doctor);

        // Scrollbar Listener:
        // http://www.java2s.com/Tutorial/Java/0240__Swing/ListeningtoJListEventswithaListSelectionListener.htm
        patientList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String patientName = patientList.getSelectedValue();
                    if (patientName != null) {
                        Patient selectedPatient = d.searchPatient(patientName);
                        if (selectedPatient != null) {
                            displayPatientDetails(selectedPatient);
                            remove(logo);
                        }
                    }
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: shows labels for patient data along with text boxes so that doctor can add a new Patient
    //          with what is entered into the textboxes
    public void showPatientDataPanel(Doctor d) {
        if (patientDataPanel != null) {
            remove(patientDataPanel);
        }

        patientDataPanel = new JPanel();
        patientDataPanel.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        String[] labels = {"Name: ", "Age: ", "Weight (kg): ", "Height (cm): "};
        for (int i = 0; i < 4; i++) {
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            gridBagConstraints.insets = new Insets(0, 0, 0, 5);
            patientDataPanel.add(new JLabel(labels[i]), gridBagConstraints);
        }
        textBoxPane();

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        patientDataPanel.add(addButton(d), gridBagConstraints);

        add(patientDataPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // MODIFIES: this, d
    // EFFECTS: add button that when pressed, will allow the user to add a new Patient, once a all the data of a new
    //          entered and the save button is clicked, the user will be asked to save the patient to file, If yes,
    //          then the Patient will be save to the JSON file, if no then the patient will not be constructed
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public JButton addButton(Doctor d) {
        JButton saveButton = new JButton("Add Patient");
        saveButton.addActionListener(new ActionListener() {
            // Option Pane: https://www.javatpoint.com/java-joptionpane
            @Override
            public void actionPerformed(ActionEvent e) {
                Patient p = makePatient();
                int option = JOptionPane.showConfirmDialog(MainMenuUI.this,
                        "Confirm patient addition: This will add " + p.getName() + " to your patient list.",
                        "Add Patient",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    d.addPatient(p);
                    patientListModel.addElement(p.getName());
                    remove(patientDataPanel);
                    patientDataPanel = null;
                    revalidate();
                    repaint();
                    sucessfullyAdded();
                } else {
                    JOptionPane.showMessageDialog(MainMenuUI.this,
                            "Patient Not Added to Records", "Not Added",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return saveButton;
    }

    // EFFECTS: texts boxes for the name, age, weight, height fields where the doctor will enter the patient data
    public void textBoxPane() {
        nameField = new JTextField(20);
        ageField = new JTextField(20);
        weightField = new JTextField(20);
        heightField = new JTextField(20);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        patientDataPanel.add(nameField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        patientDataPanel.add(ageField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        patientDataPanel.add(weightField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        patientDataPanel.add(heightField, gridBagConstraints);
    }

    // REQUIRES: nameField = String, ageField = int, weightField = int, heightField = int
    // EFFECTS: creates a new Patient with the data the doctor has entered
    public Patient makePatient() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        int weight = Integer.parseInt(weightField.getText());
        int height = Integer.parseInt(heightField.getText());

        return new Patient(name, age, weight, height);
    }

    //EFFECTS: Top panel of the application with the Doctors name, and the logo on the right side
    public JPanel dashboardPanel(Doctor d) {
        JPanel dashboard = new JPanel(new BorderLayout());
        text = new JLabel("Dr " + d.getName() + "'s Dashboard");
        text.setFont(new Font("Tahoma", Font.BOLD, 22));
        text.setForeground(Color.white);
        text.setBorder(new EmptyBorder(5, 5, 0, 0));
        dashboard.add(text, BorderLayout.WEST);
        dashboard.setBackground(new Color(132, 174, 240));

        ImageIcon logoIcon = new ImageIcon("data/image.png");
        JLabel logoLabel = new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(120, -1,
                Image.SCALE_SMOOTH)));
        logoLabel.setBorder(new EmptyBorder(5, 0, 5, 5));
        dashboard.add(logoLabel, BorderLayout.EAST);

        return dashboard;
    }

    // EFFECTS: Logout Button, that once clicked will save the current state of the application and prompt the user
    //          back to the login Screen
    public JButton logoutButton(MediRecords mr) {
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDoctorOptions(mr);
                MainMenuUI.this.dispose();
                new LoginScreenUI();
            }
        });
        return logoutButton;
    }

    // MODIFIES: mainJsonWriter
    // EFFECTS: Prompts the user to save the state of the application the the JSON file.
    public void saveDoctorOptions(MediRecords mr) {
        int option = JOptionPane.showConfirmDialog(MainMenuUI.this,
                "Would you like to save your progress before logging out?", "Save Patients",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            saveMediRecord(mr);
            MainMenuUI.this.dispose();
            new LoginScreenUI();
        } else {
            MainMenuUI.this.dispose();
            new LoginScreenUI();
        }
    }

    // EFFECTS: a settings button that will allow the user to change their name or password
    public JButton settingsButton() {
        JButton settings = new JButton("Settings");
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsMenu();
            }
        });
        return settings;
    }

    // EFFECTS: A button that once clicked, shows the panel to enter new patient data to construct a new Patient
    public JButton addPatientButton(Doctor d) {
        JButton addPatientButton = new JButton("Add Patient");
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPatientDataPanel(d);
                remove(logo);
            }
        });
        return addPatientButton;
    }

    // EFFECTS: This method creates and returns a JPanel containing a list of patients, "Add Patient" and "Search"
    //          buttons, a JScrollPane for the patient list, and "Settings" and "Logout" buttons in the bottom panel.
    //          This will represent the left side of the application
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public JPanel leftPanel(MediRecords mr) {
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setBackground(lightColor);
        gridBagConstraints = new GridBagConstraints();
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new GridBagLayout());

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(addPatientButton(doctor), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(searchButton(), gridBagConstraints);

        JScrollPane patientScrollPane = new JScrollPane(patientList);
        patientScrollPane.setPreferredSize(new Dimension(150, 1000));
        patientScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        patientScrollPane.setBackground(lightColor);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        leftPanel.add(patientScrollPane, gridBagConstraints);

        bottomPanel.add(logoutButton(mr), BorderLayout.EAST);
        bottomPanel.add(settingsButton(), BorderLayout.WEST);
        bottomPanel.setBackground(lightColor);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(bottomPanel, gridBagConstraints);

        leftPanel.setBackground(lightColor);
        return leftPanel;
    }

    // MODIFIES: mainJsonWriter
    // EFFECTS: saves the current state of the application to JSON
    private void saveMediRecord(MediRecords mediRecords) {
        try {
            this.mainJsonWriter.open();
            this.mainJsonWriter.write(mediRecords);
            this.mainJsonWriter.close();
        } catch (FileNotFoundException var2) {
            System.out.println("Unable to save file!");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the patients Details (name, age, weight, height, medical history) on the right side of the
    //          window
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void displayPatientDetails(Patient p) {
        if (patientDataPanel != null) {
            remove(patientDataPanel);
        }

        patientDataPanel = new JPanel(new BorderLayout());
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(425, 2000));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Patient: " + p.getName());
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(10, 0, 2, 0));
        infoPanel.add(nameLabel);

        String[] labels = {"Age: ", "Weight: ", "Height: "};
        String[] values = {Integer.toString(p.getAge()), Integer.toString(p.getWeight()),
                Integer.toString(p.getHeight())};

        for (int i = 0; i < labels.length; i++) {
            JLabel infoLabel = new JLabel(labels[i] + values[i]);
            infoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            infoLabel.setAlignmentX(LEFT_ALIGNMENT);
            infoPanel.add(infoLabel);
        }

        JLabel medicalHistoryTitle = new JLabel("Medical History:");
        medicalHistoryTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        medicalHistoryTitle.setAlignmentX(LEFT_ALIGNMENT);
        medicalHistoryTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
        infoPanel.add(medicalHistoryTitle);
        JScrollPane mrPane = displayMedicalRecords(p.getMedicalRecord());
        infoPanel.add(mrPane);

        patientDataPanel.add(infoPanel, BorderLayout.WEST);
        patientDataPanel.add(buttonPanel(p), BorderLayout.EAST);

        add(patientDataPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // EFFECTS: Button Panel on the display Patient details window that has 3 buttons, remove Patient which removes the
    //          given Patient, change Patient which allows the use to change patient details, and add new medical record
    //          which allows the user to add a new medical Record to this Patient
    public JPanel buttonPanel(Patient p) {
        JPanel buttonPanel = new JPanel();
        JPanel panel = new JPanel(new BorderLayout());
        buttonPanel.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(removePatientButton(), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(changePatientButton(p), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(newMedicalRecordButton(p), gridBagConstraints);

        panel.add(buttonPanel, BorderLayout.NORTH);
        return panel;
    }

    // MODIFIES: patient List Model
    // EFFECTS: loads the given doctors patients into the patient list model that is displayed in the scoll pane on
    //          left side of the screen
    public void loadPatients(Doctor d) {
        for (Patient patient : d.getPatients()) {
            patientListModel.addElement(patient.getName());
        }
    }

    // EFFECTS: allows the user to enter a name and search their patients, if that patient is found, the details are
    //          on the patient data Panel, otherwise the patient could not be found and an error message pops up
    public JButton searchButton() {
        JButton searchButton = new JButton("Search Patient");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWindow();
            }
        });
        return searchButton;
    }

    // EFFECTS: once a patient is added, this prompt is display to show the patient has successfully been added to this
    //          doctor, additionally this closes the patient Data Panel
    public void sucessfullyAdded() {
        int option = JOptionPane.showConfirmDialog(MainMenuUI.this,
                "Patient Successfully Added!",
                "Successfully Added!",
                JOptionPane.DEFAULT_OPTION);
        if (option == JOptionPane.DEFAULT_OPTION) {
            patientDataPanel = null;
            revalidate();
            repaint();
        }
    }

    // EFFECTS: a remove button, that once clicked prompts the user to confirm they want to remove this patient. If yes,
    //          then the patient is removed from the doctor, If no, then the prompt is closed.
    public JButton removePatientButton() {
        JButton removeButton = new JButton("Remove Patient");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = patientList.getSelectedIndex();
                    int option = JOptionPane.showConfirmDialog(MainMenuUI.this,
                            "Are you sure you want to remove this Patient",
                            "Remove Patient",
                            JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        removePatientFromDoctor(selectedIndex);
                    }
            }
        });
        return removeButton;
    }

    // EFFECTS: a button that allows the user to change patient data (age, weight, height)
    public JButton changePatientButton(Patient p) {
        JButton changeButton = new JButton("Change Patient");
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Age", "Weight", "Height"};
                int selectedOption = JOptionPane.showOptionDialog(MainMenuUI.this,
                        "Select the attribute you want to change:",
                        "Change Patient",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);
                changePatientAttribute(p, options[selectedOption]);
            }
        });
        return changeButton;
    }

    // EFFECTS: ALlows the user to enter the new patient data they want to change (age, weight, height)
    public void changePatientAttribute(Patient p, String attribute) {
        String newValue = JOptionPane.showInputDialog(MainMenuUI.this,
                "Enter new " + attribute + ":");
        if (newValue != null && !newValue.isEmpty()) {
            if (attribute.equals("Age")) {
                int newAge = Integer.parseInt(newValue);
                p.setAge(newAge);
            } else if (attribute.equals("Weight")) {
                int newAge = Integer.parseInt(newValue);
                p.setWeight(newAge);
            } else if (attribute.equals("Height")) {
                int newAge = Integer.parseInt(newValue);
                p.setHeight(newAge);
            }
            displayPatientDetails(p);
        }
    }

    // MODIFIES: this
    // EFFECTS: the search window that allows the user to enter a patient name and search for that patient
    public void searchWindow() {
        String name = JOptionPane.showInputDialog(this,"Enter Name");
        Patient p = doctor.searchPatient(name);
        if (p != null) {
            displayPatientDetails(p);
        } else {
            JOptionPane.showMessageDialog(this, "Patient not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: doctor
    // EFFECTS: removes the patient from the doctor, and patient list model, and closes the patient data panel
    public void removePatientFromDoctor(int selectedIndex) {
        Patient p = doctor.getPatients().get(selectedIndex);
        doctor.removePatient(p.getName());
        patientListModel.remove(selectedIndex);
        remove(patientDataPanel);
        patientDataPanel = null;
        revalidate();
        repaint();
    }

    // EFFECTS: displayed the medical records of a given patient
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public JScrollPane displayMedicalRecords(ArrayList<MedicalRecord> medicalHistory) {
        JPanel mr = new JPanel();
        mr.setLayout(new BoxLayout(mr, BoxLayout.Y_AXIS));
        int wrappingWidth = 290;

        for (MedicalRecord m : medicalHistory) {
            JLabel historyLabel = createWrappedLabel("Date: " + m.getDate(), wrappingWidth);
            historyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            historyLabel.setBorder(new EmptyBorder(0, 25, 0, 0));
            mr.add(historyLabel);

            JLabel symptomsLabel = createWrappedLabel("Symptoms: " + Arrays.toString(m.getSymptoms()), wrappingWidth);
            symptomsLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            symptomsLabel.setBorder(new EmptyBorder(0, 25, 0, 0));
            mr.add(symptomsLabel);

            JLabel prescriptionsLabel = createWrappedLabel("Prescriptions: " + Arrays.toString(m.getPrescriptions()),
                    wrappingWidth);
            prescriptionsLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            prescriptionsLabel.setBorder(new EmptyBorder(0, 25, 0, 0));
            mr.add(prescriptionsLabel);

            JLabel doctorNotesLabel = createWrappedLabel("Doctor's Notes: " + m.getDoctorNotes(), wrappingWidth);
            doctorNotesLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            doctorNotesLabel.setBorder(new EmptyBorder(0, 25, 10, 0));
            mr.add(doctorNotesLabel);
        }

        JScrollPane medicalRecords = new JScrollPane(mr);
        medicalRecords.getVerticalScrollBar().setUnitIncrement(16);
        medicalRecords.setBorder(new EmptyBorder(0, 0, 0, 0));
        return medicalRecords;
    }

    // EFFECTS: allows the text to naturally wrap onto the next line
    // Text Wrapping: https://stackoverflow.com/questions/2420742/make-a-jlabel-wrap-its-text-by-setting-a-max-width
    public JLabel createWrappedLabel(String text, int width) {
        return new JLabel("<html><div style='width: " + width + "px;'>" + text + "</div></html>");
    }

    // EFFECTS: creates a button that says new medical records, once pressed, allows the user to add a new medical
    //          record
    public JButton newMedicalRecordButton(Patient p) {
        JButton button = new JButton("New Medical Record");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMedicalRecordWindow(p);
                JLabel medicalHistoryTitle = new JLabel("Medical History:");
                medicalHistoryTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
                medicalHistoryTitle.setAlignmentX(LEFT_ALIGNMENT);
                medicalHistoryTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
                infoPanel.add(medicalHistoryTitle);
                JScrollPane mrPane = displayMedicalRecords(p.getMedicalRecord());
                infoPanel.add(mrPane);
            }
        });
        return button;
    }

    // MODIFIES: this
    // EFFECTS: Creates a new window that allows the user to enter information to create a new medical record, once the
    //          add button is pressed, this medical record is added to this patient and the patient data panel is
    //          updated with the nnew medical record added
    public void newMedicalRecordWindow(Patient p) {
        medicalRecordWindow = new JDialog();
        medicalRecordWindow.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        String[] labels = {"Date: ", "Symptoms (Separated By Comma): ", "Prescriptions (Separated By Comma): ",
                "Doctors Note: "};
        for (int i = 0; i < 4; i++) {
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            gridBagConstraints.insets = new Insets(0, 0, 0, 5);
            medicalRecordWindow.add(new JLabel(labels[i]), gridBagConstraints);
        }

        textBoxPaneMR();

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        medicalRecordWindow.add(addButtonMR(p, medicalRecordWindow), gridBagConstraints);
        medicalRecordWindow.add(addButtonMR(p, medicalRecordWindow), gridBagConstraints);

        medicalRecordWindow.setBackground(lightColor);
        medicalRecordWindow.setSize(700, 500);
        medicalRecordWindow.setLocationRelativeTo(null);
        medicalRecordWindow.setVisible(true);
    }

    // MODIFES: this, p
    // EFFECTS: Add button that allows the user to add a new medical record. once it is pressed, the mr is added to
    //          this patient and the patient data panel is updated, also, the window is closed
    public JButton addButtonMR(Patient p, JDialog window) {
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.addMedicalRecord(makeMedicalRecord());
                window.dispose();
                displayPatientDetails(p);
                revalidate();
                repaint();
            }
        });
        return addButton;
    }

    // EFFECTS: text fields for the add medical records window that allow the user to enter data to create a new
    //          medical record
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void textBoxPaneMR() {
        JLabel date =  new JLabel(String.valueOf(java.time.LocalDate.now()));
        symptomField = new JTextArea();
        prescriptionField = new JTextArea();
        doctorsNoteField = new JTextArea();

        symptomField.setPreferredSize(new Dimension(300, 75));
        prescriptionField.setPreferredSize(new Dimension(300, 75));
        doctorsNoteField.setPreferredSize(new Dimension(300, 75));

        doctorsNoteField.setLineWrap(true);
        doctorsNoteField.setWrapStyleWord(true);
        prescriptionField.setLineWrap(true);
        prescriptionField.setWrapStyleWord(true);
        doctorsNoteField.setLineWrap(true);
        doctorsNoteField.setWrapStyleWord(true);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        medicalRecordWindow.add(date, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        medicalRecordWindow.add(symptomField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        medicalRecordWindow.add(prescriptionField, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        medicalRecordWindow.add(doctorsNoteField, gridBagConstraints);
    }

    // EFFECTS: makes a new medical record based on what is entered into the text fields in the add medical record
    //          window
    public MedicalRecord makeMedicalRecord() {
        String date = String.valueOf(java.time.LocalDate.now());
        String[] symptoms = symptomField.getText().split(",");
        String[] prescriptions = prescriptionField.getText().split(",");
        String doctorsNote = doctorsNoteField.getText();

        return new MedicalRecord(date, symptoms, prescriptions, doctorsNote);
    }

    // MODIFIES: this
    // EFFECTS: settings menu that allows the user to change their name and password
    public void settingsMenu() {
        JDialog settingsMenu = new JDialog();
        settingsMenu.setTitle("Settings");
        settingsMenu.setLayout(new GridBagLayout());
        settingsMenu.setSize(400, 550);
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        settingsMenu.add(changeNameButton(settingsMenu), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        settingsMenu.add(changePassowrdButton(settingsMenu), gridBagConstraints);


        settingsMenu.setSize(350, 200);
        settingsMenu.setLocationRelativeTo(null);
        settingsMenu.setVisible(true);
    }

    // EFFECTS: a button that once clicked will allow the user to change their name
    public JButton changeNameButton(JDialog settingsMenu) {
        JButton changeNameButton = new JButton("Change Name");
        changeNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeName();
                settingsMenu.dispose();
            }
        });
        return changeNameButton;
    }

    // EFFECTS: a buttons that once pressed will allow the user to change their passowrd
    public JButton changePassowrdButton(JDialog settingsMenu) {
        JButton passwordChangeButton = new JButton("Change Password");
        passwordChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
                settingsMenu.dispose();
            }
        });
        return passwordChangeButton;
    }

    // MODIFES: doctor
    // EFFECTS: allows the user enter their new name, also sets the texts on the top panel to reflect the updated name
    public void changeName() {
        String newName = JOptionPane.showInputDialog(MainMenuUI.this,
                "Enter new name : ");
        if (newName != null && !newName.isEmpty()) {
            doctor.setName(newName);
            JOptionPane.showMessageDialog(this, "Name Successfully Changed");
            text.setText("Dr " + doctor.getName() + "'s Dashboard");
            revalidate();
            repaint();
        }
    }

    // MODIFES: doctor
    // EFFECTS: allows the user to enter a new password
    public void changePassword() {
        String newPassword = JOptionPane.showInputDialog(MainMenuUI.this,
                "Enter new Password : ");
        if (newPassword != null && !newPassword.isEmpty()) {
            doctor.setName(newPassword);
            JOptionPane.showMessageDialog(this, "Password Successfully Changed");
            doctor.setPassword(newPassword);
            revalidate();
            repaint();
        }
    }
}
