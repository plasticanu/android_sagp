package com.example.comp6442_group_assignment.FakeServerStuff;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.util.List;

/**
 * FakeServer class is used to simulate the server. It is totally runnable but only two thread can be run at a time for now.
 * One for android testing and one fake client will be run in pc to simulate new data stream.
 */
public class FakeServer {
    public static void main(String[] args) throws IOException {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                InputStreamReader inputStreamReader = null;
                OutputStreamWriter outputStreamWriter = null;
                BufferedReader bufferedReader = null;
                BufferedWriter bufferedWriter = null;
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(6060);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                while (true) {

                    try {
                        UserSession userSession = new UserSession();

                        System.out.println(userSession.getState());

                        assert serverSocket != null;
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
                                    case "ap":
                                        System.out.println("All Posts request");
                                        String[] tokens_ap = msgFromClient.split(" ");
                                        int postNum_ap = Integer.parseInt(tokens_ap[1]);
                                        List<Post> posts_ap = userSession.allPosts();
                                        if (posts_ap != null) {
                                            List<Post> rtnPosts_ap = posts_ap.subList(postNum_ap, postNum_ap + 10);
                                            msgToClient = "aps";
                                            for (Post post : rtnPosts_ap) {
                                                msgToClient += ";" + post.toString();
                                            }
                                        } else {
                                            msgToClient = "apf;All Posts Failed.Not logged in or user not exists. ";
                                        }
                                        System.out.println("All Posts response...");
                                        break;
                                    case "sr":
                                        System.out.println("Search request");
                                        String search_sr = msgFromClient.substring(3);
                                        List<Post> posts_sr = userSession.search(search_sr);
                                        if (posts_sr != null) {
                                            msgToClient = "srs";
                                            for (Post post : posts_sr) {
                                                msgToClient += ";" + post.toString();
                                            }
                                        } else {
                                            msgToClient = "srf;Search Failed.Not logged in. ";
                                        }
                                        System.out.println("Search response...");
                                        break;
                                    case "ua":
                                        System.out.println("Update Account request");
                                        String[] tokens_ua = msgFromClient.split(" ");
                                        String userName_ua = tokens_ua[1];
                                        String password_ua = tokens_ua[2];
                                        String email_ua = tokens_ua[3];
                                        String FirstName_ua = tokens_ua[4];
                                        String LastName_ua = tokens_ua[5];
                                        String PhoneNumber_ua = tokens_ua[6];
                                        if (userSession.updateProfile(userName_ua, password_ua, email_ua, FirstName_ua, LastName_ua, PhoneNumber_ua)) {
                                            msgToClient = "uas;" + userName_ua + ";" + FirstName_ua + ";" + LastName_ua + ";" + email_ua + ";" + PhoneNumber_ua;
                                        } else {
                                            msgToClient = "uaf;Update Account Failed.Not logged in or user not exists. ";
                                        }
                                        System.out.println("Update Account response...");
                                        break;
                                    case "fp":
                                        System.out.println("Follow Post request");
                                        String[] tokens_fp = msgFromClient.split(" ");
                                        String postId_fp = tokens_fp[1];
                                        if (userSession.followPost(postId_fp)) {
                                            msgToClient = "fps;Post followed";
                                        } else {
                                            msgToClient = "fpf;Follow Post Failed.Not logged in or already followed. ";
                                        }
                                        System.out.println("Follow Post response...");
                                        break;
                                    case "uf":
                                        System.out.println("Unfollow Post request");
                                        String[] tokens_uf = msgFromClient.split(" ");
                                        String postId_uf = tokens_uf[1];
                                        if (userSession.unfollowPost(postId_uf)) {
                                            msgToClient = "ufs;Post unfollowed";
                                        } else {
                                            msgToClient = "uff;Unfollow Post Failed.Not logged in or already unfollowed. ";
                                        }
                                        System.out.println("Unfollow Post response...");
                                        break;
                                    case "un":
                                        System.out.println("Update Notification request");
                                        if (userSession.updateNotifications() != null) {
                                            msgToClient = "uns;";
                                            for (String notification : userSession.updateNotifications()) {
                                                msgToClient += notification + ";";
                                            }
                                        } else {
                                            msgToClient = "unf;Update Notification Failed.Not logged in. ";
                                        }
                                        System.out.println("Update Notification response...");
                                        break;
                                    case "cn":
                                        System.out.println("Clear Notification request");
                                        if (userSession.clearNotifications()) {
                                            msgToClient = "cns;Notifications cleared";
                                        } else {
                                            msgToClient = "cnf;Clear Notification Failed.Not logged in. ";
                                        }
                                        System.out.println("Clear Notification response...");
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
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (inputStreamReader != null) {
                            try {
                                inputStreamReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                InputStreamReader inputStreamReader = null;
                OutputStreamWriter outputStreamWriter = null;
                BufferedReader bufferedReader = null;
                BufferedWriter bufferedWriter = null;
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(6070);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                while (true) {

                    try {
                        UserSession userSession = new UserSession();

                        System.out.println(userSession.getState());

                        assert serverSocket != null;
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
                                    case "ap":
                                        System.out.println("All Posts request");
                                        String[] tokens_ap = msgFromClient.split(" ");
                                        int postNum_ap = Integer.parseInt(tokens_ap[1]);
                                        List<Post> posts_ap = userSession.allPosts();
                                        if (posts_ap != null) {
                                            List<Post> rtnPosts_ap = posts_ap.subList(postNum_ap, postNum_ap + 10);
                                            msgToClient = "aps";
                                            for (Post post : rtnPosts_ap) {
                                                msgToClient += ";" + post.toString();
                                            }
                                        } else {
                                            msgToClient = "apf;All Posts Failed.Not logged in or user not exists. ";
                                        }
                                        System.out.println("All Posts response...");
                                        break;
                                    case "sr":
                                        System.out.println("Search request");
                                        String search_sr = msgFromClient.substring(3);
                                        List<Post> posts_sr = userSession.search(search_sr);
                                        if (posts_sr != null) {
                                            msgToClient = "srs";
                                            for (Post post : posts_sr) {
                                                msgToClient += ";" + post.toString();
                                            }
                                        } else {
                                            msgToClient = "srf;Search Failed.Not logged in. ";
                                        }
                                        System.out.println("Search response...");
                                        break;
                                    case "ua":
                                        System.out.println("Update Account request");
                                        String[] tokens_ua = msgFromClient.split(" ");
                                        String userName_ua = tokens_ua[1];
                                        String password_ua = tokens_ua[2];
                                        String email_ua = tokens_ua[3];
                                        String FirstName_ua = tokens_ua[4];
                                        String LastName_ua = tokens_ua[5];
                                        String PhoneNumber_ua = tokens_ua[6];
                                        if (userSession.updateProfile(userName_ua, password_ua, email_ua, FirstName_ua, LastName_ua, PhoneNumber_ua)) {
                                            msgToClient = "uas;" + userName_ua + ";" + FirstName_ua + ";" + LastName_ua + ";" + email_ua + ";" + PhoneNumber_ua;
                                        } else {
                                            msgToClient = "uaf;Update Account Failed.Not logged in or user not exists. ";
                                        }
                                        System.out.println("Update Account response...");
                                        break;
                                    case "fp":
                                        System.out.println("Follow Post request");
                                        String[] tokens_fp = msgFromClient.split(" ");
                                        String postId_fp = tokens_fp[1];
                                        if (userSession.followPost(postId_fp)) {
                                            msgToClient = "fps;Post followed";
                                        } else {
                                            msgToClient = "fpf;Follow Post Failed.Not logged in or already followed. ";
                                        }
                                        System.out.println("Follow Post response...");
                                        break;
                                    case "uf":
                                        System.out.println("Unfollow Post request");
                                        String[] tokens_uf = msgFromClient.split(" ");
                                        String postId_uf = tokens_uf[1];
                                        if (userSession.unfollowPost(postId_uf)) {
                                            msgToClient = "ufs;Post unfollowed";
                                        } else {
                                            msgToClient = "uff;Unfollow Post Failed.Not logged in or already unfollowed. ";
                                        }
                                        System.out.println("Unfollow Post response...");
                                        break;
                                    case "un":
                                        System.out.println("Update Notification request");
                                        if (userSession.updateNotifications() != null) {
                                            msgToClient = "uns;";
                                            for (String notification : userSession.updateNotifications()) {
                                                msgToClient += notification + ";";
                                            }
                                        } else {
                                            msgToClient = "unf;Update Notification Failed.Not logged in. ";
                                        }
                                        System.out.println("Update Notification response...");
                                        break;
                                    case "cn":
                                        System.out.println("Clear Notification request");
                                        if (userSession.clearNotifications()) {
                                            msgToClient = "cns;Notifications cleared";
                                        } else {
                                            msgToClient = "cnf;Clear Notification Failed.Not logged in. ";
                                        }
                                        System.out.println("Clear Notification response...");
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
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (inputStreamReader != null) {
                            try {
                                inputStreamReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
