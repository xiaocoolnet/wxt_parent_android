package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.main.MySendMessageToTeacherActivity;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<Map<String, String>> classAddresses = new ArrayList<>();
    private List<List<Map<String, String>>> teachersAddresses = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;

    public MyExpandableListViewAdapter(List<Map<String, String>> classAddresses, List<List<Map<String, String>>> teachersAddresses, Context mContext) {
        this.classAddresses = classAddresses;
        this.teachersAddresses = teachersAddresses;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return classAddresses.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return teachersAddresses.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classAddresses.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return teachersAddresses.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView title;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.class_addresses, parent, false);
        }
        title = (TextView) convertView.findViewById(R.id.textGroup);
        title.setText(classAddresses.get(groupPosition).get("group"));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView name;
        final ImageView sms, call;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.teachers_addresses, parent, false);
        }
        name = (TextView) convertView.findViewById(R.id.text_teacherName);
        sms = (ImageView) convertView.findViewById(R.id.sms);
        call = (ImageView) convertView.findViewById(R.id.call);
        final String teacherPhone = teachersAddresses.get(groupPosition).get(childPosition).get("teacherPhone");
        Log.i("Info_teacherPhone", teacherPhone);
        name.setText(teachersAddresses.get(groupPosition).get(childPosition).get("teacherName"));
        final String teacherID = teachersAddresses.get(groupPosition).get(childPosition).get("teacherID");
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MySendMessageToTeacherActivity.class);
                intent.putExtra("teacherID", teacherID);
                mContext.startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + teacherPhone));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
