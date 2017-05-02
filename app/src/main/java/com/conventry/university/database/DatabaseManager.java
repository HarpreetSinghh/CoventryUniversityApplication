package com.conventry.university.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.conventry.university.MyApplication;
import com.conventry.university.beans.Category;
import com.conventry.university.beans.Email;
import com.conventry.university.utils.AppConstants;
import com.conventry.university.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    static DatabaseManager self = null;

    private DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DatabaseManager getInstance() {
        if(self == null) {
            self = new DatabaseManager(MyApplication.getAppContext(), AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
        }return self;
    }

    private String tableCategory = "CREATE TABLE tbl_category (categoryId LONG PRIMARY KEY, name TEXT, icon TEXT);";

    private String tableEmail = "CREATE TABLE tbl_email (emailId LONG PRIMARY KEY, fromUser TEXT, toUser TEXT, cc TEXT, subject TEXT, message TEXT, sentDate TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableCategory);
        db.execSQL(tableEmail);
        db.execSQL("INSERT INTO tbl_category VALUES (1001, 'Email Login', 'emaillogin.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (2001, 'Modules', 'modules.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (3001, 'Login', 'login.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (4001, 'Weather', 'weather.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (5001, 'Search', 'search.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (6001, 'News', 'news.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (7001, 'Campus Map', 'campusmap.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (8001, 'Buses', 'buses.png')");
        db.execSQL("INSERT INTO tbl_category VALUES (9001, 'TimeTable', 'timetable.png')");

        // yyyy-MM-dd hh:mm:ss

        db.execSQL("INSERT INTO tbl_email VALUES (1002, 'Rhiannon Bigham', 'Harpreet Singh', '', 'PHP Meet-up', '" +
                "Please visit  http://www.meetup.com/PHP-Warwickshire/ to register your attendance','2017-04-01 14:30:24')");

        db.execSQL("INSERT INTO tbl_email VALUES (1003, 'Yanguo Jing', 'Harpreet Singh', '', 'Presentation feedback', '" +
                "You have given a good presentation and a demonstration of your mobile app.','2017-04-05 13:16:24')");

        db.execSQL("INSERT INTO tbl_email VALUES (1004, 'Dwayne Webb', 'Harpreet Singh', '', 'Live Graduate jobs', '" +
                "Hello Harpreet, Have you been offered a graduate job yet','2017-04-08 18:42:24')");

        db.execSQL("INSERT INTO tbl_email VALUES (1005, 'Ali Bushnell', 'Harpreet Singh', '', 'New warning on phishing emails', '" +
                "We have been made aware, both via internal and external sources that fraudsteres are currently sending out high volumes of phishing data.','2017-04-10 18:42:24')");

        db.execSQL("INSERT INTO tbl_email VALUES (1006, 'Atlassian Bitbucked', 'Harpreet Singh', '', 'Free unlimited collaborators on Bitbucket for students and teachers', '" +
                "Free unlimited private repositories and collaborators for all students and teachers.','2017-04-15 17:00:16')");

        db.execSQL("INSERT INTO tbl_email VALUES (1007, 'Kyle Madden', 'Harpreet Singh', '', 'Graduate Management Programme', '" +
                "Do you want to work for the Graduate Employer of the year 2017.','2017-04-17 18:42:24')");

        db.execSQL("INSERT INTO tbl_email VALUES (1008, 'Peter Every', 'Harpreet Singh', '', '303COM Second Marker', '" +
                "Dear Project Students, Please find attached PDF with names of supervisors.','2017-04-18 18:42:24')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion != oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS tbl_category");
            db.execSQL("DROP TABLE IF EXISTS tbl_email");
        }
        onCreate(db);
    }

    public synchronized List<Category> getCategoriesList() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM tbl_category", null);
            if(cursor != null)
                categoryList = getCategoryDetails(cursor);
        } catch(Exception e) {
            AppUtils.doLogError(AppConstants.ERROR_TAG, "getCategoriesList" + e.getMessage());
        } finally {
            closeDB(null, cursor, db);
        }
        return categoryList;
    }


    private List<Category> getCategoryDetails(Cursor cursor) {
        List<Category> studentList = new ArrayList<>();
        Category category = null;
        while(cursor.moveToNext()) {
            category = new Category();
            category.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
            category.setName(cursor.getString(cursor.getColumnIndex("name")));
            category.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            studentList.add(category);
        }
        return studentList;
    }

    public synchronized List<Email> getEmailList() {
        List<Email> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM tbl_email order by emailId desc", null);
            if(cursor != null)
                categoryList = getEmailDetails(cursor);
        } catch(Exception e) {
            AppUtils.doLogError(AppConstants.ERROR_TAG, "getEmailList" + e.getMessage());
        } finally {
            closeDB(null, cursor, db);
        }
        return categoryList;
    }


    private List<Email> getEmailDetails(Cursor cursor) {
        List<Email> emailList = new ArrayList<>();
        Email email = null;
        while(cursor.moveToNext()) {
            email = new Email();
            email.setEmailId(cursor.getInt(cursor.getColumnIndex("emailId")));
            email.setFrom(cursor.getString(cursor.getColumnIndex("fromUser")));
            email.setTo(AppUtils.getEmails(cursor.getString(cursor.getColumnIndex("toUser"))));
            email.setCc(AppUtils.getEmails(cursor.getString(cursor.getColumnIndex("cc"))));
            email.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
            email.setMessage(cursor.getString(cursor.getColumnIndex("message")));
            email.setSentDate(cursor.getString(cursor.getColumnIndex("sentDate")));
            emailList.add(email);
        }
        return emailList;
    }

    private void closeDB(ContentValues cv, Cursor cursor, SQLiteDatabase db) {
        if(cv != null) {
            cv.clear();
        }
        if(cursor != null) {
            cursor.close();
        }
        if(db != null) {
            db.close();
        }
    }
}
