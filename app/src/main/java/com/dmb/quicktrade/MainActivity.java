package com.dmb.quicktrade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dmb.quicktrade.Products.ProductsActivity;
import com.dmb.quicktrade.Users.LoginActivity;
import com.dmb.quicktrade.Users.RegisterActivity;
import com.dmb.quicktrade.Users.ShowUsersActivity;
import com.dmb.quicktrade.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    String userUID;

    DatabaseReference dbr;

    final static int LOGIN_ACTIVITY_CODE = 2;
    final static int REGISTER_ACTIVITY_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuUserList:
                Intent intent = new Intent(this, ShowUsersActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loginActivity(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent,LOGIN_ACTIVITY_CODE);
    }

    public void registerActivity(View v){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent,REGISTER_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case LOGIN_ACTIVITY_CODE:
                switch (resultCode){
                    case RESULT_OK:

                        userUID = data.getExtras().getString("userUID");

                        dbr = FirebaseDatabase.getInstance().getReference("usuarios/"+userUID);
                        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User user = dataSnapshot.getValue(User.class);
                                if(user.getAuth().equals("user")){
                                    Intent i = new Intent(getApplicationContext(), ProductsActivity.class);
                                    i.putExtra("userUID", userUID);
                                    startActivity(i);
                                }else{
                                    Intent i = new Intent(getApplicationContext(), ShowUsersActivity.class);
                                    i.putExtra("userUID", userUID);
                                    startActivity(i);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        break;
                    case RESULT_CANCELED:
                        break;
                }

                break;
            case REGISTER_ACTIVITY_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        Toast.makeText(getApplicationContext(),"Usuario Registrado Correctamente",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), ProductsActivity.class);
                        i.putExtra("userUID",data.getExtras().getString("userUID"));
                        startActivity(i);
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}
