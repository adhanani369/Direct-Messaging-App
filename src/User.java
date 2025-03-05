import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * User Class
 * <p>
 * A user class that holds the data pertaining to a specific user.
 * Also holds friends and blocked users and their methods.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public class User implements UserInterface {
    private String username;
    private String password;
    private String bio;
    private String pfp;
    private boolean friendsOnlyChat;
    private boolean isAdmin;

    private ArrayList<User> friends = new ArrayList<>();
    private ArrayList<User> blocked = new ArrayList<>();
    private ArrayList<UserMessageData> userMessages = new ArrayList<>();

    //All new users must have all of these parameters.
    public User(String username, String password, String bio) {

        this.username = username;
        this.password = password;
        if (bio.isEmpty()) {
            this.bio = "default";
        } else {
            this.bio = bio;
        }
//        this.pfp = pfp;
        this.friendsOnlyChat = false; // default value
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void setAdmin() {
        this.isAdmin = true;
    }

    @Override
    public boolean isFriendsOnlyChat() {
        return friendsOnlyChat;
    }

    @Override
    public void setFriendsOnlyChat(boolean friendsOnlyChat) {
        this.friendsOnlyChat = friendsOnlyChat;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getBio() {
        return this.bio;
    }

    @Override
    public ArrayList<User> getFriends() {
        return this.friends;
    }

    @Override
    public ArrayList<User> getBlocked() {
        return this.blocked;
    }

    @Override
    public String getFriendsString() {
        StringBuilder returnValue = new StringBuilder();
        for (User user : friends) {
            returnValue.append(user.getUsername()).append(",");
        }
        return returnValue.toString();
    }

    @Override
    public String getBlockedString() {
        StringBuilder returnValue = new StringBuilder();
        for (User user : blocked) {
            returnValue.append(user.getUsername()).append(",");
        }
        return returnValue.toString();
    }

    @Override
    public ArrayList<UserMessageData> getUserMessages() {
        return this.userMessages;
    }

    @Override
    public String getPfp() {
        return pfp;
    }

    @Override
    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    @Override
    public void setBlocked(ArrayList<User> blocked) {
        this.blocked = blocked;
    }

    @Override
    public void setUserMessages(ArrayList<UserMessageData> userMessages) {
        this.userMessages = userMessages;
    }

    @Override
    public void addUserMessageData(UserMessageData umd) {
        this.userMessages.add(umd);
    }

    //methods
    @Override
    public void friendUser(User user) {
        friends.add(user);
        user.friends.add(this);
    }

    @Override
    public void blockUser(User user) {
        friends.remove(user);
        user.friends.remove(this);
        blocked.add(user);
    }

    @Override
    public void unfriendUser(User user) {
        friends.remove(user);
        user.friends.remove(this);
    }

    @Override
    public void unblockUser(User user) {
        blocked.remove(user);
    }

    public String toString() {
        if (this.isAdmin) {
            return username + "," + password + "," + bio + "," + "true";
        } else {
            return username + "," + password + "," + bio + "," + "false";
        }
    }

}
