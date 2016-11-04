package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class NoFriendContentAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;

    public NoFriendContentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 1;
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
        convertView = inflater.inflate(R.layout.no_friend_data,null);
        return convertView;
    }
}
