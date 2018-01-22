package com.dmb.quicktrade.Products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dmb.quicktrade.R;
import com.dmb.quicktrade.model.Product;
import com.dmb.quicktrade.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProductsActivity extends AppCompatActivity {

    EditText name,description,price;
    Spinner spCategory;
    Product product;
    DatabaseReference dbr;
    DatabaseReference userReference;
    FirebaseUser currentUser;
    User user;
    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);

        name = findViewById(R.id.editProdName);
        description = findViewById(R.id.editProdDesc);
        price = findViewById(R.id.editProdPrice);
        spCategory = findViewById(R.id.spCategory);

        /*String[] arraySpinner = new String[] {
                "Tecnologia", "Coches", "Hogar"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spCategory.setAdapter(adapter);*/

        getProductData();
    }

    public void getProductData(){
        userUID = getIntent().getExtras().getString("userUID");
        product = (Product) getIntent().getExtras().getSerializable("product");
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice());
        Log.e("UserUID: ",product.getUserUID());
    }

    public void checkFields(View v){
        if(name.getText().toString().isEmpty() || description.getText().toString().isEmpty() || price.getText().toString().isEmpty()){
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            editProduct();
        }
    }

    public void editProduct(){
        dbr = FirebaseDatabase.getInstance().getReference("productos");
        Query q = dbr.orderByChild("name").equalTo(product.getName());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (product.getUserUID().equals(userUID)){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String key = dataSnapshot1.getKey();
                        dbr.child(key).child("name").setValue(name.getText().toString());
                        dbr.child(key).child("description").setValue(description.getText().toString());
                        dbr.child(key).child("price").setValue(price.getText().toString());
                        //dbr.child(key).child("category").setValue(spCategory.getSelectedItem().toString());

                        Toast.makeText(EditProductsActivity.this, "Producto Modificado", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EditProductsActivity.this, "No tienes permiso para modificar este articulo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        EditProductsActivity.this.finish();
    }

    public void deleteProduct(View v){
        dbr = FirebaseDatabase.getInstance().getReference("productos");
        Query q = dbr.orderByChild("name").equalTo(product.getName());
        q.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (product.getUserUID().equals(userUID)){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String key = dataSnapshot1.getKey();
                        DatabaseReference bbdd = dbr.child(key);

                        bbdd.removeValue();

                        Toast.makeText(EditProductsActivity.this, "Articulo eliminado", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EditProductsActivity.this, "No tienes permiso para modificar este articulo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
        EditProductsActivity.this.finish();
    }
}
