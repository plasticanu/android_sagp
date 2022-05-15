package com.example.comp6442_group_assignment.FakeServerStuff;
import android.annotation.SuppressLint;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.Search.Search;
import com.example.comp6442_group_assignment.State.LoggedInState;
import com.example.comp6442_group_assignment.State.UserState;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.util.List;

/**
 * FakeServer class is used to simulate the server. It is totally runnable but only three threads can be run at a time for now.
 * One for android testing, one fake client will be run in pc to simulate other user input, and one is the automatic
 * client that will simulate a stream of data being sent to the server by other users.
 */
public class FakeServer {
    public static Search searchEngine;
    public static List<Post> posts;

    private FakeServer() throws ParserConfigurationException, IOException, SAXException {
        searchEngine = Search.getInstance();
        posts = Post.readFromPost();
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        // Initialize the search engine and posts list
        FakeServer server = new FakeServer();

        // Three threads are currently identical to each other but their connecting socket.
        Thread thread1 = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                            String msgToClient = getResponse(msgFromClient, userSession, searchEngine, posts);

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                            String msgToClient = getResponse(msgFromClient, userSession, searchEngine, posts);

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

        Thread thread3 = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                Socket socket = null;
                InputStreamReader inputStreamReader = null;
                OutputStreamWriter outputStreamWriter = null;
                BufferedReader bufferedReader = null;
                BufferedWriter bufferedWriter = null;
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(6080);
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
                            String msgToClient = getResponse(msgFromClient, userSession, searchEngine, posts);

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

        Thread thread4 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        posts = Post.readFromPost();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        thread1.start();
        thread2.start();
        thread3.start();
    }

