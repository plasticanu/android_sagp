package com.example.comp6442_group_assignment;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.comp6442_group_assignment.FakeServerStuff.CreateUserXml.writeXml;

public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

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

    public User(String userName, String password, String email, String firstName, String lastName, String phoneNumber) {
        this.userName = userName;
        this.password = password;
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

    @Override @NotNull
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
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
     * @return list of users registered in the system
     */
    public static List<User> readUsers() throws ParserConfigurationException, IOException, SAXException {
        String address = "app/src/main/assets/user.xml";
        File file = new File(address);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        List<User> users = new ArrayList<>(); // rtn list of users

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
                User currentUser = new User(userName, password, email, firstName, lastName, phoneNumber);
                users.add(currentUser);
            }
        }
        return users;
    }

    /**
     * Check if the user is already registered in the system, should be a server function
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

    public static void writeToUser(List<User> users) throws ParserConfigurationException, IOException, SAXException {
        String address = "app/src/main/assets/user.xml";
        File file = new File(address);

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
        }

        // write dom document to a file
        try (FileOutputStream output =
                     new FileOutputStream(address)) {
            writeXml(doc, output);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a new user to users.xml file, should be a server function
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
            User newUser = new User(userName, password, email, firstName, lastName, phoneNumber);
            users.add(newUser);
            writeToUser(users);
            return true;
        }
    }

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
}
