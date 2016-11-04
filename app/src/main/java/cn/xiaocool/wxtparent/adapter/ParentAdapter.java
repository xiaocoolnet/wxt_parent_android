package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ParentInfo;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by THB on 2016/7/19.
 */
public class ParentAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ParentInfo> friends;
    public ParentAdapter(Context context, ArrayList<ParentInfo> friends){
        this.friends = friends;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_friend_info,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(friends.get(position).getName());
        //holder.tv_phone.setText(friends.get(position).getPhone());
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+friends.get(position).getPhoto(),holder.iv_avator);
        return convertView;
    }
    class ViewHolder{
        TextView tv_name,tv_phone;
        ImageView iv_avator;
        public ViewHolder(View view){
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_avator = (ImageView) view.findViewById(R.id.avator);
            tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        }
    }
}
