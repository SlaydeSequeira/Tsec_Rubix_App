package com.example.basiclogintoapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    String X="hotels";
    int flag;

    CardView map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        // Retrieve data from Intent
        Intent intent = getIntent();
        String selectedData = intent.getStringExtra("DATA");
        String selectedData2 = intent.getStringExtra("DATA2");
        String selectedAuthor = intent.getStringExtra("AUTHOR");
        String selectedPicture = intent.getStringExtra("PICTURE");
        map=findViewById(R.id.approve);
        X = intent.getStringExtra("X");
        int pos = intent.getIntExtra("pos",0);
        Log.d(TAG, "onCreate: "+X+pos);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference(X).child(String.valueOf(pos));
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String x= String.valueOf(snapshot.child("x").getValue());
                        String y= String.valueOf(snapshot.child("y").getValue());
                        Intent i = new Intent(DisplayActivity.this,MainActivity2.class);
                        i.putExtra("X",x);
                        i.putExtra("Y",y);
                        i.putExtra("Z",selectedData);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        // Set the range of the RatingBar
        ratingBar.setMin(0);
        ratingBar.setMax(5);

        // Set up a listener for when the rating changes
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                databaseReference = FirebaseDatabase.getInstance().getReference(X);
                int ratingInt = (int) rating; // Convert float rating to integer
                flag=1;
                if (flag == 1) {
                // Firebase Realtime Database code to retrieve rating value
                databaseReference.child(String.valueOf(pos)).child("rev").child(String.valueOf(ratingInt)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        flag=0;
                        // Access the value stored at the specific child node under "rev"
                        // Replace YourDataType.class with the actual data type of your data
                        String data = String.valueOf(snapshot.getValue());
                        int temp = Integer.parseInt(data);
                        temp = temp + 1;
                        databaseReference.child(String.valueOf(pos)).child("rev").child(String.valueOf(ratingInt)).setValue(String.valueOf(temp));

                        if (!data.isEmpty()) {
                            // Handle the retrieved data, for example, display it in a Toast
                            Toast.makeText(DisplayActivity.this, "Data for Rating " + rating + ": " + data.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the case where the value is null (not found or not a valid data type)
                            Toast.makeText(DisplayActivity.this, "Data not found for Rating " + rating, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error case
                        Toast.makeText(DisplayActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            }
        });
// Display the data in your activity's UI elements
        TextView textViewData = findViewById(R.id.textViewData);
        TextView textViewData2 = findViewById(R.id.textViewData2);
        TextView textViewAuthor = findViewById(R.id.textViewAuthor);
        ImageView imageView = findViewById(R.id.imageView);

        textViewData.setText(selectedData);
        textViewData2.setText(selectedData2);
        textViewAuthor.setText("Rating: " + selectedAuthor);

        Glide.with(this)
                .load(selectedPicture)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}
