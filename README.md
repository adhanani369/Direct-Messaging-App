# Phase 1 ReadMe  
## Classes:  
- Database
- DirectMessage
- User
- UserMessageData

### DirectMessage:
The __DirectMessage__ class of each method that is sent that holds sender, recipient, time sent, and status.  
Interface:  

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

#### Get and Set Methods:  
Does as assumed (deliver(), markAsRead(), and delete() are setters).

#### Is Valid Content
Is a checker to see if the message being sent is an actual message that isn't null or empty.

#### Is Valid Media Path:
Is a checker to see if the message being sent is an image, will be implemented as photo messaging in the future.

### User:
The __User__ class is where we create and modify User objects.  
Interface:

    User(String username, String password, String bio);
    
    boolean isAdmin();
    boolean isFriendsOnlyChat();
    String getUsername();
    String getPassword();
    String getBio();
    ArrayList<User> getFriends();
    ArrayList<User> getBlocked();
    String getFriendsString();
    String getBlockedString();
    ArrayList<UserMessageData> getUserMessages();
    getPfp();

    void setAdmin();
    void setFriendsOnlyChat(boolean friendsOnlyChat);
    void setUsername(String username);
    void setPassword(String password);
    void setBio(String bio);
    void setFriends(ArrayList<User> friends);
    void setBlocked(ArrayList<User> blocked);
    void setUserMessages(ArrayList<UserMessageData> userMessages);
    void setPfp(String pfp);

    void friendUser(User user);
    void blockUser(User user);
    void unfriendUser(User user);
    void unblockUser(User user);
    void addUserMessageData(UserMessageData umd);

    String toString();
#### Constructor
Creates a new User object with either username and password or username, password, and bio.

#### Get and Set Methods:
Does as assumed.

#### Friend/Block and Unfriend/Unblock Methods:
The friend/block methods add the inputted User to the friends/blocked list of the current User.
The unfriend/unblock methods remove the inputted User from the friends/blocked list of the current User.

#### Add user message data:
Adds the input userMessageData to this user's messages

#### toString Method:
Does as assumed.

#### User Profile Picture:  
String representation of user's profile picture.


### UserMessageData:  
The __UserMessageData__ class holds all the messages between 2 users. It has an arraylist of all the messages between the two users.
Interface: 

    ArrayList<DirectMessage> getMessages();
    boolean isHasUpdated();
    void setHasUpdated(boolean hasUpdated);
    boolean isHidden();
    void setHidden(boolean hidden);
    void addMessage(DirectMessage message);
    void deleteMessage(DirectMessage message);
    DirectMessage getLatestMessage();
    String toString();

#### Get and Set Methods:
Does as assumed (isHasUpdated() and isHidden() are getters).

#### Add Message:
Adds the input message to this UserMessageData.

#### Delete Message:
Removes the input message from this UserMessageData.

#### toString Method:
Does as assumed.


### Database:
The __Database__ class is where most of our file IO methods are housed.  
Interface:

    void readUserFile();
    void readFriendsFile();
    void readBlockedFile();
    ArrayList<User> getUsers();
    boolean addUser(String username, String password, String bio);
    boolean adminLogin(String data);
    boolean login(String data);
    User userExists(String username);
    ArrayList<User> getUserFriends(User user);
    String[] getMessages(User user, User other);
    void writeUserFile();
    void writeFriendsFile();
    void writeBlockedFile();
    void writeMessagesFile();
    ArrayList<User> searchForUser(String searchTerm);
    ArrayList<User> getUsersWhoHaveSentNewMessage(User user);
    String deleteMessage(User user, User other, String message);

#### File Methods:  
The methods that have to do with reading and writing to different files are used to make sure the data can be recovered even when the server is rebooted. We primarily have 4 files where all of our data is stored.

#### Add users:  
The Add method is used exclusively when the server is adding a new user to the database list of users.

#### Get Users:
Returns an arraylist of all the users currently in the database.

#### Admin login:
Attempts to log in to an admin account.

#### Login:
Attempts to log in to any account.

#### User exist check:
Returns the user that has the inputted username.

#### Get messages:
Returns all the messages between two users.

#### Search for user:
Returns an arraylist of all the users whose username contains the searchTerm.

#### Get users who have sent new message:
Returns an arraylist of user who have sent the input user a message that has not been read yet

#### Delete message:
Finds the message between two users that matches the input string. Returns true if it finds the message and false if it doesn't. 

## Run Local tests:
The RunLocalTest class contains test cases to verify the functionality of various methods in the project classes. 

