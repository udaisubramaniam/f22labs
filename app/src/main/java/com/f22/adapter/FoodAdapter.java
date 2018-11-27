package com.f22.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f22.helper.DBHelper;
import com.f22.HomeActivity;
import com.f22.ItemDetailActivity;
import com.f22.R;
import com.f22.datamodel.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    Context context;
    List<Food> foodList;
    DBHelper dbHelper;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        dbHelper = new DBHelper(context);
        ((HomeActivity)context).setCount(dbHelper.getTotalQuantities());
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, final int position) {
        final Food currentItem = foodList.get(position);

        Glide.with(context)
                .load(currentItem.getImage_url())
                .into(holder.iv_food);

        holder.tv_name.setText(currentItem.getItem_name());
        holder.tv_rating.setText(Double.toString(currentItem.getAverage_rating()));
        holder.tv_price.setText(context.getResources().getString(R.string.price, currentItem.getItem_price()));

        Cursor cursor = dbHelper.getFoodItem(currentItem.getItem_name());
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            int currentQuantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FOODQUANTITY));
            currentItem.setQuantity(currentQuantity);
            holder.tv_quantity.setText(Integer.toString(currentQuantity));
        } else {
            dbHelper.insertFoodItem(currentItem);
        }
        cursor.close();

        holder.iv_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("currentItem", currentItem);
                intent.putExtra("currentPosition", position);
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = currentItem.getQuantity();
                currentItem.setQuantity(++quantity);
                holder.tv_quantity.setText(Integer.toString(quantity));
                dbHelper.updateFoodItem(currentItem);
                ((HomeActivity)context).setCount(dbHelper.getTotalQuantities());
            }
        });

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = currentItem.getQuantity();
                if(quantity!=0){
                    currentItem.setQuantity(--quantity);
                    holder.tv_quantity.setText(Integer.toString(quantity));
                    dbHelper.updateFoodItem(currentItem);
                    ((HomeActivity)context).setCount(dbHelper.getTotalQuantities());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_food, iv_plus,iv_minus;
        TextView tv_name, tv_rating, tv_price, tv_quantity;

        FoodViewHolder(View itemView) {
            super(itemView);

            iv_food = itemView.findViewById(R.id.iv_food);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_price = itemView.findViewById(R.id.tv_price);

            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            iv_plus = itemView.findViewById(R.id.iv_plus);
            iv_minus = itemView.findViewById(R.id.iv_minus);

        }
    }
}