package com.example.navdeep.jmitfc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class startpage extends AppCompatActivity {
    private FirebaseAuth myauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        myauth=FirebaseAuth.getInstance();
        myauth.addAuthStateListener(lis1);



    }
    FirebaseAuth.AuthStateListener lis1 = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Intent in = new Intent(startpage.this, MainActivity.class);
                startActivity(in);



            } else {

                Intent in2 = new Intent(startpage.this, firstpage.class);
                startActivity(in2);
            }
        }
    };
}