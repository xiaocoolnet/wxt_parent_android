package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MeInvitefamilyAdapter extends BaseAdapter{
    private List<String> familyphoneList;
    private List<String> familynameList;
    private LayoutInflater inflater;

    public MeInvitefamilyAdapter(List<String> familyphoneList, List<String> familynameList, Context mContext) {
        this.familyphoneList = familyphoneList;
        this.familynameList = familynameList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return familynameList.size();
    }

    @Override
    public Object getItem(int position) {
        return familynameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
