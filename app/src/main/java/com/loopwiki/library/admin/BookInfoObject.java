package com.loopwiki.library.admin;

/**
 * Created by sambad on 1/23/18.
 */


public class BookInfoObject {
    public String title;
    public String author;
    public String imgUrl;
    public String[] returnInfo(){
        String[] info = {title, author, imgUrl};
        return info;
    }
}
