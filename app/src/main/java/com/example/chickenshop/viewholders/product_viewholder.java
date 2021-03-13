package com.example.chickenshop.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenshop.R;

public class product_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView chickenimage;
    public TextView chickenitem,price,spinner;
    public  Button plus,minus;
    public Button addcart;
    public product_viewholder(@NonNull View itemView) {
        super(itemView);
        chickenimage = itemView.findViewById(R.id.chickenitem_image);
        chickenitem = itemView.findViewById(R.id.chickenitem_name);
        price = itemView.findViewById(R.id.price_value);
        spinner = itemView.findViewById(R.id.quantity_text);
        addcart = itemView.findViewById(R.id.addtocart_button);
        plus = itemView.findViewById(R.id.plus_button);
        minus = itemView.findViewById(R.id.minus_button);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.plus_button:
                int increment = Integer.parseInt(spinner.getText().toString()) + 1;
                spinner.setText(String.valueOf(increment));
                break;

            case R.id.minus_button:
                if (Integer.parseInt(spinner.getText().toString()) > 0){
                    int decrement = Integer.parseInt(spinner.getText().toString()) -1;
                    spinner.setText(String.valueOf(decrement));
                }

                break;







                
        }
    }
}
