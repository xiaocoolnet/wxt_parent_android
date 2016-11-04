package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.BabyTeachers;

/**
 * Created by Administrator on 2016/3/31.
 */
public class BabyTeacherAdapter extends BaseAdapter {
    private List<BabyTeachers.BabyTeacherBean> babyTeacherBeanList;
    private LayoutInflater inflater;
    private Context context;


    public BabyTeacherAdapter(List<BabyTeachers.BabyTeacherBean> babyTeacherBeanList, Context context) {
        this.babyTeacherBeanList = babyTeacherBeanList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return babyTeacherBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return babyTeacherBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_baby_teacher, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        final String phone = babyTeacherBeanList.get(position).getTeacherphone();

        vh.image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });

        vh.image_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:" + phone);//设置号码
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);//调用发短信Action
//                sms_intent.putExtra("sms_body", "HelloWorld");//用Intent设置短信内容
                context.startActivity(sms_intent);
            }
        });


        vh.tv_baby_teacher_name.setText(babyTeacherBeanList.get(position).getTeachername());

//        电话号码 暂不显示
//        vh.tv_baby_teacher_phone.setText(babyTeacherBeanList.get(position).getTeacherphone());

        return convertView;
    }


    class ViewHolder {
        TextView tv_baby_teacher_name, tv_baby_teacher_phone;
        ImageView image_call, image_sms;

        public ViewHolder(View convertView) {
            tv_baby_teacher_name = (TextView) convertView.findViewById(R.id.tv_baby_teacher_name);
            tv_baby_teacher_phone = (TextView) convertView.findViewById(R.id.tv_baby_teacher_phone);
            image_call = (ImageView) convertView.findViewById(R.id.call);
            image_sms = (ImageView) convertView.findViewById(R.id.sms);


        }
    }


}
