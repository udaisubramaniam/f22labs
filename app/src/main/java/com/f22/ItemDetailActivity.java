package com.f22;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.f22.datamodel.Food;
import com.f22.helper.DBHelper;

public class ItemDetailActivity extends AppCompatActivity {

    TextView tv_name, tv_price, tv_rating, tv_quantity;
    ImageView iv_food, iv_plus, iv_minus;
    DBHelper dbHelper;
    int position;
    Food currentItem;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_item_detail);

        dbHelper = new DBHelper(this);

        currentItem = getIntent().getParcelableExtra("currentItem");
        position = getIntent().getIntExtra("currentPosition", -1);
        initLoad(currentItem);
    }

    void initLoad(final Food currentItem){
        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_rating = findViewById(R.id.tv_rating);
        tv_quantity = findViewById(R.id.tv_quantity);
        iv_food = findViewById(R.id.iv_food);
        iv_plus = findViewById(R.id.iv_plus);
        iv_minus = findViewById(R.id.iv_minus);

        tv_name.setText(currentItem.getItem_name());
        tv_price.setText(getResources().getString(R.string.price, currentItem.getItem_price()));
        tv_rating.setText(Double.toString(currentItem.getAverage_rating()));
        tv_quantity.setText(Integer.toString(currentItem.getQuantity()));
        Glide.with(this)
                .load(currentItem.getImage_url())
                .into(iv_food);

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = currentItem.getQuantity();
                currentItem.setQuantity(++quantity);
                tv_quantity.setText(Integer.toString(quantity));
                dbHelper.updateFoodItem(currentItem);
                invalidateOptionsMenu();
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = currentItem.getQuantity();
                if(quantity!=0){
                    currentItem.setQuantity(--quantity);
                    tv_quantity.setText(Integer.toString(quantity));
                    dbHelper.updateFoodItem(currentItem);
                    invalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("position",position);
                returnIntent.putExtra("totalCount", dbHelper.getTotalQuantities());
                setResult(RESULT_OK,returnIntent);
                finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem item = menu.findItem(R.id.menubar_item);
        item.setActionView(R.layout.actionbar_layout);
        RelativeLayout notifCount = (RelativeLayout)item.getActionView();
        TextView tv = notifCount.findViewById(R.id.tv_totalQuantityCount);
        count = dbHelper.getTotalQuantities();
        if(count>0) {
            tv.setText(Integer.toString(dbHelper.getTotalQuantities()));
        } else {
            tv.setText("");
        }

        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>0) {
                    Intent intent = new Intent(ItemDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}