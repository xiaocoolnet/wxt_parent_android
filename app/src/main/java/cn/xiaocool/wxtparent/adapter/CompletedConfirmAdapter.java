package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ConfirmChildren;

/**
 * Created by Administrator on 2016/4/19.
 */
public class CompletedConfirmAdapter extends BaseAdapter {
    private List<ConfirmChildren.ConfirmChildrenData> confirmChildrenDatas;
    private LayoutInflater inflater;
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage,displayImage2;
    private Handler handler;

    public CompletedConfirmAdapter(List<ConfirmChildren.ConfirmChildrenData> confirmChildrenDatas, Context mContext, Handler handler) {
        this.confirmChildrenDatas = confirmChildrenDatas;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.handler = handler;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_avatar).showImageOnFail(R.drawable.default_avatar)
                .cacheInMemory(true).cacheOnDisc(true).build();
        displayImage2 = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public int getCount() {
        return confirmChildrenDatas.size();

    }

    @Override
    public Object getItem(int position) {
        return confirmChildrenDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_completed_confirm, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + confirmChildrenDatas.get(position).getTeacheravatar(), vh.iv_head, displayImage);
        imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + confirmChildrenDatas.get(position).getPhoto(), vh.iv_daijie, displayImage2);
        vh.tv_name.setText(confirmChildrenDatas.get(position).getTeachername());
        Date date = new Date();
        date.setTime(Long.parseLong(confirmChildrenDatas.get(position).getDelivery_time()) * 1000);
        vh.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        vh.tv_content.setText(confirmChildrenDatas.get(position).getContent());
        vh.tv_item_content.setText(confirmChildrenDatas.get(position).getTeacherDuty());
        if(confirmChildrenDatas.get(position).getDelivery_status().equals("1")){
            vh.tv_state.setText("已同意");
            vh.tv_state.setTextColor(Color.parseColor("#ABE9C0"));
        }else if(confirmChildrenDatas.get(position).getDelivery_status().equals("2")){
            vh.tv_state.setText("未同意");
            vh.tv_state.setTextColor(Color.parseColor("#FF962B"));
        }
        if (confirmChildrenDatas.get(position).getPhoto().equals("")){
            vh.iv_daijie.setVisibility(View.GONE);
        }else {
            vh.iv_daijie.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView iv_head, iv_daijie;
        TextView tv_name, tv_item_content, tv_time, tv_content,tv_state;

        public ViewHolder(View convertView) {
            iv_head = (ImageView) convertView.findViewById(R.id.item_head);
            iv_daijie = (ImageView) convertView.findViewById(R.id.daijie_img);
            tv_name = (TextView) convertView.findViewById(R.id.item_title);
            tv_item_content = (TextView) convertView.findViewById(R.id.item_content);
            tv_time = (TextView) convertView.findViewById(R.id.item_time);
            tv_content = (TextView) convertView.findViewById(R.id.daijie_content);
            tv_state = (TextView) convertView.findViewById(R.id.confirm_state);
        }
    }
}
