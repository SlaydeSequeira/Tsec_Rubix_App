package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.Model.OrderItem;
import com.example.basiclogintoapp.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create dummy data
        List<OrderItem> dummyData = createDummyData();
        // Initialize the adapter with the dummy data
        orderAdapter = new OrderAdapter(this, dummyData);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(orderAdapter);
    }

    private List<OrderItem> createDummyData() {
        List<OrderItem> dummyData = new ArrayList<>();

        // Add some dummy items
        dummyData.add(new OrderItem("Title 1", "Description 1", "$10", "https://dummyimage.com/100x100/000/fff"));
        dummyData.add(new OrderItem("Title 2", "Description 2", "$15", "https://dummyimage.com/100x100/000/fff"));
        dummyData.add(new OrderItem("Title 3", "Description 3", "$20", "https://dummyimage.com/100x100/000/fff"));

        return dummyData;
    }
}