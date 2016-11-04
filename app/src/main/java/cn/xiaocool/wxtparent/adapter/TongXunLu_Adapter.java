package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/6/5 0005.
 */
public class TongXunLu_Adapter extends BaseAdapter {

    private Context context;
    private List<String> list;


    public TongXunLu_Adapter(Context context, List<String> list) {
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
        if(convertView==null)
        {
            convertView=View.inflate(context, R.layout.item_space_tongxunlu,null);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);

        }
        else
        {
            vh= (ViewHolder) convertView.getTag();

        }

        vh.tv_name.setText(list.get(position));

        return convertView;
    }


    class ViewHolder {
        private TextView tv_name, tv_phone;
        private ImageView iv_user;

        public ViewHolder(View convertView) {
            tv_name= (TextView) convertView.findViewById(R.id.tv_txl_name);
            tv_phone= (TextView) convertView.findViewById(R.id.tv_txl_phone);

            iv_user= (ImageView) convertView.findViewById(R.id.iv_txl);


        }
    }
}
