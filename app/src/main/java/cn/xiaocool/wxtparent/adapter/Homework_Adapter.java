package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Homework;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.HomeworkDetailActivity;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class Homework_Adapter extends BaseAdapter {


    private Context context;
    private Handler handler;
    private UserInfo user;
    private ArrayList<Homework.HomeworkData> list_home_data;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;


    public Homework_Adapter(ArrayList<Homework.HomeworkData> list_home_data, Context context, Handler handler) {
        this.list_home_data = list_home_data;
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
        return list_home_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_home_data.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_space_myhomework, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(list_home_data.get(position).getTitle());
        vh.tv_content.setText(list_home_data.get(position).getContent());
        vh.tv_send.setText(list_home_data.get(position).getTeacherName());
        Date date = new Date();
        date.setTime(Long.parseLong(list_home_data.get(position).getCreate_time()) * 1000);
        vh.tv_time1.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));


        //判断是否有照片
        if (list_home_data.get(position).getPics().size()>1){
            vh.iv_homework.setVisibility(View.GONE);
            vh.parent_warn_img_gridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(list_home_data.get(position).getPics(),context);
            vh.parent_warn_img_gridview.setAdapter(parWarnImgGridAdapter);
            vh.parent_warn_img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", list_home_data.get(position).getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (list_home_data.get(position).getPics().size()==1){
            vh.parent_warn_img_gridview.setVisibility(View.GONE);
            vh.iv_homework.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + list_home_data.get(position).getPics().get(0), vh.iv_homework, displayImage);
            vh.iv_homework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", list_home_data.get(position).getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            vh.parent_warn_img_gridview.setVisibility(View.GONE);
            vh.iv_homework.setVisibility(View.GONE);

        }

        //详情页面
        vh.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeworkDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("homework", list_home_data.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        vh.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeworkDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("homework", list_home_data.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        vh.rl_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeworkDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("homework", list_home_data.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        vh.rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeworkDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("homework", list_home_data.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        vh.rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeworkDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("homework", list_home_data.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        if(list_home_data.get(position).getRecivers().size()!=0){
            int total,already,noalready=0;
            total=list_home_data.get(position).getRecivers().size();
            for(int i=0;i<list_home_data.get(position).getRecivers().size();i++){
                if(list_home_data.get(position).getRecivers().get(i).getReadTime().equals("null")||list_home_data.get(position).getRecivers().get(i).getReadTime().length()==0){
                    noalready++;
                }
            }
            already=total-noalready;
            vh.tv_total_send.setText("总发"+total);
            vh.tv_read.setText("已阅读"+already);
            vh.tv_noread.setText("未读"+noalready);
        }
        if(list_home_data.get(position).getReadTime().equals("null")){
            vh.tv_state.setText("未读");
            vh.tv_state.setTextColor(Color.parseColor("#FFA500"));
        }else{
            vh.tv_state.setText("已读");
            vh.tv_state.setTextColor(Color.parseColor("#9BE5B4"));
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_title, tv_content, tv_send, tv_time1,tv_total_send,tv_read,tv_noread,tv_state;
        private ImageView iv_homework;
        private GridView parent_warn_img_gridview;
        RelativeLayout rl_title,rl_send,rl_read;
        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.textView1);
            tv_content = (TextView) convertView.findViewById(R.id.textView2);
            tv_send = (TextView) convertView.findViewById(R.id.textView3);
            tv_time1 = (TextView) convertView.findViewById(R.id.textView4);
            tv_total_send = (TextView) convertView.findViewById(R.id.total_send);
            tv_read = (TextView) convertView.findViewById(R.id.alread_text);
            tv_noread = (TextView) convertView.findViewById(R.id.noalread_text);
            iv_homework = (ImageView) convertView.findViewById(R.id.parent_warn_img);
            parent_warn_img_gridview = (GridView) convertView.findViewById(R.id.parent_warn_img_gridview);
            tv_state = (TextView) convertView.findViewById(R.id.state);
            rl_read = (RelativeLayout) convertView.findViewById(R.id.rl_read);
            rl_send = (RelativeLayout) convertView.findViewById(R.id.rl_send);
            rl_title = (RelativeLayout) convertView.findViewById(R.id.rl_title);
        }
    }
}
