package com.unibague.pazyregion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unibague.pazyregion.Model.Product;
import com.unibague.pazyregion.R;
import com.unibague.pazyregion.Shareds.SharedPreferenceUserData;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RC_SIGN_IN=1;
    public static final int IMAGE_COD=2;
    public static final int INTENT_GALLERY=1;

    FirebaseAuth firebaseAuthB;
    FirebaseFirestore firebaseFirestoreB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    StorageReference storageReference;

    SharedPreferenceUserData sharedPreferenceUserData;

    TextInputLayout text_input_layout_product_title;
    TextInputLayout text_input_layout_product_availability;
    TextInputLayout text_input_layout_product_description;
    TextInputLayout text_input_layout_product_price;

    TextInputEditText text_input_edit_product_title;
    TextInputEditText text_input_edit_product_availability;
    TextInputEditText text_input_edit_product_description;
    TextInputEditText text_input_edit_product_price;

    ImageView image_view_product;

    MaterialButton material_button_photo;
    MaterialButton material_button_gallery;
    MaterialButton material_button_product_register;



    String path;
    File fileImage;
    Bitmap bitmap;
    Uri imageUri;
    Uri resultStartCrop;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_register);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();
        firebaseCrashlyticsB= FirebaseCrashlytics.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog= new ProgressDialog(this);

        text_input_layout_product_title= findViewById(R.id.text_input_layout_product_title);
        text_input_layout_product_availability= findViewById(R.id.text_input_layout_product_availability);
        text_input_layout_product_description= findViewById(R.id.text_input_layout_product_description);
        text_input_layout_product_price= findViewById(R.id.text_input_layout_product_price);

        text_input_edit_product_title= findViewById(R.id.text_input_edit_product_title);
        text_input_edit_product_availability= findViewById(R.id.text_input_edit_product_availability);
        text_input_edit_product_description= findViewById(R.id.text_input_edit_product_description);
        text_input_edit_product_price= findViewById(R.id.text_input_edit_product_price);

        image_view_product=findViewById(R.id.image_view_product);

        material_button_photo= findViewById(R.id.material_button_photo);
        material_button_gallery=findViewById(R.id.material_button_gallery);
        material_button_product_register=findViewById(R.id.material_button_product_register);

        sharedPreferenceUserData = new SharedPreferenceUserData(this);

        material_button_product_register.setOnClickListener(this);
        material_button_gallery.setOnClickListener(this);
        material_button_photo.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(ProductRegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProductRegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProductRegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

    }

    private String loadNameUser(){
        String nameUser= sharedPreferenceUserData.getNameUser();
        return nameUser;
    }

    private String loadEmailUser(){
        String emailUser= sharedPreferenceUserData.getEmailUser();
        return emailUser;
    }

    private String loadPhone(){
        String phoneUser = sharedPreferenceUserData.getPhoneUser();
        return phoneUser;
    }

    private String date() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateandTime = simpleDateFormat.format(new Date());
        return currentDateandTime;
    }

    private void galleryPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,INTENT_GALLERY);
    }

    private void takePhoto(){
        try{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileImage = createImageFile();
            imageUri = FileProvider.getUriForFile(ProductRegisterActivity.this, "com.unibague.pazyregion.provider", fileImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, IMAGE_COD);
            }
        }catch (Exception e){
            Log.w("TAG", "takePhoto in failed", e);
            firebaseCrashlyticsB.log("takePhoto");
            firebaseCrashlyticsB.recordException(e);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "evidencia" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png", storageDir
        );
        path = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_COD && resultCode==RESULT_OK){
            startCrop(imageUri);
        }else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            resultStartCrop = UCrop.getOutput(data);
            try {
                if (resultStartCrop != null) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultStartCrop));
                    image_view_product.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(ProductRegisterActivity.this, "Por favor, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                Log.w("TAG", "IMAGE_COD in failed", e);
                firebaseCrashlyticsB.log("onActivityResult");
                firebaseCrashlyticsB.recordException(e);
            }
        }

        if(requestCode==INTENT_GALLERY && resultCode==RESULT_OK){
            Uri imageUri = data.getData();
            if(imageUri!=null){
                startCrop(imageUri);
            }
        }else if (requestCode== UCrop.REQUEST_CROP&& resultCode==RESULT_OK){
            resultStartCrop = UCrop.getOutput(data);
            try {
                if (resultStartCrop != null) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultStartCrop));
                    image_view_product.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(ProductRegisterActivity.this, "Por favor, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                Log.w("TAG", "INTENT_GALLERYin failed", e);
                firebaseCrashlyticsB.log("onActivityResult");
                firebaseCrashlyticsB.recordException(e);
            }
        }

    }

    private void startCrop(@NonNull Uri uri){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "note"+timeStamp;
        UCrop ucrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),imageFileName)));
        ucrop.useSourceImageAspectRatio();
        ucrop.withMaxResultSize(1080,1080);
        ucrop.withOptions(getCropOptions());
        ucrop.start(ProductRegisterActivity.this);
    }
    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setToolbarTitle("Recortar Imagen");
        return options;
    }

    private void loadData(){

        try{
            String title =text_input_edit_product_title.getText().toString().trim();
            String date=date();
            String availability=text_input_edit_product_availability.getText().toString().trim();
            String description=text_input_edit_product_description.getText().toString().trim();
            String price=text_input_edit_product_price.getText().toString().trim();
            String email=loadEmailUser();
            String phone= loadPhone();
            String nameUser= loadNameUser();


            if(!title.isEmpty() && !availability.isEmpty() && !description.isEmpty() && !price.isEmpty()){
                text_input_layout_product_title.setError(null);
                text_input_layout_product_availability.setError(null);
                text_input_layout_product_description.setError(null);
                text_input_layout_product_price.setError(null);

                progressDialog.setTitle("Cargando...");
                progressDialog.setMessage("Espere un momento.");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StorageReference filePath = storageReference.child(email).child("photo").child(resultStartCrop.getLastPathSegment());
                filePath.putFile(resultStartCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> downloadPhoto = taskSnapshot.getStorage().getDownloadUrl();
                        while(!downloadPhoto.isComplete());
                        Uri url = downloadPhoto.getResult();
                        Product product = new Product(title, date,availability,description,price,email,phone,url.toString(),nameUser);
                        firebaseFirestoreB.collection("Product").document().set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                onBackPressed();
                                Toast.makeText(ProductRegisterActivity.this,"Se guardo exitoxamente!",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                });



            }if(title.isEmpty()){
                text_input_layout_product_title.setError("Este campo es importante");
                text_input_layout_product_title.requestFocus();
            }if(availability.isEmpty()){
                text_input_layout_product_availability.setError("Este campo es importante");
                text_input_layout_product_availability.requestFocus();
            }if(description.isEmpty()){
                text_input_layout_product_description.setError("Este campo es importante");
                text_input_layout_product_description.requestFocus();
            }if(price.isEmpty()){
                text_input_layout_product_price.setError("Este campo es importante");
                text_input_layout_product_price.requestFocus();
            }
        }catch (Exception e){
            firebaseCrashlyticsB.log("loadData");
            firebaseCrashlyticsB.recordException(e);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_button_product_register:
                loadData();
                break;
            case R.id.material_button_photo:
                takePhoto();
                break;
            case R.id.material_button_gallery:
                galleryPhoto();
                break;
        }
    }
}