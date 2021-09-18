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

import com.bumptech.glide.Glide;
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

public class UpdateProductActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int IMAGE_COD=2;
    public static final int INTENT_GALLERY=1;

    FirebaseAuth firebaseAuthB;
    FirebaseFirestore firebaseFirestoreB;
    FirebaseCrashlytics firebaseCrashlyticsB;
    StorageReference storageReference;

    SharedPreferenceUserData sharedPreferenceUserData;

    TextInputLayout text_input_layout_product_titleU;
    TextInputLayout text_input_layout_product_availabilityU;
    TextInputLayout text_input_layout_product_descriptionU;
    TextInputLayout text_input_layout_product_priceU;

    TextInputEditText text_input_edit_product_titleU;
    TextInputEditText text_input_edit_product_availabilityU;
    TextInputEditText text_input_edit_product_descriptionU;
    TextInputEditText text_input_edit_product_priceU;

    ImageView image_view_productU;

    MaterialButton material_button_photoU;
    MaterialButton material_button_galleryU;
    MaterialButton material_button_product_updateU;
    MaterialButton material_button_product_deleteU;


    String path;
    File fileImage;
    Bitmap bitmap;
    Uri imageUri;
    Uri resultStartCrop;
    ProgressDialog progressDialog;

    String documentIdB;
    String titleB;
    String availabilityB;
    String descriptionB;
    String priceB;
    String photoB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        firebaseAuthB = FirebaseAuth.getInstance();
        firebaseFirestoreB = FirebaseFirestore.getInstance();
        firebaseCrashlyticsB= FirebaseCrashlytics.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog= new ProgressDialog(this);

        text_input_layout_product_titleU= findViewById(R.id.text_input_layout_product_titleU);
        text_input_layout_product_availabilityU= findViewById(R.id.text_input_layout_product_availabilityU);
        text_input_layout_product_descriptionU= findViewById(R.id.text_input_layout_product_descriptionU);
        text_input_layout_product_priceU= findViewById(R.id.text_input_layout_product_priceU);

        text_input_edit_product_titleU= findViewById(R.id.text_input_edit_product_titleU);
        text_input_edit_product_availabilityU= findViewById(R.id.text_input_edit_product_availabilityU);
        text_input_edit_product_descriptionU= findViewById(R.id.text_input_edit_product_descriptionU);
        text_input_edit_product_priceU= findViewById(R.id.text_input_edit_product_priceU);

        image_view_productU=findViewById(R.id.image_view_productU);

        material_button_photoU= findViewById(R.id.material_button_photoU);
        material_button_galleryU=findViewById(R.id.material_button_galleryU);
        material_button_product_updateU=findViewById(R.id.material_button_product_updateU);
        material_button_product_deleteU=findViewById(R.id.material_button_product_deleteU);

        sharedPreferenceUserData = new SharedPreferenceUserData(this);

        material_button_product_updateU.setOnClickListener(this);
        material_button_galleryU.setOnClickListener(this);
        material_button_photoU.setOnClickListener(this);
        material_button_product_deleteU.setOnClickListener(this);



        titleB = getIntent().getExtras().getString("title");
        availabilityB= getIntent().getExtras().getString("availability");
        descriptionB= getIntent().getExtras().getString("description");
        priceB= getIntent().getExtras().getString("price");
        photoB= getIntent().getExtras().getString("photoUrl");

        text_input_edit_product_titleU.setText(titleB);
        text_input_edit_product_availabilityU.setText(availabilityB);
        text_input_edit_product_descriptionU.setText(descriptionB);
        text_input_edit_product_priceU.setText(priceB);

        Glide.with(UpdateProductActivity.this).load(photoB).circleCrop().into(image_view_productU);

        if (ContextCompat.checkSelfPermission(UpdateProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UpdateProductActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateProductActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
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
            imageUri = FileProvider.getUriForFile(UpdateProductActivity.this, "com.unibague.pazyregion.provider", fileImage);
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
                    image_view_productU.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(UpdateProductActivity.this, "Por favor, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
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
                    image_view_productU.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(UpdateProductActivity.this, "Por favor, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
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
        ucrop.start(UpdateProductActivity.this);
    }
    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setToolbarTitle("Recortar Imagen");
        return options;
    }

    private String loadNameUser(){
        String nameUser= sharedPreferenceUserData.getNameUser();
        return nameUser;
    }

    private void loadData(){

        try{
            String title =text_input_edit_product_titleU.getText().toString().trim();
            String date=date();
            String availability=text_input_edit_product_availabilityU.getText().toString().trim();
            String description=text_input_edit_product_descriptionU.getText().toString().trim();
            String price=text_input_edit_product_priceU.getText().toString().trim();
            String email=loadEmailUser();
            String phone= loadPhone();
            String nameUser=loadNameUser();


            if(!title.isEmpty() && !availability.isEmpty() && !description.isEmpty() && !price.isEmpty()){
                text_input_layout_product_titleU.setError(null);
                text_input_layout_product_availabilityU.setError(null);
                text_input_layout_product_descriptionU.setError(null);
                text_input_layout_product_priceU.setError(null);

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
                        documentIdB = getIntent().getExtras().getString("documentId").toString();
                        Product product = new Product(title, date,availability,description,price,email,phone,url.toString(),nameUser);
                        firebaseFirestoreB.collection("Product").document(documentIdB).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                onBackPressed();
                                Toast.makeText(UpdateProductActivity.this,"Se guardo exitoxamente!",Toast.LENGTH_LONG).show();
                                finish();
                            }

                        });
                    }
                });



            }if(title.isEmpty()){
                text_input_layout_product_titleU.setError("Este campo es importante");
                text_input_layout_product_titleU.requestFocus();
            }if(availability.isEmpty()){
                text_input_layout_product_availabilityU.setError("Este campo es importante");
                text_input_layout_product_availabilityU.requestFocus();
            }if(description.isEmpty()){
                text_input_layout_product_descriptionU.setError("Este campo es importante");
                text_input_layout_product_descriptionU.requestFocus();
            }if(price.isEmpty()){
                text_input_layout_product_priceU.setError("Este campo es importante");
                text_input_layout_product_priceU.requestFocus();
            }
        }catch (Exception e){
            firebaseCrashlyticsB.log("loadData");
            firebaseCrashlyticsB.recordException(e);
        }
    }

    private void deleteDocument(){
        firebaseFirestoreB.collection("Product").document(documentIdB).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProductActivity.this,"Se elimino exitoxamente!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_button_product_updateU:
                loadData();
                break;
            case R.id.material_button_photoU:
                takePhoto();
                break;
            case R.id.material_button_galleryU:
                galleryPhoto();
                break;
            case R.id.material_button_product_deleteU:
                deleteDocument();
                break;
        }

    }
}