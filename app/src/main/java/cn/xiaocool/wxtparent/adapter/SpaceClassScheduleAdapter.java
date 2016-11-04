package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ClassSchedule;

/**
 * Created by Administrator on 2016/3/25.
 */
public class SpaceClassScheduleAdapter extends BaseAdapter{
    private List<ClassSchedule.ClassScheduleData> classScheduleDataList;
    private LayoutInflater inflater;

    public SpaceClassScheduleAdapter(List<ClassSchedule.ClassScheduleData> classScheduleDataList, Context mContext) {
        this.classScheduleDataList = classScheduleDataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return classScheduleDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return classScheduleDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_class_schedule,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.scheduName.setText(classScheduleDataList.get(position).getSyllabus_name());
        return convertView;
    }
    class ViewHolder{
        TextView scheduName;
        public ViewHolder(View convertView) {
            scheduName = (TextView) convertView.findViewById(R.id.item_schedule_name);
        }
    }
}
