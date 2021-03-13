package com.example.chickenshop.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenshop.R;

public class cart_viewholder extends RecyclerView.ViewHolder {
    public TextView qty, itemname;
    public ImageView chickenimage;
    public Button remove_item;
    public cart_viewholder(@NonNull View itemView) {
        super(itemView);
        qty = itemView.findViewById(R.id.Quantity);
        itemname = itemView.findViewById(R.id.itemname);
        chickenimage = itemView.findViewById(R.id.cart_chickenimage);
        remove_item = itemView.findViewById(R.id.remove_from_cart_button);
    }
}
