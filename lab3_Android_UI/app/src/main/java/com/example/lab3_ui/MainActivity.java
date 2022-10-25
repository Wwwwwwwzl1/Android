package com.example.lab3_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String[] names = new String[]
            { "Lion", "Tiger", "Monkey", "Dog","cat","Elephant"};
    private int[] imageIds = new int[]
            { R.drawable.lion , R.drawable.tiger
                    , R.drawable.monkey , R.drawable.dog,R.drawable.cat,R.drawable.elephant};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names.length; i++)
        {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("header", imageIds[i]);
            listItem.put("personName", names[i]);
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.simple_item,
                new String[] { "header" , "personName"},
                new int[] { R.id.header , R.id.name});
        ListView list = (ListView) findViewById(R.id.mylist);
//        // 为ListView设置Adapter
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i=0;i<parent.getCount();i++){

                    View item=parent.getChildAt(i);

                    if (position == i) {
                        item.setBackgroundResource(R.color.red);
                    } else {
                        item.setBackgroundResource(R.color.white);
                    }
                }
                Toast toast = Toast.makeText(MainActivity.this, names[position],Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 1150);
                toast.show();
            }
        });

    }
}