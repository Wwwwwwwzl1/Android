package com.example.lab3_ui;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionModeActivity extends AppCompatActivity {
    private String[] names = new String[]
            { "One", "Two", "Three", "Four","Five"};
    ActionMode actionMode;
    int selected = 0;
    private  List<Boolean> selected_posistion=new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionmode);

        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names.length; i++)
        {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("Name", names[i]);
            selected_posistion.add(false);
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.simple_item_1,
                new String[] { "Name"},
                new int[] { R.id.name});
        ListView list = (ListView) findViewById(R.id.actionModeList);

        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

          if(actionMode==null) actionMode=startActionMode(actionModeCallback);

          selected_posistion.set(position,!selected_posistion.get(position));

                if(selected_posistion.get(position)){
                        view.setBackgroundResource(R.color.purple_200);
              selected++;
                    } else {
              view.setBackgroundResource(R.color.white);
              selected--;
                    }

                actionModeCallback.onActionItemClicked(actionMode,null);
            }
        });

    }
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {


        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_blank, menu);
            return true;
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            actionMode.setTitle(selected + " selected");
            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }



        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

}