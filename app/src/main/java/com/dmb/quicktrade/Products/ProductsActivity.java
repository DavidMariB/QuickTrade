package com.dmb.quicktrade.Products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dmb.quicktrade.Adapters.ProductsAdapter;
import com.dmb.quicktrade.R;
import com.dmb.quicktrade.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProductsAdapter adapter;
    DatabaseReference dbr;
    ListView productsList;
    CheckBox checkBoxUserProducts;
    Spinner categorySpinner;

    int itemPosition;
    boolean filtered;

    private final static int ADD_ITEM_ACTIVITY = 2;
    private final static int MODIFY_ITEM_ACTIVITY = 3;

    DatabaseReference userReference;
    DatabaseReference allItems;
    DatabaseReference itemReference;
    String userUID;

    Product currentProd;

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> filteredProducts = new ArrayList<>();
    ArrayList<Product> userProducts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        checkBoxUserProducts = findViewById(R.id.checkBoxMyItems);
        categorySpinner = findViewById(R.id.categorySpinner);
        productsList = findViewById(R.id.listProducts);
        userUID = getIntent().getExtras().getString("userUID");
        userReference = FirebaseDatabase.getInstance().getReference("usuarios/" + userUID);
        allItems = FirebaseDatabase.getInstance().getReference("productos");

        dbr = FirebaseDatabase.getInstance().getReference("productos");

        String[] arraySpinner = new String[]{
                "Todo", "Tecnologia", "Coches", "Hogar"
        };
        final ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        categorySpinner.setAdapter(adapter);

        retrieveData();
        selectCategory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.users_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.itmSignOut:
                mAuth.signOut();
                Toast.makeText(this, "Sesión cerrada corretamente", Toast.LENGTH_SHORT).show();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        products = new ArrayList<>();
        retrieveData();
    }

    public void retrieveData() {
        Query q = dbr.orderByKey();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    products.add(dataSnapshot1.getValue(Product.class));
                }
                adapter = new ProductsAdapter(ProductsActivity.this, products);
                productsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addProductActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
        intent.putExtra("userUID", userUID);
        startActivityForResult(intent, ADD_ITEM_ACTIVITY);
    }

    public void checkBoxFilter() {
        checkBoxUserProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categorySpinner.getSelectedItemPosition() != 0) {
                    categorySpinner.setSelection(0);
                } else {
                    if (checkBoxUserProducts.isChecked()) {
                        userProducts.clear();
                        for (int i = 0; i < products.size(); i++) {
                            if (products.get(i).getUserUID().equals(userUID)) {
                                userProducts.add(products.get(i));
                            }
                        }
                        updateAdapter(userProducts);
                    } else {
                        updateAdapter(products);
                    }
                }
            }
        });
    }

    public void selectCategory() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (categorySpinner.getItemAtPosition(position).toString()) {

                    case "Todo":
                        filtered = false;
                        allItems.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                products.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Product product = dataSnapshot1.getValue(Product.class);
                                    products.add(product);
                                }
                                if (checkBoxUserProducts.isChecked()) {
                                    userProducts.clear();
                                    for (int i = 0; i < products.size(); i++) {
                                        if (products.get(i).getUserUID().equals(userUID)) {
                                            userProducts.add(products.get(i));
                                        }
                                    }
                                    updateAdapter(userProducts);
                                } else {
                                    updateAdapter(products);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        break;

                    case "Coches":
                        filteredProducts.clear();
                        filtered = true;
                        if (checkBoxUserProducts.isChecked()) {
                            for (int i = 0; i < userProducts.size(); i++) {
                                if (userProducts.get(i).getCategory().equals("Coches")) {
                                    filteredProducts.add(userProducts.get(i));
                                }
                            }
                            updateAdapter(filteredProducts);
                        } else {
                            for (int i = 0; i < products.size(); i++) {
                                if (products.get(i).getCategory().equals("Coches")) {
                                    filteredProducts.add(products.get(i));
                                }
                            }
                            updateAdapter(filteredProducts);
                        }
                        break;

                    case "Hogar":
                        filteredProducts.clear();
                        filtered = true;
                        if (checkBoxUserProducts.isChecked()) {
                            for (int i = 0; i < userProducts.size(); i++) {
                                if (userProducts.get(i).getCategory().equals("Hogar")) {
                                    filteredProducts.add(userProducts.get(i));
                                }
                            }
                            updateAdapter(filteredProducts);
                        } else {
                            for (int i = 0; i < products.size(); i++) {
                                if (products.get(i).getCategory().equals("Hogar")) {
                                    filteredProducts.add(products.get(i));
                                }
                            }
                            updateAdapter(filteredProducts);
                        }
                        break;

                    case "Tecnologia":
                        filteredProducts.clear();
                        filtered = true;
                        if (checkBoxUserProducts.isChecked()) {
                            for (int i = 0; i < userProducts.size(); i++) {
                                if (userProducts.get(i).getCategory().equals("Tecnologia")) {
                                    filteredProducts.add(userProducts.get(i));
                                }
                            }
                            updateAdapter(filteredProducts);
                        } else {
                            for (int i = 0; i < products.size(); i++) {
                                if (products.get(i).getCategory().equals("Tecnologia")) {
                                    filteredProducts.add(products.get(i));
                                }
                            }
                            updateAdapter(filteredProducts);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_ITEM_ACTIVITY:
                switch (resultCode){
                    case RESULT_OK:
                        if(categorySpinner.getSelectedItemPosition() != 0){
                            categorySpinner.setSelection(0);
                        }else{
                            products.clear();
                            allItems.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Product product = dataSnapshot1.getValue(Product.class);
                                        products.add(product);
                                    }
                                    if(checkBoxUserProducts.isChecked()){
                                        userProducts.clear();
                                        for (int i = 0 ; i < products.size() ; i++){
                                            if(products.get(i).getUserUID().equals(userUID)) {
                                                userProducts.add(products.get(i));
                                            }
                                        }
                                        updateAdapter(userProducts);
                                    }else{
                                        updateAdapter(products);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(),"Se ha Cancelado el nuevo Item.",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
            case MODIFY_ITEM_ACTIVITY:
                switch (resultCode){
                    case RESULT_OK:
                        if(categorySpinner.getSelectedItemPosition() != 0){
                            categorySpinner.setSelection(0);
                        }else{
                            products.clear();
                            allItems.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Product product = dataSnapshot1.getValue(Product.class);
                                        products.add(product);
                                    }
                                    if(checkBoxUserProducts.isChecked()){
                                        userProducts.clear();
                                        for (int i = 0 ; i < products.size() ; i++){
                                            if(products.get(i).getUserUID().equals(userUID)) {
                                                userProducts.add(products.get(i));
                                            }
                                        }
                                        updateAdapter(userProducts);
                                    }else{
                                        updateAdapter(products);
                                    }
                                    Toast.makeText(getApplicationContext(),"Producto Modificado.",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        break;

                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    public void getItemPosition(int itemPosition){
        this.itemPosition = itemPosition;
    }

    private void updateAdapter(ArrayList<Product> products){
        adapter.updateAdapter(products);
    }
}