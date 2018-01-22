package com.dmb.quicktrade.Products;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dmb.quicktrade.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dmb.quicktrade.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    EditText name,description,price;

    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name =findViewById(R.id.regProdName);
        description = findViewById(R.id.regProdDesc);
        price = findViewById(R.id.regProdPrice);
        spinnerCategory = findViewById(R.id.spCategory);

        dbr = FirebaseDatabase.getInstance().getReference("productos");

        String[] arraySpinner = new String[] {
                "Tecnologia", "Coches", "Hogar"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnerCategory.setAdapter(adapter);

    }

    public void addProduct() {
            String key = dbr.push().getKey();
            Product product = new Product(name.getText().toString(), description.getText().toString(), spinnerCategory.getSelectedItem().toString(), price.getText().toString(), getIntent().getExtras().getString("userUID"), key,false);
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
