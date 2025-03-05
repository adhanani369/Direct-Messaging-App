import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Base64;

/**
 * Client Class
 * <p>
 *
 * A Class for the client and the methods
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public class Client implements ClientInterface {

    private Socket socket;
    private BufferedReader bfr;
    private BufferedWriter bfw;
    String username;
    Boolean close = false;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bfw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            // Improved error handling
            JOptionPane.showMessageDialog(null, "Failed to open streams", "Network Error",
                    JOptionPane.ERROR_MESSAGE);
            closeEverything(socket, bfr, bfw);
        }
    }

    @Override
    //Temp fix for uncontrolled errors.
    public void closeEverything(Socket sock, BufferedReader reader, BufferedWriter writer) {

        try {

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            if (sock != null) {
                sock.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //DONE XXXXXXX
    @Override
    public String[] getUsers() {

        try {

            ArrayList<String> users = new ArrayList<>();

            bfw.write("getUsers," + username);
            bfw.newLine();
            bfw.flush();

            String first = bfr.readLine();
            users.add(first);
            if (!first.equals("None")) {
                while (bfr.ready()) {
                    first = bfr.readLine();
                    users.add(first);
                }
            }

            return users.toArray(new String[users.size()]);

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }

    }

    //DONE XXXXX
    @Override
    public String[] getFriends(String usern) {

        try {

            ArrayList<String> friends = new ArrayList<>();

            bfw.write("getFriends," + usern);
            bfw.newLine();
            bfw.flush();

            String first = bfr.readLine();
            friends.add(first);
            if (!first.equals("None")) {
                while (bfr.ready()) {
                    first = bfr.readLine();
                    friends.add(first);
                }
            }

            return friends.toArray(new String[friends.size()]);

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }

    }

    //DONE XXXX
    @Override
    public boolean friendsOnlyOrNot(String usern) {

        try {

            bfw.write("friendsOnlyOrNot," + usern);
            bfw.newLine();
            bfw.flush();

            return Boolean.parseBoolean(bfr.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    //Login Works XXXXXX.
    @Override
    public boolean tryLogin(String usern, String password) {
        if (socket == null || socket.isClosed()) {
            return false;
        }
        try {
            bfw.write("tryLogin," + usern + "," + password);
            bfw.newLine();
            bfw.flush();
            return Boolean.parseBoolean(bfr.readLine());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to communicate", "Network Error",
                    JOptionPane.ERROR_MESSAGE);
            closeEverything(socket, bfr, bfw);
            return false;
        }
    }

    //DONE XXXXXX
    @Override
    public boolean tryLoginAsAdmin(String usern, String password) {
        if (socket == null || socket.isClosed()) {
            return false;
        }
        try {
            bfw.write("tryAdminLogin," + usern + "," + password);
            bfw.newLine();
            bfw.flush();
            return Boolean.parseBoolean(bfr.readLine());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to communicate", "Network Error",
                    JOptionPane.ERROR_MESSAGE);
            closeEverything(socket, bfr, bfw);
            return false;
        }
    }

    //Sends message to another user. Returns if the process was sent or not.
    //Will add to the local cache and will send to the server.

    //DONE XXXXXXX
    @Override
    public boolean sendMessage(String userToSendTo, String message) {

        try {

            bfw.write("sendMessage," + this.username + "," + userToSendTo + "," + message);
            bfw.newLine();
            bfw.flush();

            String result = bfr.readLine();
            if (result.equals("Blocked")) {

                JOptionPane.showMessageDialog(null,
                        "This user has blocked you and/or you have blocked this user.\n " +
                                "You cannot send messages to blocked users and/or if you are blocked.",
                        "Y", JOptionPane.ERROR_MESSAGE);
                return false;

            } else if (result.equals("Friends")) {
                JOptionPane.showMessageDialog(null,
                        "You or the person you are sending to has friends only chat on.\n" +
                                "You cannot send messages to non-friends with this setting on.",
                        "Y", JOptionPane.ERROR_MESSAGE);
                return false;

            } else if (result.equals("Failed")) {

                JOptionPane.showMessageDialog(null,
                        "This person does not exist anymore.",
                        "Y", JOptionPane.ERROR_MESSAGE);
                return false;

            } else {

                return true;

            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteMessage(String user, String other, String message) {

        try {

            bfw.write("deleteMessage," + user + "," + other + "," + message);
            bfw.newLine();
            bfw.flush();

            return Boolean.parseBoolean(bfr.readLine());

        } catch (IOException e) {

            closeEverything(this.socket, this.bfr, this.bfw);
            return false;

        }

    }

    @Override
    public String[] newMessages(String usern) {

        try {

            bfw.write("checkForNewMessages," + usern);
            bfw.newLine();
            bfw.flush();

            ArrayList<String> usersWithNewMessages = new ArrayList<>();

            String check = bfr.readLine();
            if (!check.equals("None")) {

                usersWithNewMessages.add(check);

                if (bfr.ready()) {
                    while (check != null && bfr.ready()) {
                        check = bfr.readLine();
                        usersWithNewMessages.add(check);
                    }
                }
            } else {
                usersWithNewMessages.add("None");
            }

            return usersWithNewMessages.toArray(new String[usersWithNewMessages.size()]);

        } catch (IOException e) {

            closeEverything(this.socket, this.bfr, this.bfw);
            return null;

        }

    }

    /*The way I plan on using this method is for displaying the message history for the two users.
    This is for only this phase though, because we will implement an actual display. For now, I will use
     simple GUI to display everything on one large module.*/

    //DONE XXXXXXXXXXXXX
    @Override
    public void displayMessageHistoryWithUser(String usern) {

        String messageLine = "";

        try {

            boolean fon = friendsOnlyOrNot(usern);

            if (fon) {

                String[] friends = getFriends(usern);
                String messageChoice = (String) JOptionPane.showInputDialog(null,
                        "Please select a friend to get messages from: ", "Y",
                        JOptionPane.PLAIN_MESSAGE,
                        null, friends,
                        null);

                bfw.write("displayMessageHistoryWithUser," + usern + "," + messageChoice);
                bfw.newLine();
                bfw.flush();

                String initial = bfr.readLine();

                if (!initial.equals("None")) {

                    messageLine += initial + "\n";

                    while (initial != null && bfr.ready()) {

                        initial = bfr.readLine();
                        if (initial != null) {
                            messageLine += initial + "\n";
                        }

                    }

                    JOptionPane.showMessageDialog(null,
                            messageLine,
                            "Y", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    JOptionPane.showMessageDialog(null,
                            "There are no previous messages with this user.",
                            "Y", JOptionPane.ERROR_MESSAGE);

                }

            } else {

                String[] users = getUsers();
                String messageChoice = (String) JOptionPane.showInputDialog(null,
                        "Please select a user to get messages from: ", "Y",
                        JOptionPane.PLAIN_MESSAGE,
                        null, users,
                        null);

                bfw.write("displayMessageHistoryWithUser," + username + "," + messageChoice);
                bfw.newLine();
                bfw.flush();

                String initial = bfr.readLine();

                if (!initial.equals("None")) {

                    messageLine += initial + "\n";

                    while (initial != null && bfr.ready()) {

                        initial = bfr.readLine();
                        if (initial != null) {
                            messageLine += initial + "\n";
                        }

                    }

                    JOptionPane.showMessageDialog(null,
                            messageLine,
                            "Y", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    JOptionPane.showMessageDialog(null,
                            "There are no previous messages with this user.",
                            "Y", JOptionPane.ERROR_MESSAGE);

                }

            }

        } catch (IOException e) {

            closeEverything(this.socket, this.bfr, this.bfw);

        }

    }

    public boolean addFriend(String username) {

        try {

            bfw.write("addFriend," + this.username + "," + username);
            bfw.newLine();
            bfw.flush();

            return Boolean.parseBoolean(bfr.readLine());

        } catch (IOException e) {

            closeEverything(this.socket, this.bfr, this.bfw);
            return false;

        }

    }

    public String[] getMessagesToDeleteBetweenUsers(String usern, String other) {
        try {

            bfw.write("displayMessageHistoryWithUser," + usern + "," + other);
            bfw.newLine();
            bfw.flush();

            ArrayList<String> returnable = new ArrayList<>();

            String checker = bfr.readLine();
            if (checker.equals("None")) {
                String[] none = new String[1];
                none[0] = "None";
            } else {

                returnable.add(checker);
                if (bfr.ready()) {
                    while (checker != null && bfr.ready()) {
                        returnable.add(checker);
                        checker = bfr.readLine();
                    }
                }
            }

            return returnable.toArray(new String[returnable.size()]);

        } catch (IOException e) {
            closeEverything(this.socket, this.bfr, this.bfw);
            return null;
        }
    }

    /*Will ask server if user already exists, if not, it will create a new user
    and send the user to the server to handle. If success will return true, if user already exists
    will return false.*/

    //DONE XXXXXX
    @Override
    public boolean registerNewUser(String usern, String password, String bio) {
        try {
            String registrationData = String.join(",", usern, password, bio);
            bfw.write("registerNewUser," + registrationData);
            bfw.newLine();
            bfw.flush();
            return Boolean.parseBoolean(bfr.readLine());
        } catch (IOException e) {
            closeEverything(this.socket, this.bfr, this.bfw);
            return false;
        }
    }

    //DONE XXXXXXXX
    @Override
    public boolean uploadProfilePicture(String usern, String pfpPath) {
        try {
            bfw.write("uploadProfilePicture," + usern + "," + pfpPath);
            bfw.newLine();
            bfw.flush();

            String successOrNot = bfr.readLine();

            return Boolean.parseBoolean(successOrNot);
        } catch (IOException e) {
            closeEverything(this.socket, this.bfr, this.bfw);
            return false;
        }

    }

    public boolean modifyBio(String usern, String newBio) {
        try {
            bfw.write("modifyBio," + usern + "," + newBio);
            bfw.newLine();
            bfw.flush();

            String successOrNot = bfr.readLine();

            return Boolean.parseBoolean(successOrNot);
        } catch (IOException e) {
            closeEverything(this.socket, this.bfr, this.bfw);
            return false;
        }
    }

    //DONE XXXXXXX
    @Override
    public void logOut() {

        this.username = null;

    }

    @Override
    public void adminShutDown(String usern) {
        try {

            bfw.write("adminShutDown," + usern);
            bfw.newLine();
            bfw.flush();
            closeEverything(socket, bfr, bfw);
            this.close = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitClient() {
        try {

            bfw.write("exit, null");
            bfw.newLine();
            bfw.flush();
            closeEverything(this.socket, this.bfr, this.bfw);
            this.close = true;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater (new LoginWindow());

        Socket newSocket = null;
        Client client = null;
        try {

            newSocket = new Socket("localHost", 1234);
            client = new Client(newSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (client != null && !client.close && newSocket.isConnected()) {

            String[] choices = {"TryLogin", "RegisterUser", "SendMessage", "DisplayMessages",
                    "DeleteMessage", "CheckForNewMessages", "UploadProfilePicture", "modifyBio",
                    "LogOut", "AdminLogin", "Exit"};

            String choice = (String) JOptionPane.showInputDialog(null,
                    "Select operation to perform", "Y",
                    JOptionPane.PLAIN_MESSAGE,
                    null, choices,
                    null);
            if (choice == null) {
                //send close to server
                client.exitClient();
                break;
            }
            switch (choice) {

//                //DONE XXXXXXXXXXXXXXXX
//                case "TryLogin":
//
//                    if (client.username == null) {
//
//                        String username = JOptionPane.showInputDialog(null,
//                                "Enter Username: ",
//                                "Y", JOptionPane.QUESTION_MESSAGE);
//
//                        String password = JOptionPane.showInputDialog(null,
//                                "Enter Password: ",
//                                "Y", JOptionPane.QUESTION_MESSAGE);
//
//                        boolean successOrNot = client.tryLogin(username, password);
//
//                        if (successOrNot) {
//
//                            JOptionPane.showMessageDialog(null,
//                                    "Login Success!",
//                                    "Y", JOptionPane.INFORMATION_MESSAGE);
//                            client.username = username;
//
//                        } else {
//
//                            JOptionPane.showMessageDialog(null,
//                                    "Login Failed!",
//                                    "Y", JOptionPane.ERROR_MESSAGE);
//
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null,
//                                "You are logged in already, please log out to change user!",
//                                "Y", JOptionPane.ERROR_MESSAGE);
//                    }
//                    break;

                //DONE XXXXXXXX
//                case "RegisterUser":
//
//                    if (client.username == null) {
//
//                        String registerUsername;
//                        String registerPassword;
//                        String bio;
//
//                        do {
//                            registerUsername = JOptionPane.showInputDialog(null,
//                                    "Enter Username", "Y", JOptionPane.PLAIN_MESSAGE);
//                            if ((registerUsername == null) || (registerUsername.isEmpty())) {
//                                JOptionPane.showMessageDialog(null,
//                                        "Username cannot be empty!", "Y", JOptionPane.ERROR_MESSAGE);
//                            }
//                        } while ((registerUsername == null) || (registerUsername.isEmpty()));
//
//                        do {
//                            registerPassword = JOptionPane.showInputDialog(null,
//                                    "Enter Password", "Y", JOptionPane.PLAIN_MESSAGE);
//                            if ((registerPassword == null) || (registerPassword.isEmpty())) {
//                                JOptionPane.showMessageDialog(null,
//                                        "Password cannot be empty!", "Y", JOptionPane.ERROR_MESSAGE);
//                            }
//                        } while ((registerPassword == null) || (registerPassword.isEmpty()));
//
//
//                        bio = JOptionPane.showInputDialog(null,
//                                "Enter Bio | Empty Bio field will be set to default", "Y",
//                                JOptionPane.PLAIN_MESSAGE);
//                        if (bio == null || bio.isEmpty()) {
//                            bio = "default";
//                        }
//                        if (client.registerNewUser(registerUsername, registerPassword, bio)) {
//                            JOptionPane.showMessageDialog(null, "User successfully registered",
//                                    "Y", JOptionPane.PLAIN_MESSAGE);
//                        } else {
//                            JOptionPane.showMessageDialog(null, "User with that username " +
//                                    "already exists", "Y", JOptionPane.ERROR_MESSAGE);
//                        }
//                    } else {
//
//                        JOptionPane.showMessageDialog(null,
//                                "You are logged in already!",
//                                "Y", JOptionPane.ERROR_MESSAGE);
//
//                    }
//                    break;


                //DONE XXXXXXXX
                case "SendMessage":

                    if (client.username != null) {

                        boolean friendsOnly = client.friendsOnlyOrNot(client.username);

                        if (friendsOnly) {

                            String[] friends = client.getFriends(client.username);

                            if (!friends[0].equals("None")) {

                                String messageChoice = (String) JOptionPane.showInputDialog(null,
                                        "Please select a friend to send to: ", "Y",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null, friends,
                                        null);

                                String message = JOptionPane.showInputDialog(null,
                                        "Enter message: ",
                                        "Y", JOptionPane.QUESTION_MESSAGE);

                                boolean success = client.sendMessage(messageChoice, message);

                                if (success) {

                                    JOptionPane.showMessageDialog(null,
                                            "Message sent!",
                                            "Y", JOptionPane.INFORMATION_MESSAGE);

                                }
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "No Friends Found!",
                                        "Y", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {

                            String[] users = client.getUsers();

                            if (!users[0].equals("None")) {

                                String messageChoice = (String) JOptionPane.showInputDialog(null,
                                        "Please select a user to send to: ", "Y",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null, users,
                                        null);

                                String message = JOptionPane.showInputDialog(null,
                                        "Enter message: ",
                                        "Y", JOptionPane.QUESTION_MESSAGE);

                                boolean success = client.sendMessage(messageChoice, message);

                                if (success) {

                                    JOptionPane.showMessageDialog(null,
                                            "Message sent!",
                                            "Y", JOptionPane.INFORMATION_MESSAGE);

                                }

                            } else {

                                JOptionPane.showMessageDialog(null,
                                        "No Users Found!",
                                        "Y", JOptionPane.ERROR_MESSAGE);

                            }

                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please log in first!",
                                "Y", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "AddFriends":

                    if (client.username != null) {



                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please log in first!",
                                "Y", JOptionPane.ERROR_MESSAGE);
                    }

                    break;

                case "DeleteMessage":

                    if (client.username != null) {

                        String userToFindMessageToDelete = (String) JOptionPane.showInputDialog(null,
                                "Please select a user to find message to delete: ", "Y",
                                JOptionPane.PLAIN_MESSAGE,
                                null, client.getUsers(),
                                null);

                        String[] messagesToFind = client.getMessagesToDeleteBetweenUsers(client.username,
                                userToFindMessageToDelete);

                        String clientMessageToDelete = (String) JOptionPane.showInputDialog(null,
                                "Please select a message to delete: ", "Y",
                                JOptionPane.PLAIN_MESSAGE,
                                null, messagesToFind,
                                null);

                        boolean result = client.deleteMessage(client.username,
                                userToFindMessageToDelete, clientMessageToDelete);

                        if (result) {

                            JOptionPane.showMessageDialog(null,
                                    "Deleted: " + clientMessageToDelete,
                                    "Y", JOptionPane.INFORMATION_MESSAGE);

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "Failed to delete!",
                                    "Y", JOptionPane.ERROR_MESSAGE);

                        }

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please log in first!",
                                "Y", JOptionPane.ERROR_MESSAGE);
                    }

                    break;

                case "CheckForNewMessages":
                    if (client.username != null) {

                        String[] usersWithMessagesNotChecked = client.newMessages(client.username);

                        if (usersWithMessagesNotChecked[0].equals("None")) {
                            JOptionPane.showMessageDialog(null,
                                    "No new messages!",
                                    "Y", JOptionPane.INFORMATION_MESSAGE);
                        } else {

                            JOptionPane.showInputDialog(null,
                                    "These are the users who have sent you a new message.\n"
                                            + "Please search for them in display messages!", "Y",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null, usersWithMessagesNotChecked,
                                    null);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please log in first!",
                                "Y", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                //DONE XXXXXXXXXXXXX
                case "UploadProfilePicture":

                    if (client.username == null) {
                        JOptionPane.showMessageDialog(null,
                                "Please Log in first", "Y", JOptionPane.ERROR_MESSAGE);
                    } else {
                        File pfpFile = null;

                        JFileChooser fileChooser = new JFileChooser();
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only .jpg files",
                                "jpg");
                        fileChooser.setFileFilter(filter);
                        int option = fileChooser.showOpenDialog(null);
                        if (option == JFileChooser.APPROVE_OPTION) {
                            pfpFile = fileChooser.getSelectedFile();

                            try (FileInputStream fileInputStream = new FileInputStream(pfpFile)) {
                                byte[] bytes = new byte[(int) pfpFile.length()];
                                fileInputStream.read(bytes);
                                if (client.uploadProfilePicture(client.username,
                                        Base64.getEncoder().encodeToString(bytes))) {
                                    JOptionPane.showMessageDialog(null,
                                            "PFP successfully changed", "Y", JOptionPane.PLAIN_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "PFP not changed",
                                            "Y", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;

                case "modifyBio":
                    String newBio;
                    if (client.username == null) {
                        JOptionPane.showMessageDialog(null,
                                "Please Log in first", "Y", JOptionPane.ERROR_MESSAGE);
                    } else {
                        do {
                            newBio = JOptionPane.showInputDialog(null,
                                    "Enter Bio", "Y", JOptionPane.PLAIN_MESSAGE);
                            if ((newBio == null) || (newBio.isEmpty())) {
                                JOptionPane.showMessageDialog(null,
                                        "Bio cannot be empty!", "Y", JOptionPane.ERROR_MESSAGE);
                            }
                        } while ((newBio == null) || (newBio.isEmpty()));
                        if (client.modifyBio(client.username, newBio)) {
                            JOptionPane.showMessageDialog(null, "Bio successfully changed",
                                    "Y", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Bio not changed", "Y",
                                    JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                    break;


                case "DisplayMessages":

                    if (client.username != null) {
                        client.displayMessageHistoryWithUser(client.username);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please Log in first", "Y", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "LogOut":

                    if (client.username != null) {
                        JOptionPane.showMessageDialog(null,
                                "Bye! Have a nice day!",
                                "Y", JOptionPane.INFORMATION_MESSAGE);
                        client.logOut();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You cannot log out before logging in!", "Y", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                //DONE XXXXXXXX
                case "AdminLogin":

                    if (client.username == null) {

                        String adminName = JOptionPane.showInputDialog(null,
                                "Enter Username: ",
                                "Y", JOptionPane.QUESTION_MESSAGE);

                        String adminPassword = JOptionPane.showInputDialog(null,
                                "Enter Password: ",
                                "Y", JOptionPane.QUESTION_MESSAGE);

                        boolean adminLoginSuccess = client.tryLoginAsAdmin(adminName, adminPassword);

                        if (adminLoginSuccess) {

                            JOptionPane.showMessageDialog(null,
                                    "Login Success!",
                                    "Y", JOptionPane.INFORMATION_MESSAGE);

                            int selectedOption = JOptionPane.showConfirmDialog(null,
                                    "Do you want to close the server?",
                                    "Y",
                                    JOptionPane.YES_NO_OPTION);

                            if (selectedOption == JOptionPane.YES_OPTION) {

                                client.adminShutDown(adminName);

                            } else {

                                client.logOut();

                            }

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "Login Failed!",
                                    "Y", JOptionPane.ERROR_MESSAGE);

                        }
                    } else {

                        JOptionPane.showMessageDialog(null,
                                "Please Log out first!", "Y", JOptionPane.ERROR_MESSAGE);

                    }
                    break;

                case "Exit":
                    client.exitClient();
                    break;

                default:
                    break;

            }
        }
    }
}
