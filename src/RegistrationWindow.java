import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;

import static java.lang.reflect.Array.getLength;

public class RegistrationWindow extends JFrame implements Runnable{

    // Fields for registration data
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton registerButton;
    private JButton exitButton;
    private JTextArea bioField;
    private Client client;

    public RegistrationWindow(Client client) {

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        bioField = new JTextArea(2, 20);
        bioField.setDocument(new JTextFieldLimit(120));

    }

    public void run() {

        this.client = client;
        setTitle("Register - Y");
        setSize(300, 300);
        setLayout(new BorderLayout());

        JPanel gridOptions = new JPanel(new GridLayout(3, 2, 10, 10));

        gridOptions.add(new JLabel("Username:"));
        gridOptions.add(usernameField);

        gridOptions.add(new JLabel("Password:"));
        gridOptions.add(passwordField);

        gridOptions.add(new JLabel("Bio (max 120 chars):"));
        gridOptions.add(new JScrollPane(bioField)); // Allows for scrolling if the user input exceeds the visible area

        JPanel south = new JPanel();

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performRegistration(usernameField.getText(), passwordField.getText(), bioField.getText());
            }
        });
        south.add(registerButton);

        add(gridOptions, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel north = new JPanel();
        exitButton = new JButton("Exit");
        north.add(exitButton);

        add(north, BorderLayout.NORTH);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitCompletely();
            }
        });

    }

    private void performRegistration(String username, String password, String bio) {

        if (client.registerNewUser(username, password, bio)) {
            JOptionPane.showMessageDialog(null, "User successfully registered",
                    "Y", JOptionPane.PLAIN_MESSAGE);

            SwingUtilities.invokeLater(new LoginWindow(client));

        } else {
            JOptionPane.showMessageDialog(null, "User with that username " +
                    "already exists", "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitCompletely() {
        client.exitClient();
        this.dispose();
    }


//    public String encodeFileToBase64Binary(File file) throws IOException {
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            byte[] bytes = new byte[(int) file.length()];
//            fileInputStream.read(bytes);
//            return Base64.getEncoder().encodeToString(bytes);
//        }
//    }

    // Define the JTextFieldLimit class inside the RegistrationWindow class
    class JTextFieldLimit extends PlainDocument {
        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

}
