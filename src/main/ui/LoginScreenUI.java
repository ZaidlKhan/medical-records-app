package ui;

import model.MediRecords;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class represents the starting user interface that opens when the application is first opened. It prompts the
 * user with a login and signup button that allows the user to login or signup. This class also loads the
 * mediRecords from the JSON file. Once either the login or signup button is pressed, this window is closed
 **/
public class LoginScreenUI extends JFrame {

    private MediRecords mr;
    private final JsonWriter mainJsonWriter;
    private final JsonReader mainJsonReader;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public LoginScreenUI() {
        setSize(400, 550);
        setTitle("MediRecords");
        setLocationRelativeTo(null);

        this.mainJsonWriter = new JsonWriter("./data/MediRecords.json");
        this.mainJsonReader = new JsonReader("./data/MediRecords.json");

        mr = loadMediRecords();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon("data/image.png");
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(300, -1, Image.SCALE_DEFAULT)));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        buttonPanel.add(loginButton(), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        buttonPanel.add(signupButton(), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        buttonPanel.add(imageLabel, gridBagConstraints);

        buttonPanel.setBackground(new Color(145, 185, 246));

        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public JButton loginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.addActionListener(e -> {
            new LoginUI(mr);
            LoginScreenUI.this.dispose();
        });
        return loginButton;
    }

    public JButton signupButton() {
        JButton signupButton = new JButton("Signup");
        signupButton.setPreferredSize(new Dimension(150, 50));
        signupButton.addActionListener(e -> {
            new SignupUI(mr);
            LoginScreenUI.this.dispose();
        });
        return signupButton;
    }

    private MediRecords loadMediRecords() {
        try {
            this.mr = this.mainJsonReader.read();
        } catch (IOException var2) {
            System.out.println("Unable to read from file: ./data/Medirecords.json");
        }
        return mr;
    }
}