### Classes Tested
The following classes are tested in the RunLocalTest class:
- DirectMessage
- UserMessageData
- User
- Database
### Test Cases
#### DirectMessage Class Methods Verification
- __Objective:__ Ensure that the methods in the DirectMessage class function correctly.
- Test Cases:
  - Constructor Tests: Verify the constructors with text and media content.
  - Setter and Getter Tests: Test the setter and getter methods for all attributes. 
  - Message Delivery Test: Test the delivery of messages. 
  - Mark as Read Test: Verify the functionality to mark messages as read. 
  - Deletion Test: Test the deletion of messages.
  
#### UserMessageData Class Methods Verification
- __Objective:__ Verify the functionality of methods in the UserMessageData class.
- Test Cases:
  - Adding and Getting Messages Test: Test adding messages and retrieving them from UserMessageData. 
  - Latest Message Test: Verify the functionality to get the latest message. 
  - Has Updated Test: Test setting and getting the update status. 
  - Is Hidden Test: Verify setting and getting the hidden status.
  
#### User Class Fields Verification
- __Objective__: Ensure that fields in the User class are initialized and accessed correctly.
- Test Cases:
  - Username Field Test: Verify the initialization and access of the username field.
  - Password Field Test: Verify the initialization and access of the password field.
  - Bio Field Test: Verify the initialization and access of the bio field.
  - Friends Field Test: Verify the initialization and access of the friends field.
  - Blocked Field Test: Verify the initialization and access of the blocked field.
  - User Messages Field Test: Verify the initialization and access of the userMessages field.
  
#### User Class Methods Verification
- __Objective:__ Verify the functionality of methods in the User class.
- Test Cases:
  - Get Username Test: Test the getUsername method.
  - Get Password Test: Test the getPassword method.
  - Get Bio Test: Test the getBio method.
  - Get Friends and Blocked Test: Verify the getFriends and getBlocked methods after initialization.
  - Set and Get User Messages Test: Test setting and getting user messages.
  - Friend and Block User Test: Verify the functionality to add friends and block users.
  - Unfriend and Unblock User Test: Test removing friends and unblocking users.

#### Database Class Methods Verification
- __Objective:__ Ensure that the methods in the Database class function correctly.
- Test Cases:
  - Read User File Test: Verify the reading of user data from a file.
  - Add User Test: Test adding a new user.
  - Search For User Test: Verify the search functionality for users.
  - Login Test: Test user login functionality.
  - Delete This User Test: Verify the deletion of a user.
  - Write User File Test: Test writing user data to a file.
 
# Phase 2 ReadMe:
## Classes:  
- Server
    - SUBCLASS: ClientHandler
- Client


### Server:

    public Server(ServerSocket serverSocket)
    void startServer()
    void closeServer()
    void closeEverything(Socket sock, BufferedReader reader, BufferedWriter writer)
    main Method
    run Method
    class ClientHandler implements Runnable
        ClientHandler(Socket socket)

#### public Server(ServerSocket serverSocket)
Sets the server's ServerSocket to the input socket and sets the server's Database to a new empty database.

#### startServer()
This method is run in the main method when the server is first started up. It reads all the files, creates the database, and then waits for clients to connect. When a client connects it creates a new ClientHandler object for that client and then creates a thread with that ClientHandler object and runs its run() method.

#### closeServer()
Closes the server

#### closeEverything(Socket sock, BufferedReader reader, BufferedWriter writer)
Closes the connection with the input client socket and its readers and writers.

#### main Method
Creates the server socket and then calls startServer().

#### run Method
After the client connects the client should send an action request to the server through the socket and this run method will read that request and attempt to perform the respective function.

#### Client to Server Function Requests
- registerNewUser
  - Attempts to create a new user in the database with the parameters received from the client. If successful, sends success message to the client. If not, sends failure message.
- tryLogin
  - Attempts to log in with the parameters received from the client using database's login method. If successful, sends success message to the client. If not, sends failure message.
- friendsOnlyOrNot
  - Checks if the user with the username sent by the client has the setting for message friends only turned off or on and returns true or false based on that.
- sendMessage
  - Attempts to create a message from one user to the other. Checks blocked and friends conditions for messaging. If successful, sends success message to the client. If not, sends failure message along with the cause of failure.
- displayMessageHistoryWithUser
  - Sends the message history between two users to the client. If there is no message history then sends "None".
- deleteMessage
  - Attempts to find and delete the message between two users based off client input of the client's user, the other user, and the message. Returns true if successful and false if not.
- uploadProfilePicture
  - Attempts to set the client's user's profile picture to the one received from the client. If successful, sends success message. If not, sends failure message.
- modifyBio
  - Attempts to set the client's user's bio to the one received from the client. If successful, sends success message. If not, sends failure message.
