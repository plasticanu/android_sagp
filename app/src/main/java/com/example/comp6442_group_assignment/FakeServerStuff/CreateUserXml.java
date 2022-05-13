/**
 * This class was used for creating the XML file. It is officially abandoned.
 * The commented code is kept for future reference.
 * Nobody should use this class.
 */

//package com.example.comp6442_group_assignment.FakeServerStuff;
//
//import com.example.comp6442_group_assignment.User;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.*;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CreateUserXml {
//    // write doc to output stream
//    public static void writeXml(Document doc,
//                                OutputStream output)
//            throws TransformerException {
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(output);
//
//        transformer.transform(source, result);
//
//    }
//
//    public static void main(String[] args) throws ParserConfigurationException {
//        // Create a new user list
//        User user1 = new User("user1", "qwerty", "user1@dmail.com", "First1", "Last1", "12345678");
//        User user2 = new User("user2", "qwerty", "user2@dmail.com", "First2", "Last2", "12345678");
//        User user3 = new User("user3", "qwerty", "", "", "", "");
//        List<User> users = new ArrayList<>();
//        users.add(user1);
//        users.add(user2);
//        users.add(user3);
//
//        String address = "app/src/main/assets/user.xml";
//        File file = new File(address);
//        if (file.exists()) {
//            // If the file exist
//            System.out.println("File already exists, please delete first then modify. ");
//        } else {
//            // If the file does not exist
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//            // root elements
//            Document doc = docBuilder.newDocument();
//            Element rootElement = doc.createElement("Users");
//            doc.appendChild(rootElement);
//
//            for (User u : users) {
//                // user elements
//                Element user = doc.createElement("User");
//                rootElement.appendChild(user);
//
//                // user name elements
//                Element userName = doc.createElement("UserName");
//                userName.appendChild(doc.createTextNode(u.getUserName()));
//                user.appendChild(userName);
//
//                // password elements
//                Element password = doc.createElement("Password");
//                password.appendChild(doc.createTextNode(u.getPassword()));
//                user.appendChild(password);
//
//                // email elements
//                Element email = doc.createElement("Email");
//                email.appendChild(doc.createTextNode(u.getEmail()));
//                user.appendChild(email);
//
//                // first name elements
//                Element firstName = doc.createElement("FirstName");
//                firstName.appendChild(doc.createTextNode(u.getFirstName()));
//                user.appendChild(firstName);
//
//                // last name elements
//                Element lastName = doc.createElement("LastName");
//                lastName.appendChild(doc.createTextNode(u.getLastName()));
//                user.appendChild(lastName);
//
//                // phone number elements
//                Element phoneNumber = doc.createElement("PhoneNumber");
//                phoneNumber.appendChild(doc.createTextNode(u.getPhoneNumber()));
//                user.appendChild(phoneNumber);
//            }
//
//            // write dom document to a file
//            try (FileOutputStream output =
//                         new FileOutputStream(address)) {
//                writeXml(doc, output);
//            } catch (IOException | TransformerException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
