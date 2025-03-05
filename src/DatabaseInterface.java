import java.util.ArrayList;

/**
 * Database Interface
 * <p>
 * Interface for database class.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public interface DatabaseInterface {

    void readUserFile();

    void readFriendsFile();

    void readBlockedFile();
    void readMessagesFile();

    ArrayList<User> getUsers(User user);

    boolean addUser(String username, String password, String bio);

    boolean adminLogin(String data);

    boolean login(String data);

    User userExists(String username);

    ArrayList<User> getUserFriends(User user);

    String[] getMessages(User user, User other);

    void writeMessagesFile();

    void writeUserFile();

    void writeFriendsFile();

    void writeBlockedFile();

    ArrayList<User> searchForUser(String searchTerm);

    ArrayList<User> getUsersWhoHaveSentNewMessage(User user);

    String deleteMessage(User user, User other, String message);


}
