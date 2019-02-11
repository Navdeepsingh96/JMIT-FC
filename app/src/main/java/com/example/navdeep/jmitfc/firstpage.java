package com.example.navdeep.jmitfc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firstpage extends AppCompatActivity {
    private FirebaseAuth myauth;




    EditText et1;
    TextView b1, b2, b4;

    String email;
    SharedPreferences settings;




    Button b5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);



        myauth = FirebaseAuth.getInstance();
        myauth.addAuthStateListener(lis1);

        et1=(EditText)findViewById(R.id.editText8);
        SharedPreferences sp1=getPreferences(MODE_PRIVATE);
        String ls=sp1.getString("displayname","");
        et1.setText( ""+ String.valueOf(ls));





        b1 = (TextView) findViewById(R.id.textView2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fixture=new Intent(firstpage.this, fixtures.class);
                startActivityForResult(fixture,4);

            }
        });

        b2 = (TextView) findViewById(R.id.textView3);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent squad=new Intent(firstpage.this, Squad.class);
                startActivityForResult(squad,1);

            }
        });

        b4 = (TextView) findViewById(R.id.textView5);

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=et1.getText().toString();
                if (TextUtils.isEmpty(id)){
                    Toast.makeText(firstpage.this,"Set DISPLAYNAME first",Toast.LENGTH_SHORT).show();
                }
                else{
                Intent ping=new Intent(firstpage.this,Pingg.class);
                ping.putExtra("id",id);


                startActivityForResult(ping,45);

                }
            }
        });
        b5=(Button) findViewById(R.id.button13);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ;
                final String name=et1.getText().toString();
                SharedPreferences sp=getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed1=sp.edit();
                ed1.putString("displayname", name);
                if (ed1.commit()==true){
                    Toast.makeText(firstpage.this,"saved",Toast.LENGTH_SHORT).show();
                    et1.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

                }
                else
                    Toast.makeText(firstpage.this,"saved",Toast.LENGTH_SHORT).show();


            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutus: {

                Intent intent3 = new Intent(firstpage.this, Aboutus.class);
                startActivityForResult(intent3,1);
                break;
            }


            case R.id.quit: {
                AlertDialog.Builder ad2 = new AlertDialog.Builder(firstpage.this);
                ad2.setTitle("Is it really goodbye :( ");

                ad2.setPositiveButton("YEAH!!", li1);
                ad2.setNegativeButton("NO :) ", li2);
                ad2.show();
                break;
            }
            case R.id.logout:

                AlertDialog.Builder ad1 = new AlertDialog.Builder(firstpage.this);
                ad1.setTitle("DO YOU REALLY WANNA LOGOUT BUD!!");

                ad1.setPositiveButton("YEAH!!", di1);
                ad1.setNegativeButton("NOOOO!!!!", di2);

                ad1.show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    DialogInterface.OnClickListener li1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                firstpage.this.finishAffinity();
            }
        }
    };
    DialogInterface.OnClickListener li2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(firstpage.this, ":)", Toast.LENGTH_LONG).show();

        }
    };


    DialogInterface.OnClickListener di1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            logout();

        }
    };
    DialogInterface.OnClickListener di2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(firstpage.this, "cancelled", Toast.LENGTH_LONG).show();
        }
    };


    private void logout() {
        myauth.removeAuthStateListener(lis1);
        FirebaseAuth.getInstance().signOut();
        // Intent intent3 = new Intent(firstpage.this, startpage.class);
        //startActivity(intent3);


    }


    FirebaseAuth.AuthStateListener lis1 = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            email = user.getEmail();

            if (user == null) {
                Intent intent3 = new Intent(firstpage.this, startpage.class);
                startActivity(intent3);



            } else {
                Toast.makeText(firstpage.this, "SIGNED IN USING: " + email, Toast.LENGTH_SHORT).show();
            }


        }

    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                firstpage.this.finishAffinity();
            }
        }

        return super.onKeyDown(keyCode, event);
    }


}
