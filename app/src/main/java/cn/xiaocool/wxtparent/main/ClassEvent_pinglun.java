package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Annou_pl_adapter;
import cn.xiaocool.wxtparent.adapter.ClassEvent_2_pl_adapter;
import cn.xiaocool.wxtparent.bean.Announcement;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class ClassEvent_pinglun extends BaseActivity implements View.OnClickListener {
    private ImageView exit, add, image_homework, image_dianzan;
    private Button btn_baoming;
    ArrayList<Comments> commentsArrayList;
    private PullToRefreshListView listView_pull;
    private TextView textView1, textView2, textView3, textView4, textView7;
    private LinearLayout linear_zan;
    private ClassEvent_2_pl_adapter classEvent_2_pl_adapter;
    private ListView listView;
    private String refid, type, id, title, content, photo, create_time, username, readcount, userid;

    private static final int CLASSEVENT_PRAISE_KEY = 104;
    private static final int DEL_CLASSEVENT_PRAISE_KEY = 105;
    private UserInfo user;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions displayImage;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case CLASSEVENT_PRAISE_KEY:
//                    if (msg.obj != null) {
//                        JSONObject jo = (JSONObject) msg.obj;
//                        try {
//                            String state = jo.getString("status");
//                            String result = jo.getString("data");
//                            if (state.equals(CommunalInterfaces._STATE)) {
//                                ToastUtils.ToastShort(getApplicationContext(), "点赞成功");
//                                getAllInformation();
//                            } else {
//                                ToastUtils.ToastShort(getApplicationContext(), result);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    break;
//
//
//                case DEL_CLASSEVENT_PRAISE_KEY:
//                    if (msg.obj != null) {
//                        try {
//                            JSONObject json = (JSONObject) msg.obj;
//                            String state = json.getString("status");
//                            String result = json.getString("data");
//                            if (state.equals(CommunalInterfaces._STATE)) {
//                                ToastUtils.ToastShort(getApplicationContext(), "已取消");
//                                getAllInformation();
//                            } else {
//                                ToastUtils.ToastShort(getApplicationContext(), result);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;


                case CommunalInterfaces.SPACE_SEND_PINGLUN:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");
                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray commentArray = obj.optJSONArray("data");
                        commentsArrayList.clear();
                        Announcement.AnnouncementData announcementDate = new Announcement.AnnouncementData();
                        if (commentArray != null) {
                            for (int i = 0; i < commentArray.length(); i++) {
                                JSONObject commentObject = commentArray.optJSONObject(i);
                                Comments commentBean = new Comments();
                                commentBean.setUserid(commentObject.optString("userid"));
                                commentBean.setAvatar(commentObject.optString("avatar"));
                                commentBean.setName(commentObject.optString("name"));
                                commentBean.setContent(commentObject.optString("content"));
                                commentBean.setComment_time(commentObject.optString("comment_time"));
                                commentBean.setPhoto(commentObject.optString("photo"));
                                commentsArrayList.add(commentBean);
                            }
                            announcementDate.setComment(commentsArrayList);
                        }
                        classEvent_2_pl_adapter = new ClassEvent_2_pl_adapter(getApplicationContext(), commentsArrayList);
                        listView.setAdapter(classEvent_2_pl_adapter);
//                        setListViewHeightBasedOnChildren(listView);

                    }
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_event_pinglun);
        listView_pull = (PullToRefreshListView) findViewById(R.id.list_class_pinglun);
        listView = listView_pull.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(10);

        image_homework = (ImageView) findViewById(R.id.image_homework);
        image_dianzan = (ImageView) findViewById(R.id.image_dianzan);
        btn_baoming = (Button) findViewById(R.id.btn_baoming);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView7 = (TextView) findViewById(R.id.textView7);

        user = new UserInfo(getApplicationContext());
        user.readData(getApplicationContext());
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.katong).showImageOnFail(R.drawable.katong)
                .cacheInMemory(true).cacheOnDisc(true).build();


        commentsArrayList = new ArrayList<>();
        refid = getIntent().getStringExtra("refid");
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        photo = getIntent().getStringExtra("photo");
        create_time = getIntent().getStringExtra("create_time");
        username = getIntent().getStringExtra("username");
        readcount = getIntent().getStringExtra("readcount");
        userid = getIntent().getStringExtra("userid");
        id = getIntent().getStringExtra("id");


        textView1.setText(title);
        textView1.setText(content);
        textView3.setText(username);

        Date date = new Date();
        date.setTime(Long.parseLong(create_time) * 1000);
        textView4.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));

        textView7.setText(readcount);


        //判断是否有照片
        if (photo != null) {
            image_homework.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + photo, image_homework, displayImage);
            Log.d("img", "http://wxt.xiaocool.net/uploads/microblog/" + photo);
            image_homework.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        image_homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassEvent_pinglun.this, ShowImageActivity.class);
                intent.putExtra("Image", photo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });


        Log.e("ayay", refid + "hw_pl");
        Log.e("ayay", type + "hw_pl");
        new SpaceRequest(getApplicationContext(), handler).get_comments(id, refid, type + "");


        add = (ImageView) findViewById(R.id.btn_jia);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Annou_pinglun_add.class);
                intent.putExtra("type", type);
                intent.putExtra("refid", refid);
                startActivity(intent);
            }
        });
        exit = (ImageView) findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_baoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClassEvent_pinglun.this,ClassEvent_Baoming_Activity.class);

                startActivity(intent);


            }
        });
//        image_dianzan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        listView_pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (NetUtil.isConnnected(getApplicationContext()) == true) {
                    getAllInformation();

                } else {
                    ToastUtils.ToastShort(getApplicationContext(), "暂无网络");
                }
                /**
                 * 过1秒结束下拉刷新
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView_pull.onPullDownRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                /**
                 * 过1秒后 结束向上加载
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView_pull.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });


    }


    @Override
    public void onClick(View v) {

    }


    private void getAllInformation() {
        new SpaceRequest(getApplicationContext(), handler).get_comments(id, refid, type + "");

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
