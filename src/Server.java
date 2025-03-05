import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Server Class
 * <p>
 *
 * A Class for the server and the methods
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public class Server implements ServerInterface {

    private Database database;
    private static final int PORT = 1234;
    private ServerSocket sc = null;

    private static final Object KEY = new Object();

    public Server(ServerSocket serverSocket) {

        this.sc = serverSocket;
        this.database = new Database();

    }

    @Override
    public void startServer() {

        database.readUserFile();
        database.readBlockedFile();
        database.readFriendsFile();
        database.readMessagesFile();

        try {

            while (!sc.isClosed()) {
                Socket socket = sc.accept();
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }

        } catch (IOException e) {
            System.out.println("Socket closed");
        }

    }

    @Override
    public void closeServer() {

        try {

            database.writeBlockedFile();
            database.writeFriendsFile();
            database.writeMessagesFile();
            database.writeUserFile();

            if (sc != null) {

                sc.close();

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * ClientHandler Class
     * <p>
     *
     * A Class for the client handler and the run method
     *
     * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
     * @version March 30th 2024
     */
    class ClientHandler implements Runnable {

        private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        private Socket socket;
        private BufferedReader bfr;
        private BufferedWriter bfw;
        private boolean closed = false;

        public ClientHandler(Socket socket) {

            try {

                this.socket = socket;
                this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bfw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ClientHandler c = this;
                clientHandlers.add(c);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            try {
                String readIn;
                while (!closed && socket.isConnected()) {
                    if ((readIn = bfr.readLine()) != null) {
                        if (readIn.contains(",")) {
                            String methodChoice = readIn.substring(0, readIn.indexOf(","));
                            String data = readIn.substring(readIn.indexOf(",") + 1);

                            switch (methodChoice) {

                                //DONE XXXXX
                                case "registerNewUser":
                                    String[] newUser = data.split(",");
                                    boolean setOrNot = database.addUser(newUser[0], newUser[1], newUser[2]);
                                    bfw.write(String.valueOf(setOrNot));
                                    bfw.newLine();
                                    bfw.flush();
                                    break;

                                //DONE XXXXXXX
                                case "tryLogin":

                                    boolean bool = database.login(data);
                                    if (bool) {
                                        bfw.write("true");
                                        bfw.newLine();
                                        bfw.flush();
                                    } else {
                                        bfw.write("false");
                                        bfw.newLine();
                                        bfw.flush();
                                    }
                                    break;

                                case "friendsOnlyOrNot":

                                    User userCheck = database.userExists(data);
                                    bfw.write(String.valueOf(userCheck.isFriendsOnlyChat()));
                                    bfw.newLine();
                                    bfw.flush();
                                    break;


                                //In the future were gonna have to figure out how implement the delivered and read.
                                //DONE XXXXXX
                                case "sendMessage":

                                    User currentUser = database.userExists(data.substring(0, data.indexOf(",")));
                                    data = data.substring(data.indexOf(",") + 1);
                                    User userToSendTo = database.userExists(data.substring(0, data.indexOf(",")));
                                    data = data.substring(data.indexOf(",") + 1);
                                    if (userToSendTo != null) {

                                        if ((userToSendTo.isFriendsOnlyChat() &&
                                                !userToSendTo.getFriends().contains(currentUser)) ||
                                                (currentUser.isFriendsOnlyChat() &&
                                                        !currentUser.getFriends().contains(userToSendTo))) {

                                            bfw.write("Friends");
                                            bfw.newLine();
                                            bfw.flush();

                                        } else if (userToSendTo.getBlocked().contains(currentUser) ||
                                                currentUser.getBlocked().contains(userToSendTo)) {

                                            bfw.write("Blocked");
                                            bfw.newLine();
                                            bfw.flush();

                                        } else if ((userToSendTo.isFriendsOnlyChat() &&
                                                userToSendTo.getFriends().contains(currentUser)) &&
                                                (currentUser.isFriendsOnlyChat() &&
                                                        currentUser.getFriends().contains(userToSendTo))) {

                                            boolean currentUserMessages = false;
                                            for (UserMessageData umd : currentUser.getUserMessages()) {
                                                if (umd.getOther().equals(userToSendTo)) {
                                                    currentUserMessages = true;
                                                    umd.addMessage(new DirectMessage(currentUser,
                                                            userToSendTo, data, false));
                                                }
                                            }
                                            if (!currentUserMessages) {
                                                currentUser.addUserMessageData(new UserMessageData(currentUser,
                                                        userToSendTo));
                                            }

                                            boolean otherUserMessages = false;
                                            for (UserMessageData umd : userToSendTo.getUserMessages()) {
                                                if (umd.getOther().equals(currentUser)) {
                                                    otherUserMessages = true;
                                                    umd.addMessage(new DirectMessage(userToSendTo, currentUser, data,
                                                            false));
                                                }
                                            }
                                            if (!otherUserMessages) {
                                                userToSendTo.addUserMessageData(new UserMessageData(userToSendTo,
                                                        currentUser));
                                            }

                                            bfw.write("Success");
                                            bfw.newLine();
                                            bfw.flush();

                                        } else if (!userToSendTo.isFriendsOnlyChat() &&
                                                !currentUser.isFriendsOnlyChat()) {

                                            boolean currentUserMessages = false;
                                            for (UserMessageData umd : currentUser.getUserMessages()) {
                                                if (umd.getOther().equals(userToSendTo)) {
                                                    currentUserMessages = true;
                                                    umd.addMessage(new DirectMessage(currentUser, userToSendTo,
                                                            data, false));
                                                }
                                            }
                                            if (!currentUserMessages) {
                                                currentUser.addUserMessageData(new UserMessageData(currentUser,
                                                        userToSendTo));
                                                for (UserMessageData umd : currentUser.getUserMessages()) {
                                                    if (umd.getOther().equals(userToSendTo)) {
                                                        umd.addMessage(new DirectMessage(currentUser, userToSendTo,
                                                                data, false));
                                                    }
                                                }
                                            }

                                            boolean otherUserMessages = false;
                                            for (UserMessageData umd : userToSendTo.getUserMessages()) {
                                                if (umd.getOther().equals(currentUser)) {
                                                    otherUserMessages = true;
                                                    umd.addMessage(new DirectMessage(userToSendTo, currentUser, data,
                                                            false));
                                                }
                                            }
                                            if (!otherUserMessages) {
                                                userToSendTo.addUserMessageData(new UserMessageData(userToSendTo,
                                                        currentUser));
                                                for (UserMessageData umd : userToSendTo.getUserMessages()) {
                                                    if (umd.getOther().equals(currentUser)) {
                                                        umd.addMessage(new DirectMessage(userToSendTo, currentUser, data,
                                                                false));
                                                    }
                                                }
                                            }

                                            bfw.write("Success");
                                            bfw.newLine();
                                            bfw.flush();

                                        }

                                    } else {

                                        bfw.write("Failed");
                                        bfw.newLine();
                                        bfw.flush();

                                    }
                                    break;

                                case "displayMessageHistoryWithUser":
                                    String[] findMessage = data.split(",");
                                    User messegeUser = database.userExists(findMessage[0]);
                                    User otherMessageGetter = database.userExists(findMessage[1]);
                                    String[] messages = database.getMessages(messegeUser, otherMessageGetter);
                                    if (messages[0].equals("None")) {
                                        bfw.write("None");
                                        bfw.newLine();
                                        bfw.flush();
                                    } else {
                                        for (String mess : messages) {
                                            bfw.write(mess);
                                            bfw.newLine();
                                        }
                                        bfw.flush();
                                    }
                                    break;

                                case "deleteMessage":

                                    User user1 = database.userExists(data.substring(0, data.indexOf(",")));
                                    data = data.substring(data.indexOf(",") + 1);
                                    User otherUser1 = database.userExists(data.substring(0, data.indexOf(",")));
                                    data = data.substring(data.indexOf(",") + 1);
                                    String suc = database.deleteMessage(user1, otherUser1, data);

                                    bfw.write(suc);
                                    bfw.newLine();
                                    bfw.flush();

                                    break;

                                //DONE XXXXX
                                case "uploadProfilePicture":
                                    String userPFPString = data.split(",")[0];
                                    String PFP = data.split(",")[1];

                                    User userPFP = database.userExists(userPFPString);
                                    if (userPFP != null) {
                                        userPFP.setPfp(PFP);
                                        bfw.write("true");
                                    } else {
                                        bfw.write("false");
                                    }
                                    bfw.newLine();
                                    bfw.flush();
                                    break;

                                case "modifyBio":
                                    String userBioString = data.split(",")[0];
                                    String newBio = data.split(",")[1];

                                    User userBio = database.userExists(userBioString);
                                    if (userBio != null) {
                                        userBio.setBio(newBio);
                                        bfw.write("true");
                                    } else {
                                        bfw.write("false");
                                    }
                                    bfw.newLine();
                                    bfw.flush();
                                    break;

                                case "getFriends":
                                    User friendUserCheck = database.userExists(data);
                                    ArrayList<User> usersF = database.getUserFriends(friendUserCheck);
                                    if (usersF.isEmpty()) {
                                        bfw.write("None");
                                        bfw.newLine();
                                        bfw.flush();
                                    } else {
                                        for (User u : usersF) {

                                            bfw.write(u.getUsername());
                                            bfw.newLine();

                                        }
                                        bfw.flush();
                                    }
                                    break;

                                case "addFriend":

                                    String[] usersToUse = data.split(",");
                                    User currentUserCheck = database.userExists(usersToUse[0]);
                                    User otherCurrentUserCheck = database.userExists(usersToUse[1]);
                                    if (database.addFriend(currentUserCheck, otherCurrentUserCheck)) {

                                        bfw.write("true");
                                        bfw.newLine();
                                        bfw.flush();

                                    } else {

                                        bfw.write("false");
                                        bfw.newLine();
                                        bfw.flush();

                                    }

                                    break;

                                case "getUsers":
                                    User checkForUser = database.userExists(data);
                                    ArrayList<User> usersD = database.getUsers(checkForUser);
                                    if (usersD.isEmpty()) {
                                        bfw.write("None");
                                        bfw.newLine();
                                        bfw.flush();
                                    } else {
                                        for (User u : usersD) {

                                            bfw.write(u.getUsername());
                                            bfw.newLine();

                                        }
                                        bfw.flush();
                                    }
                                    break;


                                case "checkForNewMessages":

                                    User currentUserToCheckMessages = database.userExists(data);
                                    ArrayList<User> usersForThis =
                                            database.getUsersWhoHaveSentNewMessage(currentUserToCheckMessages);
                                    if (usersForThis.isEmpty()) {
                                        bfw.write("None");
                                        bfw.newLine();
                                        bfw.flush();
                                    } else {
                                        for (User u : usersForThis) {
                                            bfw.write(u.getUsername());
                                            bfw.newLine();
                                        }
                                        bfw.flush();
                                    }

                                    break;

                                //DONE XXXXX
                                case "tryAdminLogin":

                                    boolean admindBool = database.adminLogin(data);
                                    if (admindBool) {
                                        bfw.write("true");
                                        bfw.newLine();
                                        bfw.flush();
                                    } else {
                                        bfw.write("false");
                                        bfw.newLine();
                                        bfw.flush();
                                    }
                                    break;

                                //DONE XXXXXX
                                case "adminShutDown":

                                    closeServer();
                                    closeEverything(socket, bfr, bfw);
                                    break;

                                case "exit":
                                    this.closeEverything(this.socket, this.bfr, this.bfw);
                                    clientHandlers.remove(this);
                                    break;

                                default:
                                    // Handle unknown command
                                    break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println("Error handling client [" + socket.getInetAddress() + "]: " + e.getMessage());
                e.printStackTrace();
            } finally {
                closeEverything(socket, bfr, bfw);
            }
        }

        private void closeEverything(Socket sock, BufferedReader reader, BufferedWriter writer) {
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
            this.closed = true;
        }

    }
}
