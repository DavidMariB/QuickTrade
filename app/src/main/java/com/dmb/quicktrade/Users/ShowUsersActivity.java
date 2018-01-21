package com.dmb.quicktrade.Users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dmb.quicktrade.R;
import com.dmb.quicktrade.Adapters.UsersAdapter;
import com.dmb.quicktrade.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUsersActivity extends AppCompatActivity {

    UsersAdapter adapter;
    ArrayList<User> users = new ArrayList<>();
    DatabaseReference dbr;
    ListView usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        usersList = findViewById(R.id.userList);

        dbr = FirebaseDatabase.getInstance().getReference("usuarios");

        retrieveData();
        editUserActivity();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        users = new ArrayList<>();
        retrieveData();
    }

    public void retrieveData(){
        Query q = dbr.orderByKey();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    users.add(dataSnapshot1.getValue(User.class));
                }
                adapter = new UsersAdapter(ShowUsersActivity.this, users);
                usersList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editUserActivity(){

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowUsersActivity.this, EditUserActivity.class);
                intent.putExtra("user", users.get(position));
                startActivity(intent);
            }
        });
    }

}