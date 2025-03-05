import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

public class LoginWindow extends JFrame implements Runnable {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;
    private Client client;

    //Login window that sets the main view on startup.
    public LoginWindow() {
        Socket newSocket = null;
        Client client = null;
        try {

            newSocket = new Socket("localHost", 1234);
            this.client = new Client(newSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }

        usernameField = new JTextField();
        passwordField = new JPasswordField();

    }

    public LoginWindow(Client client1) {

        this.client = client1;
        usernameField = new JTextField();
        passwordField = new JPasswordField();

    }

    public void run() {

        // Set up the frame
        setTitle("Login - Y");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for fields
        JLabel yIntro = new JLabel("Welcome to Y", SwingConstants.CENTER);
        yIntro.setForeground(Color.blue);

        JPanel labsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        c.insets = new Insets (5, 0, 0, 0);
        labsPanel.add (yIntro, c);

        JPanel PanelSouth = new JPanel();
        JPanel PanelNorth = new JPanel();

        // Username field
        PanelSouth.add(new JLabel("Username:"));
        PanelSouth.add(usernameField);

        // Password field
        PanelSouth.add(new JLabel("Password:"));
        PanelSouth.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        PanelSouth.add(loginButton);

        // Register button
        registerButton = new JButton("Register");
        PanelSouth.add(registerButton);

        exitButton = new JButton("Exit");
        PanelNorth.add(exitButton);

        add(labsPanel, BorderLayout.CENTER);
        add(PanelSouth, BorderLayout.SOUTH);
        add(PanelNorth, BorderLayout.NORTH);

        //Action Listeners
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayRegistrationForm();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitCompletely();
            }
        });


    }



    private void Login(String username, String password) {

        boolean successOrNot = client.tryLogin(username, password);

        if (successOrNot) {

            JOptionPane.showMessageDialog(null,
                    "Login Success!",
                    "Y", JOptionPane.INFORMATION_MESSAGE);
            client.username = username;

            goToHomePage();

        } else {

            JOptionPane.showMessageDialog(null,
                    "Login Failed!",
                    "Y", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void displayRegistrationForm() {
        // Open the registration window
        SwingUtilities.invokeLater (new RegistrationWindow(client));
        this.dispose(); // Close the login window
    }

    private void exitCompletely() {
        client.exitClient();
        this.dispose();
    }

    private void goToHomePage() {
        //Open home page window
        SwingUtilities.invokeLater(new HomePage(client));
        this.dispose();
    }

}
