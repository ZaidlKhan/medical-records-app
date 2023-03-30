package ui;


import javax.swing.*;
import java.awt.*;
import model.*;
import persistence.JsonReader;
import java.io.IOException;

/**
 * The LoginUI class is a JFrame that represents the login user interface for the MediRecords application. The class
 * allows the user to log in and access their account. once the user is logged in, they are prompted to load previous
 * patients. Once the user chooses if they want to load previous patients, the main menu screen is opened with the
 * Doctors Patients and Data
 **/
public class LoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final JsonReader mainJsonReader;

    public LoginUI(MediRecords mr) {
        this.mainJsonReader = new JsonReader("./data/MediRecords.json");
        MediRecords mr1 = loadMediRecords(mr);

        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel userPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill =  GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        userPanel.add(username(), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        userPanel.add(usernameLabel(), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        userPanel.add(passwordLabel(), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        userPanel.add(password(), gridBagConstraints);

        buttonsAndImageLabel(mr1, userPanel, gridBagConstraints);
        userPanel.setBackground(new Color(145, 185, 246));
        getContentPane().add(userPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // MODIFIES: buttonPanel
    // EFFECTS: add a return button, login button and image label to the UI
    private void buttonsAndImageLabel(MediRecords mr1, JPanel userPanel, GridBagConstraints gridBagConstraints) {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(65, 0,0,0);
        userPanel.add(loginButton(mr1), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(5, 0,0,0);
        userPanel.add(returnButton(), gridBagConstraints);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 0,20,0);
        userPanel.add(imageLabel(), gridBagConstraints);
    }

    // EFFECTS: Shows a JPanel that says "name:"
    public JLabel usernameLabel() {
        JLabel usernameLabel = new JLabel("Name:");
        usernameLabel.setPreferredSize(new Dimension(90, 40));
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.white);
        usernameLabel.setSize(new Dimension(100, 40));
        return usernameLabel;
    }

    // EFFECTS: Text box that allows users to input their name
    public JTextField username() {
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(130, 30));
        return usernameField;
    }

    // EFFECTS: Shows a JPanal with that says "Password:"
    public JLabel passwordLabel() {
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setPreferredSize(new Dimension(90, 40));
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.white);
        passwordLabel.setSize(new Dimension(100, 40));
        return passwordLabel;
    }

    // EFFECTS: Password box that allows users to input their password
    public JPasswordField password() {
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(130, 30));
        return passwordField;
    }

    // EFFECTS: Login button that once clicked authenticates the user,
    //          if the correct name and password is entered, the user
    //          is sent to the main menu of the application
    public JButton loginButton(MediRecords mr) {
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> {
            authenticate(mr);
        });
        return loginButton;
    }

    // EFFECTS: Return button, once clicked returns the user to the login screen
    public JButton returnButton() {
        JButton returnButton = new JButton("Return");
        returnButton.setPreferredSize(new Dimension(100, 30));
        returnButton.addActionListener(e -> {
            new LoginScreenUI();
        });
        return returnButton;
    }

    // EFFECTS: returns a JLabel of the logo image
    public JLabel imageLabel() {
        ImageIcon imageIcon = new ImageIcon("data/image.png");
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(300,
                -1, Image.SCALE_DEFAULT)));
        return imageLabel;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // EFFECTS: Authenticates the user, if entered name and password are correct,
    //          prompts the user to load previous patients, otherwise, prompts an error message
    //          that says Invalid username or password."
    public void authenticate(MediRecords mr) {
        String username = getUsername();
        String password = getPassword();

        Doctor d = mr.searchDoctor(username);

        if (d == null || !d.checkPassword(password)) {
            JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            loadPreviousPatients(mr, d);
        }
    }

    // MODIFIES: mr
    // EFFECTS: load the previously saved MediRecods
    private MediRecords loadMediRecords(MediRecords mr) {
        try {
            mr = this.mainJsonReader.read();
        } catch (IOException var2) {
            System.out.println("Unable to read from file: ./data/Medirecords.json");
        }
        return mr;
    }

    // EFFECTS: prompt that allows user to load previous patients, If yes,
    //          mainMenu() will run with the doctors previous patients, If not
    //          the doctors previous patient data will be overridden, and mainMenu()
    //          will run as if the doctor is new
    private void loadPreviousPatients(MediRecords mr, Doctor d) {
        int option = JOptionPane.showConfirmDialog(this,
                "Would you like to load previous patients?",
                "Load Previous Patients",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            new MainMenuUI(mr, d);
        } else {
            mr.getRegisteredDoctor().remove(d);
            Doctor d1 = new Doctor(d.getName(), d.getPassword());
            mr.addDoctor(d1);
            this.dispose();
            new MainMenuUI(mr, d1);
        }
    }
}
