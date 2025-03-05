import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame implements Runnable {

    private JButton exitButton;
    private JButton friendsButton;
    private JButton messagingButton;
    private JButton settingsButton;
    private Client client;

    public HomePage(Client client) {

        this.client = client;

    }

    public void run() {

        // Set up the frame
        setTitle("Y");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel yIntro = new JLabel("Y", SwingConstants.CENTER);
        yIntro.setForeground(Color.blue);
        yIntro.setSize(50,50);

        JPanel labsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        c.insets = new Insets (5, 0, 0, 0);
        labsPanel.add (yIntro, c);

        JPanel PanelSouth = new JPanel();
        JPanel PanelNorth = new JPanel();

        friendsButton = new JButton("Friends");
        PanelSouth.add(friendsButton);

        exitButton = new JButton("Exit");
        PanelNorth.add(exitButton);

        add(labsPanel, BorderLayout.CENTER);
        add(PanelSouth, BorderLayout.SOUTH);
        add(PanelNorth, BorderLayout.NORTH);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitCompletely();
            }
        });

    }

    private void exitCompletely() {
        client.exitClient();
        this.dispose();
    }


}
