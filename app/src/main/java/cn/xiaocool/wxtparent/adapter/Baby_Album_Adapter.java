package cn.xiaocool.wxtparent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.Homework;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.Homework_Pinglun;
import cn.xiaocool.wxtparent.main.ImgDetailActivity;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.LogUtils;

/**
 * Created by Administrator on 2016/5/28 0028.
 */
public class Baby_Album_Adapter extends BaseAdapter {


    private static final int HOMEWORK_PRAISE_KEY = 104;
    private static final int DEL_HOMEWORK_PRAISE_KEY = 105;
    private UserInfo user;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    private Handler handler;
    private List<Homework.HomeworkData> list;
    private Context context;
    private int position;
    private ArrayList<String> pics;

    public Baby_Album_Adapter(List<Homework.HomeworkData> list, Context context, Handler handler) {
        this.list = list;
        this.context = context;
        this.handler = handler;
        user = new UserInfo(context);
        user.readData(context);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_fragment_baby_album_01, null);
            vh = new ViewHolder(convertView);


            convertView.setTag(vh);


        } else {
            vh = (ViewHolder) convertView.getTag();

        }


        vh.tv_from.setText(list.get(position).getUsername());
        vh.tv_content.setText(list.get(position).getContent());


        Date date = new Date();
        date.setTime(Long.parseLong(list.get(position).getCreate_time()) * 1000);
        vh.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));


        //判断图片并显示（一张图片显示imageview，多于一张显示gridview）
        if (list.get(position).getPhoto() != null) {
            pics = new ArrayList<String>();
            pics.add("uploads/microblog/notice5971307016.png");
            pics.add("uploads/microblog/notice5971417777.png");
            pics.add("uploads/microblog/notice5971480427.png");
            pics.add("uploads/microblog/notice5971055923.png");
            vh.img_gridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(pics, context);
            vh.img_gridview.setAdapter(parWarnImgGridAdapter);
            vh.img_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    /*ArrayList<String> pics = new ArrayList<String>();
                    pics.add(homeworkDataList.get(position).getPhoto());*/
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", (ArrayList<String>) pics);
                    intent.putExtra("type", "4");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });
        } else {
            vh.img_gridview.setVisibility(View.GONE);

        }


        vh.layout_zan.setVisibility(View.GONE);
        //判断是否有人点赞
        if (isPraise(list.get(position).getWorkPraise())) {
            //点赞成功后图片变红
            vh.iv_zan.setSelected(true);
            notifyDataSetChanged();
        } else {
            //取消点赞后
            vh.iv_zan.setSelected(false);
            notifyDataSetChanged();
        }

//

        if (list.get(position).getWorkPraise().size() > 0) {
            vh.layout_zan.setVisibility(View.VISIBLE);
            String names = "";
            for (int i = 0; i < list.get(position).getWorkPraise().size(); i++) {
                names = names + " " + list.get(position).getWorkPraise().get(i).getName();
            }
            notifyDataSetChanged();
            vh.tv_zan.setText(names);
            notifyDataSetChanged();


        }

        //点赞事件
        vh.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPraise(list.get(position).getWorkPraise())) {
                    LogUtils.d("FindFragment", "delPraise___" + list.get(position).getId());
                    delPraise(list.get(position).getId());
                    notifyDataSetChanged();
                } else {
                    LogUtils.d("FindFragment", "workPraise___" + list.get(position).getId());
                    workPraise(list.get(position).getId());
                    notifyDataSetChanged();
                }
            }
        });


        final String refid = list.get(position).getId();
        vh.iv_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.baby_album_iv6) {
                    Intent intent = new Intent(context, Homework_Pinglun.class);
                    //从一个Activity中要通过intent调出另一个Activity的话，需要使用 FLAG_ACTIVITY_NEW_TASK
                    // 否则的话，会有force close：
                    //如果调出的Activtivity只是一个功能片段，并没有实际的意义，也没有必要出现在长按Home键调出最近使用过的程序类表中，那么使用FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    //Intent传递List<Map<String,String>>的使用
//                        intent.putExtra("list", (Serializable)list_home_data.get(position));

                    intent.putExtra("type", 2 + "");//作业的type类型是2，通知公告的是3
                    intent.putExtra("refid", refid);
                    Log.e("ayay", refid + "hw");

                    context.startActivity(intent);

                }
            }
        });

        notifyDataSetChanged();


        return convertView;
    }


    private void workPraise(String workBindId) {
        Log.i("begintopppp-=====", "222222");
        new SpaceRequest(context, handler).myHomework_set_like(workBindId, HOMEWORK_PRAISE_KEY, "2");
        notifyDataSetChanged();
    }

    // 取消点赞
    private void delPraise(String workBindId) {
        new SpaceRequest(context, handler).myHomework_del_like(workBindId, DEL_HOMEWORK_PRAISE_KEY, "2");
        notifyDataSetChanged();
    }

    /**
     * 判断当前用户是否点赞
     */
    private boolean isPraise(ArrayList<LikeBean> praises) {
        for (int i = 0; i < praises.size(); i++) {
            if (praises.get(i).getUserid().equals(user.getUserId())) {
                Log.d("praisesid", praises.get(i).getUserid());
                notifyDataSetChanged();
                return true;
            }
        }
        notifyDataSetChanged();
        return false;
    }


    class ViewHolder {

        private TextView tv_content, tv_from, tv_time, tv_zan;
        private ImageView iv_image, iv_zan, iv_pinglun, iv_fenxiang;
        private GridView img_gridview;
        private LinearLayout layout_zan;

        public ViewHolder(View convertView) {

            tv_content = (TextView) convertView.findViewById(R.id.baby_album_tv1);
            tv_from = (TextView) convertView.findViewById(R.id.baby_album_tv2);
            tv_time = (TextView) convertView.findViewById(R.id.baby_album_tv3);
            tv_zan = (TextView) convertView.findViewById(R.id.baby_album_tv5);

            iv_image = (ImageView) convertView.findViewById(R.id.baby_album_iv1);
            iv_zan = (ImageView) convertView.findViewById(R.id.baby_album_iv4);
            iv_pinglun = (ImageView) convertView.findViewById(R.id.baby_album_iv6);
            iv_fenxiang = (ImageView) convertView.findViewById(R.id.baby_album_iv5);

            layout_zan = (LinearLayout) convertView.findViewById(R.id.layout_zan);
            img_gridview = (GridView) convertView.findViewById(R.id.img_gridview);


        }
    }


}
