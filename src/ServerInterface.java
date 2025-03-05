import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Server Interface
 * <p>
 *
 * A class for the server interface
 *
 * @author Ian Oleinick, Shlok Anand, Brandon Qian, Lab 13
 * @version March 30th 2024
 */
public interface ServerInterface {

    void startServer();
    void closeServer();

}
