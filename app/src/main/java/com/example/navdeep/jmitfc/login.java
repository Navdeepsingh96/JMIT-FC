package com.example.navdeep.jmitfc;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button b1;
    EditText et1,et2;

    private FirebaseAuth myauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myauth=FirebaseAuth.getInstance();




        et1=(EditText)findViewById(R.id.editText3);
        et2=(EditText)findViewById(R.id.editText4);
        b1=(Button)findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1.setEnabled(false);
                et2.setEnabled(false);
                registeruser();


            }
        });

    }
    private void registeruser()
    {
        if (TextUtils.isEmpty(et1.getText().toString())){
            Toast.makeText(this, "fill email", Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(et2.getText().toString())) {
            Toast.makeText(this, "fill password", Toast.LENGTH_LONG).show();
        }
        else if (et2.getText().toString().length()<6){
            Toast.makeText(this, "password length < 6", Toast.LENGTH_LONG).show();

        }
        else {
            String e = et1.getText().toString();
            String p = et2.getText().toString();

            myauth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        et1.setText("");
                        et2.setText("");

                        Toast.makeText(login.this, "REGISTERED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                        Intent ian = new Intent(login.this, firstpage.class);
                        startActivity(ian);
                    } else if (task.isSuccessful() == false) {
                        et1.setEnabled(true);
                        et2.setEnabled(true);

                        Toast.makeText(login.this, "CHECK ALL CONDITION/ INTERNET CONNECTION", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent(login.this,MainActivity.class);
            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

}
