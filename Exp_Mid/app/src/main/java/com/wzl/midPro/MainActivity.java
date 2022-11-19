package com.wzl.midPro;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener {

    private NoteDatabase dbHelper;

    private FloatingActionButton fab;
    private ListView lv;
    private ListView lv_plan;
    private LinearLayout lv_layout;
    private LinearLayout lv_plan_layout;

    private Context context = this;
    private NoteAdapter adapter;

    private List<Note> noteList = new ArrayList<Note>();

    private TextView mEmptyView;

    private Toolbar myToolbar;

    private PopupWindow popupWindow; // 左侧弹出菜单
    private PopupWindow popupCover; // 菜单蒙版
    private LayoutInflater layoutInflater;
    private RelativeLayout main;
    private ViewGroup customView;
    private ViewGroup coverView;
    private WindowManager wm;
    private DisplayMetrics metrics;
    private TagAdapter tagAdapter;

    private TextView setting_text;
    private ImageView setting_image;
    private ListView lv_tag;
    private TextView add_tag;

    private BroadcastReceiver myReceiver;
    private Achievement achievement;

    private SharedPreferences sharedPreferences;
    private Button content_button;

    private AlarmManager alarmManager;

    private String choose_Color;
    private  List<TextView> colorlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        achievement = new Achievement(context);

        //初始所有
        initView();

        myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_menu_black_24dp));

        //分类筛选
        myToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow();
            }
        });

    }


    public void chooseColor(View viewed){
        for (TextView view:
              colorlist) {
            switch (view.getId()){

                case R.id.tag_1:
                    view.setBackgroundResource(R.color.tag_1);break;
                case R.id.tag_2:
                    view.setBackgroundResource(R.color.tag_2);break;
                case R.id.tag_3:
                    view.setBackgroundResource(R.color.tag_3); break;
                case R.id.tag_4:
                    view.setBackgroundResource(R.color.tag_4);break;
                case R.id.tag_5:
                    view.setBackgroundResource(R.color.tag_5);break;
                case R.id.tag_6:
                    view.setBackgroundResource(R.color.tag_6);break;
                case R.id.tag_7:
                    view.setBackgroundResource(R.color.tag_7);break;
                case R.id.tag_8:
                    view.setBackgroundResource(R.color.tag_8);break;
                case R.id.tag_9:
                    view.setBackgroundResource(R.color.tag_9);break;
            }
        }

        switch (viewed.getId()){

            case R.id.tag_1: choose_Color="tag-01-color";
                viewed.setBackgroundResource(R.drawable.textview_border2);break;
            case R.id.tag_2:choose_Color="tag-02-color";
                viewed.setBackgroundResource(R.drawable.textview_border3);break;
            case R.id.tag_3: choose_Color="tag-03-color";
                viewed.setBackgroundResource(R.drawable.textview_border1); break;
            case R.id.tag_4:choose_Color="tag-04-color";
                viewed.setBackgroundResource(R.drawable.textview_border4);break;
            case R.id.tag_5:choose_Color="tag-05-color";
                viewed.setBackgroundResource(R.drawable.textview_border5);break;
            case R.id.tag_6:choose_Color="tag-06-color";
                viewed.setBackgroundResource(R.drawable.textview_border6);break;
            case R.id.tag_7:choose_Color="tag-07-color";
                viewed.setBackgroundResource(R.drawable.textview_border7);break;
            case R.id.tag_8:choose_Color="tag-08-color";
                viewed.setBackgroundResource(R.drawable.textview_border8);break;
            case R.id.tag_9:choose_Color="tag-09-color";
                viewed.setBackgroundResource(R.drawable.textview_border9);break;
        }

    }

    private void showPopUpWindow() {
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        popupCover = new PopupWindow(coverView, width, height, false);
        popupWindow = new PopupWindow(customView, (int) (width * 0.7), (height), true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupCover.setAnimationStyle(R.style.AnimationCover);


        //display the popup window
        findViewById(R.id.main_layout).post(new Runnable() {//等待main_layout加载完，再show popupwindow
            @Override
            public void run() {
                popupCover.showAtLocation(main, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.showAtLocation(main, Gravity.NO_GRAVITY, 0, 0);


                //listview
                lv_tag = customView.findViewById(R.id.lv_tag);
                //添加标签
                add_tag = customView.findViewById(R.id.add_tag);

                add_tag.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sharedPreferences.getString("tagList","").split("_").length <9) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            View inflate = View.inflate(MainActivity.this, R.layout.add_tag, null);

                            colorlist.add((TextView) inflate.findViewById(R.id.tag_1));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_2));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_3));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_4));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_5));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_6));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_7));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_8));
                            colorlist.add((TextView) inflate.findViewById(R.id.tag_9));
                            builder
                                    .setView(inflate)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
                                            EditText tag_name=(EditText)inflate.findViewById(R.id.tag_name);

                                            List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_")); //获取tags
                                            String name = tag_name.getText().toString();
                                            System.out.println("newadd"+tag_name);
                                            if (!tagList.contains(name)) {
                                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                                String oldtagList = sharedPreferences.getString("tagList", null);
                                                String newtagList = oldtagList + "_" + name;
                                                String oldtagListColor=sharedPreferences.getString("tagListColor", null);
                                                String newtagListColor = oldtagListColor + "_" + choose_Color;

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("tagList", newtagList);
                                                editor.putString("tagListColor",newtagListColor);
                                                editor.commit();
                                                refreshTagList();
                                            }
                                            else Toast.makeText(context, "Repeated tag!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                        else{
                            Toast.makeText(context, "自定义的标签够多了！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
                List<String> tagListColor =  Arrays.asList( sharedPreferences.getString("tagListColor", null).split("_")); //

               tagAdapter = new TagAdapter(context, tagList, CountTagNum(tagList),tagListColor);

                lv_tag.setAdapter(tagAdapter);


                lv_tag.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
                        int tag = position + 1;
                        List<Note> temp = new ArrayList<>();
                        //全部
                        if(tag==1) {
                            temp=noteList;

                        }else {
                            for (int i = 0; i < noteList.size(); i++) {
                                if (noteList.get(i).getTag() == tag) {
                                    Note note = noteList.get(i);
                                    temp.add(note);
                                }
                            }
                        }
                        NoteAdapter tempAdapter = new NoteAdapter(context, temp);
                        lv.setAdapter(tempAdapter);
                        myToolbar.setTitle(tagList.get(position));
                        popupWindow.dismiss();
                        Log.d(TAG, position + "");
                    }
                });

                lv_tag.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setMessage("All related notes will be tagged as \"not tag\" !")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int tag = position + 1;
                                                    for (int i = 0; i < noteList.size(); i++) {
                                                        //被删除tag的对应notes tag = 1
                                                        Note temp = noteList.get(i);
                                                        if (temp.getTag() == tag) {
                                                            temp.setTag(1);
                                                            NoteOP op = new NoteOP(context);
                                                            op.open();
                                                            op.updateNote(temp);
                                                            op.close();
                                                        }
                                                    }
                                                    List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
                                                    List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_")); //获取tags
                                                    if(tag + 1 < tagList.size()) {
                                                        for (int j = tag + 1; j < tagList.size() + 1; j++) {
                                                            //大于被删除的tag的所有tag减一
                                                            for (int i = 0; i < noteList.size(); i++) {
                                                                Note temp = noteList.get(i);
                                                                if (temp.getTag() == j) {
                                                                    temp.setTag(j - 1);
                                                                    NoteOP op = new NoteOP(context);
                                                                    op.open();
                                                                    op.updateNote(temp);
                                                                    op.close();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    //edit the preference
                                                    List<String> newTagList = new ArrayList<>();
                                                    List<String> newTagListColor=new ArrayList<>();
                                                    newTagList.addAll(tagList);
                                                    newTagList.remove(position);

                                                    newTagListColor.addAll(tagListColor);
                                                    newTagListColor.remove(position);

                                                    String newtagList = TextUtils.join("_", newTagList);
                                                    String newtagListColor = TextUtils.join("_", newTagListColor);
                                                    Log.d(TAG, "onClick: " + newtagList);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("tagList", newtagList);
                                                    editor.putString("tagListColor",newtagListColor);
                                                    System.out.println("newtagList"+newtagList);
                                                    editor.commit();

                                                    refreshTagList();
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
//                                }
//                            });

                            return true;

                    }
                });



                coverView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        popupCover.dismiss();
                    }
                });
            }
        });

    }
