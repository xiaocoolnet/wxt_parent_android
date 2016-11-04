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
import cn.xiaocool.wxtparent.bean.UserInfo;


/**
 * Created by Administrator on 2016/3/20.
 */
public class PradiseEventsAdapter extends BaseAdapter {

    private List<Classevents.ClassEventData> homeworkDataList;
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;

    private UserInfo user;

    public PradiseEventsAdapter(Context mContext) {
        this.context = mContext;

        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();
        this.inflater = LayoutInflater.from(mContext);
        user = new UserInfo(context);
        user.readData(context);
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return homeworkDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null){
            convertView = inflater.inflate(R.layout.pradise_event_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    class ViewHolder{
        TextView item_time,item_number;
        ImageView item_img;
        public ViewHolder(View convertView) {


            item_number = (TextView) convertView.findViewById(R.id.item_number);
            item_time = (TextView) convertView.findViewById(R.id.item_time);
            item_img = (ImageView) convertView.findViewById(R.id.item_img);

        }
    }



}
