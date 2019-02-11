package com.example.navdeep.jmitfc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Squad extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    Button b1;
    EditText et1;
    NotificationManager nm;
    private FirebaseAuth myauth;
    FirebaseUser user;
    Notification n;
    public static int uniqueID=8846465;
    boolean notif=false;
    TextView t1,t2,t3,t4;
    String Squad;
    String child="",child2="",child3="",child4="";
    String[]paths={"select position","Goalkeeper","Defender","Midfielder","Attacker"};
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferencegk,databaseReferencedef,databaseReferencemid,databaseReferenceatt;

    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        myauth=FirebaseAuth.getInstance();
        myauth.addAuthStateListener(lis1);

        firebaseDatabase=firebaseDatabase.getInstance();
        databaseReferencegk=firebaseDatabase.getReference("goalkeepers");
        databaseReferencegk.addChildEventListener(vl1);

        databaseReferencedef=firebaseDatabase.getReference("defenders");
        databaseReferencedef.addChildEventListener(vl2);

        databaseReferencemid=firebaseDatabase.getReference("midfielder");
        databaseReferencemid.addChildEventListener(vl3);

        databaseReferenceatt=firebaseDatabase.getReference("attackers");
        databaseReferenceatt.addChildEventListener(vl4);

        t1=(TextView)findViewById(R.id.textView12);
        t2=(TextView)findViewById(R.id.textView15);
        t3=(TextView)findViewById(R.id.textView17);
        t4=(TextView)findViewById(R.id.textView19);


        et1=(EditText)findViewById(R.id.editText7);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter <String> adapter=new ArrayAdapter <String> (Squad.this ,android.R.layout.simple_spinner_dropdown_item,paths);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        b1=(Button)findViewById(R.id.button7);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et1.getText().toString())){
                    Toast.makeText(Squad.this, "fill all", Toast.LENGTH_LONG).show();
                }

                else {

                    Squad="";
                    Squad=Squad+et1.getText().toString();



                    et1.setText("");
                    et1.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

                    myref.child(Squad).setValue(spinner.getSelectedItem()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Squad.this,"Submitted",Toast.LENGTH_SHORT).show();

                            }
                            else

                                Toast.makeText(Squad.this,"NOT Submitted",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }
    ChildEventListener vl1=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SharedPreferences getpefs= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean notifs=getpefs.getBoolean("notification",true);
            if (notifs==true&&notif)
            {

                //Intent intent=new Intent(Squad.this,Squad.class);
                Intent intent;
                if (user==null)
                {
                    intent=new Intent(Squad.this,MainActivity.class);
                }
                else {
                    intent = new Intent(Squad.this,Squad.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                PendingIntent pi=PendingIntent.getActivity(Squad.this,1,intent,0);

                String body="Welcome a New Member!!";
                String title="JMIT FC";
               // String ticker="ticker";
                Notification.Builder builder=new Notification.Builder(Squad.this);
                builder.setContentTitle(title);
                builder.setContentText(body);
                builder.setSmallIcon(R.mipmap.notif);
                builder.setContentIntent(pi);
                //builder.setDefaults(Notification.FLAG_AUTO_CANCEL);
                //builder.setOngoing(true);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    nm.notify(0, builder.build());
                }
                finish();




            }
            else if (notifs==false)
            {

            }
            child=child+"\n";
            child=child+dataSnapshot.getKey().toString();
            t1.setText(child);


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            child=child+"\n";
            child=child+dataSnapshot.getKey().toString();
            t1.setText(child);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            child=child+"\n";
            child=child+dataSnapshot.getKey().toString();
            t1.setText(child);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener vl2=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SharedPreferences getpefs= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean notifs=getpefs.getBoolean("notification",true);
            if (notifs==true&&notif)
            {

                //Intent intent=new Intent(Squad.this,Squad.class);
                Intent intent;
                if (user==null)
                {
                    intent=new Intent(Squad.this,MainActivity.class);
                }
                else {
                    intent = new Intent(Squad.this,Squad.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                PendingIntent pi=PendingIntent.getActivity(Squad.this,1,intent,0);

                String body="Welcome a New Member!!";
                String title="JMIT FC";
                // String ticker="ticker";
                Notification.Builder builder=new Notification.Builder(Squad.this);
                builder.setContentTitle(title);
                builder.setContentText(body);
                builder.setSmallIcon(R.mipmap.notif);
                builder.setContentIntent(pi);
                //builder.setDefaults(Notification.FLAG_AUTO_CANCEL);
                //builder.setOngoing(true);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    nm.notify(0, builder.build());
                }
                finish();



            }
            else if (notifs==false)
            {

            }

            child2=child2+"\n";
            child2=child2+dataSnapshot.getKey().toString();
            t2.setText(child2);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            child2=child2+"\n";
            child2=child2+dataSnapshot.getKey().toString();
            t2.setText(child2);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            child2=child2+"\n";
            child2=child2+dataSnapshot.getKey().toString();
            t2.setText(child2);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener vl3=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SharedPreferences getpefs= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean notifs=getpefs.getBoolean("notification",true);
            if (notifs==true&&notif)
            {

                //Intent intent=new Intent(Squad.this,Squad.class);
                Intent intent;
                if (user==null)
                {
                    intent=new Intent(Squad.this,MainActivity.class);
                }
                else {
                    intent = new Intent(Squad.this,Squad.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                PendingIntent pi=PendingIntent.getActivity(Squad.this,1,intent,0);

                String body="Welcome a New Member!!";
                String title="JMIT FC";
                // String ticker="ticker";
                Notification.Builder builder=new Notification.Builder(Squad.this);
                builder.setContentTitle(title);
                builder.setContentText(body);
                builder.setSmallIcon(R.mipmap.notif);
                builder.setContentIntent(pi);
                //builder.setDefaults(Notification.FLAG_AUTO_CANCEL);
                //builder.setOngoing(true);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    nm.notify(0, builder.build());
                }
                finish();


            }
            else if (notifs==false)
            {

            }
            child3=child3+"\n";
            child3=child3+dataSnapshot.getKey().toString();
            t3.setText(child3);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            child3=child3+"\n";
            child3=child3+dataSnapshot.getKey().toString();
            t3.setText(child3);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            child3=child3+"\n";
            child3=child3+dataSnapshot.getKey().toString();
            t3.setText(child3);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ChildEventListener vl4=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            child4=child4+"\n";
            child4=child4+dataSnapshot.getKey().toString();
            t4.setText(child4);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            child4=child4+"\n";
            child4=child4+dataSnapshot.getKey().toString();
            t4.setText(child4);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            child4=child4+"\n";
            child4=child4+dataSnapshot.getKey().toString();
            t4.setText(child4);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int position=spinner.getSelectedItemPosition();
        switch (position)
        {
            case 0:
                b1.setEnabled(false);
                break;
            case 1:
                b1.setEnabled(true);
                myref=databaseReferencegk;
                break;
            case 2:
                b1.setEnabled(true);
                myref=databaseReferencedef;
                break;
            case 3:
                b1.setEnabled(true);
                myref=databaseReferencemid;
                break;

            case 4:
                b1.setEnabled(true);
                myref=databaseReferenceatt;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
         finishActivity(1);
     }
        return super.onKeyDown(keyCode, event);
    }

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
    FirebaseAuth.AuthStateListener lis1 = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();

        }
    };


}
