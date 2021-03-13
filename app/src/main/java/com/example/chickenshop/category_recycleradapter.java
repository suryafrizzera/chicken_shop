package com.example.chickenshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chickenshop.model.category_item;

import java.util.ArrayList;
import java.util.List;

public class category_recycleradapter extends RecyclerView.Adapter<category_recycleradapter.category_viewholder> {

    private Context context;
    private List<category_item> category_items = new ArrayList<>();

    public category_recycleradapter(Context context, List<category_item> category_items) {
        this.context = context;
        this.category_items = category_items;
    }

    public category_recycleradapter() {
    }

    @NonNull
    @Override
    public category_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid,parent,false);
        return new category_viewholder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull category_viewholder holder, int position) {
        category_item item = category_items.get(position);
        holder.category_name.setText(item.getCategory());
        holder.category_image.setImageResource(item.getCategory_image());

    }

    @Override
    public int getItemCount() {
        return category_items.size();
    }

    public class category_viewholder extends RecyclerView.ViewHolder{
        ImageButton category_image;
        TextView category_name;
        public category_viewholder(@NonNull View itemView, final Context context) {
            super(itemView);
            category_image = itemView.findViewById(R.id.category_image);
            category_name = itemView.findViewById(R.id.category_text);

            category_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  context.startActivity(new Intent(context.getApplicationContext(),listactivity.class));
                }
            });

        }
    }
}
