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

import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Teacher_style;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public class Web_Adapter extends BaseAdapter {

    private Context context;
    private List<Teacher_style.DataEntity> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;

    public Web_Adapter(Context context, List<Teacher_style.DataEntity> list) {
        this.context = context;
        this.list = list;
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.main_daijiequeren).showImageOnFail(R.drawable.main_daijiequeren)
                .cacheInMemory(true).cacheOnDisc(true).build();


    }

    @Override
    public int getCount() {
        if(list.size()<=3)
        {
            return list.size();
        }
        else
        {
            return 3;
        }

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

        ViewHolder vh;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_teacher_style, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);


        } else {
            vh = (ViewHolder) convertView.getTag();

        }


        //判断是否有照片
        if (list.get(position).getThumb().length() > 1) {
            vh.image_teacher.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net" + list.get(position).getThumb(), vh.image_teacher, displayImage);
            Log.d("img", "http://wxt.xiaocool.net" + list.get(position).getThumb());
            vh.image_teacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


        vh.tv_title.setText(list.get(position).getPost_title());
        vh.tv_summary.setText(list.get(position).getPost_excerpt());
        vh.tv_time.setText(list.get(position).getPost_date());


        return convertView;
    }


    class ViewHolder {
        private TextView tv_title, tv_summary, tv_time;
        private ImageView image_teacher;

        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);


            image_teacher = (ImageView) convertView.findViewById(R.id.image_teacher);


        }
    }
}
