package com.wzl.midPro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;

    private List<Note> backupList;//用来备份原始数据
    private List<Note> noteList;//这个数据是会改变的，所以要有个变量来备份一下原始数据
    private FilterSearch mFilter;

    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        backupList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme((sharedPreferences.getBoolean("nightMode", false)? R.style.NightTheme: R.style.DayTheme));
        View v = View.inflate(mContext, R.layout.note_item, null);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_title=v.findViewById(R.id.tv_title);
        TextView time = (TextView)v.findViewById(R.id.time);

        List<String> tagListColor = Arrays.asList(sharedPreferences.getString("tagListColor", null).split("_"));
//        tv_content.setText(allText.split("\n")[0]);
        tv_content.setText(noteList.get(position).getContent());

            tv_title.setText(noteList.get(position).getTitle());

       time.setText(noteList.get(position).getTime());

        //Save note id to tag
        v.setTag(noteList.get(position).getId());

    switch (tagListColor.get(noteList.get(position).getTag() - 1)) {
        case "tag-01-color":
            v.setBackgroundResource(R.color.tag_1);
            break;
        case "tag-02-color":
            v.setBackgroundResource(R.color.tag_2);
            break;
        case "tag-03-color":
            v.setBackgroundResource(R.color.tag_3);
            break;
        case "tag-04-color":
            v.setBackgroundResource(R.color.tag_4);
            break;
        case "tag-05-color":
            v.setBackgroundResource(R.color.tag_5);
            break;
        case "tag-06-color":
            v.setBackgroundResource(R.color.tag_6);
            break;
        case "tag-07-color":
            v.setBackgroundResource(R.color.tag_7);
            break;
        case "tag-08-color":
            v.setBackgroundResource(R.color.tag_8);
            break;
        case "tag-09-color":
            v.setBackgroundResource(R.color.tag_9);
            break;

    }

        return v;
    }

    @Override
    public Filter getFilter() {
        if (mFilter ==null){
            mFilter = new FilterSearch();
        }
        return mFilter;
    }


    class FilterSearch extends Filter {
        //performFiltering(CharSequence charSequence)中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<Note> resultlist;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，显示所有的数据
                resultlist = backupList;
            } else {//否则把符合条件的数据对象添加到集合中
                resultlist = new ArrayList<>();
                for (Note note : backupList) {
                    if (note.getContent().contains(charSequence)) {
                        resultlist.add(note);
                    }

                }
            }
            result.values = resultlist; //将得到的集合保存到FilterResults的value变量中
            result.count = resultlist.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            noteList = (List<Note>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }

}
