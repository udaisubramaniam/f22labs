package com.f22;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.f22.adapter.CartAdapter;
import com.f22.datamodel.Food;
import com.f22.helper.DBHelper;
import com.f22.viewmodel.CartViewModel;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tv_allItemsTotal, tv_deliveryCharge, tv_grandTotal;
    Button b_applyCoupon;
    CartAdapter adapter;
    DBHelper dbHelper;
    double allItemsTotal = 0;
    double deliveryCharge = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerview);
        tv_allItemsTotal = findViewById(R.id.tv_allItemsTotal);
        tv_deliveryCharge = findViewById(R.id.tv_deliveryCharge);
        tv_grandTotal = findViewById(R.id.tv_grandTotal);
        b_applyCoupon = findViewById(R.id.b_applyCoupon);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        dbHelper = new DBHelper(this);

        CartViewModel model = ViewModelProviders.of(this).get(CartViewModel.class);

        model.getFoodItems(dbHelper.getAllFoodItems()).observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foodList) {
                adapter = new CartAdapter(CartActivity.this, foodList);
                recyclerView.setAdapter(adapter);
                updateItemTotal(foodList);
            }
        });

        b_applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartActivity.this);
                LayoutInflater inflater = CartActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_apply_coupon, null);
                dialogBuilder.setView(dialogView);

                final EditText et_coupon = dialogView.findViewById(R.id.et_coupon);
                Button b_apply = dialogView.findViewById(R.id.b_apply);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                b_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_coupon.getText().toString().equals("F22LABS")){
                            alertDialog.dismiss();
                            if(allItemsTotal>=400) {
                                Toast.makeText(CartActivity.this, "20% discount applied successfully", Toast.LENGTH_SHORT).show();
                                applyDiscount(allItemsTotal*0.8, deliveryCharge);
                            } else {
                                Toast.makeText(CartActivity.this, "Min cart value should be 400", Toast.LENGTH_SHORT).show();
                            }
                        } else if(et_coupon.getText().toString().equals("FREEDEL")){
                            alertDialog.dismiss();
                            if(allItemsTotal>=100) {
                                Toast.makeText(CartActivity.this, "Free delivery applied successfully", Toast.LENGTH_SHORT).show();
                                applyDiscount(allItemsTotal, 0.0);
                            } else {
                                Toast.makeText(CartActivity.this, "Min order amount should be 100", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                                Toast.makeText(CartActivity.this, "Invalid coupon code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    void applyDiscount(Double allItemsTotal, Double deliveryCharge){
        tv_allItemsTotal.setText(getString(R.string.all_items_total, allItemsTotal));
        tv_deliveryCharge.setText(getString(R.string.delivery_charge, deliveryCharge));
        tv_grandTotal.setText(getString(R.string.grand_total, allItemsTotal + deliveryCharge));
    }

    void updateItemTotal(List<Food> foodList){
        for(int i=0;i<foodList.size();i++){
            allItemsTotal+= foodList.get(i).getQuantity()*foodList.get(i).getItem_price();
        }
        tv_allItemsTotal.setText(getString(R.string.all_items_total, allItemsTotal));
        tv_deliveryCharge.setText(getString(R.string.delivery_charge, deliveryCharge));
        tv_grandTotal.setText(getString(R.string.grand_total, allItemsTotal + deliveryCharge));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}