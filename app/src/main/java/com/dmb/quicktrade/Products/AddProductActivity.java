package com.dmb.quicktrade.Products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dmb.quicktrade.MainActivity;
import com.dmb.quicktrade.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.quicktrade.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    EditText name,description,price;
    ImageButton btnPickImage;
    ImageView prodImage;
    TextView tvPrevImg;
    String imgRef;

    DatabaseReference dbr;
    FirebaseStorage fs = FirebaseStorage.getInstance();
    StorageReference sr = fs.getReference();

    static final int REQUEST_IMAGE_CAPTURE = 111;
    static final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name =findViewById(R.id.regProdName);
        description = findViewById(R.id.regProdDesc);
        price = findViewById(R.id.regProdPrice);
        spinnerCategory = findViewById(R.id.spCategory);
        btnPickImage = findViewById(R.id.btnPickPicture);
        tvPrevImg = findViewById(R.id.tvPrevImage);
        prodImage = findViewById(R.id.prodImage);

        dbr = FirebaseDatabase.getInstance().getReference("productos");

        String[] arraySpinner = new String[] {
                "Tecnologia", "Coches", "Hogar"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnerCategory.setAdapter(adapter);

        registerForContextMenu(btnPickImage);

        openImageMenu();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.openCamera:
                openCamera();
                return true;
            case R.id.openGallery:
                chooseImage();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void openImageMenu(){
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
    }

    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                tvPrevImg.setText("Vista Previa Imagen:");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                prodImage.setImageBitmap(bitmap);
                prodImage.setVisibility(View.VISIBLE);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.e("TAG: ","El buen debbug");
            tvPrevImg.setText("Vista Previa Imagen:");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            prodImage.setImageBitmap(imageBitmap);
            uploadImage();
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo Imagen...");
            progressDialog.show();

            StorageReference ref = sr.child("imagenes/"+ UUID.randomUUID().toString());
            imgRef = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Imagen Subida", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Fallo al subir la imagen "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Subiendo: "+(int)progress+"%");
                        }
                    });
        }
    }


    public void addProduct() {
            String key = dbr.push().getKey();
            Product product = new Product(name.getText().toString(), description.getText().toString(),
                    spinnerCategory.getSelectedItem().toString(), price.getText().toString(),
                    getIntent().getExtras().getString("userUID"), key,false,imgRef);
            dbr.child(key).setValue(product);
            setResult(RESULT_OK, getIntent());
            finish();
    }

    public void checkFields(View v) {
        if(description.getText().toString().isEmpty() || name.getText().toString().isEmpty() || price.getText().toString().isEmpty()){
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            addProduct();
        }
    }
}
