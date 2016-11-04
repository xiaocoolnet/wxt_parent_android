package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ParentWarn;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.ShowImageActivity;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class ParentWarn_Adapter extends BaseAdapter {

    ArrayList<ParentWarn.DataBean> list;
    Context context;
    Handler handler;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    private UserInfo user;

    public ParentWarn_Adapter(ArrayList<ParentWarn.DataBean> list, Context context, Handler handler) {
        this.list = list;
        this.context = context;
        this.handler = handler;
        user = new UserInfo(context);
        user.readData(context);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_parentwarn, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        vh.tv_name.setText(list.get(position).getUsername());
        vh.tv_content.setText(list.get(position).getContent());

        Date date = new Date();
        date.setTime(Long.parseLong(list.get(position).getCreate_time()) * 1000);
        vh.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));

        vh.tv_teacher.setText(list.get(position).getTeachername());
        //判断是否有照片
        if (list.get(position).getPhoto().length() > 1) {
            vh.iv_content.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + list.get(position).getPhoto(), vh.iv_content, displayImage);
            Log.d("img", "http://wxt.xiaocool.net/uploads/microblog/" + list.get(position).getPhoto());
            vh.iv_content.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        vh.iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtra("Image", list.get(position).getPhoto().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });


        return convertView;
    }


    class ViewHolder {
        private TextView tv_name, tv_content, tv_teacher, tv_time;
        private ImageView iv_content;


        public ViewHolder(View convertView) {
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            iv_content = (ImageView) convertView.findViewById(R.id.iv_content);


        }
    }


}
