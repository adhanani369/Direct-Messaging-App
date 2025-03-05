import java.util.ArrayList;

/**
 * User Interface
 * <p>
 * An interface for the User class.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public interface UserInterface {

    boolean isAdmin();

    void setAdmin();

    boolean isFriendsOnlyChat();

    void setFriendsOnlyChat(boolean friendsOnlyChat);

    String getUsername();

    String getPassword();

    String getBio();

    ArrayList<User> getFriends();

    ArrayList<User> getBlocked();

    String getFriendsString();

    String getBlockedString();

    ArrayList<UserMessageData> getUserMessages();

    String getPfp();

    void setPfp(String pfp);

    void setUsername(String username);

    void setPassword(String password);

    void setBio(String bio);

    void setFriends(ArrayList<User> friends);

    void setBlocked(ArrayList<User> blocked);

    void setUserMessages(ArrayList<UserMessageData> userMessages);

    void addUserMessageData(UserMessageData umd);

    void friendUser(User user);

    void blockUser(User user);

    void unblockUser(User user);

    void unfriendUser(User user);

}
