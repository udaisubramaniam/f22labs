package com.f22.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.f22.datamodel.Food;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FoodDB.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "foodTable";
    public static final String COLUMN_FOODNAME = "foodname";
    public static final String COLUMN_FOODPRICE = "foodprice";
    public static final String COLUMN_FOODRATING = "foodrating";
    public static final String COLUMN_FOODQUANTITY = "foodquantity";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_FOODNAME + " TEXT PRIMARY KEY, " +
                        COLUMN_FOODPRICE + " DOUBLE, " +
                        COLUMN_FOODRATING + " DOUBLE, " +
                        COLUMN_FOODQUANTITY + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFoodItem(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FOODNAME, food.getItem_name());
        contentValues.put(COLUMN_FOODPRICE, food.getItem_price());
        contentValues.put(COLUMN_FOODRATING, food.getAverage_rating());
        contentValues.put(COLUMN_FOODQUANTITY, food.getQuantity());

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateFoodItem(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FOODNAME, food.getItem_name());
        contentValues.put(COLUMN_FOODPRICE, food.getItem_price());
        contentValues.put(COLUMN_FOODRATING, food.getAverage_rating());
        contentValues.put(COLUMN_FOODQUANTITY, food.getQuantity());
        db.update(TABLE_NAME, contentValues, COLUMN_FOODNAME + " = ? ", new String[] { food.getItem_name() } );
        return true;
    }

    public Cursor getFoodItem(String foodname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_FOODNAME + " = ? ", new String[] { foodname });
        return res;
    }

    public Cursor getAllFoodItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
        return res;
    }

    public int getTotalQuantities(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );

        int currentTotal = 0;
        if (cursor.moveToFirst()){
            do{
                int itemQuantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FOODQUANTITY));
                if(itemQuantity!=0) {
                    currentTotal+=itemQuantity;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();

        return currentTotal;
    }
}