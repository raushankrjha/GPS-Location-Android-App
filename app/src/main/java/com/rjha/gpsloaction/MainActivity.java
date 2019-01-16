package com.rjha.gpsloaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText edtemail,edtname,edtmobile,edtpassword;
    String email,name,mobile ;
    String password;
    Button bsignup;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//check user already login or not
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
        }

        bsignup=(Button)findViewById(R.id.bttnsignup);
        edtemail=(EditText)findViewById(R.id.editText_emailAddress);
        edtname=(EditText)findViewById(R.id.editText_name);
        edtmobile=(EditText)findViewById(R.id.editText_mobile);
        edtpassword=(EditText)findViewById(R.id.editText_password);
        progressDialog=new ProgressDialog(this);


        firebaseAuth= FirebaseAuth.getInstance();
        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Register();

            }
        });
    }

    public void Register()
    {
        email = edtemail.getText().toString();
        name = edtname.getText().toString();
        mobile = edtmobile.getText().toString();
        password = edtpassword.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "plzz Enter Your Email", Toast.LENGTH_SHORT).show();

        }
        else if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "plzz Enter  Name", Toast.LENGTH_SHORT).show();

        }
        else  if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "plzz Enter Password", Toast.LENGTH_SHORT).show();

        }
        else if (mobile.isEmpty()) {
            Toast.makeText(getApplicationContext(), "plzz Enter Password", Toast.LENGTH_SHORT).show();

        }
        else {

            progressDialog.setMessage("Registering User....");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Toast.makeText(MainActivity.this, "Registration Sucess.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                myRef.child(userId).child("email").setValue(email);
                                myRef.child(userId).child("name").setValue(name);
                                myRef.child(userId).child("mobile").setValue(mobile);
                                myRef.child(userId).child("Location").setValue("empty");


                                finish();
                                Intent i = new Intent(MainActivity.this, HomePageActivity.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(MainActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });

        }

    }
}
