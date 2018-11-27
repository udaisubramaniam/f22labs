package com.f22.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private String item_name;
    private double item_price;
    private double average_rating;
    private String image_url;
    private int quantity;

    public Food(String item_name, double item_price, double average_rating, int quantity) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.average_rating = average_rating;
        this.quantity = quantity;
    }

    public Food(String item_name, double item_price, double average_rating, String image_url, int quantity) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.average_rating = average_rating;
        this.image_url = image_url;
        this.quantity = quantity;
    }

    public Food(Parcel in) {
        item_name = in.readString();
        item_price = in.readDouble();
        average_rating = in.readDouble();
        image_url = in.readString();
        quantity = in.readInt();
    }

    public String getItem_name() {
        return item_name;
    }

    public double getItem_price() {
        return item_price;
    }

    public double getAverage_rating() {
        return average_rating;
    }

    public String getImage_url() { return image_url; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(item_name);
        parcel.writeDouble(item_price);
        parcel.writeDouble(average_rating);
        parcel.writeString(image_url);
        parcel.writeInt(quantity);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}