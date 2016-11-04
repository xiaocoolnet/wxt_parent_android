package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import cn.xiaocool.wxtparent.bean.Classevents;
import cn.xiaocool.wxtparent.net.request.constant.WebHome;

/**
 * Created by Administrator on 2016/3/20.
 */
public class ClassEventsAdapter extends BaseAdapter {
    private List<Classevents.ClassEventData> list;
    private LayoutInflater inflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    public ClassEventsAdapter(List<Classevents.ClassEventData> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_classeventlv,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //添加数据
        //接口需要变动
        imageLoader.displayImage(WebHome.NET_CLASSEVENT_HOST+list.get(position).getActivity_pic(),holder.classevent_lv_imageview,displayImage);
        holder.classevent_lv_title.setText(list.get(position).getActivity_title());
        holder.classevent_lv_content.setText(list.get(position).getActivity_content());
        return convertView;
    }

    public class ViewHolder{
        ImageView classevent_lv_imageview,classevent_lv_access;
        TextView classevent_lv_title,classevent_lv_content;
        public ViewHolder(View convertView){
            classevent_lv_imageview = (ImageView) convertView.findViewById(R.id.classevent_lv_imageview);
            classevent_lv_title = (TextView) convertView.findViewById(R.id.classevent_lv_title);
            classevent_lv_content = (TextView) convertView.findViewById(R.id.classevent_lv_content);
            classevent_lv_access = (ImageView) convertView.findViewById(R.id.classevent_lv_access);
        }
    }
}
