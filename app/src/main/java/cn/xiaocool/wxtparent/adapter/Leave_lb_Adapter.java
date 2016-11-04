package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import cn.xiaocool.wxtparent.bean.LeaveInfo;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class Leave_lb_Adapter extends BaseAdapter {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<LeaveInfo> list;
    private Context context;
    private UserInfo user;
    private DisplayImageOptions displayImage;

    public Leave_lb_Adapter(Context context, ArrayList<LeaveInfo> list) {
        this.list = list;
        this.context = context;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.main_daijiequeren).showImageOnFail(R.drawable.main_daijiequeren)
                .cacheInMemory(true).cacheOnDisc(true).build();
        user = new UserInfo(context);
        user.readData(context);


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

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_leave, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(list.get(position).getContent());
        holder.tv_type.setText(list.get(position).getLeaveType());
        holder.tv_teacher_name.setText(list.get(position).getTeacherName());
        Date date1 = new Date();
        date1.setTime(Long.parseLong(list.get(position).getBegintime()) * 1000);
        Date date2 = new Date();
        date2.setTime(Long.parseLong(list.get(position).getEndtime()) * 1000);
        holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(date1)+"至"+new SimpleDateFormat("yyyy-MM-dd").format(date2));
        if(list.get(position).getFeedback()==null||list.get(position).getFeedback().equals("null")||list.get(position).getFeedback().length()==0){
            holder.rl_comment.setVisibility(View.GONE);
        }else{
            holder.rl_comment.setVisibility(View.VISIBLE);
            holder.tv_comment_content.setText(list.get(position).getFeedback());
            holder.tv_comment_name.setText(list.get(position).getTeacherName());
            Date date3 = new Date();
            date3.setTime(Long.parseLong(list.get(position).getDealtime()) * 1000);
            holder.tv_comment_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date3));
            imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+list.get(position).getTeacherAvator(),holder.iv_comment_avator);
        }
        //判断图片并显示（一张图片显示imageview，多于一张显示gridview）
        if (list.get(position).getPics().size()>1){
            holder.iv_content.setVisibility(View.GONE);
            holder.parent_warn_img_gridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter( list.get(position).getPics(),context);
            holder.parent_warn_img_gridview.setAdapter(parWarnImgGridAdapter);
            holder.parent_warn_img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", list.get(position).getPics());
                    intent.putExtra("type", "newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (list.get(position).getPics().size()==1){
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + list.get(position).getPics().get(0), holder.iv_content, displayImage);
            holder.iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", list.get(position).getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.parent_warn_img_gridview.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.GONE);

        }
        holder.tv_baby_name.setText(list.get(position).getStudentName());
        imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + list.get(position).getStudentAvator(), holder.iv_baby_avator);
        holder.tv_calss_name.setText(list.get(position).getClassName());
        if(list.get(position).getState().equals("0")){
            holder.tv_state.setText("未反馈");
            holder.tv_state.setTextColor(Color.parseColor("#FD7D36"));
        }else if(list.get(position).getState().equals("1")) {
            holder.tv_state.setText("已同意");
            holder.tv_state.setTextColor(Color.parseColor("#ABE9C0"));
        }else if(list.get(position).getState().equals("2")) {
            holder.tv_state.setText("未同意");
            holder.tv_state.setTextColor(Color.parseColor("#FD7D36"));
        }
        return convertView;
    }


    class ViewHolder {
        TextView tv_content,tv_teacher_name,tv_time,tv_comment_name,tv_comment_content,tv_comment_time,tv_state,tv_type;
        ImageView iv_content,iv_comment_avator,iv_baby_avator;
        GridView parent_warn_img_gridview;
        RelativeLayout rl_comment;
        TextView tv_baby_name,tv_calss_name;
        public ViewHolder(View convertView) {
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
            tv_baby_name = (TextView) convertView.findViewById(R.id.item_baby_title);
            iv_baby_avator = (ImageView) convertView.findViewById(R.id.item_baby_head);
            tv_calss_name = (TextView) convertView.findViewById(R.id.item_baby_content);
            tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            tv_type = (TextView) convertView.findViewById(R.id.tv_type);
        }

    }


}
