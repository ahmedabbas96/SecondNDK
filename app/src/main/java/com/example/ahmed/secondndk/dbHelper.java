package com.example.ahmed.secondndk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    //Datebase Version
    private static  final int DATABASE_VERSION = 1;
    //Datebase Name
    private static final String DATABASE_NAME = "user";
    // Table Name
    private static final String TABLE_USERS = "Users";
    // key names

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";

    //table create statements
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_USER_ID + " INTEGER NOT NULL PRIMARY KEY, " + KEY_USER_NAME
            + " TEXT, " + KEY_USER_EMAIL + " TEXT, " + KEY_USER_PHONE + " TEXT " + " )";

    public dbHelper (Context context){
        super(context, DATABASE_NAME,  null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USERS);
        onCreate(db);
    }
    /**
     * ===================================================================
     */

    public boolean insertUser (String name, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        db.insert("Users", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Users where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_USERS);
        return numRows;
    }

    public boolean updateUser (Integer id, String name, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Users",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }



}
