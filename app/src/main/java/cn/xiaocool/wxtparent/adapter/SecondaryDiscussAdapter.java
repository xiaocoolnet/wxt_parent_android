package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.find.DiscussInfo;

/**
 * Created by mac on 16/3/3.
 */
public class SecondaryDiscussAdapter extends BaseAdapter {
    private ArrayList<DiscussInfo> workDiscusses;
    private LayoutInflater mLayoutInflater;

    public SecondaryDiscussAdapter(Context mContext, DiscussInfo workDiscuss) {
        mLayoutInflater = LayoutInflater.from(mContext);
        if (workDiscuss != null && workDiscuss.getSonDiscuss() != null) {
            this.workDiscusses = workDiscuss.getSonDiscuss();
        } else {
            this.workDiscusses = new ArrayList<DiscussInfo>();
        }
    }

    @Override
    public int getCount() {
        return workDiscusses.size();
    }

    @Override
    public DiscussInfo getItem(int position) {
        return workDiscusses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        DiscussInfo workDiscuss = workDiscusses.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.profession_circle_secondary_discuss_item, parent, false);
            viewHolder.tv_secondary_discuss = (TextView) convertView.findViewById(R.id.tv_profession_circle_secondary_discuss_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_secondary_discuss.setText(Html.fromHtml("<font size=\"3\" color=\"#006dce\">" + workDiscuss.getMemberName()
                + "</font><font size=\"3\" color=\"black\">回复</font><font size=\"3\" color=\"#006dce\">" + workDiscuss.getToMemberName() + ":" + "</font><font size=\"3\" color=\"black\">"
                + workDiscuss.getWorkDiscussMatter() + "</font>"));
        return convertView;
    }

    private class ViewHolder {
        TextView tv_secondary_discuss;
    }
}

