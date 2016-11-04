package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.StudentInfo;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class StudentInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StudentInfo> studentInfos;
    private LayoutInflater inflater;
    private Handler handler;
    public StudentInfoAdapter(Context context,ArrayList<StudentInfo> studentInfos ,Handler handler){
        this.context = context;
        this.studentInfos = studentInfos;
        this.handler = handler;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return studentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return studentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_student_info,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + studentInfos.get(position).getPhoto(), holder.iv_avator);
        holder.tv_name.setText(studentInfos.get(position).getName());
        if(studentInfos.get(position).getIsFriend()==1){
            holder.tv_add.setBackgroundResource(R.drawable.button_boder_green);
            holder.tv_add.setText("已添加");
            holder.tv_add.setTextColor(Color.parseColor("#9BE5B4"));
            holder.tv_add.setLinksClickable(false);
        }else if(studentInfos.get(position).getIsFriend()==0){
            holder.tv_add.setBackgroundResource(R.drawable.button_boder_gray);
            holder.tv_add.setText("添加");
            holder.tv_add.setTextColor(Color.parseColor("#949294"));
            holder.tv_add.setLinksClickable(true);
            holder.tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SpaceRequest(context,handler).addFriend(studentInfos.get(position).getId());
                    holder.tv_add.setBackgroundResource(R.drawable.button_boder_green);
                    holder.tv_add.setText("已添加");
                    holder.tv_add.setTextColor(Color.parseColor("#9BE5B4"));
                    holder.tv_add.setLinksClickable(false);
                }
            });
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_name,tv_add;
        ImageView iv_avator;
        public ViewHolder(View view){
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_avator = (ImageView) view.findViewById(R.id.iv_avatar);
            tv_add = (TextView) view.findViewById(R.id.tv_add);
        }
    }
}
