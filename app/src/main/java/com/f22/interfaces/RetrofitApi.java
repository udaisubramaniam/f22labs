package com.f22.interfaces;

import com.f22.datamodel.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    String BASE_URL = "https://android-full-time-task.firebaseio.com/";

    @GET("data.json")
    Call<List<Food>> getFoodItems();
}