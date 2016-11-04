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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Announcement;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.Annou_Pinglun;
import cn.xiaocool.wxtparent.main.ShowImageActivity;
import cn.xiaocool.wxtparent.main.Web_Introduce_pinglun;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.LogUtils;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class WebIntroduce_Adapter extends BaseAdapter {

    private List<Announcement.AnnouncementData> list_webIntroduce_data;
    private Context context;
    private Handler handler;
    private static final int WEBINTRODUCE_PRAISE_KEY = 104;
    private static final int DEL_WEBINTRODUCE_PRAISE_KEY = 105;
    private UserInfo user;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions displayImage;


    public WebIntroduce_Adapter(List<Announcement.AnnouncementData> list_webIntroduce_data, Context context, Handler handler) {
        Log.e("tttt", "Annou_Adapter");
        this.list_webIntroduce_data = list_webIntroduce_data;
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
        return list_webIntroduce_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_webIntroduce_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_web_introduce, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        vh.tv_title.setText(list_webIntroduce_data.get(position).getTitle());
        vh.tv_content.setText(list_webIntroduce_data.get(position).getContent());
        vh.tv_send.setText(list_webIntroduce_data.get(position).getUsername());


        vh.tv_num1.setText(list_webIntroduce_data.get(position).getReadcount() + "");


        vh.tv_num2.setText(list_webIntroduce_data.get(position).getAllreader() + "");
        Date date = new Date();
        date.setTime(Long.parseLong(list_webIntroduce_data.get(position).getCreate_time()) * 1000);
        vh.tv_time1.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));

        vh.linear_zan.setVisibility(View.GONE);
        //判断是否有人点赞
        if (isPraise(list_webIntroduce_data.get(position).getWorkPraise())) {
            //点赞成功后图片变红
            vh.iv_zan.setSelected(true);
            notifyDataSetChanged();
        } else {
            //取消点赞后
            vh.iv_zan.setSelected(false);
            notifyDataSetChanged();
        }

        //判断是否有照片
        if (list_webIntroduce_data.get(position).getPhoto().length() > 1) {
            vh.iv_homework.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + list_webIntroduce_data.get(position).getPhoto(), vh.iv_homework, displayImage);
            Log.d("img", "http://wxt.xiaocool.net/uploads/microblog/" + list_webIntroduce_data.get(position).getPhoto());
            vh.iv_homework.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        vh.iv_homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtra("Image", list_webIntroduce_data.get(position).getPhoto().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });

        if (list_webIntroduce_data.get(position).getWorkPraise().size() > 0) {
            vh.linear_zan.setVisibility(View.VISIBLE);
            String names = "";
            for (int i = 0; i < list_webIntroduce_data.get(position).getWorkPraise().size(); i++) {
                names = names + " " + list_webIntroduce_data.get(position).getWorkPraise().get(i).getName();
            }
            notifyDataSetChanged();
            vh.tv_zan.setText(names);
            notifyDataSetChanged();


        }
        //点赞事件
        vh.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPraise(list_webIntroduce_data.get(position).getWorkPraise())) {
                    LogUtils.d("FindFragment", "delPraise___" + list_webIntroduce_data.get(position).getId());
                    delPraise(list_webIntroduce_data.get(position).getId());
                    notifyDataSetChanged();
                } else {
                    LogUtils.d("FindFragment", "workPraise___" + list_webIntroduce_data.get(position).getId());
                    workPraise(list_webIntroduce_data.get(position).getId());
                    notifyDataSetChanged();
                }
            }
        });


        final String refid = list_webIntroduce_data.get(position).getId();
        vh.iv_ping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.image_pinglun) {
                    Intent intent = new Intent(context, Web_Introduce_pinglun.class);
                    //从一个Activity中要通过intent调出另一个Activity的话，需要使用 FLAG_ACTIVITY_NEW_TASK
                    // 否则的话，会有force close：
                    //如果调出的Activtivity只是一个功能片段，并没有实际的意义，也没有必要出现在长按Home键调出最近使用过的程序类表中，那么使用FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    //Intent传递List<Map<String,String>>的使用
//                        intent.putExtra("list", (Serializable)list_webIntroduce_data.get(position));

                    intent.putExtra("type", 3 + "");//作业的type类型是2，通知公告的是3
                    intent.putExtra("refid", refid);
                    Log.e("ayay", refid + "hw");

                    context.startActivity(intent);

                }
            }
        });
        return convertView;
    }


    private void workPraise(String workBindId) {
        Log.i("begintopppp-=====", "222222");
        new SpaceRequest(context, handler).annou_set_like(workBindId, WEBINTRODUCE_PRAISE_KEY, "3");
        notifyDataSetChanged();
    }

    // 取消点赞
    private void delPraise(String workBindId) {
        new SpaceRequest(context, handler).annou_del_like(workBindId, DEL_WEBINTRODUCE_PRAISE_KEY, "3");
        notifyDataSetChanged();
    }

    /**
     * 判断当前用户是否点赞
     */
    private boolean isPraise(ArrayList<LikeBean> praises) {
        for (int i = 0; i < praises.size(); i++) {
            if (praises.get(i).getUserid().equals(user.getUserId())) {
                Log.d("praisesid", praises.get(i).getUserid());
                notifyDataSetChanged();
                return true;
            }
        }
        notifyDataSetChanged();
        return false;
    }

    class ViewHolder {
        private TextView tv_title, tv_content, tv_send, tv_time1, tv_num1, tv_num2, tv_zan;
        private ImageView iv_homework, iv_zan, iv_ping;
        private LinearLayout linear_zan;


        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.textView1);
            tv_content = (TextView) convertView.findViewById(R.id.textView2);
            tv_send = (TextView) convertView.findViewById(R.id.textView3);
            tv_time1 = (TextView) convertView.findViewById(R.id.textView4);
            tv_num1 = (TextView) convertView.findViewById(R.id.textView7);
            tv_num2 = (TextView) convertView.findViewById(R.id.textView9);
            tv_zan = (TextView) convertView.findViewById(R.id.textView10);

            iv_homework = (ImageView) convertView.findViewById(R.id.image_homework);
            iv_zan = (ImageView) convertView.findViewById(R.id.image_dianzan);
            iv_ping = (ImageView) convertView.findViewById(R.id.image_pinglun);


            linear_zan= (LinearLayout) convertView.findViewById(R.id.linear_zan);


        }
    }

}
