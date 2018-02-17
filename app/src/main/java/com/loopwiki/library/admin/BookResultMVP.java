package com.loopwiki.library.admin;

/**
 * Created by sambad on 1/23/18.
 */


public interface BookResultMVP {

    interface Presenter{
        void OnCreateInitialization(String barcode);
        void addBook();
        //test method
        void setViewImgUrl(String url);
    }

    interface View{
        void setBookViews(BookInfoObject bookInfo);
        String getBookTextViewTitle();
        String getBookTextViewAuthor();
        String getImgUrl();
        //test method
        void setImgUrl(String url);
    }
}
