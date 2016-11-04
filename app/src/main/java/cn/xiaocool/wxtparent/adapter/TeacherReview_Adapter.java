package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class TeacherReview_Adapter extends BaseAdapter {

    Context context;

    public TeacherReview_Adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_teacher_review, null);


        return convertView;
    }
}
