package com.loopwiki.library;

/**
 * Created by sambad on 2/15/18
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by sambad on 15/02/2018.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "library";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_USERS = "users";

    //TABLE USERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "id";

    //COLUMN user name
    public static final String KEY_USER_NAME = "username";

    //SQL for creating users table
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USER_NAME + " TEXT"
            + " ) ";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Table when oncreate gets called
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
    }

    //using this method we can add users to user table
    public void addUser(User user) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put username in  @values
        values.put(KEY_USER_NAME, user.userName);

        //Put email in  @values
        //values.put(KEY_ROLE, user.role);


        // insert row
        long todo_id = db.insert(TABLE_USERS, null, values);
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME},//Selecting columns want to query
                KEY_USER_NAME + "=?",
                new String[]{user.userName},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1));

            return user1;
        }
        assert cursor != null;
        cursor.close();
        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isUserExists(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME},//Selecting columns want to query
                KEY_USER_NAME + "=?",
                new String[]{user},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }
        assert cursor != null;
        cursor.close();
        //if email does not exist return false
        return false;
    }
    public long insertData(String user)
    {
        SQLiteDatabase dbb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_NAME, user);
        long id = dbb.insert(TABLE_USERS, null , contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {KEY_ID,KEY_USER_NAME};
        Cursor cursor =db.query(TABLE_USERS,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name =cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
            buffer.append(cid+ "   " + name +" \n");
        }
        cursor.close();
        return buffer.toString();
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(TABLE_USERS ,KEY_USER_NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(TABLE_USERS,contentValues, KEY_USER_NAME+" = ?",whereArgs );
        return count;
    }
}
