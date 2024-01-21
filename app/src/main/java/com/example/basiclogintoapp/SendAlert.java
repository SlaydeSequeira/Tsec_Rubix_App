package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class SendAlert extends AppCompatActivity {

    CardView c1,c2,c3;
    private EditText editTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_send_alert);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        c1=findViewById(R.id.card1);
        c2=findViewById(R.id.card2);
        c3=findViewById(R.id.card3);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyNumber= "100";
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + emergencyNumber));

                // Start the dialer activity
                startActivity(dialIntent);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyNumber= "102";
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + emergencyNumber));

                // Start the dialer activity
                startActivity(dialIntent);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyNumber= "108";
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + emergencyNumber));

                // Start the dialer activity
                startActivity(dialIntent);
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendNotificationToAllUsers(message);
                }
            }
        });
    }

    private void sendNotificationToAllUsers(String message) {
        FirebaseMessaging.getInstance().subscribeToTopic("allUsers")
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successfully subscribed to the topic, now send the message
                        // You can modify the 'data' payload as needed
                        FCMUtils.sendNotificationToTopic("allUsers", "Notification", message);
                    } else {
                        Toast.makeText(SendAlert.this,"failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
