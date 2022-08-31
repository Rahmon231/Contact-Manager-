package com.lemzeeyyy.contactmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, Utility.DATABASE_NAME, null, Utility.DATABASE_VERSION);
    }
    //create tables in the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table_name(id, name, phoneNumber);
        String CREATE_CONTACT_TABLE = " CREATE TABLE " + Utility.TABLE_NAME + "("
                +Utility.KEY_ID +" INTEGER PRIMARY KEY," + Utility.KEY_NAME + " TEXT,"
                +Utility.KEY_PHONE_NO + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);//creating table

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE = String.valueOf(R.string.db_drop);
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Utility.DATABASE_NAME});

        onCreate(sqLiteDatabase);
    }
    //add contact
    public void addContact(Contact contact){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utility.KEY_NAME,contact.getName());
        values.put(Utility.KEY_PHONE_NO,contact.getPhoneNumber());

        // insert to row
        database.insert(Utility.TABLE_NAME,null,values);
        Log.d("DBHandler", "addContact: Item added");
        database.close(); //close db connectiong
    }

    //get a contact
    public Contact getContact(int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Utility.TABLE_NAME,
                new String[]{Utility.KEY_ID,
                        Utility.KEY_NAME,
                        Utility.KEY_PHONE_NO},
                Utility.KEY_ID+"=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        if(cursor!=null)
            cursor.moveToFirst();

        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhoneNumber(cursor.getString(2));

        return contact;
    }
    //get all contacts
    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        //select all contacts from the database table
        String selectAll = "SELECT * FROM " + Utility.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectAll,null);

        //loop through the data
        if(cursor.moveToFirst()){
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));

                //add contact object to list
                contactList.add(contact);
            }while (cursor.moveToNext());
        }
        return contactList;
    }

    //update the contact
    public int updateContact(Contact contact){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utility.KEY_NAME,contact.getName());
        values.put(Utility.KEY_PHONE_NO,contact.getPhoneNumber());

        //update the row
        //update(table_name, contentValues, id = getId)
        return database.update(Utility.TABLE_NAME,values,Utility.KEY_ID+"=?",
                new String[]{String.valueOf(contact.getId())});
    }

    // delete single contact
    public void deleteContact(Contact contact){
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(Utility.TABLE_NAME,Utility.KEY_ID+"=?",
                new String[]{String.valueOf(contact.getId())});
        database.close();
    }
    //get contacts count
    public int getCount(){
        String countQuery = "SELECT * FROM " + Utility.TABLE_NAME ;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery,null);

        return cursor.getCount();
    }
}
