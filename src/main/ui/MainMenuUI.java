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

public class MainMenuUI extends JFrame {

    private JList<String> patientList;
    private JButton addPatientButton;
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
    private Color lightColor;
    private JLabel nameLabel;
    private JLabel text;
    private JPanel infoPanel;
    private JPanel basePanel;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public MainMenuUI(MediRecords mr, Doctor d) {

        this.mainJsonWriter = new JsonWriter("./data/MediRecords.json");

        lightColor = new Color(187, 210, 246);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        doctor = d;

        ImageIcon logoIcon = new ImageIcon("data/image3.png");
        JLabel logo = new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(300, -1,
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

    public void showPatientDataPanel(Doctor d) {
        if (patientDataPanel != null) {
            remove(patientDataPanel);
        }

        patientDataPanel = new JPanel();
        patientDataPanel.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        String[] labels = {"Name: ", "Age: ", "Weight: ", "Height: "};
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

    public JButton addButton(Doctor d) {
        JButton saveButton = new JButton("Add Patient");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Patient p = makePatient();
                int option = JOptionPane.showConfirmDialog(MainMenuUI.this,
                        "Would you like to save this patient to file?", "Save Patients",
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
                            "Patient Not Saved To File", "Not Saved",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return saveButton;
    }

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

    public Patient makePatient() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        int weight = Integer.parseInt(weightField.getText());
        int height = Integer.parseInt(heightField.getText());

        return new Patient(name, age, weight, height);
    }

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

    public JButton logoutButton(MediRecords mr) {
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuUI.this.dispose();
                saveMediRecord(mr);
                new LoginScreenUI();
            }
        });
        return logoutButton;
    }

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

    public JButton addPatientButton(Doctor d) {
        addPatientButton = new JButton("Add Patient");
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPatientDataPanel(d);
            }
        });
        return addPatientButton;
    }

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

    private void saveMediRecord(MediRecords mediRecords) {
        try {
            this.mainJsonWriter.open();
            this.mainJsonWriter.write(mediRecords);
            this.mainJsonWriter.close();
        } catch (FileNotFoundException var2) {
            System.out.println("Unable to save file!");
        }
    }

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void displayPatientDetails(Patient p) {
        if (patientDataPanel != null) {
            remove(patientDataPanel);
        }

        patientDataPanel = new JPanel(new BorderLayout());
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(425, 2000));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        nameLabel = new JLabel("Patient: " + p.getName());
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

    public JPanel buttonPanel(Patient p) {
        JPanel buttonPanel = new JPanel();
        JPanel panel = new JPanel(new BorderLayout());
        buttonPanel.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(removePatientButton(doctor), gridBagConstraints);

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

    public void loadPatients(Doctor d) {
        for (Patient patient : d.getPatients()) {
            patientListModel.addElement(patient.getName());
        }
    }

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

    public JButton removePatientButton(Doctor d) {
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
                    } else {
                    JOptionPane.showMessageDialog(MainMenuUI.this,
                            "Please select a patient to remove.",
                            "No Patient Selected",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        return removeButton;
    }

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

    public void removePatientFromDoctor(int selectedIndex) {
        Patient p = doctor.getPatients().get(selectedIndex);
        doctor.removePatient(p.getName());
        patientListModel.remove(selectedIndex);
        remove(patientDataPanel);
        patientDataPanel = null;
        revalidate();
        repaint();
    }

    public JScrollPane displayMedicalRecords(ArrayList<MedicalRecord> medicalHistory) {
        JPanel mr = new JPanel();
        mr.setLayout(new BoxLayout(mr, BoxLayout.Y_AXIS));
        for (MedicalRecord m : medicalHistory) {
            JLabel historyLabel = new JLabel("Date: " + m.getDate());
            historyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            historyLabel.setBorder(new EmptyBorder(0, 25,0,0));
            mr.add(historyLabel);

            JLabel symptomsLabel = new JLabel("Symptoms: " + Arrays.toString(m.getSymptoms()));
            symptomsLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            symptomsLabel.setBorder(new EmptyBorder(0, 25,0,0));
            mr.add(symptomsLabel);

            JLabel prescriptionsLabel = new JLabel("Prescriptions: " + Arrays.toString(m.getPrescriptions()));
            prescriptionsLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            prescriptionsLabel.setBorder(new EmptyBorder(0, 25,0,0));;
            mr.add(prescriptionsLabel);

            JLabel doctorNotesLabel = new JLabel("Doctor's Notes: " + m.getDoctorNotes());
            doctorNotesLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            doctorNotesLabel.setBorder(new EmptyBorder(0, 25, 10, 0));
            mr.add(doctorNotesLabel);
        }
        JScrollPane medicalRecords = new JScrollPane(mr);
        medicalRecords.getVerticalScrollBar().setUnitIncrement(16);
        medicalRecords.setBorder(new EmptyBorder(0,0,0,0));
        return medicalRecords;
    }

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

    public MedicalRecord makeMedicalRecord() {
        String date = String.valueOf(java.time.LocalDate.now());
        String[] symptoms = symptomField.getText().split(",");
        String[] prescriptions = prescriptionField.getText().split(",");
        String doctorsNote = doctorsNoteField.getText();

        return new MedicalRecord(date, symptoms, prescriptions, doctorsNote);
    }

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

    public JButton changePassowrdButton(JDialog settingsMenu) {
        JButton passwordChangeButton = new JButton("Change Password");
        passwordChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        return passwordChangeButton;
    }

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
