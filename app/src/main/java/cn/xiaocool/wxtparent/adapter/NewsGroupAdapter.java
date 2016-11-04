package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import cn.xiaocool.wxtparent.bean.NewsGroup;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;
import cn.xiaocool.wxtparent.main.NewGroupDetailActivity;

/**
 * Created by Administrator on 2016/4/22.
 */
public class NewsGroupAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsGroup> newsGroups;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private LayoutInflater inflater;
    private DisplayImageOptions displayImage;
    private UserInfo user;
    public NewsGroupAdapter(ArrayList<NewsGroup> newsGroups, Context mContext) {
        this.context = mContext;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();
        this.newsGroups = newsGroups;
        this.inflater = LayoutInflater.from(mContext);
        user = new UserInfo(context);
        user.readData(context);
    }
    @Override
    public int getCount() {
        return newsGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return newsGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_news_group,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(newsGroups.get(position).getMessage_content());
        holder.tv_send_name.setText(newsGroups.get(position).getSend_user_name());
        Date date = new Date();
        date.setTime(Long.parseLong(newsGroups.get(position).getMessage_time()) * 1000);
        holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        //判断是否有照片
        if (newsGroups.get(position).getPics().size()>1){
            holder.iv_homework.setVisibility(View.GONE);
            holder.parent_warn_img_gridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(newsGroups.get(position).getPics(),context);
            holder.parent_warn_img_gridview.setAdapter(parWarnImgGridAdapter);
            holder.parent_warn_img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", newsGroups.get(position).getPics());
                    intent.putExtra("type", "newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (newsGroups.get(position).getPics().size()==1){
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_homework.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + newsGroups.get(position).getPics().get(0), holder.iv_homework, displayImage);
            holder.iv_homework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", newsGroups.get(position).getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_homework.setVisibility(View.GONE);
        }

        if(newsGroups.get(position).getRecivers().size()!=0){
            int total,already,noalready=0;
            total=newsGroups.get(position).getRecivers().size();
            for(int i=0;i<newsGroups.get(position).getRecivers().size();i++){
                if(newsGroups.get(position).getRecivers().get(i).getReadTime().equals("null")||newsGroups.get(position).getRecivers().get(i).getReadTime().length()==0){
                    noalready++;
                }
            }
            already=total-noalready;
            holder.tv_total_send.setText("总发"+total);
            holder.tv_read.setText("已阅读"+already);
            holder.tv_noread.setText("未读"+noalready);
        }

        //详情页面
        holder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewGroupDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newGroup", newsGroups.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewGroupDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newGroup", newsGroups.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.rl_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewGroupDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newGroup", newsGroups.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        if(newsGroups.get(position).getRead_time().equals("null")){
            holder.tv_state.setText("未读");
            holder.tv_state.setTextColor(Color.parseColor("#FFA500"));
        }else{
            holder.tv_state.setText("已读");
            holder.tv_state.setTextColor(Color.parseColor("#9BE5B4"));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_content,tv_total_send,tv_read,tv_noread,tv_time,tv_send_name,tv_state;
        private ImageView iv_homework;
        private GridView parent_warn_img_gridview;
        RelativeLayout rl_read,rl_send;
        public ViewHolder(View view){
            tv_content = (TextView) view.findViewById(R.id.myhomework_content);
            tv_total_send = (TextView) view.findViewById(R.id.total_send);
            tv_read = (TextView) view.findViewById(R.id.alread_text);
            tv_noread = (TextView) view.findViewById(R.id.noalread_text);
            tv_time = (TextView) view.findViewById(R.id.myhomework_time);
            tv_send_name = (TextView) view.findViewById(R.id.myhomework_teacher_name);
            iv_homework = (ImageView) view.findViewById(R.id.parent_warn_img);
            parent_warn_img_gridview = (GridView) view.findViewById(R.id.parent_warn_img_gridview);
            tv_state = (TextView) view.findViewById(R.id.state);
            rl_read = (RelativeLayout) view.findViewById(R.id.rl_read);
            rl_send = (RelativeLayout) view.findViewById(R.id.rl_send);
        }


    }

}
