import java.util.ArrayList;

/**
 * User Message Data Interface
 * <p>
 * Interface for user message data class.
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public interface UserMessageDataHandler {
    ArrayList<DirectMessage> getMessages();

    boolean isHasUpdated();

    void setHasUpdated(boolean hasUpdated);

    boolean isHidden();

    void setHidden(boolean hidden);

    void addMessage(DirectMessage message);

    DirectMessage getLatestMessage();
}
