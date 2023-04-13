package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

/**
 * The SignupUI class is a JFrame that represents the user interface for the doctor registration process.
 * Once the doctor has entered their username and password and click the signup button, the main menu screen opens,
 * and the doctor is added to the mediRecords
 **/
public class SignupUI extends JFrame {

    private GridBagConstraints gridBagConstraints;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupUI(MediRecords mr) {
        setSize(400, 550);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel userPanel = new JPanel(new GridBagLayout());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
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

        gridBagConstraints.gridx = 0;
        addButtonsAndImage(mr, userPanel);

        userPanel.setBackground(new Color(145, 185, 246));

        getContentPane().add(userPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // MODIFIES: userPanel
    // EFFECTS: adds a signup, return button, and image label to the UI
    public void addButtonsAndImage(MediRecords mr, JPanel userPanel) {
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(65, 0,1,0);
        userPanel.add(signupButton(mr), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        userPanel.add(returnButton(), gridBagConstraints);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 0, 20, 0);
        userPanel.add(imageLabel(), gridBagConstraints);
    }

    // EFFECTS: creates a JLabel that says "Name" on it
    public JLabel usernameLabel() {
        JLabel usernameLabel = new JLabel();
        usernameLabel.setPreferredSize(new Dimension(90, 40));
        usernameLabel.setText("Name:");
        usernameLabel.setForeground(Color.white);
        usernameLabel.setSize(new Dimension(100, 40));
        return usernameLabel;
    }

    // EFFECTS: creates a TextField that allows the user to enter their name to sign up
    public JTextField username() {
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(130, 30));
        return usernameField;
    }

    // EFFECTS: creates a J Label that says "Password" on it
    public JLabel passwordLabel() {
        JLabel passwordLabel = new JLabel();
        passwordLabel.setPreferredSize(new Dimension(90, 40));
        passwordLabel.setText("Password:");
        passwordLabel.setForeground(Color.white);
        return passwordLabel;
    }

    // EFFECTS: creates a password Field that allows the user to enter a password and signup
    public JPasswordField password() {
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(130, 30));
        return passwordField;
    }

    // MODIFIES: this, mediRecords
    // EFFECTS: creates a sign in button that when pressed, constructs a new doctor with the given name an password, and
    //          adds the doctor to the mediRecords app
    public JButton signupButton(MediRecords mediRecords) {
        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(e -> {
            Doctor d = new Doctor(getUsername(), getPassword());
            mediRecords.setLoadingModel(true);
            mediRecords.addDoctor(d);
            mediRecords.setLoadingModel(false);
            new MainMenuUI(mediRecords, d);
        });
        return signupButton;
    }

    // EFFECTS: A button that when pressed, returns the user to the start up screen
    public JButton returnButton() {
        JButton signupButton = new JButton("Return");
        signupButton.addActionListener(e -> {
            new LoginScreenUI();
        });
        return signupButton;
    }

    // EFFECTS: creates a JLabel wih the logo on it
    public JLabel imageLabel() {
        ImageIcon imageIcon = new ImageIcon("data/image.png");
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(300,
                -1, Image.SCALE_DEFAULT)));
        return imageLabel;
    }

    // EFFECTS: returns the text for the name field that the doctor has entered
    public String getUsername() {
        return usernameField.getText();
    }

    // EFFECTS: returns the text for the password field that the doctor has entered
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

}