- getFriends
  - Attempts to return all the friends of the client's user. If no friends return "None".
- getUsers
  - Attempts to return all the users currently in the database. If there are no users return "None".
- checkForNewMessages
  - Attempts to return all the unread messages the client's user has. If there are no unread messages return "None".
- tryAdminLogin
  - Attempts to log in to an admin account using login credentials provided by the client. If successful, return true. If not, return false.
- adminShutDown
  - Closes the server and ends the server run method.
- exit
  - Closes the server's connection to the client that requested this function.

  
### subclass ClientHandler implements Runnable

#### ClientHandler(Socket socket)
Sets this ClientHandler's socket to the input socket, creates the readers and writers for the socket, and then adds this ClientHandler to the static arraylist of ClientHandlers.


### Client:

First off, the client.java can be run as many times as you want, each will create a new thread connected to the server. When creating this class, we wanted to make it easier on ourselves by creating seperate methods to be a quick shortcut when implementing a future GUI. For the purpose of this project and grading, I will give a basic summary of each method, and how they are used in the main method of the client

    public Client(Socket socket)
    void closeEverything(Socket socket, BufferedReader bfr, BufferedWriter bfw)
    String[] getUsers()
    String[] getFriends(String username)
    boolean friendsOnlyOrNot(String username)
    boolean tryLogin(User user)
    boolean tryLoginAsAdmin(String username, String password)
    boolean sendMessage(User userToSendTo, String message)
    void logOut()
    void adminShutDown(String username)
    void exitClient()
    boolean deleteMessage(String user, String other, String message)
    String[] newMessages(String username)
    void displayMessageHistoryWithUser(String username)
    boolean registerNewUser(String username, String password, String bio)
    boolean uploadProfilePicture(String username, String pfpPath)
    boolean modifyBio(String username, String newBio)
    main Method

#### public Client(Socket socket)
Sets this client's socket to the input socket and sets the readers and writers for that socket. 

#### void closeEverything(Socket socket, BufferedReader bfr, BufferedWriter bfw)
Closes the client's socket connections to the server and ends the client main method.

#### String[] getUsers()
Asks the server to perform the getUsers function. Returns a String array of all users currently in the server database. If there are no users then the array contains "None".

#### String[] getFriends(String username)
Asks the server to perform the getFriends function and sends the required data to do so. Returns a String array of all the user's friends. If there are no friends then the array contains "None".

#### boolean friendsOnlyOrNot(String username)
Asks the server to perform the friendsOnlyOrNot function and sends the required data to do so. Returns true if the user has message friends only setting turned on and false if not.

#### String tryLogin(User user)
Asks the server to perform the tryLogin function and sends the required data to do so. Returns the server's confirmation message.

#### boolean tryLoginAsAdmin(String username, String password)
Asks the server to perform the tryLoginAsAdmin function and sends the required data to do so. Returns the server's confirmation message.

#### boolean sendMessage(User userToSendTo, String message)
Asks the server to perform the sendMessage function and sends the required data to do so. If the server function was successful, then returns true. If not, then displays a GUI informing the user and returns false.   

#### void logOut()
Logs the current user out of the client and takes them back to the select operation page.

#### void adminShutDown(String username)
Asks the server to perform the adminShutdown function and closes the client.

#### void exitClient()
Asks the server to perform the exit function and closes the client.

#### boolean deleteMessage(String user, String other, String message)
Asks the server to perform the deleteMessage function and sends the required data to do so. If successful, return true. If not, return false.

#### String[] newMessages(String username)
Asks the server to perform the checkForNewMessages function and sends the required data to do so. Returns a string array of all the new messages the client user has. If there are no new messages the array contains "None".

#### void displayMessageHistoryWithUser(String username)
Gives the user a dropdown list of users to choose from. If the user has message friends only turned on only list friends. Takes the users dropdown input and displays the message history between two users. If there is no message history, inform the user with a GUI.

#### boolean registerNewUser(String username, String password, String bio)
Asks the server to perform the registerNewUser function and sends the required data to do so. Return true if the server function was successful and false if not.

#### boolean uploadProfilePicture(String username, String pfpPath)
Asks the server to perform the uploadProfilePicture function and sends the required data to do so. Returns true if the server function was successful and false if not.

#### boolean modifyBio(String username, String newBio)
Asks the server to perform the modifyBio function and sends the required data to do so. If successful, return true. If not, return false.

#### main Method
Initiates a connection between this client socket and the server socket. Then opens the select operation page until the user exits or shuts down the server. When the user inputs any of the operations and enters valid inputs, this method will send the request for the respective server function to the server and receives the output of those functions.