package com.harryfultz.studentmanager.appConfigAndDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;

enum DbConstants {
    DATABASE_NAME("student_db"), TABLE_NAME("students"), USERNAME("username"), NUMBER("phone_number"), GENDER("Gender"), ID("id");
    String _value;

    DbConstants(String value) {
        this._value = value;
    }


}


public class DatabaseHelper {

    private DatabaseAdapter dbAdapter;
    private Context context;

    public DatabaseHelper(Context context) {
        dbAdapter = new DatabaseAdapter(context);
        this.context = context;
    }

    public void addStudent(String username, String phone, int gender) {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(String.valueOf(DbConstants.USERNAME), username);
        contentValues.put(String.valueOf(DbConstants.GENDER), gender);
        contentValues.put(String.valueOf(DbConstants.NUMBER), phone);
        try {
            db.insert(String.valueOf(DbConstants.TABLE_NAME), null, contentValues);
            MyDynamicToast.successMessage(context, "Studenti u shtua.");
        } catch (SQLException e) {
            MyDynamicToast.errorMessage(context, " " + e.getMessage());
        }

        db.close();

    }

    public void deleteAllStudents() {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        try {
            db.execSQL(DatabaseAdapter.DROP_TABLE);
            db.execSQL(DatabaseAdapter.CREATE_TABLE);
            MyDynamicToast.successMessage(context, "Studentët u fshinë.");
        } catch (SQLException e) {
            MyDynamicToast.errorMessage(context, " " + e.getMessage());
        }
        db.close();
    }

    void deleteStudent(String username) {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        String[] whereArgs = {username};
        db.delete(String.valueOf(DbConstants.TABLE_NAME), DbConstants.USERNAME + "=?", whereArgs);
        db.close();
    }

    Cursor getListContent() {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + DbConstants.TABLE_NAME, null);
    }

    public boolean checkIfStudentExists(String username) {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        String checkStudentQuery = "SELECT * FROM " + DbConstants.TABLE_NAME + " WHERE " + DbConstants.USERNAME + " = \"" + username + "\";";
        Cursor cursor = db.rawQuery(checkStudentQuery, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;


    }

    public String getSpecificNumberFromStudent(String studentName) {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        String queryString = "SELECT " + DbConstants.NUMBER + " FROM " + DbConstants.TABLE_NAME + " WHERE " + DbConstants.USERNAME + " = \"" + studentName + "\";";
        Cursor c = db.rawQuery(queryString, null);
        if (c.getCount() > 0 && c.moveToFirst()) {
            String result = c.getString(c.getColumnIndex(String.valueOf(DbConstants.NUMBER)));
            c.close();
            return result;
        } else {
            c.close();
            return null;
        }

    }

    public String[] getAllNumbers() {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + DbConstants.NUMBER + " FROM " + DbConstants.TABLE_NAME, null);
        String[] numbers = new String[c.getCount()];
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            numbers[i] = c.getString(c.getColumnIndex(String.valueOf(DbConstants.NUMBER)));
            c.moveToNext();
        }
        c.close();

        return numbers;
    }

    public String[] getAllStudents() {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + DbConstants.USERNAME + " FROM " + DbConstants.TABLE_NAME, null);
        String names[] = new String[c.getCount()];
        for (int i = 0; i < getNumberOfRows(); i++) {
            if (c.moveToNext()) {
                names[i] = c.getString(c.getColumnIndex(String.valueOf(DbConstants.USERNAME)));
            }
        }
        c.close();
        db.close();
        return names;
    }

    public int getNumberOfRows() {
        SQLiteDatabase db = dbAdapter.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + DbConstants.NUMBER + " FROM " + DbConstants.TABLE_NAME, null);

        return c.getCount();
    }

    private static class DatabaseAdapter extends SQLiteOpenHelper {

        private static final String CREATE_TABLE = "CREATE TABLE " + DbConstants.TABLE_NAME + "(" + DbConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DbConstants.USERNAME + " VARCHAR(255), " + DbConstants.GENDER + " INTEGER, " + DbConstants.NUMBER + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DbConstants.TABLE_NAME;
        private static final int DATABASE_VERSION = 2;
        private Context context;


        DatabaseAdapter(Context context) {
            super(context, String.valueOf(DbConstants.DATABASE_NAME), null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DatabaseAdapter.CREATE_TABLE);
                MyDynamicToast.successMessage(context, "DB created!");
            } catch (SQLException e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(context, " " + e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DatabaseAdapter.DROP_TABLE);
                MyDynamicToast.successMessage(context, "DB Upgraded!");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(context, " " + e.getMessage());
            }
        }
    }

}
