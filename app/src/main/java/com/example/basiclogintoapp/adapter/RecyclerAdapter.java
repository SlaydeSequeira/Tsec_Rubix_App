package com.example.basiclogintoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.R;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final int count;
    private String[] data;
    private String[] data2;
    private String[] picture;
    private String[] location;
    private Context context;

    public RecyclerAdapter(Context context, String[] data, int count, String[] data2, String[] location, String[] picture) {
        this.context = context;
        this.data = data;
        this.data2 = data2;
        this.picture = picture;
        this.location = location;
        this.count=count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data[position]);
        holder.textView2.setText(data2[position]);
        holder.id.setText("Qty: "+location[position]);
        Glide.with(context)
                .load(picture[position])
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent i = new Intent(v.getContext(),);
               // v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
        TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            textView2 = itemView.findViewById(R.id.text2);
            imageView = itemView.findViewById(R.id.image);
            id = itemView.findViewById(R.id.text3);
        }
    }
}
