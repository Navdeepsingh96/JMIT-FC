package com.example.navdeep.jmitfc;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fixtures extends AppCompatActivity {
    EditText et1,et2;
    Button b1,b2,b3,b4;
    String fixture,child="",time="",date="";
    Calendar cal1;
    private FirebaseAuth myauth;

    NotificationManager nm;
    boolean notif=false;
    public static int uniqueID=5486465;
    Notification n;
    TextView t1,t2;
    ScrollView scroll;


    DateFormat df1;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    DatabaseReference databaseReference,view;
    int i=5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures);

        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        myauth=FirebaseAuth.getInstance();
        myauth.addAuthStateListener(lis1);



        firebaseDatabase=firebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("fixtures");
        Query queryref=databaseReference.limitToLast(150);
        scroll =(ScrollView)findViewById(R.id.scrollView);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });


        queryref.addChildEventListener(childEventListener);

        et1=(EditText)findViewById(R.id.editText5);
        et2=(EditText)findViewById(R.id.editText6);
        t1=(TextView)findViewById(R.id.textView10);
        t1.setCursorVisible(false);
        t2=(TextView)findViewById(R.id.textView11);


        cal1=Calendar.getInstance();
        df1=DateFormat.getDateInstance();

        b1=(Button)findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int min=cal1.get(Calendar.MINUTE);
                int hr=cal1.get(Calendar.HOUR_OF_DAY);
                new TimePickerDialog(fixtures.this,tl1,hr,min,true).show();


            }
        });
        b2=(Button)findViewById(R.id.button4);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year=cal1.get(Calendar.YEAR);
                int month=cal1.get(Calendar.MONTH);
                int date=cal1.get(Calendar.DATE);
                new DatePickerDialog(fixtures.this,tl2,year,month,date).show();

            }
        });
        b3=(Button)findViewById(R.id.button5);
    b3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fixture="";
            fixture=fixture+et1.getText().toString();
            fixture=fixture+" vs ";
            fixture=fixture+et2.getText().toString();

            fixture=fixture+" ON ";
            fixture=fixture+" "+date+" :"+time;


            t1.setText(fixture);

        }

    });
        b4=(Button)findViewById(R.id.button6);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et1.getText().toString())){
                    Toast.makeText(fixtures.this, "fill all", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(et2.getText().toString())) {
                    Toast.makeText(fixtures.this, "fill all", Toast.LENGTH_LONG).show();
                }
                else {
                    String team1=et1.getText().toString();
                    String team2=et2.getText().toString();
                    fixture=team1+" vs "+team2+" on "+date+" at time: "+time;


                    if (TextUtils.isEmpty(time)){
                        Toast.makeText(fixtures.this, "set time", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(date))
                    {
                        Toast.makeText(fixtures.this, "set date", Toast.LENGTH_LONG).show();
                    }
                    else {



                        databaseReference.child(fixture).setValue("IS ON!!!").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    t1.setText("");
                                    et1.setText("");
                                    et1.clearFocus();
                                    et2.setText("");
                                    et2.clearFocus();
                                    time="";
                                    date="";

                                    Toast.makeText(fixtures.this, "Submitted", Toast.LENGTH_SHORT).show();


                                    InputMethodManager imm = (InputMethodManager) getSystemService(
                                            Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(et2.getWindowToken(), 0);


                                } else

                                    Toast.makeText(fixtures.this, "NOT Submitted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
    }
    FirebaseAuth.AuthStateListener lis1 = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();

        }
    };
    DatePickerDialog.OnDateSetListener tl2=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            cal1.set(Calendar.YEAR,i);
            cal1.set(Calendar.MONTH,i1);
            cal1.set(Calendar.DATE,i2);
            String myFormat = "dd-MM-yyyy"; // your own format
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            date= sdf.format(cal1.getTime());
        }
    };
    TimePickerDialog.OnTimeSetListener tl1= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

            cal1.set(Calendar.HOUR_OF_DAY,i);
            cal1.set(Calendar.MINUTE,i1);
            String myFormat = "hh:mm a"; // your own format
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            time= sdf.format(cal1.getTime());


        }
    };

    @Override
    protected void onPause() {
        notif=false;
        super.onPause();

    }

    @Override
    protected void onStart() {
        notif=false;
        super.onStart();
    }

    @Override
    protected void onStop() {
        notif=true;
        super.onStop();
    }

    ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SharedPreferences getpefs= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean notifs=getpefs.getBoolean("notification",true);












            child=child+"\n";
                child = child+"\n"+dataSnapshot.getKey().toString();

                t2.setText(child);


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            child=child+"\n";
            child = child+"\n"+dataSnapshot.getKey().toString();

            t2.setText(child);


        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            child=child+"\n";
            child = child+"\n"+dataSnapshot.getKey().toString();

            t2.setText(child);


        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
     if (keyCode==KeyEvent.KEYCODE_BACK)
     {
         finishActivity(1);
     }
        return super.onKeyDown(keyCode, event);
    }

}
