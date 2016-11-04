package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.UserInfo;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class Annou_pl_adapter extends BaseAdapter {

    private Context context;

    private ArrayList<Comments> commentsList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private UserInfo user;
    private DisplayImageOptions displayImage;

    public Annou_pl_adapter(Context context, ArrayList<Comments> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();
        user = new UserInfo(context);
        user.readData(context);

    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_annou_pinglun, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);


        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        Date date = new Date();
        date.setTime(Long.parseLong(commentsList.get(position).getComment_time()) * 1000);
        vh.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        vh.tv_username.setText(commentsList.get(position).getName());
        vh.tv_content.setText(commentsList.get(position).getContent());
        if (commentsList.get(position).getPhoto().length() > 1) {
//            Picasso.with(context).load("http://wxt.xiaocool.net/uploads/microblog/" + commentsList.get(position).getPhoto()).into(vh.iv_content);

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + commentsList.get(position).getPhoto(), vh.iv_content, displayImage);
            Log.d("img", "http://wxt.xiaocool.net/uploads/microblog/" + commentsList.get(position).getPhoto());
            vh.iv_content.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return convertView;
    }

    class ViewHolder {
        private TextView tv_username, tv_content, tv_time;
        private ImageView iv_user, iv_content;

        public ViewHolder(View convertView) {
            tv_username = (TextView) convertView.findViewById(R.id.tv_user_name);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            iv_user = (ImageView) convertView.findViewById(R.id.image_uesr);
            iv_content = (ImageView) convertView.findViewById(R.id.image_content);
        }
    }

}
