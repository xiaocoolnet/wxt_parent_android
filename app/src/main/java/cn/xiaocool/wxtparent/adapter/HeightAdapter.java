package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Weight;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class HeightAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Weight> weights;
    private LayoutInflater inflater;
    public HeightAdapter(Context context, ArrayList<Weight> weights){
        this.context = context;
        this.weights = weights;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return weights.size();
    }

    @Override
    public Object getItem(int position) {
        return weights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_height_info,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_weight.setText(weights.get(position).getWeight());
        holder.tv_time.setText(weights.get(position).getLogdate());
        return convertView;
    }
    class ViewHolder{
        TextView tv_weight,tv_time,tv_age;
        public ViewHolder(View view){
            tv_weight = (TextView) view.findViewById(R.id.weight);
            tv_time = (TextView) view.findViewById(R.id.time);
            tv_age = (TextView) view.findViewById(R.id.age);
        }
    }
}
