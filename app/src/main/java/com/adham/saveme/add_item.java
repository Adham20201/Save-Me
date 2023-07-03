package com.adham.saveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;



//ITEM DETAILS ACTIVITY

public class add_item extends AppCompatActivity {

    EditText title, name, address, description;
    ImageView img;
    Button add_item;
    ImageButton imgbtn;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private String title1, name1, address1, description1;
    private recyclerview_db recyclerview_db;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReferencePrograms;

    Food food;
    private Uri imageUri;

    RadioButton phoneModeBtn, arduinoModeBtn, tamerModeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        food = new Food();

        title = (EditText) findViewById(R.id.food_title);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        description = (EditText) findViewById(R.id.description);
        imgbtn = (ImageButton) findViewById(R.id.add_pictures);

        img = (ImageView) findViewById(R.id.image);
        add_item = (Button) findViewById(R.id.add_btn);


        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2){
            cameraPermissions = new String[]{android.Manifest.permission.CAMERA};
        } else{
            cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            storagePermissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }



        recyclerview_db =new recyclerview_db(this);

        //open gallery or camera
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });


        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoodData();
                uploadFoodImage();
            }
        });


        phoneModeBtn = findViewById(R.id.PhoneModeRadio);
        arduinoModeBtn = findViewById(R.id.ArduinoModeRadio);
        tamerModeBtn = findViewById(R.id.tamerRadio);

        phoneModeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (phoneModeBtn.isChecked() & arduinoModeBtn.isChecked()){
                    arduinoModeBtn.setChecked(false);
                }
                if (phoneModeBtn.isChecked() & tamerModeBtn.isChecked()){
                    tamerModeBtn.setChecked(false);
                }
                if (phoneModeBtn.isChecked()){
                    food.setType("Sweety");
                };
            }
        });

        arduinoModeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (arduinoModeBtn.isChecked() & phoneModeBtn.isChecked()){
                    phoneModeBtn.setChecked(false);
                }
                if (arduinoModeBtn.isChecked() & tamerModeBtn.isChecked()){
                    tamerModeBtn.setChecked(false);
                }
                if (arduinoModeBtn.isChecked()){
                    food.setType("Salty");
                };
            }
        });

        tamerModeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tamerModeBtn.isChecked() & phoneModeBtn.isChecked()){
                    phoneModeBtn.setChecked(false);
                }
                if (tamerModeBtn.isChecked() & arduinoModeBtn.isChecked()){
                    arduinoModeBtn.setChecked(false);
                }
                if (tamerModeBtn.isChecked()){
                    food.setType("Not defined");
                };
            }
        });

    }

    private void getFoodData(){
        name1= name.getText().toString().trim();
        title1= title.getText().toString().trim();
        address1= address.getText().toString().trim();
        description1= description.getText().toString().trim();


        food.setTitle(title1);
        food.setName(name1);
        food.setAddress(address1);
        food.setDescription(description1);
        food.setId(mAuth.getCurrentUser().getUid());
    }

    private void uploadFoodImage(){

        storageReferencePrograms = storage.getReference("Waste Food").child(mAuth.getCurrentUser().getUid()).child("Food").child(food.getTitle());
        StorageReference fileReferenceBackground = storageReferencePrograms.child(food.getTitle());

        UploadTask uploadTask = fileReferenceBackground.putFile(imageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return fileReferenceBackground.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    food.setImage(downloadUri.toString());

                    uploadFood();
                }
            }
        });

    }

    private void uploadFood(){
        db.collection("Waste Food").document(mAuth.getCurrentUser().getUid()).collection("Food").document(food.getTitle())
                .set(food, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(add_item.this, "Done", Toast.LENGTH_SHORT).show();


                        name.getText().clear();
                        title.getText().clear();
                        address.getText().clear();
                        description.getText().clear();

                        img.setImageResource(R.drawable.image);

                        if (tamerModeBtn.isChecked() ){
                            tamerModeBtn.setChecked(false);
                        }
                        else if (arduinoModeBtn.isChecked()){
                            arduinoModeBtn.setChecked(false);
                        }
                        else if (phoneModeBtn.isChecked()){
                            phoneModeBtn.setChecked(false);
                        };

                    }
                });
    }

    private void getData() {
        name1= name.getText().toString().trim();
        title1= title.getText().toString().trim();
        address1= address.getText().toString().trim();
        description1= description.getText().toString().trim();


        food.setTitle(title1);
        food.setName(name1);
        food.setAddress(address1);
        food.setDescription(description1);

        recyclerview_db.addItem(
                ""+title1,
                ""+name1,
                ""+address1,
                ""+description1,
                ""+imageUri
        );
        Toast.makeText(this, "record added", Toast.LENGTH_LONG).show();
        startActivity(new Intent (add_item.this, dashboard.class));
    }

    private void imagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select for image");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2){
                        pickFromCamera();
                    } else{
                        if (!checkCameraPermissions()) {
                            requestCameraPermission();
                        } else {
                            pickFromCamera();
                        }
                    }

                } else if (which == 1) {
                    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2){
                        pickFromStorage();
                    } else{
                        if (!checkStoragePermission()) {
                            requestStoragePermissions();
                        } else {
                            pickFromStorage();

                        }
                    }

                }
            }
        });
        builder.create().show();
    }

    private void pickFromStorage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_GALLERY_CODE);
    }


    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(i, IMAGE_PICK_CAMERA_CODE);


    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    public void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Storage permission required", Toast.LENGTH_LONG).show();
                    }

                }


            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if(resultCode==RESULT_OK)
        {
            if(requestCode==IMAGE_PICK_GALLERY_CODE)
            {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON )
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode ==IMAGE_PICK_CAMERA_CODE)
            {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON )
                        .setAspectRatio(1,1)
                        .start(this);

            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                if(resultCode==RESULT_OK)
                {
                    Uri resultUri =result.getUri();
                    imageUri=resultUri;
                    img.setImageURI(resultUri);
                }
                else if (resultCode ==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error=result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_LONG).show();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}