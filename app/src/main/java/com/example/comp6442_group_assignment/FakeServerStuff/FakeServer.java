package com.example.comp6442_group_assignment.FakeServerStuff;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
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

                    if (msgFromClient.substring(0, 2).equals("li")) {
                        String[] tokens = msgFromClient.split(" ");
                        String userName = tokens[1];
                        String password = tokens[2];
                        try {
                            if (userSession.login(userName, password)) {
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
                    }
                    else if (msgFromClient.substring(0, 2).equals("lo")) {
                        if (userSession.logout()) {
                            msgToClient = "los;Logged out";
                            System.out.println(userSession.getState());
                        } else {
                            msgToClient = "los;Not logged in";
                        }
                    }

                    bufferedWriter.write(msgToClient);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                }

            } catch (IOException e) {
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
