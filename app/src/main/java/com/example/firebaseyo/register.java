package com.example.firebaseyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        res=(Button)findViewById(R.id.regbtn);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edemail = (EditText)findViewById(R.id.edemail);
                EditText edpass = (EditText)findViewById(R.id.edpass);
                String mail= edemail.getText().toString();
                String mailpass= edpass.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(edemail.getText().toString(),edpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(register.this,"註冊成功",Toast.LENGTH_LONG).show();
                            finish();
                        }else{Toast.makeText(register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();}
                    }
                });
            }
        });
    }
}
