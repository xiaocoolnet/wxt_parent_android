package cn.xiaocool.wxtparent.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class Baby_Album_Pinglun_Adapter extends BaseAdapter {


    private Context context;
    private List<Objects> list;


    public Baby_Album_Pinglun_Adapter(Context context, List<Objects> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 10;
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
            convertView = View.inflate(context, R.layout.item_baby_album_pinglun, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }



//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(convertView,
//                "translationX", -200, 0);
//        objectAnimator.setDuration(800);
//        objectAnimator.start();
        return convertView;

    }

    class ViewHolder {
        private ImageView iv1;
        private TextView tv1, tv2, tv3;
        private View convertView;

        public ViewHolder(View convertView) {
            tv1 = (TextView) convertView.findViewById(R.id.baby_album_PL_item_tv1);
            tv2 = (TextView) convertView.findViewById(R.id.baby_album_PL_item_tv2);
            tv3 = (TextView) convertView.findViewById(R.id.baby_album_PL_item_tv3);

            iv1 = (ImageView) convertView.findViewById(R.id.baby_album_PL_item_iv1);


        }
    }

}
