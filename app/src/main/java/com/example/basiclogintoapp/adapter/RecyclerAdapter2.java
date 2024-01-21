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
import com.example.basiclogintoapp.VerificationPage;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    private final int count;
    private final String x;
    private final String[] Ver;
    String[] Author;
    String[] data2;
    String[] data;
    Context context;
    String[] picture;

    public RecyclerAdapter2(Context context, String[] data, int count, String[] data2, String[] Author, String[] picture,String x,String[] Ver) {
        this.data = data;
        this.context = context;
        this.count = count;
        this.data2 = data2;
        this.picture = picture;
        this.Author = Author;
        this.x =x;
        this.Ver=Ver;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.add, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;
        if (Ver[position] != null && Ver[position].equals("1")) {
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

        // Add click listener to launch VerificationPage when the image is clicked
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to launch VerificationPage
                Intent intent = new Intent(v.getContext(), VerificationPage.class);

                // Pass data to VerificationPage using Intent extras
                intent.putExtra("itemName", data[position]);
                intent.putExtra("itemDescription", data2[position]);
                intent.putExtra("itemAverageRating", Author[position]);
                intent.putExtra("itemImage", picture[position]);
                intent.putExtra("pos",position);
                intent.putExtra("X",x);
                // Start VerificationPage
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
            imageView = itemView.findViewById(R.id.imageinrecycler);
            id = itemView.findViewById(R.id.text3);
            ver = itemView.findViewById(R.id.ver);

        }
    }
}
