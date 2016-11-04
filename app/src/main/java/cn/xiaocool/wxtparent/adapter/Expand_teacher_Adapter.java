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
import cn.xiaocool.wxtparent.bean.Tongxunlu;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class Expand_teacher_Adapter extends BaseExpandableListAdapter {


    private LayoutInflater inflate;
    private Context context;
    private List<Map<String, String>> classAddresses = new ArrayList<>();
    private List<List<Map<String, String>>> teachersAddresses = new ArrayList<>();
//    private List<Tongxunlu.DataEntity> list_data_01;
//    private List<Tongxunlu.DataEntity.TeacherinfoEntity> list_data_02;

//    public Expand_teacher_Adapter(Context context, List<Tongxunlu.DataEntity> list_data_01, List<Tongxunlu.DataEntity.TeacherinfoEntity> list_data_02) {
//        this.context = context;
//        this.list_data_01 = list_data_01;
//        this.list_data_02 = list_data_02;
//        inflate = LayoutInflater.from(context);
//    }


    public Expand_teacher_Adapter(Context context, List<Map<String, String>> classAddresses, List<List<Map<String, String>>> teachersAddresses) {
        this.context = context;
        this.classAddresses = classAddresses;
        this.teachersAddresses = teachersAddresses;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return classAddresses.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return teachersAddresses.size();
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
            convertView = inflate.inflate(R.layout.item_tongxl_expand_title, parent, false);
        }
        title = (TextView) convertView.findViewById(R.id.tv_tongxl_title);
        title.setText(classAddresses.get(groupPosition).get("classname"));
        return convertView;


    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TextView name;
//        final ImageView sms, call;

        TextView call;

        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_tongxl_expand_content, parent, false);
        }
        name = (TextView) convertView.findViewById(R.id.tv_user_name);
//        sms = (ImageView) convertView.findViewById(R.id.sms);
//        call = (ImageView) convertView.findViewById(R.id.tv_user_phone);

        call = (TextView) convertView.findViewById(R.id.tv_user_phone);

        final String phone = teachersAddresses.get(groupPosition).get(childPosition).get("phone");
        Log.i("Info_teacherPhone", phone);
        name.setText(teachersAddresses.get(groupPosition).get(childPosition).get("name"));
        call.setText(phone);
//        final String teacherID = teachersAddresses.get(groupPosition).get(childPosition).get("teacherID");
//        sms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MySendMessageToTeacherActivity.class);
//                intent.putExtra("teacherID", teacherID);
//                mContext.startActivity(intent);
//            }
//        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });
        return convertView;

    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
