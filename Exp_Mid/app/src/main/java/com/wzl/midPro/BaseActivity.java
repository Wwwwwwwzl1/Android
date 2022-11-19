package com.wzl.midPro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class BaseActivity extends AppCompatActivity {

    public final String TAG = "BaseActivity";
    public final String ACTION = "NIGHT_SWITCH";
    protected BroadcastReceiver receiver;
    protected IntentFilter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.tvBackground, typedValue, true);


        filter = new IntentFilter();
        filter.addAction(ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                needRefresh();
            }
        };

        registerReceiver(receiver, filter);
    }



    protected abstract void needRefresh();

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
