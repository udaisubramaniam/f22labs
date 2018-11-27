package com.f22;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.f22.adapter.FoodAdapter;
import com.f22.datamodel.Food;
import com.f22.viewmodel.FoodViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FoodAdapter adapter;
    Button b_filter;
    List<Food> currentList= new ArrayList<>();
    boolean sortPriceFlag = false, sortRatingFlag = false;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerview);
        b_filter = findViewById(R.id.b_filter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FoodViewModel model = ViewModelProviders.of(this).get(FoodViewModel.class);

        model.getFoodItems().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foodList) {
                currentList = foodList;
                adapter = new FoodAdapter(HomeActivity.this, currentList);
                recyclerView.setAdapter(adapter);
            }
        });

        b_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = HomeActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_apply_filter, null);
                dialogBuilder.setView(dialogView);

                RadioGroup rbGroup = dialogView.findViewById(R.id.rb_group);
                RadioButton rbSortPrice = dialogView.findViewById(R.id.rb_sortPrice);
                RadioButton rbSortRating = dialogView.findViewById(R.id.rb_sortRating);

                if(sortPriceFlag){
                    rbSortPrice.setChecked(true);
                } else if(sortRatingFlag){
                    rbSortRating.setChecked(true);
                }
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if(i == R.id.rb_sortPrice){
                            Collections.sort(currentList, new Comparator<Food>() {
                                @Override
                                public int compare(Food o1, Food o2) {
                                    return Double.compare(o1.getItem_price(),o2.getItem_price());
                                }
                            });
                            sortPriceFlag = true;
                            sortRatingFlag = false;
                        } else {
                            Collections.sort(currentList, new Comparator<Food>() {
                                @Override
                                public int compare(Food o1, Food o2) {
                                    return Double.compare(o2.getAverage_rating(),o1.getAverage_rating());
                                }
                            });
                            sortPriceFlag = false;
                            sortRatingFlag = true;
                        }
                        adapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem item = menu.findItem(R.id.menubar_item);
        item.setActionView(R.layout.actionbar_layout);
        RelativeLayout notifCount = (RelativeLayout) item.getActionView();
        TextView tv = notifCount.findViewById(R.id.tv_totalQuantityCount);
        if(count>0) {
            tv.setText(Integer.toString(count));
        } else {
            tv.setText("");
        }

        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>0) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void setCount(int count){
        this.count = count;
        invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", -1);
                int totalCount = data.getIntExtra("totalCount", -1);
                setCount(totalCount);
                adapter.notifyItemChanged(position);
            }
        }
    }
}