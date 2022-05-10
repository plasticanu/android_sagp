package com.example.comp6442_group_assignment;

import android.util.Log;

import com.example.comp6442_group_assignment.Search.Parser;
import com.example.comp6442_group_assignment.Search.SentenceTokenizer;
import com.example.comp6442_group_assignment.Search.StringTokenizer;
import com.example.comp6442_group_assignment.Search.Tags.Tag;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * @Author Zhidong Piao u7139999
 */
public class ParserTest {
    @Test
    public void sharpTagTest(){
        ArrayList<String> inputList = new ArrayList<String>();
        inputList.add("#a2bcADWAE");
        inputList.add("@ABC");
        inputList.add("#abcA rhjd");
        inputList.add("##af#");
        inputList.add("awr");
        for(int i = 0; i < 5; i ++){
            SentenceTokenizer st = new SentenceTokenizer(inputList.get(i));
            Parser p = new Parser(st);
            ArrayList<Tag> t = p.parseTag();
            System.out.println(t.toString());
        }
    }
}
