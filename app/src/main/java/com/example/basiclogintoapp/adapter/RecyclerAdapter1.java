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
import com.example.basiclogintoapp.DisplayActivity;
import com.example.basiclogintoapp.R;


public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.ViewHolder>
{

    private final int count;
    String[] Verified;
    String[] Author;
    String[] data2;
    String[] data;
    Context context;
    String[] picture;
    String x;

    public RecyclerAdapter1(Context context, String[] data, int count, String[] data2, String[] Author, String[] picture, String x,String[] Verified)
    {
        this.data = data;
        this.context= context;
        this.count= count;
        this.data2 = data2;
        this.picture=picture;
        this.Author=Author;
        this.x=x;
        this.Verified=Verified;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.add,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;

        if (Verified[position] != null && Verified[position].equals("1")) {
            if (holder.ver != null) {
                holder.ver.setVisibility(View.VISIBLE);
            }
        }

        holder.textView.setText(data[position]);
        holder.textView2.setText(data2[position]);
        holder.id.setText("Rating: " + Author[position]);
        Glide.with(context)
                .load(picture[position])
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the clicked item
                int clickedPosition = position;

                // Prepare the data to be sent to the DisplayActivity
                String selectedData = data[clickedPosition];
                String selectedData2 = data2[clickedPosition];
                String selectedAuthor = Author[clickedPosition];
                String selectedPicture = picture[clickedPosition];

                // Create an Intent to start DisplayActivity
                Intent intent = new Intent(v.getContext(), DisplayActivity.class);

                // Put the data into the Intent
                intent.putExtra("DATA", selectedData);
                intent.putExtra("DATA2", selectedData2);
                intent.putExtra("AUTHOR", selectedAuthor);
                intent.putExtra("PICTURE", selectedPicture);
                intent.putExtra("X",x);
                intent.putExtra("pos",clickedPosition);

                // Start the DisplayActivity
                v.getContext().startActivity(intent);
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
        ImageView ver;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            textView2 = itemView.findViewById(R.id.text2);
            ver = itemView.findViewById(R.id.ver);
            imageView= itemView.findViewById(R.id.imageinrecycler);
            id = itemView.findViewById(R.id.text3);
        }
    }
}
