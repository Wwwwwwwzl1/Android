package com.wzl.midPro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends BaseActivity {

    private Switch nightMode;
    private Switch reverseSort;
    private Switch reverseSortTag;
    private SharedPreferences sharedPreferences;

    private static boolean night_change;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Intent intent = getIntent();


        initView();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


         myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_settings_black_24dp));
    }

    @Override
    protected void needRefresh() {
        //因为自身的刷新与其他activity不同步，所以此处留白
    }

    private void initView(){
  
        reverseSort = findViewById(R.id.reverseSort);
        reverseSortTag = findViewById(R.id.reverseSortTag);

        reverseSort.setChecked(sharedPreferences.getBoolean("reverseSort", false));
        reverseSortTag.setChecked(sharedPreferences.getBoolean("SortByTag", false));

        reverseSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("reverseSort", isChecked);
                editor.commit();
            }
        });
        reverseSortTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("SortByTag", isChecked);
                editor.commit();
            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent = new Intent();
            intent.setAction("NIGHT_SWITCH");
            sendBroadcast(intent);
            finish();
            overridePendingTransition(R.anim.in_lefttoright, R.anim.out_lefttoright);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


}
