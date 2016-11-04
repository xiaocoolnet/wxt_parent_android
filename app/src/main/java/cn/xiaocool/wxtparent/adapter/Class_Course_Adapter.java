package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class Class_Course_Adapter extends BaseAdapter {


    private Context context;
    private List<String> list;


    public Class_Course_Adapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_class_course, null);
            vh = new ViewHolder(convertView);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        String xingqi = list.get(position);

        if (xingqi.contains("星期1")) {
            xingqi = "星期一";
        } else if (xingqi.contains("星期2")) {
            xingqi = "星期二";
        } else if (xingqi.contains("星期3")) {
            xingqi = "星期三";
        } else if (xingqi.contains("星期4")) {
            xingqi = "星期四";
        } else if (xingqi.contains("星期5")) {
            xingqi = "星期五";
        }


        vh.tv_xingqi.setText(xingqi);


        return convertView;
    }


    class ViewHolder {
        TextView tv_xingqi, tv_shang_01, tv_shang_02, tv_shang_03, tv_xia_01, tv_xia_02, tv_xia_03;

        public ViewHolder(View convertView) {
            this.tv_xingqi = (TextView) convertView.findViewById(R.id.tv_xingqi);


        }
    }


}
