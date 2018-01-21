package com.dmb.quicktrade.Users;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dmb.quicktrade.R;
import com.dmb.quicktrade.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference dbr;
    EditText username,name,surname,email,address,password;
    String getUsername,getName,getSurname,getEmail,getPassword,getAddress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        username = findViewById(R.id.regUsername);
        name = findViewById(R.id.regName);
        surname = findViewById(R.id.regSurname);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        address = findViewById(R.id.regAddress);

        dbr = FirebaseDatabase.getInstance().getReference("usuarios");
    }

    public void userRegister(View v){

        getUsername = username.getText().toString();
        getName = name.getText().toString();
        getSurname = surname.getText().toString();
        getEmail = email.getText().toString();
        getPassword = password.getText().toString();
        getAddress = address.getText().toString();

        createAccount();

        Toast.makeText(this, "Usuario Añadido", Toast.LENGTH_SHORT).show();
    }

    public void createUser(String key){
        User user = new User(getUsername,getName,getSurname,getEmail,getAddress,"user");
        dbr.child(key).setValue(user);
    }

    public void createAccount(){

        mAuth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(user.getUid());
                            getIntent().putExtra("userUID",user.getUid());
                            setResult(RESULT_OK,getIntent());
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "No se ha podido crear el usuario.", Toast.LENGTH_SHORT).show();
                        }

                        RegisterActivity.this.finish();
                    }
                });
    }
}