    /**
     * This method is used to parse the client request message and return the response message. It will also update the user session state.
     * @param request
     * @param userSession
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getResponse(String request, UserSession userSession, Search searchEngine, List<Post> posts) throws IOException, ParserConfigurationException, SAXException {
        String response = "";
        if (request != null && request.length() >= 2) {
            switch (request.substring(0, 2)) {
                case "li":
                    System.out.println("Login request");
                    String[] tokens_li = request.split(" ");
                    String userName_li = tokens_li[1];
                    String password_li = tokens_li[2];
                    try {
                        if (userSession.login(userName_li, password_li)) {
                            User user = userSession.user;
                            response = "lis;" + user.getUserName() + ";" + user.getFirstName() + ";" + user.getLastName() + ";" + user.getEmail() + ";" + user.getPhoneNumber();
                            System.out.println(userSession.getState());
                        } else {
                            response = "lif;Invalid username or password or already logged in. ";
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
                        response = "los;Logged out";
                        System.out.println(userSession.getState());
                    } else {
                        response = "los;Not logged in";
                    }
                    System.out.println("Logout response...");
                    break;
                case "hm":
                    System.out.println("Home request");
                    String[] tokens_hm = request.split(" ");
                    int postNum_hm = Integer.parseInt(tokens_hm[1]); // post number
                    response = "hms";
                    List<Post> rtn_hm = posts.subList(posts.size() - postNum_hm - 10, posts.size() - postNum_hm);
                    for (Post post : rtn_hm) {
                        response += ";" + post.toString();
                    }
                    System.out.println("Home response...");
                    break;
                case "rg":
                    System.out.println("Register request");
                    String[] tokens_rg = request.split(" ");
                    String userName_rg = tokens_rg[1];
                    String password_rg = tokens_rg[2];
                    String email_rg = tokens_rg[3];
                    String firstName_rg = tokens_rg[4];
                    String lastName_rg = tokens_rg[5];
                    String phoneNumber_rg = tokens_rg[6];
                    if (userSession.register(userName_rg, password_rg, email_rg, firstName_rg, lastName_rg, phoneNumber_rg)) {
                        response = "rjs;" + userName_rg + ";" + firstName_rg + ";" + lastName_rg + ";" + email_rg + ";" + phoneNumber_rg;
                        System.out.println(userSession.getState());
                    } else {
                        response = "rjf;Register Failed. Username already exists. ";
                    }
                    System.out.println("Register response...");
                    break;
                case "da":
                    System.out.println("Delete account request");
                    if (userSession.deleteAccount()) {
                        response = "das;Account deleted";
                        System.out.println(userSession.getState());
                    } else {
                        response = "daf;Delete Failed.";
                    }
                    System.out.println("Delete account response...");
                    break;
                case "cp":
                    System.out.println("Create Post request");
                    String content_cp = request.substring(3);
                    Post post_cp = userSession.createPost(content_cp);
                    if (post_cp != null) {
                        response = "cps;" + post_cp;
                    } else {
                        response = "cpf;Create Post Failed.Not logged in. ";
                    }
                    System.out.println("Create Post response...");
                    break;
                case "dp":
                    System.out.println("Delete Post request");
                    String[] tokens_dp = request.split(" ");
                    String postId_dp = tokens_dp[1];
                    if (userSession.deletePost(postId_dp)) {
                        response = "dps;Post deleted";
                    } else {
                        response = "dpf;Delete Post Failed.Not logged in or deleting other's post. ";
                    }
                    System.out.println("Delete Post response...");
                    break;
                case "ep":
                    System.out.println("Edit Post request");
                    String[] tokens_ep = request.split(" ");
                    String postId_ep = tokens_ep[1];
                    String content_ep = request.substring(12);
                    if (userSession.editPost(postId_ep, content_ep)) {
                        response = "eps;Post edited";
                    } else {
                        response = "epf;Edit Post Failed.Not logged in or editing other's post. ";
                    }
                    System.out.println("Edit Post response...");
                    break;
                case "lp":
                    System.out.println("Like Posts request");
                    String[] tokens_lp = request.split(" ");
                    String postId_lp = tokens_lp[1];
                    if (userSession.likePost(postId_lp)) {
                        response = "lps;Post liked";
                    } else {
                        response = "lpf;Like Post Failed.Not logged in or liking own post or already liked, or liking own post. ";
                    }
                    System.out.println("Like Posts response...");
                    break;
                case "ul":
                    System.out.println("Unlike Posts request");
                    String[] tokens_ul = request.split(" ");
                    String postId_ul = tokens_ul[1];
                    if (userSession.unlikePost(postId_ul)) {
                        response = "uls;Post unliked";
                    } else {
                        response = "ulf;Unlike Post Failed.Not logged in or unliking own post or already unliked. ";
                    }
                    System.out.println("Unlike Posts response...");
                    break;
                case "cm":
                    System.out.println("Comment Post request");
                    String[] tokens_cm = request.split(" ");
                    String postId_cm = tokens_cm[1];
                    String content_cm = request.substring(12);
                    if (userSession.commentPost(postId_cm, content_cm)) {
                        response = "cms;Post commented";
                    } else {
                        response = "cmf;Comment Post Failed.Not logged in or post not exists. ";
                    }
                    System.out.println("Comment Post response...");
                    break;
                case "pf":
                    System.out.println("Profile request");
                    User user_pf = userSession.profile();
                    if (user_pf != null) {
                        response = "pfs;" + user_pf.getUserName() + ";" + user_pf.getFirstName() + ";" + user_pf.getLastName() + ";" + user_pf.getEmail() + ";" + user_pf.getPhoneNumber();
                    } else {
                        response = "pff;Profile Failed.Not logged in. ";
                    }
                    System.out.println("Profile response...");
                    break;
                case "ap":
                    System.out.println("All Posts request");
                    String[] tokens_ap = request.split(" ");
                    int postNum_ap = Integer.parseInt(tokens_ap[1]);
                    List<Post> posts_ap = userSession.allPosts();
                    if (posts_ap != null) {
                        List<Post> rtnPosts_ap = posts_ap.subList(postNum_ap, postNum_ap + 10);
                        response = "aps";
                        for (Post post : rtnPosts_ap) {
                            response += ";" + post.toString();
                        }
                    } else {
                        response = "apf;All Posts Failed.Not logged in or user not exists. ";
                    }
                    System.out.println("All Posts response...");
                    break;
                case "sr":
                    System.out.println("Search request");
                    String search_sr = request.substring(3);
                    if (userSession.user != null) {
                        response = "srs";
                        @SuppressLint({"NewApi", "LocalSuppress"}) List<Post> posts_sr = searchEngine.search(search_sr);
                        for (Post postId : posts_sr) {
                            response += ";" + postId.toString();
                        }
                    }else {
                        response = "srf;Search Failed.Not logged in. ";
                    }
                    System.out.println("Search response...");
                    break;
                case "ua":
                    System.out.println("Update Account request");
                    String[] tokens_ua = request.split(" ");
                    String userName_ua = tokens_ua[1];
                    String password_ua = tokens_ua[2];
                    String email_ua = tokens_ua[3];
                    String FirstName_ua = tokens_ua[4];
                    String LastName_ua = tokens_ua[5];
                    String PhoneNumber_ua = tokens_ua[6];
                    if (userSession.updateProfile(userName_ua, password_ua, email_ua, FirstName_ua, LastName_ua, PhoneNumber_ua)) {
                        response = "uas;" + userName_ua + ";" + FirstName_ua + ";" + LastName_ua + ";" + email_ua + ";" + PhoneNumber_ua;
                    } else {
                        response = "uaf;Update Account Failed.Not logged in or user not exists. ";
                    }
                    System.out.println("Update Account response...");
                    break;
                case "fp":
                    System.out.println("Follow Post request");
                    String[] tokens_fp = request.split(" ");
                    String postId_fp = tokens_fp[1];
                    if (userSession.followPost(postId_fp)) {
                        response = "fps;Post followed";
                    } else {
                        response = "fpf;Follow Post Failed.Not logged in or already followed. ";
                    }
                    System.out.println("Follow Post response...");
                    break;
                case "uf":
                    System.out.println("Unfollow Post request");
                    String[] tokens_uf = request.split(" ");
                    String postId_uf = tokens_uf[1];
                    if (userSession.unfollowPost(postId_uf)) {
                        response = "ufs;Post unfollowed";
                    } else {
                        response = "uff;Unfollow Post Failed.Not logged in or already unfollowed. ";
                    }
                    System.out.println("Unfollow Post response...");
                    break;
                case "un":
                    System.out.println("Update Notification request");
                    if (userSession.updateNotifications() != null) {
                        response = "uns;";
                        for (String notification : userSession.updateNotifications()) {
                            response += notification + ";";
                        }
                    } else {
                        response = "unf;Update Notification Failed.Not logged in. ";
                    }
                    System.out.println("Update Notification response...");
                    break;
                case "cn":
                    System.out.println("Clear Notification request");
                    if (userSession.clearNotifications()) {
                        response = "cns;Notifications cleared";
                    } else {
                        response = "cnf;Clear Notification Failed.Not logged in. ";
                    }
                    System.out.println("Clear Notification response...");
                    break;
                case "rp":
                    System.out.println("Request Profile request");
                    String[] tokens_rp = request.split(" ");
                    String userName_rp = tokens_rp[1];
                    User user_rp = userSession.requestProfile(userName_rp);
                    if (user_rp != null) {
                        response = "rps;" + user_rp.getUserName() + ";" + user_rp.getFirstName() + ";" + user_rp.getLastName() + ";" + user_rp.getEmail() + ";" + user_rp.getPhoneNumber();
                    } else {
                        response = "rpf;Request Profile Failed.Not logged in or user not exists or not public. ";
                    }
                    System.out.println("Request Profile response...");
                    break;


            }
        }
        return response;
    }
}
