package com.example.navdeep.jmitfc;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Navdeep on 6/30/2016.
 */
public class preapp extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.firstxml);
    }
}
