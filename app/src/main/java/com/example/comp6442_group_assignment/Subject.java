package com.example.comp6442_group_assignment;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Subject {
    public void registerObserver(String o);
    public void removeObserver(String o);
    public void notifyObservers() throws ParserConfigurationException, IOException, SAXException;
}
