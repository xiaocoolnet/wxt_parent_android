package cn.xiaocool.wxtparent.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.NoScrollListView;
import cn.xiaocool.wxtparent.utils.JsonParser;
import cn.xiaocool.wxtparent.utils.SPUtils;


/**
 * Created by wzh on 2016/3/13.
 */
public class SpaceClickNewsActivity extends BaseActivity implements View.OnClickListener {
    private final static int MSG_REFRESH = 2;
    private Context context;
    private RelativeLayout rl_back,news_address;
    protected boolean isConflict;
    private TextView tv_newsgroup,tv_homework;
    private static final String JPUSHMESSAGE = "JPUSHMESSAGE";
    private static final String JPUSHHOMEWORK = "JPUSHHOMEWORK";
    private BadgeView bv_newsgroup,bv_homework;
    private ImageView news_hot_news,news_homework;
    private LinearLayout news_group_send,news_class_homework;
    private NoScrollListView news_list;
    private List<cn.xiaocool.wxtparent.bean.CommunicateListModel> communicateListModelList;
    private cn.xiaocool.wxtparent.adapter.ChatListAdapter chatListAdapter;
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:

                    break;

                case CommunalInterfaces.SPACE_MYHOMEWORK:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        int i = hwArray.length()-1;
                        if(i>0){
                            JSONObject itemObject = hwArray.optJSONObject(0).optJSONArray("homework_info")
                                    .optJSONObject(0);
                            tv_homework.setText(itemObject.optString("title"));
                        }
                    }
                    break;
                case CommunalInterfaces.NEWSGROUP:
                    JSONObject obj1 = (JSONObject) msg.obj;
                    if (obj1.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj1.optJSONArray("data");
                        if (hwArray.length() > 0) {
                            JSONObject itemObject = hwArray.optJSONObject(0).optJSONArray("send_message").optJSONObject(0);
                            String text = itemObject.optString("message_content");
                            if(text.length()>20){
                                tv_newsgroup.setText(text.substring(0,19)+"...");
                            }else{
                                tv_newsgroup.setText(text);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news);
        context = this;
        communicateListModelList = new ArrayList<>();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        this.registerReceiver(new Receiver(), filter);
//        //初始化组件
        initView();
//        setUpView();
    }

    private void initView() {

        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        initializeHeader();

    }

    private void initializeHeader() {

        news_group_send = (LinearLayout) findViewById(R.id.news_group_send);
        news_group_send.setOnClickListener(this);
        news_address = (RelativeLayout) findViewById(R.id.news_address);
        news_address.setOnClickListener(this);
        news_class_homework = (LinearLayout) findViewById(R.id.news_class_homework);
        news_class_homework.setOnClickListener(this);
        tv_homework = (TextView) news_class_homework.findViewById(R.id.tv_homework);
        tv_newsgroup = (TextView) findViewById(R.id.news_mySent);
        //信息群发
        news_hot_news = (ImageView)findViewById(R.id.news_hot_news);
        bv_newsgroup = new BadgeView(context);
        bv_newsgroup.setTargetView(news_hot_news);
        //作业
        news_homework = (ImageView) news_class_homework.findViewById(R.id.news_homework);
        bv_homework = new BadgeView(context);
        bv_homework.setTargetView(news_homework);
        news_list = (NoScrollListView) findViewById(R.id.news_list);

    }

    private void setUpView() {

                switch (1) {

                    //群发
                    case 0:
                        startActivity(new Intent(SpaceClickNewsActivity.this, NewsGroupActivity.class));
                        break;
                    //通讯录
                    case 1:
                        startActivity(new Intent(SpaceClickNewsActivity.this, Space_tongxunlu.class));
                        break;
                    //我的作业
                    case 2:
                        startActivity(new Intent(SpaceClickNewsActivity.this, Space_homework.class));
                        break;
                    default:

                        break;
                }
    }


    /**
     * 设置小红点
     */
    public  void setRedPoint(){
        //从本地取出各个小红点的个数
        int msgNum = (int) SPUtils.get(context, JPUSHMESSAGE, 0);
        int hmkNum = (int) SPUtils.get(context, JPUSHHOMEWORK, 0);
        setBadgeView(msgNum, bv_newsgroup);
        setBadgeView(hmkNum,bv_homework);
    }


    /**
     * 设置背景
     * @param message
     * @param message1
     */
    private void setBadgeView(int message, BadgeView message1) {
        if (message1==null)return;
        if (message==0){
            message1.setVisibility(View.GONE);
        }else {
            message1.setVisibility(View.VISIBLE);
            message1.setBadgeCount(message);
        }
    }


    /**
     * 接受推送通知并通知页面添加小红点
     */
    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Recevier1", "接收到:");
            setRedPoint();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            //群发
            case R.id.news_group_send:
                startActivity(new Intent(SpaceClickNewsActivity.this, NewsGroupActivity.class));
                break;
            //通讯录
            case R.id.news_address:
                startActivity(new Intent(SpaceClickNewsActivity.this, Space_tongxunlu.class));
                break;
            //我的作业
            case R.id.news_class_homework:
                startActivity(new Intent(SpaceClickNewsActivity.this, Space_homework.class));
                break;
        }
    }




    /**
     * 刷新页面
     */
    public void refresh() {
        new SpaceRequest(context,handler).getNewGroup();
        new SpaceRequest(context, handler).myHomework();
        if (!handler.hasMessages(MSG_REFRESH)) {
            Log.d("55555555555", "66666666");
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }



    private void getChatList() {
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=xcGetChatListData&uid="+new UserInfo(context).getUserId();
        cn.xiaocool.wxtparent.utils.VolleyUtil.VolleyGetRequest(context, url, new cn.xiaocool.wxtparent.utils.VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonParser.JSONparser(context, result)) {
                    communicateListModelList.clear();
                    communicateListModelList.addAll(JsonParser.getBeanFromJsonCommunicateListModel(result));
                    setAdapter();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void setAdapter() {
        if (chatListAdapter==null){
            chatListAdapter = new cn.xiaocool.wxtparent.adapter.ChatListAdapter(context,communicateListModelList);
            news_list.setAdapter(chatListAdapter);
        }else {
            chatListAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        setRedPoint();
        refresh();
        getChatList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }
}
