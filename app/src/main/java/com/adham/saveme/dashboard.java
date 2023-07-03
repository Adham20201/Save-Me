package com.adham.saveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

//has recycler view

public class dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add;
    recyclerview_db db2;
    ArrayList<Food> food_list=new ArrayList<>();


    FirebaseFirestore db;
    FirebaseStorage storage;

//    adapter a=new adapter(dashboard.this,food_list);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        db2=new recyclerview_db(this);

        add=(FloatingActionButton)findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashboard.this, add_item.class);
                startActivity(i);
            }
        });


    }

    //------------------------------
    private void showRecord() {

        db.collectionGroup("Food").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                food_list.add(dc.getDocument().toObject(Food.class));
                                Log.e("dataget", dc.getDocument().toObject(Food.class).getName());
                                adapter adapter1=new adapter(dashboard.this, food_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(dashboard.this));
                                recyclerView.setAdapter(adapter1);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        food_list.clear();
        showRecord();
    }
}