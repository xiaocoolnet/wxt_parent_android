package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.BabyFriends;
import cn.xiaocool.wxtparent.bean.BabyTeachers;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class BabyFriendAdapter extends BaseAdapter {


    //BabyFriends.DataEntity  gsonformat  生成的 与 之前的BabyTeachers.BabyTeacherBean 不同
    private List<BabyFriends.DataEntity> babyFriendBeanList;
    private LayoutInflater inflater;
    private Context context;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BabyFriendAdapter(List<BabyFriends.DataEntity> babyFriendBeanList, Context context) {
        this.babyFriendBeanList = babyFriendBeanList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return babyFriendBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return babyFriendBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_baby_friend, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String photo = babyFriendBeanList.get(position).getPhoto();


        displayImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
                .cacheInMemory(true).cacheOnDisc(true).build();
        imageLoader.displayImage(NetBaseConstant.NET_AVATAR_HOST + "/" + photo, vh.image_user, displayImageOptions);


//数据的接口中暂时，没有 打电话与发短信的功能
//
//        vh.image_call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + phone));
//                context.startActivity(intent);
//            }
//        });
//
//        vh.image_sms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri sms_uri = Uri.parse("smsto:" + phone);//设置号码
//                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);//调用发短信Action
////                sms_intent.putExtra("sms_body", "HelloWorld");//用Intent设置短信内容
//                context.startActivity(sms_intent);
//            }
//        });


        vh.tv_baby_friend_name.setText(babyFriendBeanList.get(position).getName());


        return convertView;
    }


    class ViewHolder {
        TextView tv_baby_friend_name, tv_baby_friend_phone;
        ImageView image_call, image_sms, image_user;

        public ViewHolder(View convertView) {
            tv_baby_friend_name = (TextView) convertView.findViewById(R.id.tv_baby_friend_name);

            image_user = (ImageView) convertView.findViewById(R.id.image_user);


        }
    }


}
