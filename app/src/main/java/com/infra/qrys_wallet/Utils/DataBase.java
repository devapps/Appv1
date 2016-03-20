package com.infra.qrys_wallet.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.infra.qrys_wallet.ProxyClasses.ContactInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    // CONTACTS VARIABLE
    public static final String DBNAME = "database",
            CONTACTTABLE = "contacttable";
    public static final String CONTACTSID = "contactsid",
            CONTACTNAME = "contactname", CONTACTPHONE = "contactphone",
           CONTACTIMAGE = "contactimagethumb";
    private static final String TAG = "SQLiteOpenHelper";
    private static DataBase mInstance = null;

    public DataBase(Context applicationcontext) {
        super(applicationcontext, DBNAME, null, 1);
    }

    public static DataBase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBase(context.getApplicationContext());
        }
        return mInstance;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        // Creating CONTACTS Table
        query = "CREATE TABLE " + CONTACTTABLE + " (" + CONTACTSID
                + " TEXT, " + CONTACTNAME + " TEXT, " + CONTACTPHONE + " TEXT," + CONTACTIMAGE + " BLOB" + ")";
        db.execSQL(query);
    }

       //Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS" + CONTACTTABLE;
        db.execSQL(query);
        // Create tables again
        onCreate(db);
    }
    // INSERT CONTACTS

    public void insertContacts(ContactInfo contactinfo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.d("Name in DB", " " + contactinfo.name);
        values.put(CONTACTSID, contactinfo.contactssid);
        values.put(CONTACTNAME, contactinfo.name);
        values.put(CONTACTPHONE, contactinfo.phone);
        values.put(CONTACTIMAGE, contactinfo.thumb);
        database.insertWithOnConflict(CONTACTTABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        //database.insert(CONTACTTABLE, null, values);
    }

    public ArrayList<ContactInfo> getAllContactsRecords() {
        String selectQuery = "SELECT * FROM " + CONTACTTABLE + " ORDER BY "
                + CONTACTSID;
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<ContactInfo> ContactssArray = new ArrayList<ContactInfo>();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ContactssArray.add(new ContactInfo(cursor.getInt(0), cursor
                        .getString(1), cursor.getString(2), cursor.getBlob(3)));

            } while (cursor.moveToNext());
        }
        return ContactssArray;
    }
    // GET ALL CONTACTS RECORDS

    private Bitmap getBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
    // UPDATE CONTACTS
    public void updateContacts(ContactInfo contactsupdate) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTSID, contactsupdate.contactssid);
        values.put(CONTACTPHONE, contactsupdate.phone);
        values.put(CONTACTNAME, contactsupdate.name);
        values.put(CONTACTIMAGE, contactsupdate.thumb);
        database.update(CONTACTTABLE, values, CONTACTSID + " = ?",
                new String[]{contactsupdate.contactssid + ""});
        database.close();
    }
    // DELETE CONTACTS
    public void deleteContacts() {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM  " + CONTACTTABLE +"";
        Log.d("query", deleteQuery);
        database.execSQL(deleteQuery);
        database.close();
    }

    // CONTACTS BULKINSERT
    public void contactsbulkInsert(ArrayList<ContactInfo> contactssinfo) {
        String sql = "INSERT OR REPLACE INTO " + CONTACTTABLE
                + " VALUES (?,?,?,?);";
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(sql);
        database.beginTransaction();
        for (ContactInfo contact : contactssinfo) {
            statement.clearBindings();
            statement.bindLong(1, contact.contactssid);
            statement.bindString(2, contact.phone);
            statement.bindString(3, contact.name);
            statement.bindString(4, String.valueOf(contact.thumb));
            statement.execute();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }






}
