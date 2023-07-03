package com.adham.saveme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton add;
    recyclerview_db db2;
    ArrayList<Food> food_list=new ArrayList<>();



    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.whitea));
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerviewH);
        db2=new recyclerview_db(getContext());

        add=(FloatingActionButton)view.findViewById(R.id.addH);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), add_item.class);
                startActivity(i);
            }
        });

        getToken();

        return view;
    }

    public void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    return;
                }
                // Get new Instance ID token
                String token = task.getResult();
                updateToken(token);

            }
        });

    }

    private void updateToken(String token){
        Toast.makeText(getContext()," Token is updated ",Toast.LENGTH_LONG).show();
        rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("token").setValue(token);
    }

    //------------------------------
    private void showRecord() {

        db.collection("Waste Food").document(mAuth.getCurrentUser().getUid()).collection("Food").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                food_list.add(dc.getDocument().toObject(Food.class));
                                Log.e("dataget", dc.getDocument().toObject(Food.class).getName());
                                adapter adapter1=new adapter(getContext(), food_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter1);
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        food_list.clear();
        showRecord();
    }
}