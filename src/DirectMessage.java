import java.util.Date;

/**
 * Direct Message class
 * <p>
 * A direct message class that stores data pertaining to a single message between two users.
 * Also contains a path for photo sending as well.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public class DirectMessage implements DirectMessageHandler {
    private User sender;
    private User recipient;
    private String content;
    private String mediaPath;
    private long timestamp;
    private String status;

    public DirectMessage(User sender, User recipient, String content, boolean isMedia) {
        this.sender = sender;
        this.recipient = recipient;
        if (isMedia) {
            if (isValidMediaPath(content)) {
                this.mediaPath = content;
                this.content = null;
            } else {
                throw new IllegalArgumentException("Invalid media path");
            }
        } else {
            if (isValidContent(content)) {
                this.content = content;
                this.mediaPath = null;
            } else {
                throw new IllegalArgumentException("Message content cannot be empty");
            }
        }
        this.timestamp = System.currentTimeMillis();
        this.status = "Sent";
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void deliver() {
        this.status = "Delivered";
    }

    public void markAsRead() {
        this.status = "Read";
    }

    public void delete() {
        this.status = "Deleted";
    }

    public boolean isValidContent(String msgContent) {
        return msgContent != null && !msgContent.isEmpty();
    }

    public boolean isValidMediaPath(String mediaURL) {
        // Other validation for media path
        return mediaURL != null && !mediaURL.isEmpty();
    }

    public String fileFormat(){
        if (mediaPath == null) {
            return  "TextMessage," + sender.getUsername() + "," + recipient.getUsername() + "," + timestamp + "," + status + "\n" + content + "\n";
        } else {
            return  "ImageMessage," + sender.getUsername() + "," + recipient.getUsername() + "," + timestamp + "," + status + "\n" + mediaPath + "\n";
        }
    }

    @Override
    public String toString() {
        if (mediaPath == null) {
            return "DirectMessage{" +
                    "sender=" + sender +
                    ", recipient=" + recipient +
                    ", content='" + content + '\'' +
                    ", timestamp=" + new Date(timestamp) +
                    ", status='" + status + '\'' +
                    '}';
        } else {
            return "DirectMessage{" +
                    "sender=" + sender +
                    ", recipient=" + recipient +
                    ", mediaPATH='" + mediaPath + '\'' +
                    ", timestamp=" + new Date(timestamp) +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
