package com.f22.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.f22.interfaces.RetrofitApi;
import com.f22.datamodel.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodViewModel extends ViewModel {

    private MutableLiveData<List<Food>> foodList;

    public LiveData<List<Food>> getFoodItems() {
        if (foodList == null) {
            foodList = new MutableLiveData<>();
            loadFoodItems();
        }

        return foodList;
    }


    private void loadFoodItems() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi api = retrofit.create(RetrofitApi.class);
        Call<List<Food>> call = api.getFoodItems();


        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                foodList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
            }
        });
    }
}