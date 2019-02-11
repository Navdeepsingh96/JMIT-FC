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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.navdeep.jmitfc.R.*;

public class MainActivity extends AppCompatActivity {

    Button b1;
    EditText et1,et2;
    TextView tv1;
    boolean doubleclick;
    private FirebaseAuth myauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        myauth=FirebaseAuth.getInstance();
        myauth.addAuthStateListener(lis1);
        tv1=(TextView)findViewById(id.textView);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,login.class);
                startActivity(intent);
            }
        });


        b1=(Button)findViewById(id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1.setEnabled(false);
                et2.setEnabled(false);
                loginuser();

            }
        });
        et1=(EditText)findViewById(id.editText);
        et2=(EditText)findViewById(id.editText2);

    }
    FirebaseAuth.AuthStateListener lis1=new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if (user==null){

            }
            else {
                Intent in2=new Intent(MainActivity.this,firstpage.class);
                startActivity(in2);
            }
        }
    };


    private void loginuser(){

        if (TextUtils.isEmpty(et1.getText().toString())){
            et1.setEnabled(true);

            et2.setEnabled(true);
            Toast.makeText(this, "fill all", Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(et2.getText().toString())) {
            et1.setEnabled(true);

            et2.setEnabled(true);
            Toast.makeText(this, "fill all", Toast.LENGTH_LONG).show();
        }
        else {
            String e = et1.getText().toString();
            String p = et2.getText().toString();
            myauth.signInWithEmailAndPassword(e, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        et1.setEnabled(false);
                        et2.setEnabled(false);
                        et1.setText("");
                        et2.setText("");


                        Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent iln = new Intent(MainActivity.this, firstpage.class);
                        startActivity(iln);
                    } else if (task.isSuccessful() == false) {

                        Toast.makeText(MainActivity.this, "CHECK YOUR LOGIN DETAILS/INTERNET CONNECTION", Toast.LENGTH_LONG).show();

                        et1.setEnabled(true);

                        et2.setEnabled(true);
                    }
                }
            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                MainActivity.this.finishAffinity();
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
