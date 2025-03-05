import java.util.ArrayList;

/**
 * User Message Data
 * <p>
 * A class that holds all the messages between two users.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public class UserMessageData implements UserMessageDataHandler {

    private final ArrayList<DirectMessage> messages = new ArrayList<>();
    private User user;
    private User other;
    private boolean hasUpdated = false; //does the UI need to be changed?
    private boolean isHidden = false; //did user remove DM from DM list

    public UserMessageData(User user, User other) {
        this.user = user;
        this.other = other;
    }
    //getters and setters

    public User getUser() {
        return user;
    }

    public User getOther() {
        return other;
    }

    public ArrayList<DirectMessage> getMessages() {
        return messages;
    }

    public boolean isHasUpdated() {
        return hasUpdated;
    }

    public void setHasUpdated(boolean hasUpdated) {
        this.hasUpdated = hasUpdated;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    //adds a message to the messages arrayList
    public void addMessage(DirectMessage message) {
        messages.add(message);
        hasUpdated = true;
        //server to set hasUpdated to false after User DM moves to the top of the list
    }

    //adds a message to the messages array but doesn't update hasUpdated
    public void addMessageFile(DirectMessage message){
        messages.add(message);
    }
    //delete message ADD to readme and Interface
    public void deleteMessage(DirectMessage message) {
        messages.remove(message);
        hasUpdated = true;
        //server to set hasUpdated to false after User DM moves to the top of the list
    }

    //returns the last message sent TO or FROM user
    public DirectMessage getLatestMessage() {
        return messages.get(messages.size() - 1);
    }

    public String fileFormat(){
        StringBuilder output = new StringBuilder();
        for(DirectMessage message : this.messages){
            output.append(message.fileFormat());
        }
        return output.append("end,").append(hasUpdated).append(",").append(isHidden).append("\n").toString();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserMessageData{\n");
        for (DirectMessage message : messages) {
            sb.append(message.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
