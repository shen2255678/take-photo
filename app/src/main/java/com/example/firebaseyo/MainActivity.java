package com.example.firebaseyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button re;
    DatabaseReference reRef ;
    Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//toolbar=findViewById(R.id.toolbar);
//progressBar=findViewById(R.id.progressbar);
     //   member=new Member();
      //  reRef= FirebaseDatabase.getInstance().getReference().child("定位資訊");
       firebaseAuth = FirebaseAuth.getInstance();
        re=(Button)findViewById(R.id.btnre);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, register.class));
            }
        });


    }
    public void test (View view)
    {

        final EditText euid = (EditText)findViewById(R.id.uid);
        EditText eups = (EditText)findViewById(R.id.ups);
        final String ui= euid.getText().toString();
        String up= eups.getText().toString();
        if((ui.isEmpty())||(up.isEmpty())) {
            new AlertDialog.Builder(this)
                    .setTitle("logon")
                    .setMessage("登入失敗")
                    .setPositiveButton("ok",null)
                    .show();
        }else{

            firebaseAuth.signInWithEmailAndPassword(euid.getText().toString(), eups.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                       // member.setEuid(euid.getText().toString().trim());
                        //reRef.push().setValue(member);


                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}
