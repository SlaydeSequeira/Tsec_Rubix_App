package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.basiclogintoapp.adapter.RecyclerAdapter1;
import com.example.basiclogintoapp.adapter.RecyclerAdapter2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPage extends AppCompatActivity {
    String[] data1 = new String[100];
    String[] data2 = new String[100];
    String[] data7 = new String[100];

    String[] Image = new String[100];
    String[] data4 = new String[100];
    String[] data5 = new String[100];
    String[] data6 = new String[100];

    String[] Image2 = new String[100];
    String x = "hotel";
    CardView c1, c2, c3;

    int count = 0;
    int count1 = 0;
    RecyclerView recyclerView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

       recyclerView1 = findViewById(R.id.recyclerView1);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = "restaurant";
                resetPosts();
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = "hotel";
                resetPosts();
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = "site";
                resetPosts();
            }
        });

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setNestedScrollingEnabled(false);

        resetPosts();
    }

    private void resetPosts() {
        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(x);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String temp = snapshot.child("count").getValue().toString();
                count1 = Integer.parseInt(temp);

                for (int i = 0; i < count1; i++) {
                    String temp2 = String.valueOf(i);
                    data4[i] = String.valueOf(snapshot.child(temp2).child("name").getValue());
                    data5[i] = String.valueOf(snapshot.child(temp2).child("description").getValue());
                    data7[i] = String.valueOf(snapshot.child(temp2).child("verified").getValue());
                    DataSnapshot revSnapshot = snapshot.child(temp2).child("rev");

                    int totalStars = 0;
                    int totalPeople = 0;

                    for (DataSnapshot ratingSnapshot : revSnapshot.getChildren()) {
                        String rating = ratingSnapshot.getKey();
                        String countString = String.valueOf(ratingSnapshot.getValue());

                        try {
                            int stars = Integer.parseInt(rating);
                            int people = Integer.parseInt(countString);

                            totalStars += stars * people;
                            totalPeople += people;
                        } catch (NumberFormatException e) {
                            // Handle the exception or log a message, if needed
                            e.printStackTrace();
                        }
                    }

                    if (totalPeople > 0) {
                        double averageStars = (double) totalStars / totalPeople;
                        data6[i] = String.valueOf(averageStars);
                    } else {
                        // Handle the case where totalPeople is 0 to avoid division by zero
                        data6[i] = "0.0";
                    }

                    Image2[i] = String.valueOf(snapshot.child(temp2).child("img").getValue());
                }

                RecyclerAdapter2 adapter1 = new RecyclerAdapter2(AdminPage.this, data4, count1, data5, data6, Image2,x,data7);
                recyclerView1.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(AdminPage.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Log out the user when the activity is destroyed
        FirebaseAuth.getInstance().signOut();
    }
}
