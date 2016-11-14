package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Address;
import cn.xiaocool.wxtparent.main.TeacherCommunicationActivity;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by THB on 2016/7/19.
 */
public class AddressAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Address> addresses;
    private LayoutInflater inflater;
    public AddressAdapter(ArrayList<Address> addresses,Context context){
        this.context = context;
        this.addresses = addresses;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_address_info,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(addresses.get(position).getName());
        holder.tv_phone.setText(addresses.get(position).getPhone());
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+addresses.get(position).getAvator(),holder.item_avator);
        final String phone = addresses.get(position).getPhone();
        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });
        holder.iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TeacherCommunicationActivity.class);
                intent.putExtra("reciver_id", addresses.get(position).getId());
                intent.putExtra("chat_name",addresses.get(position).getName());
                intent.putExtra("usertype","1");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView tv_name,tv_phone;
        ImageView iv_call,iv_chat,item_avator;
        public ViewHolder(View view){
            tv_name = (TextView) view.findViewById(R.id.address_name);
            tv_phone = (TextView) view.findViewById(R.id.address_phone);
            iv_call = (ImageView) view.findViewById(R.id.iv_call);
            iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
            item_avator = (ImageView) view.findViewById(R.id.item_avator);
        }
    }
}
