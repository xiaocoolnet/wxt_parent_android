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
import java.util.HashMap;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by wzh on 2016/4/2.
 */
public class MyBabyAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, Object>> hashMapList;
    private Context context;
    private UserInfo userInfo;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public MyBabyAdapter(Context context, ArrayList<HashMap<String, Object>> hashMapList) {
        this.context = context;
        userInfo = new UserInfo(context);
        this.layoutInflater = LayoutInflater.from(context);
        this.hashMapList = hashMapList;
    }

    @Override
    public int getCount() {
        return hashMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return hashMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.me_mybaby_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(position!=0){
            holder.main_baby.setVisibility(View.GONE);
        }
        holder.tv_name.setText(hashMapList.get(position).get("babyName").toString());
        if (hashMapList.get(position).get("schoolName")!=null&&hashMapList.get(position).get("className")!=null){
            holder.tv_className.setText(hashMapList.get(position).get("schoolName").toString()+"  "+hashMapList.get(position).get("className").toString());
        }
        imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + hashMapList.get(position).get("babyAvator"), holder.iv_avator);
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setChildId(hashMapList.get(position).get("babyId").toString());
                userInfo.setChildName(hashMapList.get(position).get("babyName").toString());
                userInfo.setChildAvator(hashMapList.get(position).get("babyAvator").toString());
                userInfo.setSchoolId(hashMapList.get(position).get("schoolid").toString());
                userInfo.setClassId(hashMapList.get(position).get("classid").toString());
                userInfo.setClassName(hashMapList.get(position).get("className").toString());
                userInfo.writeData(context);
            }
        });*/
        return convertView;
    }

    public class ViewHolder {
        TextView tv_name, tv_className;
        ImageView iv_avator,main_baby;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.item_name);
            tv_className = (TextView) view.findViewById(R.id.item_classname);
            iv_avator = (ImageView) view.findViewById(R.id.item_avator);
            main_baby = (ImageView) view.findViewById(R.id.main_baby);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
