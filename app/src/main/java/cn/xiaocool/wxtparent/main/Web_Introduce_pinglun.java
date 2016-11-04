package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Annou_pl_adapter;
import cn.xiaocool.wxtparent.adapter.WebIntroduce_pl_adapter;
import cn.xiaocool.wxtparent.bean.Announcement;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class Web_Introduce_pinglun extends BaseActivity implements View.OnClickListener {

    private ImageView exit;
    private ImageView add;
    ArrayList<Comments> commentsArrayList;
    private PullToRefreshListView listView_pull;

    private WebIntroduce_pl_adapter webIntroduce_pl_adapter;
    private ListView listView;
    private String refid;
    private String id;
    private String type;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
                        webIntroduce_pl_adapter = new WebIntroduce_pl_adapter(getApplicationContext(), commentsArrayList);
                        listView.setAdapter(webIntroduce_pl_adapter);
                    }
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__introduce_pinglun);


        listView_pull = (PullToRefreshListView) findViewById(R.id.list_web_introduce_pinglun);
        listView = listView_pull.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(10);


        commentsArrayList = new ArrayList<>();
        refid = getIntent().getStringExtra("refid");
        type = getIntent().getStringExtra("type");

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

}
