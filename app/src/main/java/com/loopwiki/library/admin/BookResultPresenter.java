package com.loopwiki.library.admin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sambad on 1/23/18.
 */

public class BookResultPresenter implements BookResultMVP.Presenter {

    private String TAG = "BookResultPresenter";
    //MVP - View variable
    private BookResultMVP.View bookResultActiviyView;
    private final String ISBN_DB_KEY = "YGKMMUIN";
    private SQLiteDatabase mDb;

    public BookResultPresenter(BookResultMVP.View view) {
        this.bookResultActiviyView = view;
    }

    @Override
    public void OnCreateInitialization(String barcode) {
        BookListDbHelper dbHelper = new BookListDbHelper((BookResultViewActivity) bookResultActiviyView);
        mDb = dbHelper.getWritableDatabase();
        new MyAsyncTask(this).execute(barcode);
    }

    @Override
    public void addBook() {
        addNewBookToDB();
    }

    //Test
    @Override
    public void setViewImgUrl(String url) {
        bookResultActiviyView.setImgUrl(url);
    }

    private void addNewBookToDB() {
        ContentValues cv = new ContentValues();
        cv.put(BookListContract.BookListEntry.BOOK_AUTHOR, bookResultActiviyView.getBookTextViewAuthor());
        cv.put(BookListContract.BookListEntry.BOOK_TITLE, bookResultActiviyView.getBookTextViewTitle());
        cv.put(BookListContract.BookListEntry.BOOK_IMAGE_URL, bookResultActiviyView.getImgUrl() );
        Long num = mDb.insert(BookListContract.BookListEntry.TABLE_NAME, null, cv);
        Log.v(TAG, "Book inserted. num value is: " + num);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, BookInfoObject> {

        BookResultPresenter mPresenter;
        public MyAsyncTask(BookResultPresenter presenter) {
            super();
            this.mPresenter = presenter;
        }

        @Override
        protected BookInfoObject doInBackground(String... urls) {
            String xmlDocument = retrieveBookInfoFromGoodReads(urls[0]);
            Log.v(TAG, "XMlDoc is: " + xmlDocument);
            Log.d("MyAsyncTask", "DoinBackground done");
            BookInfoObject book = null;
            try {
                book = parseXml(xmlDocument);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return book;
        }

        @Override
        protected void onPostExecute(BookInfoObject bookInfoObject) {
            super.onPostExecute(bookInfoObject);
            mPresenter.setBookViewsInView(bookInfoObject);
        }
    }

    public void setBookViewsInView(BookInfoObject book){
        bookResultActiviyView.setBookViews(book);
    }

    private String retrieveBookInfoFromGoodReads(String barcode) {
        InputStream stream = null;
        String result = null;
        try {
            String url = buildGoodReadsUrl(barcode);
            URL isbnUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) isbnUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            Log.v("retrieveBookInfo", "Status code is: " + status);
            Log.v("retrieveBookInfo", connection.getContent().toString());
            if(status != HttpURLConnection.HTTP_OK){
                throw new IOException("HTTP code error: " + status);
            }
            stream = connection.getInputStream();
            if(stream != null){
                result = readStream(stream);
            }
        }catch (IOException e){
            Log.e(TAG, "Error Detected: " + e);
        }
        return result;
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer();
        String inputLine = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while((inputLine = br.readLine()) != null){
            sb.append(inputLine);
        }
        return sb.toString();
    }

    private String buildGoodReadsUrl(String isbn) {
        String goodReadsKey = "lA0ttkYJJOCPPiKn0JWWMQ";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.goodreads.com")
                .appendPath("search")
                .appendPath("index.xml")
                .appendQueryParameter("key", goodReadsKey)
                .appendQueryParameter("q",isbn);
        String newUrl = builder.build().toString();
        Log.v(TAG, newUrl);
        return newUrl;
    }

    /**
     * This tutorial was used as guidance:
     * https://www.sitepoint.com/learning-to-parse-xml-data-in-your-android-app/
     * (as was android documentation)
     * @param xmlDocument
     * @return BookInfoObject Oject
     * @throws XmlPullParserException
     * @throws IOException
     */
    private BookInfoObject parseXml(String xmlDocument) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(xmlDocument));
        Log.v(TAG, "Enacting parseXml().");
        int eventType = parser.getEventType();
        BookInfoObject newBook = new BookInfoObject();
        while(eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch(eventType){
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    Log.v(TAG,"Event name is: " + name);
                    if(name .equals( "title")){
                        newBook.title = parser.nextText();
                        Log.v(TAG,"Book title is: " + newBook.title);
                    }else if(name.equals("name")){
                        newBook.author = parser.nextText();
                        Log.v(TAG,"Book author is: " + newBook.author);
                    }else if(name.equals("image_url")){
                        newBook.imgUrl = parser.nextText();
                        Log.v(TAG,"imgurl   is: " + newBook.imgUrl);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    break;
            }
            eventType = parser.next();
        }
        Log.v(TAG, newBook.author + " is the author.");
        Log.v(TAG, newBook.title + " is the title.");
        return newBook;
    }

    /**
     * These 3 functions are outdated. They retrieve book info from the IsbnDb.com API.
     * They use the typical version of network calls(HttpUrlConnection & JSON Parsing)
     * Leaving them for reference and later review.
     * - outdatedRetrieveBookInfo(String)
     * - buildUrl(String)
     * - outdatedGetAuthorFromJson(String)
     */
    private String outdatedRetrieveBookInfo(String barcode)  {
        //Open connection to ISBNdb
        //returns a stream of book info in JSON
        InputStream stream = null;
        String result = null;
        try {
            String url = buildUrl(barcode);
            URL isbnUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) isbnUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            Log.d("retrieveBookInfo", "Status code is: " + status);
            if(status != HttpURLConnection.HTTP_OK){
                throw new IOException("HTTP code error: " + status);
            }
            stream = connection.getInputStream();
            if(stream != null){
                result = readStream(stream);
            }
        }catch (IOException e){
            Log.e(TAG, "Error Detected: " + e);
        }
        return result;
    }

    private String buildUrl(String isbn) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.isbndb.com")
                .appendPath("api")
                .appendPath("v2")
                .appendPath("json")
                .appendPath(ISBN_DB_KEY)
                .appendPath("book")
                .appendPath(isbn);
        String newUrl = builder.build().toString();
        return newUrl;
    }

    private String outdatedGetAuthorFromJson(String bookInfo) {
        String author = null;
        try {
            JSONObject json = new JSONObject(bookInfo);
            JSONArray jsonArray = json.getJSONArray("data");
            JSONObject dataJson = jsonArray.getJSONObject(0);
            JSONArray authorJson = dataJson.getJSONArray("author_data");
            JSONObject authorObject = authorJson.getJSONObject(0);
            String id = authorObject.getString("id");
            String name = authorObject.getString("name");
            return name;
        }catch (JSONException e){
            Log.e(TAG, "Error caught: " + e);
        }
        return null;
    }
}
