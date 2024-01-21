package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// ... (other imports)

public class VerificationPage extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            final String itemName = intent.getStringExtra("itemName");
            final String itemDescription = intent.getStringExtra("itemDescription");
            final String itemAverageRating = intent.getStringExtra("itemAverageRating");
            final String itemImage = intent.getStringExtra("itemImage");
            final int pos = intent.getIntExtra("pos", 0);
            final String type = intent.getStringExtra("X");

            // Now you have the data, you can use it as needed in your VerificationPage
            // Example: Update UI elements with the retrieved data
            TextView itemNameTextView = findViewById(R.id.itemNameTextView);
            TextView itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
            TextView itemAverageRatingTextView = findViewById(R.id.itemAverageRatingTextView);
            ImageView itemImageView = findViewById(R.id.itemImageView);

            itemNameTextView.setText(itemName);
            itemDescriptionTextView.setText(itemDescription);
            itemAverageRatingTextView.setText("Average Rating: " + itemAverageRating + "/5 star");

            // Load the image using a library like Glide or Picasso
            // Example using Glide:
            Glide.with(this).load(itemImage).into(itemImageView);

            // Set onClickListener for the CardView
            CardView c1 = findViewById(R.id.approve);
            c1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Fetch "verified" value from Firebase
                    fetchVerifiedValue(type, pos);
                }
            });
        }
    }

    private void fetchVerifiedValue(String type, int pos) {
        DatabaseReference typeRef = databaseReference.child(type).child(String.valueOf(pos)).child("verified");
        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Check the data type and convert accordingly
                    Object value = dataSnapshot.getValue();
                    if (value instanceof Long) {
                        boolean verified = ((Long) value).intValue() != 0; // Convert Long to boolean
                        Log.d("VerificationPage", "Verified: " + verified);
                    } else {
                        Log.e("VerificationPage", "Invalid data type for 'verified': " + value.getClass().getSimpleName());
                    }

                    // Log values of "type" and "pos"
                    Log.d("VerificationPage", "Type: " + type);
                    Log.d("VerificationPage", "Pos: " + pos);
                } else {
                    Log.d("VerificationPage", "Data not found for Type: " + type + ", Pos: " + pos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("VerificationPage", "Error fetching data: " + databaseError.getMessage());
            }
        });
        typeRef.setValue(1);
        Toast.makeText(VerificationPage.this,"Buisness Verified Successfully",Toast.LENGTH_SHORT).show();
    }

}
