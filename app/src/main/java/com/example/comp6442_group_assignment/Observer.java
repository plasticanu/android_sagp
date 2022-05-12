package com.example.comp6442_group_assignment;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Observer {
    public void update(String postId, String message) throws ParserConfigurationException, IOException, SAXException;
}
