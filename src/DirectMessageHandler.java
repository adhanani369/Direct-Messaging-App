/**
 * Direct Message Interface
 * <p>
 * Interface for direct message class.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public interface DirectMessageHandler {
    User getSender();

    void setSender(User sender);

    User getRecipient();

    void setRecipient(User recipient);

    String getContent();

    void setContent(String content);

    String getMediaPath();

    void setMediaPath(String mediaPath);

    long getTimestamp();

    void setTimestamp(long timestamp);

    String getStatus();

    void setStatus(String status);

    void deliver();

    void markAsRead();

    void delete();

    boolean isValidContent(String content);

    boolean isValidMediaPath(String mediaPath);
}
