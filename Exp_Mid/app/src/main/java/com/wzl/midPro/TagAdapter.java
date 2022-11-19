package com.wzl.midPro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class TagAdapter extends BaseAdapter {

    private Context context;
    private List<String> tagList;
    private List<String> tagListColor;
    private List<Integer> numList;

    public TagAdapter(Context context, List<String> tagList, List<Integer> numList,List<String> tagListColor) {
        this.context = context;
        this.tagList = tagList;
        this.numList = numList;
        this.tagListColor=tagListColor;
    }

    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int position) {
        return tagList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        for(int i = 0; i < numList.size(); i++) Log.d("tag", numList.get(i).toString());
        Log.d("tag", "getView: " + numList.size());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        context.setTheme((sharedPreferences.getBoolean("nightMode", false)? R.style.NightTheme: R.style.DayTheme));
        View v = View.inflate(context, R.layout.tag_layout, null);
        TextView blank_tag = v.findViewById(R.id.blank_tag);
        TextView text_tag = v.findViewById(R.id.text_tag);
        LinearLayout  list=(LinearLayout)v.findViewById(R.id.tag_item);

        switch (tagListColor.get(position)){

                case "tag-01-color": list.setBackgroundResource(R.color.tag_1_color);  break;
                case "tag-02-color":list.setBackgroundResource(R.color.tag_2_color);break;
                case "tag-03-color":list.setBackgroundResource(R.color.tag_3_color);break;
                case "tag-04-color":list.setBackgroundResource(R.color.tag_4_color);break;
                case "tag-05-color":list.setBackgroundResource(R.color.tag_5_color);break;
                case "tag-06-color":list.setBackgroundResource(R.color.tag_6_color);break;
                case "tag-07-color":list.setBackgroundResource(R.color.tag_7_color);break;
                case "tag-08-color":list.setBackgroundResource(R.color.tag_8_color);break;
                case "tag-09-color":list.setBackgroundResource(R.color.tag_9_color);break;


        }
        blank_tag.setText(numList.get(position).toString());
        text_tag.setText(tagList.get(position));


        return v;
    }
}
