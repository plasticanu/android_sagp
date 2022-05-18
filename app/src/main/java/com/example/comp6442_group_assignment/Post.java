package com.example.comp6442_group_assignment;

import android.annotation.SuppressLint;

import com.example.comp6442_group_assignment.Search.Search;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Post class. This class is used to store the post information. It includes methods that read and write the user
 * information from and to persistent data.
 */
public class Post implements Subject, Serializable{
    private String postId; // Post ID.
    private String content;
    private String author;

    private String createTime;
    private List<Comment> comments;
    private List<String> likes;

    private List<String> observers; // observers list

    public Post(String postId, String content, String author, List<String> likes, String createTime, List<Comment> comments, List<String> observers) {
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.createTime = createTime;
        this.comments = comments;
        this.observers = observers;
    }

    /**
     * This constructor is used to create a new post in the background of the server.
     * It shoould be never be called alone, since all field must be initialized.
     * @param content
     * @param author
     */
    public Post(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<String> getObservers() {
        return observers;
    }

    public void setObservers(List<String> observers) {
        this.observers = observers;
    }

    public void setLikes(List<String> likes) { this.likes = likes; }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId; }

    @Override
    public void registerObserver(String o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(String o) {
        observers.remove(o);
    }


    /**
     * Notify all observers that the post has been updated. Must be called when an update from server happens.
     * i.e. calling methods in UserSession by the server.
     */
    @Override
    public void notifyObserversEdit() throws ParserConfigurationException, IOException, SAXException {
        String message = "Update on post you followed:";
        for (String o : observers) {
            List<User> users = User.readUsers();
            for (User user : users) {
                if (user.getUserName().equals(o)) {
                    user.update(postId, message);
                }
            }
        }
    }

    /**
     * Notify all observers that the post has been updated. Must be called when an update from server happens.
     * i.e. calling methods in UserSession by the server.
     */
    @Override
    public void notifyObserversComment() throws ParserConfigurationException, IOException, SAXException {
        String message = "Someone commented a post you followed:";
        for (String o : observers) {
            List<User> users = User.readUsers();
            for (User user : users) {
                if (user.getUserName().equals(o)) {
                    user.update(postId, message);
                }
            }
        }
    }

    /**
     * Notify all observers that the post has been updated. Must be called when an update from server happens.
     * i.e. calling methods in UserSession by the server.
     */
    @Override
    public void notifyObserversLike() throws ParserConfigurationException, IOException, SAXException {
        String message = "Someone liked a post you followed:";
        for (String o : observers) {
            List<User> users = User.readUsers();
            for (User user : users) {
                if (user.getUserName().equals(o)) {
                    user.update(postId, message);
                }
            }
        }
    }

    @NotNull
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                "| content='" + content + '\'' +
                "| author='" + author + '\'' +
                "| likes=" + likes +
                "| createTime='" + createTime + '\'' +
                "| comments=" + comments +
                "| observers=" + observers +
                '}';
    }

