package com.example.comp6442_group_assignment;

import static com.example.comp6442_group_assignment.Post.writeXml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * User class. This class is used to store the user information. It includes methods that read and write the user
 * information from and to persistent data.
 */
public class User implements Observer {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private List<String> notifications;

    private boolean publicProfile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public boolean isPublicProfile() { return publicProfile; }

    public void setPublicProfile(boolean publicProfile) { this.publicProfile = publicProfile; }

    public User(String userName, String password, String email, String firstName, String lastName, String phoneNumber, List<String> notifications, boolean publicProfile) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.notifications = notifications;
        this.publicProfile = publicProfile;
    }

    public User(String userName, String email, String firstName, String lastName, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void update(String postId, String message) throws ParserConfigurationException, IOException, SAXException {
        String update = message + postId;
        notifications.add(update);
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                user.getNotifications().add(update);
            }
        }
        writeToUser(users);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", notifications=" + notifications +
                ", publicProfile=" + publicProfile +
                '}';
    }

    public boolean isValid() throws ParserConfigurationException, IOException, SAXException {
        // Suppose to request to server to check if the user is valid
        // Now just read from Users.xml file
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Read a list of users from users.xml file, should be a server function
     *
     * @return list of users registered in the system
     */
    public static List<User> readUsers() throws ParserConfigurationException, IOException, SAXException {
        String testPath = "src/main/assets/user.xml"; // Just for test purpose
        String address = "app/src/main/assets/user.xml";
        File file = new File(address);
        if (!file.exists()) {
            file = new File(testPath);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        List<User> users = new LinkedList<>(); // rtn list of users

        NodeList userList = doc.getElementsByTagName("User");
        for (int i = 0; i < userList.getLength(); i++) {
            Node user = userList.item(i);
            if (user.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) user;
                String userName = userElement.getElementsByTagName("UserName").item(0).getTextContent();
                String password = userElement.getElementsByTagName("Password").item(0).getTextContent();
                String email = userElement.getElementsByTagName("Email").item(0).getTextContent();
                String firstName = userElement.getElementsByTagName("FirstName").item(0).getTextContent();
                String lastName = userElement.getElementsByTagName("LastName").item(0).getTextContent();
                String phoneNumber = userElement.getElementsByTagName("PhoneNumber").item(0).getTextContent();
                List<String> notifications = new LinkedList<>();
                NodeList notificationList = userElement.getElementsByTagName("Notification");
                for (int j = 0; j < notificationList.getLength(); j++) {
                    notifications.add(notificationList.item(j).getTextContent());
                }
                boolean publicProfile = Boolean.parseBoolean(userElement.getElementsByTagName("PublicProfile").item(0).getTextContent());
                User currentUser = new User(userName, password, email, firstName, lastName, phoneNumber, notifications, publicProfile);
                users.add(currentUser);
            }
        }
        return users;
    }

    /**
     * Check if the user is already registered in the system, should be a server function
     *
     * @param userName
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean isRegistered(String userName) throws ParserConfigurationException, IOException, SAXException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * directly write to the user.xml file, server side initialization function. This should probably never be
     * called alone.
     *
     * @param users
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void writeToUser(List<User> users) throws ParserConfigurationException, IOException, SAXException {
        boolean useTestPath = false; // Just for test purpose
        String testPath = "src/main/assets/user.xml";
        String address = "app/src/main/assets/user.xml";
        File file = new File(address);
        if (!file.exists()) {
            useTestPath = true;
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Users");
        doc.appendChild(rootElement);

        for (User u : users) {
            // user elements
            Element user = doc.createElement("User");
            rootElement.appendChild(user);

            // user name elements
            Element userName1 = doc.createElement("UserName");
            userName1.appendChild(doc.createTextNode(u.getUserName()));
            user.appendChild(userName1);

            // password elements
            Element password1 = doc.createElement("Password");
            password1.appendChild(doc.createTextNode(u.getPassword()));
            user.appendChild(password1);

            // email elements
            Element email1 = doc.createElement("Email");
            email1.appendChild(doc.createTextNode(u.getEmail()));
            user.appendChild(email1);

            // first name elements
            Element firstName1 = doc.createElement("FirstName");
            firstName1.appendChild(doc.createTextNode(u.getFirstName()));
            user.appendChild(firstName1);

            // last name elements
            Element lastName1 = doc.createElement("LastName");
            lastName1.appendChild(doc.createTextNode(u.getLastName()));
            user.appendChild(lastName1);

            // phone number elements
            Element phoneNumber1 = doc.createElement("PhoneNumber");
            phoneNumber1.appendChild(doc.createTextNode(u.getPhoneNumber()));
            user.appendChild(phoneNumber1);

            // notification elements
            Element notifications = doc.createElement("Notifications");
            user.appendChild(notifications);
            if (u.getNotifications() != null) {
                for (String n : u.getNotifications()) {
                    Element notification = doc.createElement("Notification");
                    notification.appendChild(doc.createTextNode(n));
                    notifications.appendChild(notification);
                }
            }

            // public profile elements
            Element publicProfile = doc.createElement("PublicProfile");
            publicProfile.appendChild(doc.createTextNode(Boolean.toString(u.isPublicProfile())));
            user.appendChild(publicProfile);
        }

        // write dom document to a file
        try {
            FileOutputStream output = null;
            if (useTestPath) {
                output = new FileOutputStream(testPath);
            } else {
                output = new FileOutputStream(address);
            }
            writeXml(doc, output);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a new user to users.xml file, should be a server function
     *
     * @param userName
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @return true if the user is successfully added to the system
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) throws ParserConfigurationException, IOException, SAXException {
        if (userName.length() < 4 || password.length() < 6) {
            return false;
        } else if (isRegistered(userName)) {
            return false;
        } else {
            List<User> users = readUsers();
            User newUser = new User(userName, password, email, firstName, lastName, phoneNumber, new LinkedList<>(), true);
            users.add(newUser);
            writeToUser(users);
            return true;
        }
    }

    /**
     * Delete a user with @param userName from users.xml file, should be a server function
     *
     * @param userName
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean deleteAccount(String userName) throws ParserConfigurationException, IOException, SAXException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                users.remove(user);
                break;
            }
        }
        writeToUser(users);
        return true;
    }

    /**
     * Update a user's information, should be a server function
     *
     * @param userName
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean updateAccount(String userName, String password, String email, String firstName, String lastName, String phoneNumber, boolean publicProfile) throws ParserConfigurationException, IOException, SAXException {
        if (!isRegistered(userName)) {
            System.out.println("User not found");
            return false;
        } else {
            List<User> users = readUsers();
            for (User user : users) {
                if (user.getUserName().equals(userName)) {
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPhoneNumber(phoneNumber);
                    user.setPublicProfile(publicProfile);
                    break;
                }
            }
            writeToUser(users);
            return true;
        }
    }

    /**
     * return the notification of the user with @param userName from users.xml file, should be a server function
     * @param userName
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static List<String> updateNotification(String userName) throws ParserConfigurationException, IOException, SAXException {
        List<String> notifications = new LinkedList<>();
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                notifications = user.getNotifications();
            }
        }
        return notifications; //returns a list of notifications
    }

    /**
     * Empty the notification list of a user, should be a server function
     * @param username
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean clearNotification(String username) throws ParserConfigurationException, IOException, SAXException {
        List<User> users = readUsers();
        List<String> empty = new LinkedList<>();
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                user.setNotifications(empty);
                writeToUser(users);
                return true;
            }
        }
        return false;
    }

    /**
     * request profile of another user, should be a server function
     * @param userName
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static User requestProfile(String userName) throws ParserConfigurationException, IOException, SAXException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.isPublicProfile()) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<User> users;
        users = readUsers();
//        UserSession userSession = new UserSession();
//        userSession.login("user1", "qwerty");
//        userSession.createPost("test post");
//        Post postCreated = userSession.allPosts().get(userSession.allPosts().size()-1);
//        userSession.commentPost(postCreated.getPostId(), "commented");
        System.out.println(users.get(0).getNotifications().size());
    }


}
