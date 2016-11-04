package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.CourseInfo;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class CourseInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CourseInfo> courseInfos;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public CourseInfoAdapter(Context context, ArrayList<CourseInfo> courseInfos) {
        this.context = context;
        this.courseInfos = courseInfos;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return courseInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return courseInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final  ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_course_info,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final CourseInfo courseInfo =courseInfos.get(position);
        holder.tv_title.setText(courseInfo.getTitle());
        holder.tv_content.setText(courseInfo.getContent());
        holder.tv_fasong.setText(courseInfo.getTeacherName());
        Date date = new Date();
        date.setTime(Long.parseLong(courseInfo.getTime())*1000);
        holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
        //判断是否有照片
        if (courseInfo.getPics().size()>1){
            holder.iv_content.setVisibility(View.GONE);
            holder.gridView.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(courseInfo.getPics(),context);
            holder.gridView.setAdapter(parWarnImgGridAdapter);
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", courseInfo.getPics());
                    intent.putExtra("type", "newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        } else if (courseInfo.getPics().size()==1){
            holder.gridView.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + courseInfo.getPics().get(0), holder.iv_content);
            holder.iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", courseInfo.getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.gridView.setVisibility(View.GONE);
            holder.iv_content.setVisibility(View.GONE);

        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_title,tv_content,tv_time,tv_fasong;
        GridView gridView;
        ImageView iv_content;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.textView1);
            tv_content = (TextView) view.findViewById(R.id.textView2);
            tv_fasong = (TextView) view.findViewById(R.id.textView3);
            tv_time = (TextView) view.findViewById(R.id.textView4);
            gridView = (GridView) view.findViewById(R.id.parent_warn_img_gridview);
            iv_content = (ImageView) view.findViewById(R.id.parent_warn_img);
        }
    }
}
