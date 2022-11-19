package com.wzl.midPro;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends BaseActivity {

    private NoteDatabase dbHelper;
    private Context context = this;
    private EditText edit_title;
    private EditText edit_content;
    private String old_title = "";
    private String old_content = "";
    private String old_time = "";
    private int old_Tag = 1;
    private long id = 0;
    private int openMode = 0;
    private int tag = 1;
    private boolean tagChange = false;
    private Toolbar toolbar;
private  Bitmap bitmap;
    private ScreenUtils ScreenUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.toolbar = myToolbar;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra("title") != null) {
            myToolbar.setTitle(getIntent().getStringExtra("title"));
            System.out.println("title" + getIntent().getStringExtra("title"));
        }
        if (getIntent().getStringExtra("editColor") != null)
            changeColorEdit(getIntent().getStringExtra("editColor"));

        //调用系统拍照界面


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> tagList = Arrays.asList(sharedPreferences.getString("tagList", null).split("_")); //获取tags
        List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_")); //获取tags

        List<String> tagList1 = new ArrayList<>();
        for (int i = 1; i < tagList.size(); i++) {
            tagList1.add(tagList.get(i));
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, tagList1);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tag = (int) id + 2;
                changeColorEdit(tagListColor.get(tag - 1));
                tagChange = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp));

        myToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                String title = null;

                if (edit_title.getText().toString() == null || edit_title.getText().toString().equals("")) {
                    title = edit_content.getText().toString().split("\n")[0];
                } else {
                    title = edit_title.getText().toString();
                }

                if (openMode == 4) {
                    if (edit_content.getText().toString().length() == 0) {
                        intent.putExtra("mode", -1); //nothing new happens.
                    } else {

                        intent.putExtra("mode", 0); // new one note;
                        intent.putExtra("title", title); // new one note;
                        intent.putExtra("content", edit_content.getText().toString());
                        intent.putExtra("time", dateEdit());
                        intent.putExtra("tag", tag);

                    }
                } else {
                    if (edit_content.getText().toString().equals(old_content) && edit_content.getText().toString().equals(old_title) && !tagChange)
                        intent.putExtra("mode", -1); // edit nothing
                    else {
                        intent.putExtra("mode", 1); //edit the content
                        intent.putExtra("content", edit_content.getText().toString());

                        intent.putExtra("title", title);// new one note;
                        intent.putExtra("time", dateEdit());
                        intent.putExtra("id", id);
                        intent.putExtra("tag", tag);
                    }
                }
                setResult(RESULT_OK, intent);
                finish();//返回
                overridePendingTransition(R.anim.in_lefttoright, R.anim.out_lefttoright);
            }
        });

        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);
        Intent getIntent = getIntent();
        EditText display;
        openMode = getIntent.getIntExtra("mode", 0);
        if (openMode == 3) {//打开已存在的note
            id = getIntent.getLongExtra("id", 0);
            old_title = getIntent.getStringExtra("title");
            old_content = getIntent.getStringExtra("content");
            old_time = getIntent.getStringExtra("time");
            old_Tag = getIntent.getIntExtra("tag", 1);
            edit_title.setText(old_title);
//            edit_content.setText(old_content);
//            edit_content.setSelection(old_content.length());
            mySpinner.setSelection(old_Tag - 2);
            //input是获取将被解析的字符串
            String input = old_content;
            //将图片那一串字符串解析出来,即<img src=="xxx" />
            Pattern p = Pattern.compile("<img.*?>");
            Matcher m = p.matcher(input);
            //使用SpannableString了，这个不会可以看这里哦：http://blog.sina.com.cn/s/blog_766aa3810100u8tx.html#cmt_523FF91E-7F000001-B8CB053C-7FA-8A0
            SpannableString spannable = new SpannableString(input);
            while(m.find()){
                String s = m.group();
                int start = m.start();
                int end = m.end();
                //path是去掉<img src=""/>的中间的图片路径
                String path = s.replaceAll("<img src=\"","").trim().replaceAll("\"/>","").trim();
                //Log.d("YYPT_AFTER", path);
                //利用spannableString和ImageSpan来替换掉这些图片
                int width = ScreenUtils.getScreenWidth(EditActivity.this);
                int height = ScreenUtils.getScreenHeight(EditActivity.this);

                Bitmap bitmap = ImageUtils.getSmallBitmap(path,width/2,120);
                ImageSpan imageSpan = new ImageSpan(this, bitmap);
                spannable.setSpan(imageSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            }
            edit_content.setText(spannable);
            edit_content.append("\n");

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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        EditText content=findViewById(R.id.edit_content);
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        if (requestCode == 1) {
            try {
                // 获得图片的uri
                Uri originalUri = data.getData();
                System.out.println("path"+originalUri);
                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                //获取地址
                File file = uriToFileApiQ(originalUri,this);
                file.getAbsoluteFile();

                insertImg(file.getAbsoluteFile().toString(),content);
                //Toast.makeText(AddFlagActivity.this,path,Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(EditActivity.this, "图片插入失败", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public File uriToFileApiQ(Uri uri, Context context) {
        File file = null;
        if(uri == null) return file;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = System.currentTimeMillis()+ Math.round((Math.random() + 1) * 1000)
                    +"."+ MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));

//            注释掉的方法可以获取到原文件的文件名，但是比较耗时
//            Cursor cursor = contentResolver.query(uri, null, null, null, null);
//            if (cursor.moveToFirst()) {
//                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));}

            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    //region 插入图片
    private void insertImg(String path,EditText content){
        String tagPath = "<img src=\""+path+"\"/>";//为图片路径加上<img>标签
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if(bitmap != null){
            SpannableString ss = getBitmapMime(path,tagPath);
            Editable et = content.getText();
            int start = content.getSelectionStart();
            et.insert(start,ss);
            content.setText(et);
            content.setSelection(start+ss.length());
            content.setFocusableInTouchMode(true);
            content.setFocusable(true);
            content.append("\n");
            Log.d("YYPT", content.getText().toString());
        }
    }
    //endregion

    //region 将图片插入到EditText中
//    private void insertPhotoToEditText(SpannableString ss){
//        Editable et = content.getText();
//        int start = content.getSelectionStart();
//        et.insert(start,ss);
//        content.setText(et);
//        content.setSelection(start+ss.length());
//        content.setFocusableInTouchMode(true);
//        content.setFocusable(true);
//    }
    //endregion

    private SpannableString getBitmapMime(String path,String tagPath) {
        SpannableString ss = new SpannableString(tagPath);//这里使用加了<img>标签的图片路径

        int width = ScreenUtils.getScreenWidth(EditActivity.this);
        int height = ScreenUtils.getScreenHeight(EditActivity.this);


        Bitmap bitmap = ImageUtils.getSmallBitmap(path,60,60);
        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        ss.setSpan(imageSpan, 0, tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    Html.ImageGetter imageGetter = new Html.ImageGetter(){
        @Override
        public Drawable getDrawable(String s) {
              /*  Drawable drawable = null;
                drawable = Drawable.createFromPath(s);

                drawable.setBounds(0,0,480,480);
                return drawable;*/
            int width = ScreenUtils.getScreenWidth(EditActivity.this);
            int height = ScreenUtils.getScreenHeight(EditActivity.this);
            Bitmap bitmap = ImageUtils.getSmallBitmap(s,width,480);
            Drawable drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0,0,width,height);
            return drawable;
        }
    };

        public void changeColorEdit (String tagColor){
            LinearLayout list = (LinearLayout) findViewById(R.id.edit_list);
            switch (tagColor) {
                case "tag-1-color":
                    toolbar.setBackgroundResource(R.color.tag_1);
                    list.setBackgroundResource(R.color.tag_1_main);

                    break;
                case "tag-02-color":
                    toolbar.setBackgroundResource(R.color.tag_2);
                    list.setBackgroundResource(R.color.tag_2_main);
                    break;
                case "tag-03-color":
                    toolbar.setBackgroundResource(R.color.tag_3);
                    list.setBackgroundResource(R.color.tag_3_main);
                    break;
                case "tag-04-color":
                    toolbar.setBackgroundResource(R.color.tag_4);
                    list.setBackgroundResource(R.color.tag_4_main);
                    break;
                case "tag-05-color":
                    toolbar.setBackgroundResource(R.color.tag_5);
                    list.setBackgroundResource(R.color.tag_5_main);
                    break;
                case "tag-06-color":
                    toolbar.setBackgroundResource(R.color.tag_6);
                    list.setBackgroundResource(R.color.tag_6_main);
                    break;
                case "tag-07-color":
                    toolbar.setBackgroundResource(R.color.tag_7);
                    list.setBackgroundResource(R.color.tag_7_main);
                    break;
                case "tag-08-color":
                    toolbar.setBackgroundResource(R.color.tag_8);
                    list.setBackgroundResource(R.color.tag_8_main);
                    break;
                case "tag-09-color":
                    toolbar.setBackgroundResource(R.color.tag_9);
                    list.setBackgroundResource(R.color.tag_9_main);
                    break;

            }

        }
        @Override
        protected void needRefresh () {
            startActivity(new Intent(this, EditActivity.class));

            overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
            finish();
        }

        public boolean onKeyDown ( int keyCode, KeyEvent event){

            if (keyCode == KeyEvent.KEYCODE_HOME) {
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                Intent intent = new Intent();
                if (openMode == 4) {
                    if (edit_content.getText().toString().length() == 0) {
                        intent.putExtra("mode", -1); //nothing new happens.
                    } else {
                        intent.putExtra("mode", 0); // new one note;
                        intent.putExtra("title", edit_title.getText().toString());
                        intent.putExtra("content", edit_content.getText().toString());
                        intent.putExtra("time", dateEdit());
                        intent.putExtra("tag", tag);
                    }
                } else {
                    if (edit_content.getText().toString().equals(old_content) && !tagChange)
                        intent.putExtra("mode", -1); // edit nothing
                    else {
                        intent.putExtra("mode", 1); //edit the content
                        intent.putExtra("title", edit_title.getText().toString());
                        intent.putExtra("content", edit_content.getText().toString());
                        intent.putExtra("time", dateEdit());
                        intent.putExtra("id", id);
                        intent.putExtra("tag", tag);
                    }
                }
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.in_lefttoright, R.anim.out_lefttoright);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.edit_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            final Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.delete:
                    new AlertDialog.Builder(EditActivity.this)
                            .setMessage("Delete this Note ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (openMode == 4) {
                                        intent.putExtra("mode", -1); // delete the note
                                        setResult(RESULT_OK, intent);
                                    } else {
                                        intent.putExtra("mode", 2); // delete the note
                                        intent.putExtra("id", id);
                                        setResult(RESULT_OK, intent);
                                    }
                                    finish();
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                    break;
                case R.id.takephoto:

                    Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.addCategory(Intent.CATEGORY_OPENABLE);
                    intent1.setType("image/*");
                    startActivityForResult(intent1, 1);
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        public String dateEdit () {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(date);
        }

    }