//生成标签列
    private void refreshTagList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
        List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_")); //获取tags
        tagAdapter = new TagAdapter(context, tagList, CountTagNum(tagList),tagListColor);
        lv_tag.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
    }


    @Override
    protected void needRefresh() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("opMode", 10);
        startActivity(intent);
        refreshListView();

        if (popupWindow.isShowing()) popupWindow.dismiss();
        finish();
    }

    public void initView() {

        initPrefs();
        //新增汗牛
        fab = findViewById(R.id.fab);

        //list列表
        lv = findViewById(R.id.lv);
        lv_plan = findViewById(R.id.lv_plan);

        //list的父
        lv_layout = findViewById(R.id.lv_layout);
        lv_plan_layout = findViewById(R.id.lv_plan_layout);

        //顶部导航栏
        myToolbar = findViewById(R.id.my_toolbar);
        lv_layout.setVisibility(View.VISIBLE);

        myToolbar.setTitle("All notes");

        //空白
        mEmptyView = findViewById(R.id.emptyView); // search page

        adapter = new NoteAdapter(getApplicationContext(), noteList);



        refreshListView();
        lv.setAdapter(adapter);
        lv.setEmptyView(mEmptyView); // connect empty textview with listview


        //加号跳转
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("mode", 4);     // MODE of 'new note'
                startActivityForResult(intent, 1);      //collect data from edit
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);

            }
        });

        lv.setOnItemClickListener(this);
        lv_plan.setOnItemClickListener(this);

        lv.setOnItemLongClickListener(this);
        lv_plan.setOnItemLongClickListener(this);