    /**
     * directly write to the post.xml file, server side initialization function. This should probably never be
     * called alone.
     * @param posts
     * @throws ParserConfigurationException
     */
    public static void writeToPost(List<Post> posts) throws ParserConfigurationException {
        boolean useTestPath = false;
        String testPath = "src/main/assets/post.xml";
        String address = "app/src/main/assets/post.xml";
        File file = new File(address);
        if (!file.exists()) {
            useTestPath = true;
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Posts");
        doc.appendChild(rootElement);

        for (Post post : posts) {
            Element postElement = doc.createElement("Post");
            rootElement.appendChild(postElement);

            Element postId = doc.createElement("PostId");
            postId.appendChild(doc.createTextNode(post.getPostId()));
            postElement.appendChild(postId);

            Element content = doc.createElement("Content");
            content.appendChild(doc.createTextNode(post.getContent()));
            postElement.appendChild(content);

            Element author = doc.createElement("Author");
            author.appendChild(doc.createTextNode(post.getAuthor()));
            postElement.appendChild(author);

            Element likes = doc.createElement("Likes");
            postElement.appendChild(likes);

            for (String like : post.getLikes()) {
                Element likeElement = doc.createElement("User");
                likeElement.appendChild(doc.createTextNode(like));
                likes.appendChild(likeElement);
            }

            Element createTime = doc.createElement("CreateTime");
            createTime.appendChild(doc.createTextNode(post.getCreateTime()));
            postElement.appendChild(createTime);

            Element comments = doc.createElement("Comments");
            postElement.appendChild(comments);

            if (post.getComments() != null) {
                for (Comment comment : post.getComments()) {
                    Element commentElement = doc.createElement("Comment");
                    comments.appendChild(commentElement);

                    Element commentContent = doc.createElement("Content");
                    commentContent.appendChild(doc.createTextNode(comment.getContent()));
                    commentElement.appendChild(commentContent);

                    Element commentAuthor = doc.createElement("Author");
                    commentAuthor.appendChild(doc.createTextNode(comment.getAuthor()));
                    commentElement.appendChild(commentAuthor);

                    Element commentTime = doc.createElement("Time");
                    commentTime.appendChild(doc.createTextNode(comment.getTime()));
                    commentElement.appendChild(commentTime);
                }
            }

            Element observers = doc.createElement("Observers");
            postElement.appendChild(observers);

            if (post.getObservers() != null) {
                for (String observer : post.getObservers()) {
                    Element observerElement = doc.createElement("Observer");
                    observerElement.appendChild(doc.createTextNode(observer));
                    observers.appendChild(observerElement);
                }
            }
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
     * Reads the post.xml file and returns a list of posts, should be a server function
     * @return List of posts.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<Post> readFromPost() throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = new LinkedList<>();
        String testPath = "src/main/assets/post.xml";
        String address = "app/src/main/assets/post.xml";
        File file = new File(address);
        if (!file.exists()) {
            file = new File(testPath);
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("Post");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Element postElement = (Element) nList.item(temp);
            String postId = postElement.getElementsByTagName("PostId").item(0).getTextContent();
            String content = postElement.getElementsByTagName("Content").item(0).getTextContent();
            String author = postElement.getElementsByTagName("Author").item(0).getTextContent();
            String createTime = postElement.getElementsByTagName("CreateTime").item(0).getTextContent();
            List<String> likes = new LinkedList<>();
            NodeList likeList = postElement.getElementsByTagName("User");
            for (int i = 0; i < likeList.getLength(); i++) {
                likes.add(likeList.item(i).getTextContent());
            }
            List<Comment> comments = new LinkedList<>();
            NodeList commentList = postElement.getElementsByTagName("Comment");
            for (int i = 0; i < commentList.getLength(); i++) {
                Element commentElement = (Element) commentList.item(i);
                String commentContent = commentElement.getElementsByTagName("Content").item(0).getTextContent();
                String commentAuthor = commentElement.getElementsByTagName("Author").item(0).getTextContent();
                String commentTime = commentElement.getElementsByTagName("Time").item(0).getTextContent();
                Comment comment = new Comment(commentContent, commentAuthor, commentTime);
                comments.add(comment);
            }
            List<String> observers = new LinkedList<>();
            NodeList observerList = postElement.getElementsByTagName("Observer");
            for (int i = 0; i < observerList.getLength(); i++) {
                observers.add(observerList.item(i).getTextContent());
            }
            Post post = new Post(postId, content, author, likes, createTime, comments, observers);
            posts.add(post);
        }
        return posts;
    }

    /**
     * Check if the post exists in the post.xml file.
     * @param postId
     * @return true if the post exists, false otherwise.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean exists(String postId) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                System.out.println("Post exists" + postId);
                return true;
            }
        }
        System.out.println("Post does not exist" + postId);
        return false;
    }

    public static boolean belongToUser(String postId, String userId) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                if (post.getAuthor().equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Write a new post to post.xml file, should be a server function
     * @param post
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean addToPost(Post post) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        int Id;
        if (posts.size() != 0) {
            Id = Integer.parseInt(posts.get(posts.size() - 1).getPostId()) + 1;
        } else {
            Id = 1;
        }
        @SuppressLint("DefaultLocale") String newPostId = String.format("%08d", Id);
        post.setPostId(newPostId);
        Search search = Search.getInstance(); // search instance
        search.insert(post);
        posts.add(post);
        writeToPost(posts);
        System.out.println("Post added. " + post);
        return true;
    }

    /**
     * Edit a post in post.xml file, should be a server function
     * @param postId
     * @param content
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean editPost(String postId, String content) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                post.setContent(content);
                post.notifyObserversEdit();
                writeToPost(posts);
                System.out.println("Post edited. " + post);
                return true;
            }
        }
        return false;
    }

    /**
     * Add a user to the like list of a post, should be a server function
     * @param postId
     * @param userId
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean likePost(String postId, String userId) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                if (!post.getLikes().contains(userId)) {
                    post.getLikes().add(userId);
                    post.notifyObserversLike();
                    writeToPost(posts);
                    System.out.println("Post liked. " + post);
                    return true;
                } else {
                    System.out.println("Post already liked. " + post);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Remove a user from the like list of a post, should be a server function
     * @param postId
     * @param userId
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean unlikePost(String postId, String userId) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                if (post.getLikes().contains(userId)) {
                    post.getLikes().remove(userId);
                    writeToPost(posts);
                    System.out.println("Post unliked. " + post);
                    return true;
                } else {
                    System.out.println("Post already unliked. " + post);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Delete a post from post.xml file, should be a server function
     * @param postId
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static void removeFromPost(String postId) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                posts.remove(post);
                Search search = Search.getInstance(); // search instance
                search.delete(post);
                writeToPost(posts);
                System.out.println("Post removed. " + postId);
                return;
            }
        }
        System.out.println("Post not found. " + postId);
    }

    /**
     * Add a comment to a post, should be a server function
     * @param postId
     * @param comment
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean addToComment(String postId, Comment comment) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                post.getComments().add(comment);
                post.notifyObserversComment();
                writeToPost(posts);
                System.out.println("Comment added. " + comment);
                return true;
            }
        }
        return false; // post not found
    }

    /**
     * Add a user to the observer list of a post, should be a server function
     * @param postId
     * @param username
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean followPost(String postId, String username) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                if (!post.getObservers().contains(username)) {
                    post.getObservers().add(username);
                    writeToPost(posts);
                    System.out.println("Post followed. " + post);
                    return true;
                    } else {
                        System.out.println("Post already followed. " + post);
                        return false;
                    }
            }
        }
        System.out.println("Post not found. " + postId);
        return false;
    }

    /**
     * Remove a user from the observer list of a post, should be a server function
     * @param postId
     * @param username
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean unfollowPost(String postId, String username) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                if (post.getObservers().contains(username)) {
                    post.getObservers().remove(username);
                    writeToPost(posts);
                    System.out.println("Post unfollowed. " + post);
                    return true;
                } else {
                    System.out.println("Post already unfollowed. " + post);
                    return false;
                }
            }
        }
        System.out.println("Post not found. " + postId);
        return false;
    }

    /**
     * get all post of a user, should be a server function
     * @param username
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<Post> getAllPosts(String username) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        List<Post> postsByUser = new LinkedList<>();
        for (Post post : posts) {
            if (post.getAuthor().equals(username)) {
                postsByUser.add(post);
            }
        }
        return postsByUser;
    }

    public static void writeXml(Document doc,
                                OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<Post> posts = readFromPost();
        System.out.println(posts.get(20).getPostId());
    }

}


