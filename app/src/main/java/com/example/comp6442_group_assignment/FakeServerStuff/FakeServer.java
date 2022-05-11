package com.example.comp6442_group_assignment.FakeServerStuff;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.util.List;

public class FakeServer {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ServerSocket serverSocket = null;

        serverSocket = new ServerSocket(6060);

        while (true) {

            try {
                UserSession userSession = new UserSession();

                System.out.println(userSession.getState());

                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true) {
                    String msgFromClient = bufferedReader.readLine();
                    System.out.println("Client: " + msgFromClient);
                    String msgToClient = "";

                    if (msgFromClient != null && msgFromClient.length() >= 2) {
                        switch (msgFromClient.substring(0, 2)) {
                            case "li":
                                String[] tokens_li = msgFromClient.split(" ");
                                String userName_li = tokens_li[1];
                                String password_li = tokens_li[2];
                                try {
                                    if (userSession.login(userName_li, password_li)) {
                                        User user = userSession.user;
                                        msgToClient = "lis;" + user.getUserName() + ";" + user.getPassword() + ";" + user.getFirstName() + ";" + user.getLastName() + ";" + user.getEmail() + ";" + user.getPhoneNumber();
                                        System.out.println(userSession.getState());
                                    } else {
                                        msgToClient = "lif;Invalid username or password";
                                    }
                                } catch (ParserConfigurationException e) {
                                    e.printStackTrace();
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "lo":
                                if (userSession.logout()) {
                                    msgToClient = "los;Logged out";
                                    System.out.println(userSession.getState());
                                } else {
                                    msgToClient = "los;Not logged in";
                                }
                                break;
                            case "hm":
                                msgToClient = "hms";
                                List<Post> posts = Post.readFromPost();
                                for (Post post : posts) {
                                    msgToClient += ";" + post.toString();
                                }
                                break;
                            case "rg":
                                String[] tokens_rg = msgFromClient.split(" ");
                                String userName_rg = tokens_rg[1];
                                String password_rg = tokens_rg[2];
                                String email_rg = tokens_rg[3];
                                String firstName_rg = tokens_rg[4];
                                String lastName_rg = tokens_rg[5];
                                String phoneNumber_rg = tokens_rg[6];
                                if (userSession.register(userName_rg, password_rg, email_rg, firstName_rg, lastName_rg, phoneNumber_rg)) {
                                    msgToClient = "rjs;" + userName_rg + ";" + password_rg + ";" + firstName_rg + ";" + lastName_rg + ";" + email_rg + ";" + phoneNumber_rg;
                                    System.out.println(userSession.getState());
                                } else {
                                    msgToClient = "rjf;Register Failed. Username already exists. ";
                                }
                                break;
                            case "da":
                                if (userSession.deleteAccount()) {
                                    msgToClient = "das;Account deleted";
                                    System.out.println(userSession.getState());
                                } else {
                                    msgToClient = "daf;Delete Failed.";
                                }
                        }
                    }

                    bufferedWriter.write(msgToClient);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                }

            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            }
        }

    }
}
