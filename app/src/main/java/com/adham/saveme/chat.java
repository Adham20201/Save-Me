package com.adham.saveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class chat extends AppCompatActivity {

    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    RecyclerView chatRecyclerView;
    EditText editTextMessage;
    FrameLayout sendBtn;

    String foodTitle, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatMessages = new ArrayList<>();
        foodTitle = getIntent().getStringExtra("title");
        userID = getIntent().getStringExtra("id");

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        chatRecyclerView = findViewById(R.id.chatRecycleView);
        editTextMessage = findViewById(R.id.inputMessage);

        chatAdapter = new ChatAdapter(this,chatMessages,mAuth.getCurrentUser().getUid());
        chatRecyclerView.setAdapter(chatAdapter);

        sendBtn = findViewById(R.id.layout_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        listenMessages();
    }

    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put("senderID", mAuth.getCurrentUser().getUid());
        message.put("receiverID","4adjLQy9T2ghrYVekPtaFfkn7FB3");
        message.put("message",editTextMessage.getText().toString());
        message.put("timesTamp",new Date());
        firebaseFirestore.collection("Waste Food").document(mAuth.getCurrentUser().getUid()).collection("Food").document(foodTitle).collection("Chat")
                .document(message.get("timesTamp").toString()).set(message,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editTextMessage.getText().clear();
                    }
                });
    }

    private void listenMessages(){
        firebaseFirestore.collection("Waste Food").document(mAuth.getCurrentUser().getUid()).collection("Food").document(foodTitle).collection("Chat")
                .whereEqualTo("senderID",mAuth.getCurrentUser().getUid())
                .whereEqualTo("receiverID","4adjLQy9T2ghrYVekPtaFfkn7FB3")
                .addSnapshotListener(eventListener);
        firebaseFirestore.collection("Waste Food").document(mAuth.getCurrentUser().getUid()).collection("Food").document(foodTitle).collection("Chat")
                .whereEqualTo("senderID","4adjLQy9T2ghrYVekPtaFfkn7FB3")
                .whereEqualTo("receiverID",mAuth.getCurrentUser().getUid())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return ;
        }
        if (value != null){
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(documentChange.getDocument().getString("senderID"));
                    chatMessage.setReceiverID(documentChange.getDocument().getString("receiverID"));
                    chatMessage.setMessage(documentChange.getDocument().getString("message"));
                    chatMessage.setDateTime(getReadableDateTime(documentChange.getDocument().getDate("timesTamp")));
                    chatMessage.setDateObject(documentChange.getDocument().getDate("timesTamp"));
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages,(obj1,obj2) -> obj1.dateObject.compareTo(obj2.dateObject) );
            if (count ==0){
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
        }

    };

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

}