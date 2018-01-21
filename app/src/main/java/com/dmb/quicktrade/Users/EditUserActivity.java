package com.dmb.quicktrade.Users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dmb.quicktrade.R;
import com.dmb.quicktrade.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditUserActivity extends AppCompatActivity {

    EditText name,surname,address;
    User user;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        name = findViewById(R.id.editName);
        surname = findViewById(R.id.editSurname);
        address = findViewById(R.id.editAddress);

        getUsersData();
    }

    public void getUsersData(){
        user = (User) getIntent().getExtras().getSerializable("user");
        name.setText(user.getName());
        surname.setText(user.getSurname());
        address.setText(user.getAddress());
    }

    public void editUser(View v){
        dbr = FirebaseDatabase.getInstance().getReference("usuarios");
        Query q = dbr.orderByChild("username").equalTo(user.getUsername());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    dbr.child(key).child("name").setValue(name.getText().toString());
                    dbr.child(key).child("surname").setValue(surname.getText().toString());
                    dbr.child(key).child("address").setValue(address.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, "Usuario Modificado", Toast.LENGTH_SHORT).show();
        EditUserActivity.this.finish();
    }
}
