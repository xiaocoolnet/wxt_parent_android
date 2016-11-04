package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.main.ParentInfoActivity;
import cn.xiaocool.wxtparent.bean.ParentInfo;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by wzh on 2016/4/2.
 */
public class BabyParentAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ParentInfo> hashMapList;
    private DisplayImageOptions displayImageOptions;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public BabyParentAdapter(Context context, ArrayList<ParentInfo> hashMapList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.hashMapList = hashMapList;
        displayImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public int getCount() {
        return hashMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return hashMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.baby_parent_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(hashMapList.get(position).getType().equals("1")){
            holder.iv_main.setVisibility(View.VISIBLE);
        }else if(hashMapList.get(position).getType().equals("0")){
            holder.iv_main.setVisibility(View.GONE);
        }
        holder.tv_name.setText(hashMapList.get(position).getAppellation());
        holder.tv_className.setText(hashMapList.get(position).getPhone());
        imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + hashMapList.get(position).getPhoto(), holder.iv_avator);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParentInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("parent", hashMapList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tv_name, tv_className;
        ImageView iv_avator,iv_main;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.item_name);
            tv_className = (TextView) view.findViewById(R.id.item_classname);
            iv_avator = (ImageView) view.findViewById(R.id.item_avator);
            iv_main = (ImageView) view.findViewById(R.id.main_parent);
        }
    }

}
