package com.example.basiclogintoapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basiclogintoapp.Model.Chat;
import com.example.basiclogintoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> implements TextToSpeech.OnInitListener {

    private Context context;
    private List<Chat> mChat;
    private String imgURL;
    private TextToSpeech textToSpeech;

    // Firebase
    FirebaseUser fuser;

    public static final  int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT  = 1;


    // Constructor
    public MessageAdapter(Context context, List<Chat> mChat, String imgURL) {
        this.context = context;
        this.mChat = mChat;
        this.imgURL = imgURL;
        textToSpeech = new TextToSpeech(context, this);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,
                    parent,
                    false);
            return new ViewHolder(view);

        } else {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,
                    parent,
                    false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        if (chat.getMessage().startsWith("/") && chat.getMessage().endsWith("/")) {
            String textWithoutSlashes = chat.getMessage().replace("/", "");
            holder.show_message.setText(textWithoutSlashes);
            holder.show_message.setTypeface(null, Typeface.ITALIC);
        } else if (chat.getMessage().startsWith("*") && chat.getMessage().endsWith("*")) {
            String textWithoutSlashes = chat.getMessage().replace("*", "");
            holder.show_message.setText(textWithoutSlashes);
            holder.show_message.setTypeface(null, Typeface.BOLD);
        }
        else{
            holder.show_message.setText(chat.getMessage());
            holder.show_message.setTypeface(null, Typeface.NORMAL);
        }
        if (imgURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imgURL).into(holder.profile_image);
        }

        if (position == mChat.size() -1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else{
                holder.txt_seen.setText("Delivered");
            }
        }
        else
        {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen_status);

            // Adding double tap listener to show_message TextView
            show_message.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        // Perform your action here on double tap
                        String message = show_message.getText().toString();
                        // Toast the message on double tap
                        speak(message);
                        return true;
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }

    }

    private void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported or missing data
            }
        } else {
            // Handle initialization failure
        }
    }

    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
