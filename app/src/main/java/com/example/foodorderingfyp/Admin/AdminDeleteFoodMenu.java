package com.example.foodorderingfyp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorderingfyp.ModelClass.Foods;
import com.example.foodorderingfyp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.AdminFoodViewHolder;

public class AdminDeleteFoodMenu extends AppCompatActivity {

    private DatabaseReference FoodsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_food_menu);


        // to get Database Foods table Reference
        FoodsRef = FirebaseDatabase.getInstance().getReference().child("Foods");

        recyclerView = findViewById(R.id.rv_recycleView);
        recyclerView.setHasFixedSize(true);

        // put all item in same layout in recyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();


        // to configure adapter
        FirebaseRecyclerOptions<Foods> options =
                new FirebaseRecyclerOptions.Builder<Foods>()
                        .setQuery(FoodsRef, Foods.class)
                        .build();


        // <T, ViewHolder>
        FirebaseRecyclerAdapter<Foods, AdminFoodViewHolder> adapter =
                new FirebaseRecyclerAdapter<Foods, AdminFoodViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminFoodViewHolder holder, int position, @NonNull Foods model)
                    {
                        // holder from FoodViewHolder.java
                        holder.txtAdminFoodName.setText(model.getFoodName());
                        holder.txtAdminFoodPrice.setText("RM " + model.getFoodPrice());

                        // easy way to get Image from database
                        Picasso.get().load(model.getFoodImage()).into(holder.adminImageView);


                        // get Food ID
                        holder.itemView.setOnClickListener((view) -> {
                            Intent intent = new Intent(AdminDeleteFoodMenu.this,AdminDeleteFoodItem.class);
                            intent.putExtra("foodName", model.getFoodName());
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override   // copy and create view into recyclerView from food_items_layout.xml
                    public AdminFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_food_item_layout, parent, false);
                        AdminFoodViewHolder holder = new AdminFoodViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}