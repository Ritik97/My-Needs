package com.example.myneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myneeds.model.Item;
import com.example.myneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO: Create the table
        String CREATE_MY_NEEDS_TABLE = " CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_ITEM_SIZE + " TEXT,"
                + Constants.KEY_ITEM_PRICE + " REAL,"
                + Constants.KEY_ITEM_QUANTITY + " TEXT,"
                + Constants.KEY_DATE_ADDED + " LONG);";

        db.execSQL(CREATE_MY_NEEDS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    //CRUD Operations

    //TODO: Adding a single item
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, item.getItemName());
        values.put(Constants.KEY_ITEM_SIZE, item.getItemSize());
        values.put(Constants.KEY_ITEM_PRICE, item.getItemPrice());
        values.put(Constants.KEY_ITEM_QUANTITY, item.getItemQuantity());
        values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());// timestamp of the system

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("DB Handler", "added Item: ");

    }

    //TODO: Getting a single item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM,
                        Constants.KEY_ITEM_SIZE,
                        Constants.KEY_ITEM_PRICE,
                        Constants.KEY_ITEM_QUANTITY,
                        Constants.KEY_DATE_ADDED},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Item item = new Item();
        if (cursor != null) {
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            item.setItemSize(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));
            item.setItemPrice(Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_PRICE))));
            item.setItemQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_QUANTITY)));
            ///item.setDateAdded(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)));

            //TODO: Convert Timestamp to something readable
            /*Internally, we saved the date as a timestamp, which is a long number. Here, we are converting the timestamp
             * into a readable string, and then storing it in the item object, as a string*/

            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)))
                    .getTime()); // Apr 10, 2020

            item.setDateAdded(formattedDate);
        }

        return item;

    }

    //TODO: Getting all the items
    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM,
                        Constants.KEY_ITEM_SIZE,
                        Constants.KEY_ITEM_PRICE,
                        Constants.KEY_ITEM_QUANTITY,
                        Constants.KEY_DATE_ADDED},
                null, null, null, null,
                Constants.KEY_DATE_ADDED + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                item.setItemSize(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));
                item.setItemPrice(Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_PRICE))));
                item.setItemQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_QUANTITY)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)))
                        .getTime()); // Apr 10, 2020

                item.setDateAdded(formattedDate);

                //Adding item object into the list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;

    }

    //TODO: Updating an item
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, item.getItemName());
        values.put(Constants.KEY_ITEM_SIZE, item.getItemSize());
        values.put(Constants.KEY_ITEM_PRICE, item.getItemPrice());
        values.put(Constants.KEY_ITEM_QUANTITY, item.getItemQuantity());
        values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());// timestamp of the system

        //update row
        return db.update(Constants.TABLE_NAME, values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});

    }

    //TODO: Delete Item
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        //close
        db.close();
    }

    //TODO: Get Item Count
    public int getItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        Cursor cursor = db.rawQuery(countQuery, null);


        return cursor.getCount();
    }
}


