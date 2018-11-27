package com.f22.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.f22.helper.DBHelper;
import com.f22.R;
import com.f22.datamodel.Food;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Food> foodList;
    DBHelper dbHelper;

    public CartAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final Food currentItem = foodList.get(position);

        holder.tv_name.setText(currentItem.getItem_name());
        holder.tv_price.setText(context.getResources().getString(R.string.price,currentItem.getItem_price()));

        Cursor cursor = dbHelper.getFoodItem(currentItem.getItem_name());
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            int currentQuantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FOODQUANTITY));
            currentItem.setQuantity(currentQuantity);
            holder.tv_quantity.setText(context.getString(R.string.item_quantity, currentQuantity));
            holder.tv_totalItemPrice.setText(context.getString(R.string.item_total, currentQuantity*currentItem.getItem_price()));
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_plus,iv_minus;
        TextView tv_name, tv_price, tv_quantity, tv_totalItemPrice;

        CartViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_totalItemPrice = itemView.findViewById(R.id.tv_totalItemprice);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}