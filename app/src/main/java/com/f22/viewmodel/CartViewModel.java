package com.f22.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;

import com.f22.datamodel.Food;
import com.f22.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<Food>> foodList;

    public LiveData<List<Food>> getFoodItems(Cursor cursor) {
        if (foodList == null) {
            foodList = new MutableLiveData<>();
            loadFoodItems(cursor);
        }

        return foodList;
    }


    private void loadFoodItems(Cursor cursor) {

        List<Food> tempList = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{
                String itemName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_FOODNAME));
                Double itemPrice = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_FOODPRICE));
                Double itemRating = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_FOODRATING));
                int itemQuantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FOODQUANTITY));
                if(itemQuantity!=0) {
                    tempList.add(new Food(itemName, itemPrice, itemRating, itemQuantity));
                }
            }while(cursor.moveToNext());
        }
        cursor.close();

        foodList.setValue(tempList);
    }
}