//导航栏
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //设置toolbar取代actionbar
        initPopupView();
    }



    public void initPopupView() {
        //instantiate the popup.xml layout file
        layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = (ViewGroup) layoutInflater.inflate(R.layout.setting_layout, null);
        coverView = (ViewGroup) layoutInflater.inflate(R.layout.setting_cover, null);

        main = findViewById(R.id.main_layout);
        //instantiate popup window
        wm = getWindowManager();
        metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

    }

    private void initPrefs() {
        //initialize all useful SharedPreferences for the first time the app runs


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!sharedPreferences.contains("reverseSort")) {
            editor.putBoolean("reverseSort", false);
            editor.commit();
        }
        if (!sharedPreferences.contains("SortByTag")) {
            editor.putBoolean("SortByTag", false);
            editor.commit();
        }
        if (!sharedPreferences.contains("tagList")) {
            editor.putString("tagList","all notes_no tag_study_life_work_play");
            editor.commit();
        }
        if (!sharedPreferences.contains("tagListColor")) {
            editor.putString("tagListColor","tag-01-color_tag-02-color_tag-03-color_tag-04-color_tag-05-color_tag-06-color");
            editor.commit();
        }


    }
    // 显示OverflowMenu的Icon
    public  void setOverflowIconVisible(int featureId, Menu menu) {
        // ActionBar的featureId是8，Toolbar的featureId是108
        if (featureId % 100 == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    // setOptionalIconsVisible是个隐藏方法，需要通过反射机制调用
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //显示菜单项左侧的图标
        setOverflowIconVisible(featureId,menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        DisplayMetrics dm = new DisplayMetrics();
        //search setting
        MenuItem mSearch = menu.findItem(R.id.content_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSearchView.setMaxWidth((int)(dm.widthPixels ));
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
//

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_setting:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        overridePendingTransition(R.anim.in_righttoleft, R.anim.no); break;
            case R.id.menu_clear:
                     new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Delete All Notes ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new NoteDatabase(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("notes", null, null);//delete data in table NOTES
                        db.execSQL("update sqlite_sequence set seq=0 where name='notes'"); //reset id to 1
                        refreshListView();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();break;



}

        return super.onOptionsItemSelected(item);
    }

    //刷新listview
    public void refreshListView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //initialize CRUD
        NoteOP op = new NoteOP(context);
        op.open();

        // set adapter
       noteList.clear();
        noteList.addAll(op.getAllNotes());
        adapter.notifyDataSetChanged();
        if (sharedPreferences.getBoolean("reverseSort", false)) sortNotes(noteList, 2);
        else sortNotes(noteList, 1);

        if (sharedPreferences.getBoolean("SortByTag", false)) sortNotesByTag(noteList, 3);
        else sortNotesByTag(noteList, 1);
        op.close();


    }

    //click item in listView
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_"));
        switch (parent.getId()) {
            case R.id.lv:
                Note curNote = (Note) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("title", curNote.getTitle());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("mode", 3);     // MODE of 'click to edit'
                intent.putExtra("tag", curNote.getTag());
                intent.putExtra("editColor",tagListColor.get(curNote.getTag()-1));
                startActivityForResult(intent, 1);      //collect data from edit
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
                break;
        }
    }

    // react to startActivityForResult and collect data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        int returnMode;
        long note_Id;

        returnMode = data.getExtras().getInt("mode", -1);
        note_Id = data.getExtras().getLong("id", 0);


        if (returnMode == 0) {  // create new note

            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            String title = data.getExtras().getString("title");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(title,content, time, tag);
            NoteOP op = new NoteOP(context);
            op.open();
            op.addNote(newNote);
            op.close();
            achievement.addNote(content);
        }else if (returnMode == 1) {  //update current note
            String title = data.getExtras().getString("title");
            String content = data.getExtras().getString("content");
            System.out.println("content"+content);
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(title,content, time, tag);
            newNote.setId(note_Id);
            NoteOP op = new NoteOP(context);
            op.open();
            op.updateNote(newNote);
            achievement.editNote(op.getNote(note_Id).getContent(), content);
            op.close();

        } else if (returnMode == 2) {  //delete current note
            Note curNote = new Note();
            curNote.setId(note_Id);
            NoteOP op = new NoteOP(context);
            op.open();
            op.removeNote(curNote);
            op.close();
            achievement.deleteNote();
        }
        refreshListView();
    }

    //longclick item in listView
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv:
                final Note note = noteList.get(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Do you want to delete this note ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteOP op = new NoteOP(context);
                                op.open();
                                op.removeNote(note);
                                op.close();
                                refreshListView();
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
        return true;
    }

    //按模式时间或标签顺序排序排序笔记
    public void sortNotes(List<Note> noteList, final int mode) {
        Collections.sort(noteList, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                try {
                    if (mode == 1) {
                        Log.d(TAG, "sortnotes 1");
                        return npLong(dateStrToSec(o2.getTime()) - dateStrToSec(o1.getTime()));
                    }
                    else if (mode == 2) {//reverseSort
                        Log.d(TAG, "sortnotes 2");
                        return npLong(dateStrToSec(o1.getTime()) - dateStrToSec(o2.getTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
    }
    public void sortNotesByTag(List<Note> noteList, final int mode) {

        if(mode!=3) return;
        Collections.sort(noteList, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {

                        return npLong((long) (o1.getTag()-o2.getTag()));

            }
        });
    }



    //格式转换 string -> milliseconds
    public long dateStrToSec(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long secTime = format.parse(date).getTime();
        return secTime;
    }

    //统计不同标签的笔记数
    public List<Integer> CountTagNum(List<String> StringList){
        Integer[] numbers = new Integer[StringList.size()];
        for(int i = 0; i < numbers.length; i++) numbers[i] = 0;
        for(int i = 0; i < noteList.size(); i++){
            numbers[noteList.get(i).getTag() - 1] ++;
        }
        numbers[0]=noteList.size();
        return Arrays.asList(numbers);
    }

    //turn long into 1, 0, -1
    public int npLong(Long l) {
        if (l > 0) return 1;
        else if (l < 0) return -1;
        else return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }









    //achievement system
    public class Achievement {
        private SharedPreferences sharedPreferences;

        private int noteNumber;
        private int wordNumber;

        private int noteLevel;
        private int wordLevel;

        public Achievement(Context context) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            initPref();
            getPref();
        }

        private void getPref() {
            noteNumber = sharedPreferences.getInt("noteNumber", 0);
            wordNumber = sharedPreferences.getInt("wordNumber", 0);
            noteLevel = sharedPreferences.getInt("noteLevel", 0);
            wordLevel = sharedPreferences.getInt("wordLevel", 0);
        }

        private void initPref() {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (!sharedPreferences.contains("noteLevel")) {
                editor.putInt("noteLevel", 0);
                editor.commit();
                if (!sharedPreferences.contains("wordLevel")) {
                    editor.putInt("wordLevel", 0);
                    editor.commit();

                    addCurrent(noteList);
                    if (sharedPreferences.contains("maxRemainNumber")) {
                        editor.remove("maxRemainNumber");
                        editor.commit();
                    }
                    if (sharedPreferences.contains("remainNumber")){
                        editor.remove("remainNumber");
                        editor.commit();
                    }
                    if (!sharedPreferences.contains("noteNumber")) {
                        editor.putInt("noteNumber", 0);
                        editor.commit();
                        addCurrent(noteList);
                        if (!sharedPreferences.contains("wordNumber")) {
                            editor.putInt("wordNumber", 0);
                            editor.commit();

                        }
                    }

                }
            }
        }

        //加入已写好的笔记
        private void addCurrent(List<Note> list) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int tempNN = list.size();
            editor.putInt("noteNumber", tempNN);
            if (tempNN >= 1000) editor.putInt("noteLevel", 4);
            else if (tempNN >= 100) editor.putInt("noteLevel", 3);
            else if (tempNN >= 10) editor.putInt("noteLevel", 2);
            else if (tempNN >= 1) editor.putInt("noteLevel", 1);
            int wordCount = 0;
            for (int i = 0; i < list.size(); i++) {
                wordCount += list.get(i).getContent().length();
            }
            editor.putInt("wordNumber", wordCount);
            if (wordCount >= 20000) editor.putInt("noteLevel", 5);
            else if (wordCount >= 5000) editor.putInt("noteLevel", 4);
            else if (wordCount >= 1000) editor.putInt("noteLevel", 3);
            else if (wordCount >= 500) editor.putInt("noteLevel", 2);
            else if (wordCount >= 100) editor.putInt("noteLevel", 1);
            editor.commit();
        }

        //添加笔记
        public void addNote(String content) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            noteNumber++;
            editor.putInt("noteNumber", noteNumber);

            wordNumber += content.length();
            editor.putInt("wordNumber", wordNumber);

            editor.commit();
        }

        //删除笔记
        public void deleteNote() {

        }

        //编辑笔记，修改字数
        public void editNote(String oldContent, String newContent) {
            if (newContent.length() > oldContent.length()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                wordNumber += (newContent.length() - oldContent.length());
                editor.putInt("wordNumber", wordNumber);
                editor.commit();
            }
        }

        //笔记数成就
        public void noteNumberAchievement(int num) {
            switch (num) {
                case 1:
                    if (noteLevel == 0) announcement("This is your first step!", 1, num);
                    break;
                case 10:
                    if (noteLevel == 1) announcement("Keep going, and don't give up", 1, num);
                    break;
                case 100:
                    if (noteLevel == 2) announcement("This has been a long way...", 1, num);
                    break;
                case 1000:
                    if (noteLevel == 3) announcement("Final achievement! Well Done!", 1, num);
                    break;
            }

        }

        //字数成就
        public void wordNumberAchievement(int num) {
            if (num > 20000 && wordLevel == 4) announcement("Final Achievement! Congrats!", 2, 20000);
            else if (num > 5000 && wordLevel == 3)
                announcement("A long story...", 2, 5000);
            else if (num > 1000 && wordLevel == 2)
                announcement("Double essays!", 2, 1000);
            else if (num > 500 && wordLevel == 1)
                announcement("You have written an essay!", 2, 500);
            else if (num > 100 && wordLevel == 0)
                announcement("Take it slow to create more possibilities!", 2, 100);

        }

        //对话框
        public void announcement(String message, int mode, int num) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(annoucementTitle(mode, num))
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
            setState(mode);
        }

        //对话框标题
        public String annoucementTitle(int mode, int num) {
            switch (mode) {
                case 1:
                    return "You have written " + num + " notes! ";
                case 2:
                    return "You have written " + num + " words! ";
                case 3:
                    return "You have " + num + " notes remaining visible!";
            }
            return null;
        }

        public void setState(int mode) {
            //set corresponding state to true in case repetition of annoucement
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (mode) {
                case 1:
                    noteLevel ++;
                    editor.putInt("noteLevel", noteLevel);
                    editor.commit();
                    break;
                case 2:
                    wordLevel ++;
                    editor.putInt("wordLevel", wordLevel);
                    editor.commit();
                    break;
            }
        }

        //监听
        public void listen() {
            noteNumberAchievement(noteNumber);
            wordNumberAchievement(wordNumber);
        }

    }

}
