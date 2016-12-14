package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.NewsGroupAdapter;
import cn.xiaocool.wxtparent.bean.NewsGroup;
import cn.xiaocool.wxtparent.bean.Reciver;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;


/**
 * Created by Administrator on 2016/4/22.
 */
public class NewsGroupActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout quit;
    private PullToRefreshListView news_list_view;
    private ArrayList<NewsGroup> newsGroups;
    private ListView lv;
    private Context context;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CommunalInterfaces.NEWSGROUP:
                    ProgressViewUtil.dismiss();
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        Log.e("hello-------",hwArray.toString());
                        newsGroups.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < hwArray.length(); i++){
                            itemObject = hwArray.optJSONObject(i);
                            NewsGroup newsGroup = new NewsGroup();
                            newsGroup.setRead_time(itemObject.optString("read_time"));
                            newsGroup.setMessage_id(itemObject.optString("message_id"));
                            JSONObject jsonObject = itemObject.optJSONArray("send_message").optJSONObject(0);
                            newsGroup.setMessage_content(jsonObject.optString("message_content"));
                            newsGroup.setMessage_time(jsonObject.optString("message_time"));
                            newsGroup.setSend_user_name(jsonObject.optString("send_user_name"));
                            newsGroup.setSend_user_id(jsonObject.optString("send_user_id"));
                            newsGroup.setTeacherAvator(jsonObject.optString("photo"));
                            JSONArray picArray = itemObject.optJSONArray("pic");
                            ArrayList<String> pics = new ArrayList<>();
                            for(int j=0;j<picArray.length();j++){
                                if(!picArray.optJSONObject(j).optString("picture_url").equals("null")&&!picArray.optJSONObject(j).optString("picture_url").equals("")){
                                    pics.add(picArray.optJSONObject(j).optString("picture_url"));
                                }
                            }
                            newsGroup.setPics(pics);
                            JSONArray receiveArray = itemObject.optJSONArray("receiver");
                            ArrayList<Reciver> recivers = new ArrayList<>();
                            for(int j=0;j<receiveArray.length();j++){
                                JSONObject object = receiveArray.optJSONObject(j);
                                Reciver reciver = new Reciver();
                                reciver.setId(object.optString("message_id"));
                                reciver.setReadTime(object.optString("read_time"));
                                reciver.setReceiverId(object.optString("receiver_user_id"));
                                recivers.add(reciver);
                            }
                            newsGroup.setRecivers(recivers);
                            newsGroups.add(newsGroup);
                        }
                        Collections.sort(newsGroups, new Comparator<NewsGroup>() {
                            @Override
                            public int compare(NewsGroup lhs, NewsGroup rhs) {
                                return Long.parseLong(lhs.getMessage_time())<Long.parseLong(rhs.getMessage_time())?1:-1;
                            }
                        });
                        NewsGroupAdapter adapter = new NewsGroupAdapter(newsGroups,context);
                        lv.setAdapter(adapter);
                    }
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_space_news_group);
        context = this;
        ProgressViewUtil.show(context);
        SPUtils.remove(this, "JPUSHMESSAGE");
        initView();
        new SpaceRequest(context,handler).getNewGroup();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHMESSAGE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHMESSAGE");
    }

    private void initView() {
        newsGroups = new ArrayList<>();
        quit = (RelativeLayout) findViewById(R.id.quit);
        quit.setOnClickListener(this);
        news_list_view = (PullToRefreshListView) findViewById(R.id.news_list_view);
        lv = news_list_view.getRefreshableView();
        lv.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        lv.setDividerHeight(15);
        //设置下拉上拉监听
        news_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (NetUtil.isConnnected(NewsGroupActivity.this) == true) {
                    getAllInformation();

                } else {
                    ToastUtils.ToastShort(NewsGroupActivity.this, "暂无网络");
                }
                /**
                 * 过1秒结束下拉刷新
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        news_list_view.onPullDownRefreshComplete();
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
                        news_list_view.onPullUpRefreshComplete();
                    }
                }, 1000);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsGroupActivity.this,NewGroupDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newGroup", newsGroups.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new SpaceRequest(context,handler).getNewGroup();

    }

    private void getAllInformation() {

        new SpaceRequest(context,handler).getNewGroup();

    }

}
