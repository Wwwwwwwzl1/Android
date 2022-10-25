package com.example.lab3_ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MenuActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        textView=findViewById(R.id.menutext);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        this.getMenuInflater().inflate(R.menu.menu_demo, m);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.font_small:
                smallFont();
                break;
            case R.id.font_mid:
                midFont();
                break;
            case R.id.font_big:
                bigFont();
                break;
            case R.id.mi_normal:
                toast();
                break;
            case R.id.font_red:
                red();
                break;
            case R.id.font_black:
                black();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void smallFont()
    {
        textView.setTextSize(10);
    }

    public void midFont()
    {
        textView.setTextSize(16);
    }


    public void bigFont()
    {
        textView.setTextSize(20);
    }


    public void toast()
    {
        Toast toast = Toast.makeText(this, "这是普通菜单项", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void red()
    {
        textView.setTextColor(Color.RED);
    }

    public void black()
    {
        textView.setTextColor(Color.BLACK);
    }
}
