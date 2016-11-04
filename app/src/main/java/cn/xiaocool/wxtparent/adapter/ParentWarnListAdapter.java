package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import cn.xiaocool.wxtparent.bean.ParentWarnInfo;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;


/**
 * Created by Administrator on 2016/5/7.
 */
public class ParentWarnListAdapter  extends BaseAdapter{
    private ArrayList<ParentWarnInfo> parentWarnDataList;
    private LayoutInflater inflater;
    private Context context;
    private DisplayImageOptions displayImage;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private UserInfo userInfo;
    public ParentWarnListAdapter(ArrayList<ParentWarnInfo> parentWarnDataList, Context context) {
        this.context = context;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();
        this.parentWarnDataList = parentWarnDataList;
        this.inflater = LayoutInflater.from(context);
        userInfo = new UserInfo(context);
        userInfo.readData(context);

    }


    @Override
    public int getCount() {
        return parentWarnDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return parentWarnDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_parent_warn,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(parentWarnDataList.get(position).getStudentname());
        holder.tv_content.setText(parentWarnDataList.get(position).getContent());
        holder.tv_teacher_name.setText(parentWarnDataList.get(position).getTeacherName());
        Date date = new Date();
        date.setTime(Long.parseLong(parentWarnDataList.get(position).getTime()) * 1000);
        holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        if(parentWarnDataList.get(position).getCommentContent()==null){
            holder.rl_comment.setVisibility(View.GONE);
        }else{
            holder.rl_comment.setVisibility(View.VISIBLE);
            holder.tv_comment_content.setText(parentWarnDataList.get(position).getCommentContent());
            holder.tv_comment_name.setText(parentWarnDataList.get(position).getCommentName());
            Date date1 = new Date();
            date.setTime(Long.parseLong(parentWarnDataList.get(position).getCommentTime()) * 1000);
            holder.tv_comment_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date1));
            imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+parentWarnDataList.get(position).getCommentAvator(),holder.iv_comment_avator);
        }
        //判断图片并显示（一张图片显示imageview，多于一张显示gridview）
        if (parentWarnDataList.get(position).getPics().size()>1){
            holder.iv_content.setVisibility(View.GONE);
            holder.parent_warn_img_gridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter( parentWarnDataList.get(position).getPics(),context);
            holder.parent_warn_img_gridview.setAdapter(parWarnImgGridAdapter);
            holder.parent_warn_img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", parentWarnDataList.get(position).getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (parentWarnDataList.get(position).getPics().size()==1){
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + parentWarnDataList.get(position).getPics().get(0), holder.iv_content, displayImage);
            holder.iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", parentWarnDataList.get(position).getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.GONE);

        }
        if(parentWarnDataList.get(position).getCommentContent()==null){
            holder.tv_state.setText("未回复");
            holder.tv_state.setTextColor(Color.parseColor("#FFA500"));
        }else{
            holder.tv_state.setText("已回复");
            holder.tv_state.setTextColor(Color.parseColor("#9BE5B4"));
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_name,tv_content,tv_teacher_name,tv_time,tv_comment_name,tv_comment_content,tv_comment_time,tv_state;
        ImageView iv_content,iv_comment_avator;
        GridView parent_warn_img_gridview;
        RelativeLayout rl_comment;
        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.parent_warn_title);
            tv_content = (TextView) convertView.findViewById(R.id.parent_warn_content);
            tv_teacher_name = (TextView) convertView.findViewById(R.id.parent_warn_from);
            tv_time = (TextView) convertView.findViewById(R.id.parent_warn_time);
            tv_comment_content = (TextView) convertView.findViewById(R.id.item_content);
            tv_comment_name = (TextView) convertView.findViewById(R.id.item_title);
            tv_comment_time = (TextView) convertView.findViewById(R.id.item_time);
            iv_content = (ImageView) convertView.findViewById(R.id.parent_warn_img);
            iv_comment_avator = (ImageView) convertView.findViewById(R.id.item_head);
            parent_warn_img_gridview = (GridView) convertView.findViewById(R.id.parent_warn_img_gridview);
            rl_comment = (RelativeLayout) convertView.findViewById(R.id.news_group_comment_layout);
            tv_state = (TextView) convertView.findViewById(R.id.tv_state);
        }
    }
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
    }

    public String getTime(String time){

        long todayZero = getTodayZero();
        String ret_time;
        if (todayZero>Long.parseLong(time)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setTime(Long.parseLong(time)*1000);
            ret_time = (dateFormat.format(date));

        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(Long.parseLong(time)*1000);
            ret_time = dateFormat.format(date);

        }

        return ret_time;

    }
}
