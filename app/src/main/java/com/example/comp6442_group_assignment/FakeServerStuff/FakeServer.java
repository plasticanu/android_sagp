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
                                System.out.println("Login request");
                                String[] tokens_li = msgFromClient.split(" ");
                                String userName_li = tokens_li[1];
                                String password_li = tokens_li[2];
                                try {
                                    if (userSession.login(userName_li, password_li)) {
                                        User user = userSession.user;
                                        msgToClient = "lis;" + user.getUserName() + ";" + user.getFirstName() + ";" + user.getLastName() + ";" + user.getEmail() + ";" + user.getPhoneNumber();
                                        System.out.println(userSession.getState());
                                    } else {
                                        msgToClient = "lif;Invalid username or password";
                                    }
                                } catch (ParserConfigurationException e) {
                                    e.printStackTrace();
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Login response...");
                                break;
                            case "lo":
                                System.out.println("Logout request");
                                if (userSession.logout()) {
                                    msgToClient = "los;Logged out";
                                    System.out.println(userSession.getState());
                                } else {
                                    msgToClient = "los;Not logged in";
                                }
                                System.out.println("Logout response...");
                                break;
                            case "hm":
                                System.out.println("Home request");
                                String[] tokens_hm = msgFromClient.split(" ");
                                int postNum_hm = Integer.parseInt(tokens_hm[1]); // post number
                                msgToClient = "hms";
                                List<Post> posts = Post.readFromPost();
                                List<Post> rtn_hm = posts.subList(posts.size() - postNum_hm - 10, posts.size() - postNum_hm);
                                for (Post post : rtn_hm) {
                                    msgToClient += ";" + post.toString();
                                }
                                System.out.println("Home response...");
                                break;
                            case "rg":
                                System.out.println("Register request");
                                String[] tokens_rg = msgFromClient.split(" ");
                                String userName_rg = tokens_rg[1];
                                String password_rg = tokens_rg[2];
                                String email_rg = tokens_rg[3];
                                String firstName_rg = tokens_rg[4];
                                String lastName_rg = tokens_rg[5];
                                String phoneNumber_rg = tokens_rg[6];
                                if (userSession.register(userName_rg, password_rg, email_rg, firstName_rg, lastName_rg, phoneNumber_rg)) {
                                    msgToClient = "rjs;" + userName_rg + ";" + firstName_rg + ";" + lastName_rg + ";" + email_rg + ";" + phoneNumber_rg;
                                    System.out.println(userSession.getState());
                                } else {
                                    msgToClient = "rjf;Register Failed. Username already exists. ";
                                }
                                System.out.println("Register response...");
                                break;
                            case "da":
                                System.out.println("Delete account request");
                                if (userSession.deleteAccount()) {
                                    msgToClient = "das;Account deleted";
                                    System.out.println(userSession.getState());
                                } else {
                                    msgToClient = "daf;Delete Failed.";
                                }
                                System.out.println("Delete account response...");
                                break;
                            case "cp":
                                System.out.println("Create Post request");
                                String content_cp = msgFromClient.substring(3);
                                Post post_cp = userSession.createPost(content_cp);
                                if (post_cp != null) {
                                    msgToClient = "cps;" + post_cp;
                                } else {
                                    msgToClient = "cpf;Create Post Failed.Not logged in. ";
                                }
                                System.out.println("Create Post response...");
                                break;
                            case "dp":
                                System.out.println("Delete Post request");
                                String[] tokens_dp = msgFromClient.split(" ");
                                String postId_dp = tokens_dp[1];
                                if (userSession.deletePost(postId_dp)) {
                                    msgToClient = "dps;Post deleted";
                                } else {
                                    msgToClient = "dpf;Delete Post Failed.Not logged in or deleting other's post. ";
                                }
                                System.out.println("Delete Post response...");
                                break;
                            case "ep":
                                System.out.println("Edit Post request");
                                String[] tokens_ep = msgFromClient.split(" ");
                                String postId_ep = tokens_ep[1];
                                String content_ep = msgFromClient.substring(12);
                                if (userSession.editPost(postId_ep, content_ep)) {
                                    msgToClient = "eps;Post edited";
                                } else {
                                    msgToClient = "epf;Edit Post Failed.Not logged in or editing other's post. ";
                                }
                                System.out.println("Edit Post response...");
                                break;
                            case "lp":
                                System.out.println("Like Posts request");
                                String[] tokens_lp = msgFromClient.split(" ");
                                String postId_lp = tokens_lp[1];
                                if (userSession.likePost(postId_lp)) {
                                    msgToClient = "lps;Post liked";
                                } else {
                                    msgToClient = "lpf;Like Post Failed.Not logged in or liking own post or already liked. ";
                                }
                                System.out.println("Like Posts response...");
                                break;
                            case "ul":
                                System.out.println("Unlike Posts request");
                                String[] tokens_ul = msgFromClient.split(" ");
                                String postId_ul = tokens_ul[1];
                                if (userSession.unlikePost(postId_ul)) {
                                    msgToClient = "uls;Post unliked";
                                } else {
                                    msgToClient = "ulf;Unlike Post Failed.Not logged in or unliking own post or already unliked. ";
                                }
                                System.out.println("Unlike Posts response...");
                                break;
                            case "cm":
                                System.out.println("Comment Post request");
                                String[] tokens_cm = msgFromClient.split(" ");
                                String postId_cm = tokens_cm[1];
                                String content_cm = msgFromClient.substring(12);
                                if (userSession.commentPost(postId_cm, content_cm)) {
                                    msgToClient = "cms;Post commented";
                                } else {
                                    msgToClient = "cmf;Comment Post Failed.Not logged in or post not exists. ";
                                }
                                System.out.println("Comment Post response...");
                                break;
                            case "pf":
                                System.out.println("Profile request");
                                User user_pf = userSession.profile();
                                if (user_pf != null) {
                                    msgToClient = "pfs;" + user_pf.getUserName() + ";" + user_pf.getFirstName() + ";" + user_pf.getLastName() + ";" + user_pf.getEmail() + ";" + user_pf.getPhoneNumber();
                                } else {
                                    msgToClient = "pff;Profile Failed.Not logged in. ";
                                }
                                System.out.println("Profile response...");
                                break;





                        }
                    }

                    bufferedWriter.write(msgToClient);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    System.out.println("Message sent: " + msgToClient);


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
