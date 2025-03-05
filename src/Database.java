import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Database class
 * <p>
 * A class containing all the methods for a user database and
 * reading and printing file data in order to keep data even after a reboot.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public class Database implements DatabaseInterface {

    public static ArrayList<User> users = new ArrayList<>();
    public static User thisUser;

    private static final Object KEY = new Object();

    @Override
    public void readUserFile() {
        try {
            PrintWriter appendCreate = new PrintWriter(new FileOutputStream("Users.txt", true));
            appendCreate.close();
            BufferedReader br = new BufferedReader(new FileReader("Users.txt"));
            String line = br.readLine();
            synchronized (KEY) {
                while (line != null) {

                    String[] temp = line.split(",");
                    users.add(new User(temp[0], temp[1], temp[2]));
                    line = br.readLine();

                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error Reading File.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    //We will be writing to the friends file like this. User, friend, friend, friend......
    @Override
    public void readFriendsFile() {
        try {
            PrintWriter appendCreate = new PrintWriter(new FileOutputStream("Friends.txt", true));
            appendCreate.close();
            BufferedReader br = new BufferedReader(new FileReader("Friends.txt"));
            synchronized (KEY) {
                String line = br.readLine();
                while (line != null) {
                    String[] temp = line.split(",", 2);
                    for (User user : users) {
                        if (temp[0].equals(user.getUsername())) {
                            for (String friendName : temp[1].split(",")) {
                                for (User friendUser : users) {
                                    if (friendName.equals(friendUser.getUsername())) {
                                        user.friendUser(friendUser);
                                    }
                                }
                            }
                        }
                    }
                    line = br.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error Reading File.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void readBlockedFile() {
        try {
            PrintWriter appendCreate = new PrintWriter(new FileOutputStream("Blocked.txt", true));
            appendCreate.close();
            BufferedReader br = new BufferedReader(new FileReader("Blocked.txt"));
            synchronized (KEY) {
                String line = br.readLine();
                while (line != null) {
                    String[] temp = line.split(",", 2);
                    for (User user : users) {
                        if (temp[0].equals(user.getUsername())) {
                            for (String blockedName : temp[1].split(",")) {
                                for (User blockedUser : users) {
                                    if (blockedName.equals(blockedUser.getUsername())) {
                                        user.blockUser(blockedUser);
                                    }
                                }
                            }
                        }
                    }
                    line = br.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error Reading File.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void readMessagesFile() {
        try {
            PrintWriter appendCreate = new PrintWriter(new FileOutputStream("Messages.txt", true));
            appendCreate.close();
            BufferedReader br = new BufferedReader(new FileReader("Messages.txt"));
            String line = br.readLine();

            ArrayList<UserMessageData> messages = new ArrayList<>();
            ArrayList<DirectMessage> tempDirectMessages = new ArrayList<>();
            UserMessageData tempMessageData;
            User tempUser = null;

            String[] tempParameters = new String[5];
            synchronized (KEY) {
                while (line != null) {

                    if(line.startsWith("Username,")){ // line is the username (start of new user's message data)
                        tempUser = userExists(line.split(",")[1]);
                        messages = new ArrayList<>();
                        tempDirectMessages = new ArrayList<>();
                    }  else if(line.startsWith("TextMessage,")){ // line is data for a text message
                        tempParameters = line.split(",");
                    }  else if(line.startsWith("ImageMessage,")){ // line is data for an image message
                        tempParameters = line.split(",");
                    }  else if(line.startsWith("end")){ // end of this userMessageData
                        User tempOtherUser = null;
                        if(!tempDirectMessages.isEmpty()) { // if there is message data in this userMessageData
                            if(tempUser.getUsername().equals(tempDirectMessages.get(0).getSender().getUsername())){
                                tempOtherUser = tempDirectMessages.get(0).getRecipient();
                            } else{
                                tempOtherUser = tempDirectMessages.get(0).getSender();
                            }

                            tempMessageData = new UserMessageData(tempUser,tempOtherUser);
                            for(DirectMessage directMessage : tempDirectMessages){
                                tempMessageData.addMessageFile(directMessage);
                            }

                            tempMessageData.setHasUpdated(Boolean.parseBoolean(line.split(",")[1]));
                            tempMessageData.setHidden(Boolean.parseBoolean(line.split(",")[2]));
//                            messages.add(tempMessageData);
                            tempUser.addUserMessageData(tempMessageData);
                        }
                    } else if (line.equals("final")) { // end of file
//                        tempUser.addUserMessageData(messages);

                    } else { // line is the content/media
                        DirectMessage tempMessage = new DirectMessage(userExists(tempParameters[1]),userExists(tempParameters[2]),line,Boolean.parseBoolean(tempParameters[0]));
                        tempMessage.setTimestamp(Long.parseLong(tempParameters[3]));
                        tempMessage.setStatus(tempParameters[4]);
                        tempDirectMessages.add(tempMessage);
                    }

                    line = br.readLine();

                }

            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error Reading File.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ArrayList<User> getUsers(User user) {
        synchronized (KEY) {
            ArrayList<User> holder = new ArrayList<>();
            for (User u : users) {
                if (u != user) {
                    holder.add(u);
                }
            }
            return holder;
        }
    }

    @Override
    public boolean addUser(String username, String password, String bio) {
        synchronized (KEY) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return false;
                }
            }
            users.add(new User(username, password, bio));
        }
        return true;
    }

    @Override
    public boolean login(String data) {
        String username = data.split(",")[0];
        String password = data.split(",")[1];

        synchronized (KEY) {
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean adminLogin(String data) {

        String username = data.split(",")[0];
        String password = data.split(",")[1];

        synchronized (KEY) {
            users.get(0).setAdmin();
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.isAdmin()) {
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public void writeUserFile() {
        try {
            PrintWriter pw1 = new PrintWriter(new FileOutputStream("Users.txt"));
            synchronized (KEY) {
                for (User user : users) {
                    pw1.println(user);
                }
            }
            pw1.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void writeFriendsFile() {
        try {
            PrintWriter pw2 = new PrintWriter(new FileOutputStream("Friends.txt"));
            synchronized (KEY) {
                for (User user : users) {
                    pw2.println(user.getUsername() + "," + user.getFriendsString());
                    System.out.println(user.getUsername() + "," + user.getFriendsString());
                }
                pw2.close();
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void writeBlockedFile() {
        try {
            PrintWriter pw3 = new PrintWriter(new FileOutputStream("Blocked.txt"));
            synchronized (KEY) {
                for (User user : users) {
                    pw3.println(user.getUsername() + "," + user.getBlockedString());
                    System.out.println(user.getUsername() + "," + user.getBlockedString());
                }
            }
            pw3.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    //add to README + Interface
    @Override
    public void writeMessagesFile() {
        try {
            PrintWriter pw4 = new PrintWriter(new FileOutputStream("Messages.txt"));
            synchronized (KEY) {
                for (User user : users) {
                    pw4.println("Username," + user.getUsername());
                    for(UserMessageData messageData : user.getUserMessages()){
                        pw4.print(messageData.fileFormat());
                    }
                }
                pw4.close();
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "User file not found.",
                    "Y", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public ArrayList<User> searchForUser(String searchTerm) {
        ArrayList<User> returnValue = new ArrayList<>();
        synchronized (KEY) {
            for (User user : users) {
                if (user.getUsername().contains(searchTerm)) {
                    returnValue.add(user);
                }
            }
        }
        return returnValue;
    }

    @Override
    public User userExists(String username) {

        synchronized (KEY) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;

    }

    @Override
    public ArrayList<User> getUserFriends(User user) {

        synchronized (KEY) {
            ArrayList<User> holder = new ArrayList<>();
            for (User u : getUsers(user)) {

                if (user.getFriends().contains(u)) {
                    holder.add(u);
                }

            }
            return holder;
        }

    }

    public boolean addFriend(User user, User other) {

        synchronized (KEY) {

            if (user.getFriends().contains(other)) {
                return false;
            } else {
                user.friendUser(other);
                return true;
            }

        }

    }

    @Override
    public String[] getMessages(User user, User other) {

        synchronized (KEY) {
            ArrayList<UserMessageData> thisUsersMessages = user.getUserMessages();
            ArrayList<String> messagesToBeSent = new ArrayList<>();
            UserMessageData theOne = null;
            for (UserMessageData umd : thisUsersMessages) {
                if (umd.getUser().equals(user) && umd.getOther().equals(other)) {
                    theOne = umd;
                }
            }
            if (theOne != null) {
                ArrayList<DirectMessage> messagesOfTheOne = theOne.getMessages();
                for (DirectMessage dr : messagesOfTheOne) {
                    messagesToBeSent.add(dr.toString());
                }
                return messagesToBeSent.toArray(new String[messagesToBeSent.size()]);
            } else {
                return new String[]{"None"};
            }
        }

    }

    @Override
    public ArrayList<User> getUsersWhoHaveSentNewMessage(User user) {
        synchronized (KEY) {
            ArrayList<UserMessageData> usersMessages = user.getUserMessages();
            ArrayList<User> usersToReturn = new ArrayList<>();
            for (UserMessageData us : usersMessages) {
                for (DirectMessage d : us.getMessages()) {
                    if (d.getStatus().equals("Sent")) {
                        usersToReturn.add(us.getOther());
                        break;
                    }
                }
            }
            if (usersToReturn.isEmpty()) {
                return new ArrayList<>();
            } else {
                return usersToReturn;
            }
        }
    }

    @Override
    public String deleteMessage(User user, User other, String message) {

        boolean userSuc = false;

        synchronized (KEY) {
            ArrayList<UserMessageData> usersMessages = user.getUserMessages();
            for (UserMessageData us : usersMessages) {
                for (DirectMessage d : us.getMessages()) {
                    if (d.toString().equals(message)) {
                        d.setContent("deleted");
                        userSuc = true;
                    }
                }
            }

            if (userSuc) {
                return "true";
            } else {
                return "false";
            }
        }

    }
}
