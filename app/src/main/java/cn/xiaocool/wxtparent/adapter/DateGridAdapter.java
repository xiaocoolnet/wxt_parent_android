package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/6/16.
 */
public class DateGridAdapter extends BaseAdapter{

    private int i;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mWorkImgs;
    private int[] state;
    private Context mContext;



    public DateGridAdapter(ArrayList<String> workImgs, Context context,int[] state) {
        this.mContext = context;
        this.mWorkImgs = workImgs;
        this.state = state;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.i = mWorkImgs.size();
    }

    @Override
    public int getCount() {
        return mWorkImgs.size();
    }

    @Override
    public Object getItem(int position) {
        return i;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.date_item_text, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.date_text);
            viewHolder.tv_state = (TextView) convertView.findViewById(R.id.attendance_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }

        //判断颜色
        if (mWorkImgs.get(position).equals(" ")){
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        viewHolder.textView.setText(mWorkImgs.get(position));
        if(state[position]==1){
            viewHolder.tv_state.setText("");
            convertView.setBackgroundColor(Color.parseColor("#9ccc65"));
        }
        if(state[position]==2){
            viewHolder.tv_state.setText("");
            convertView.setBackgroundColor(Color.parseColor("#2db3f8"));
        }
        return convertView;
    }

    private static class MyGridViewHolder {
        TextView textView,tv_state;
    }
}
