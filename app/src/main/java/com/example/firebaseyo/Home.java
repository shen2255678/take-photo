package com.example.firebaseyo;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser !=null){
            startActivity(new Intent(Home.this,LoginActivity.class)); //確保程式關掉還能記錄是否帳戶有無登出，若無則保持原樣
        }
    }
}
