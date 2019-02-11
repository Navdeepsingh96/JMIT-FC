package com.example.navdeep.jmitfc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.util.IllegalFormatCodePointException;

public class Pingg extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    String[]paths={"select","Im Coming to play","Not Sure","Not Today"};
    String status="";
    String status2="";
    String key;
    Button b1,b2,b3;
    TextView t1;
    Calendar cal1;
    EditText et1;
    boolean notif=false;
    NotificationManager nm;
    long one,two;
    Notification n;
    private FirebaseAuth myauth;
    FirebaseUser user;

    ScrollView scroll;
    String time="";

    String child="";

    public static int uniqueID=46464684;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    DateFormat timeformat;

    String id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pingg);


        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();

        scroll =(ScrollView)findViewById(R.id.scrollView3);
        cal1=Calendar.getInstance();
        timeformat= DateFormat.getTimeInstance();

        myauth=FirebaseAuth.getInstance();
        myauth.addAuthStateListener(lis1);
        firebaseDatabase=firebaseDatabase.getInstance();

        databaseReference=firebaseDatabase.getReference("ping");
        //databaseReference.addListenerForSingleValueEvent(vl1);
        Query queryref=databaseReference.limitToLast(150);
        queryref.addChildEventListener(childEventListener);

        //databaseReference.addChildEventListener(childEventListener);

        et1=(EditText)findViewById(R.id.editText9);
        t1=(TextView)findViewById(R.id.textView22);
        et1.clearFocus();
        Intent in=getIntent();
         id1=in.getStringExtra("id");
        Toast.makeText(Pingg.this,"Welcome: "+id1,Toast.LENGTH_SHORT).show();

        spinner=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter=new ArrayAdapter <String> (Pingg.this ,android.R.layout.simple_spinner_dropdown_item,paths);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        b1=(Button)findViewById(R.id.button11);
        b1.setEnabled(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min=cal1.get(Calendar.MINUTE);
                int hr=cal1.get(Calendar.HOUR);

                new TimePickerDialog(Pingg.this,tl1,hr,min,true).show();
            }
        });
        b2=(Button)findViewById(R.id.button12);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status=id1+": "+status2+" "+time;
                b1.setEnabled(true);
                if (status2=="Im Coming to play at"&&time.isEmpty())
                {
                    Toast.makeText(Pingg.this,"select time",Toast.LENGTH_SHORT).show();

                }

                else
                {
                databaseReference.push().setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            status="";
                            time="";
                            child="";
                            Toast.makeText(Pingg.this,"Submitted",Toast.LENGTH_SHORT).show();



                        }
                        else

                            Toast.makeText(Pingg.this,"NOT Submitted",Toast.LENGTH_SHORT).show();

                    }

                });

            }
            }


        });

        b3=(Button)findViewById(R.id.button14);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et1.getText().toString()))
                {
                    Toast.makeText(Pingg.this,"type message",Toast.LENGTH_SHORT).show();

                }

                else {
                    databaseReference.push().setValue(id1+": "+et1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                et1.setText("");
                                et1.clearFocus();
                                Toast.makeText(Pingg.this, "Sent", Toast.LENGTH_SHORT).show();
                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);


                            } else

                                Toast.makeText(Pingg.this, "NOT Sent", Toast.LENGTH_SHORT).show();

                        }

                    });

                }
                }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int position=spinner.getSelectedItemPosition();
        switch (position)
        {
            case 0:
                b2.setEnabled(false);

                break;
            case 1:
                b2.setEnabled(true);

                status2="";
                time="";
                status2="Im Coming to play at";
                b1.setEnabled(true);

                break;
            case 2:
                status2="";
                b2.setEnabled(true);
                b1.setEnabled(false);
                time="";
                status2="Not Sure";
                break;
            case 3:
                status2="";
                b2.setEnabled(true);
                b1.setEnabled(false);
                time="";
                status2="Not Today";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            finishActivity(1);
        }
        return super.onKeyDown(keyCode, event);
    }




    ValueEventListener vl1=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            one=dataSnapshot.getChildrenCount();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };






    ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            two=dataSnapshot.getChildrenCount();




            child=child+"\n";
                child = child+"\n"+dataSnapshot.getValue().toString();
                t1.setText(child);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            child=child+"\n";
                child = child+"\n"+dataSnapshot.getValue().toString();
                t1.setText(child);


        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {






        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onStart() {
        notif=false;


        et1.clearFocus();

        scroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                });


            }
        });

        super.onStart();
    }

    @Override
    protected void onPause() {
        notif=false;
        super.onPause();

    }



    @Override
    protected void onStop() {
        notif=true;
        super.onStop();
    }
    FirebaseAuth.AuthStateListener lis1 = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();

        }
    };


}
