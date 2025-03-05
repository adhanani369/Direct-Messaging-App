import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 * Client Interface
 *
 * A Class for the client and the methods
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public interface ClientInterface {

    void closeEverything(Socket socket, BufferedReader bfr, BufferedWriter bfw);

    String[] getUsers();

    String[] getFriends(String username);

    boolean friendsOnlyOrNot(String username);

    boolean tryLogin(String username, String password);

    boolean tryLoginAsAdmin(String username, String password);

    boolean sendMessage(String userToSendTo, String message);

    void logOut();

    void adminShutDown(String username);

    boolean deleteMessage(String user, String other, String message);

    String[] newMessages(String username);

    void displayMessageHistoryWithUser(String username);

    boolean registerNewUser(String username, String password, String bio);

    boolean uploadProfilePicture(String username, String pfpPath);

}
