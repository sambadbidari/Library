package com.loopwiki.library.admin;

import android.provider.BaseColumns;

/**
 * The contract for the SqLite database.
 * Created by sambad on 1/23/18.
 */


public class BookListContract {

    public static final class BookListEntry implements BaseColumns{
        public static final String TABLE_NAME = "bookTable";
        public static final String BOOK_TITLE = "bookTitle";
        public static final String BOOK_AUTHOR = "bookAuthor";
        public static final String BOOK_IMAGE_URL = "bookImageUrl";
    }
}
