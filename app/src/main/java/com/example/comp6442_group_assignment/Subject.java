package com.example.comp6442_group_assignment;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Subject {
    public void registerObserver(String o);
    public void removeObserver(String o);

    void notifyObserversEdit() throws ParserConfigurationException, IOException, SAXException;

    void notifyObserversComment() throws ParserConfigurationException, IOException, SAXException;

    void notifyObserversLike() throws ParserConfigurationException, IOException, SAXException;
}
