package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class SignupUI extends JFrame {

    private GridBagConstraints gridBagConstraints;
    private JTextField usernameField;
    private JPasswordField passwordField;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(40, 0, 0, 0);
        userPanel.add(signupButton(mr), gridBagConstraints);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 0, 20, 0);
        userPanel.add(imageLabel(), gridBagConstraints);

        userPanel.setBackground(new Color(145, 185, 246));

        getContentPane().add(userPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public JLabel usernameLabel() {
        JLabel usernameLabel = new JLabel();
        usernameLabel.setPreferredSize(new Dimension(90, 40));
        usernameLabel.setText("Name:");
        usernameLabel.setForeground(Color.white);
        usernameLabel.setSize(new Dimension(100, 40));
        return usernameLabel;
    }

    public JTextField username() {
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(130, 30));
        return usernameField;
    }

    public JLabel passwordLabel() {
        JLabel passwordLabel = new JLabel();
        passwordLabel.setPreferredSize(new Dimension(90, 40));
        passwordLabel.setText("Password:");
        passwordLabel.setForeground(Color.white);
        passwordLabel.setSize(new Dimension(100, 40));
        return passwordLabel;
    }

    public JPasswordField password() {
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(130, 30));
        return passwordField;
    }

    public JButton signupButton(MediRecords mediRecords) {
        JButton signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.addActionListener(e -> {
            Doctor d = new Doctor(getUsername(), getPassword());
            mediRecords.addDoctor(d);
            new MainMenuUI(mediRecords, d);
        });
        return signupButton;
    }

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

}
