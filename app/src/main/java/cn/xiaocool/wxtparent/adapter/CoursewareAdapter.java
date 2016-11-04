package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Courseware;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class CoursewareAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Courseware> coursewares;
    private LayoutInflater inflater;

    public CoursewareAdapter(Context context, ArrayList<Courseware> coursewares) {
        this.context = context;
        this.coursewares = coursewares;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return coursewares.size();
    }

    @Override
    public Object getItem(int position) {
        return coursewares.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_courseware_list,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_count.setText(coursewares.get(position).getCourseInfos().size()+"");
        holder.tv_name.setText(coursewares.get(position).getSubject()+"课件");
        return convertView;
    }

    class ViewHolder{
        TextView tv_name,tv_count;
        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_count = (TextView) view.findViewById(R.id.tv_count);
        }
    }
}
