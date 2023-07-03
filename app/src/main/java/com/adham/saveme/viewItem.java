package com.adham.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class viewItem extends AppCompatActivity {

    TextInputLayout title,name, address, description;
    ImageView img;
    Button request;

    String foodTitle, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        title=findViewById(R.id.title);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        description=findViewById(R.id.description);
        img=(ImageView)findViewById(R.id.image);
        request=(Button)findViewById(R.id.request);

        foodTitle = getIntent().getStringExtra("title");
        userID = getIntent().getStringExtra("id");


        name.getEditText().setText(getIntent().getStringExtra("name"));

        title.getEditText().setText(foodTitle);

        description.getEditText().setText(getIntent().getStringExtra("description"));

        address.getEditText().setText(getIntent().getStringExtra("address"));

        Picasso.get()
                .load(getIntent().getStringExtra("image"))
                .fit()
                .centerCrop()
                .into(img);


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(viewItem.this, done.class);
                i.putExtra("title",foodTitle);
                i.putExtra("id",userID);
                startActivity(i);
            }
        });
    }
}