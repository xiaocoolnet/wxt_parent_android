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
import cn.xiaocool.wxtparent.bean.HappySchool;
import cn.xiaocool.wxtparent.net.request.constant.WebHome;

/**
 * Created by Administrator on 2016/3/22.
 */
public class HappySchoolAdapter extends BaseAdapter {
    private List<HappySchool.HappySchoolData> happySchoolDataList;
    private LayoutInflater inflater;
    private DisplayImageOptions displayImage;
    public HappySchoolAdapter(Context mContext, List<HappySchool.HappySchoolData> happySchoolDataList) {
        this.happySchoolDataList = happySchoolDataList;
        this.inflater = LayoutInflater.from(mContext);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public int getCount() {
        return happySchoolDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return happySchoolDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.web_babyknow,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(WebHome.NET_HAPPYSCHOOL_HOST+happySchoolDataList.get(position).getHappy_pic(),holder.web_babyknow_happy_pic, displayImage);
        holder.web_babyknow_happy_title.setText(happySchoolDataList.get(position).getHappy_title());
        holder.web_babyknow_happy_content.setText(happySchoolDataList.get(position).getHappy_content());
        return convertView;
    }
    class ViewHolder{
        ImageView web_babyknow_happy_pic;
        TextView web_babyknow_happy_title,web_babyknow_happy_content;
        public ViewHolder(View convertView) {
            web_babyknow_happy_pic = (ImageView) convertView.findViewById(R.id.web_babyknow_happy_pic);
            web_babyknow_happy_title = (TextView) convertView.findViewById(R.id.web_babyknow_happy_title);
            web_babyknow_happy_content = (TextView) convertView.findViewById(R.id.web_babyknow_happy_content);
        }
    }
}
