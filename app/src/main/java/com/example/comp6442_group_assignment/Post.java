package com.example.comp6442_group_assignment;

import android.annotation.SuppressLint;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.comp6442_group_assignment.FakeServerStuff.CreateUserXml.writeXml;

public class Post {
    private String postId; // Post ID.
    private String content;
    private String author;

    private int likes;
    private String createTime;
    private List<Comment> comments;

    public Post(String postId, String content, String author, int likes, String createTime, List<Comment> comments) {
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.createTime = createTime;
        this.comments = comments;
    }

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

    public void setComments(ArrayList<Comment> comments) {
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) { this.likes = likes; }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId; }

    @NotNull
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", likes=" + likes +
                ", createTime='" + createTime + '\'' +
                ", comments=" + comments +
                '}';
    }

    /**
     * directly write to the post.xml file, server side initialization function. This should probably never be
     * called alone.
     * @param posts
     * @throws ParserConfigurationException
     */
    public static void writeToPost(List<Post> posts) throws ParserConfigurationException {
        String address = "app/src/main/assets/post.xml";
        File file = new File(address);

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
            likes.appendChild(doc.createTextNode(Integer.toString(post.getLikes())));
            postElement.appendChild(likes);

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
     * Reads the post.xml file and returns a list of posts, should be a server function
     * @return List of posts.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<Post> readFromPost() throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = new ArrayList<>();
        String address = "app/src/main/assets/post.xml";
        File file = new File(address);

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
            int likes = Integer.parseInt(postElement.getElementsByTagName("Likes").item(0).getTextContent());
            String createTime = postElement.getElementsByTagName("CreateTime").item(0).getTextContent();
            List<Comment> comments = new ArrayList<>();
            NodeList commentList = postElement.getElementsByTagName("Comment");
            for (int i = 0; i < commentList.getLength(); i++) {
                Element commentElement = (Element) commentList.item(i);
                String commentContent = commentElement.getElementsByTagName("Content").item(0).getTextContent();
                String commentAuthor = commentElement.getElementsByTagName("Author").item(0).getTextContent();
                String commentTime = commentElement.getElementsByTagName("Time").item(0).getTextContent();
                Comment comment = new Comment(commentContent, commentAuthor, commentTime);
                comments.add(comment);
            }
            Post post = new Post(postId, content, author, likes, createTime, comments);
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
        int Id = Integer.parseInt(posts.get(posts.size() - 1).getPostId()) + 1;
        @SuppressLint("DefaultLocale") String newPostId = String.format("%08d", Id);
        post.setPostId(newPostId);
        posts.add(post);
        writeToPost(posts);
        System.out.println("Post added. " + post);
        return true;
    }

    /**
     * Delete a post from post.xml file, should be a server function
     * @param postId
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static boolean removeFromPost(String postId) throws ParserConfigurationException, SAXException, IOException {
        List<Post> posts = readFromPost();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                posts.remove(post);
                writeToPost(posts);
                System.out.println("Post removed. " + postId);
                return true;
            }
        }
        System.out.println("Post not found. " + postId);
        return false;
    }
}


