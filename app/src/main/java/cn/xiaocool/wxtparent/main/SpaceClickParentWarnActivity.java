package cn.xiaocool.wxtparent.main;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ParentWarnListAdapter;
import cn.xiaocool.wxtparent.bean.ParentWarnInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by Administrator on 2016/4/3.
 */
public class SpaceClickParentWarnActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout up_jiantou;
    private ImageView image_add;
    private PullToRefreshListView pull_listview;
    private ListView listView;
    private ArrayList<ParentWarnInfo> list;
    private ParentWarnListAdapter parentWarn_adapter;
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.PARENT_WARN:
                    JSONObject jo = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    if (jo.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray ja = jo.optJSONArray("data");
                        list.clear();
                        Log.e("hello------",ja.toString());
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo2 = ja.optJSONObject(i);
                            ParentWarnInfo parentWarnInfo = new ParentWarnInfo();
                            parentWarnInfo.setId(jo2.optString("id"));
                            parentWarnInfo.setUserid(jo2.optString("userid"));
                            parentWarnInfo.setContent(jo2.optString("content"));
                            parentWarnInfo.setTeacherName(jo2.optString("teachername"));
                            parentWarnInfo.setTime(jo2.optString("create_time"));
                            parentWarnInfo.setStudentname(jo2.optString("studentname"));
                            JSONArray commentArray = jo2.optJSONArray("comment");
                            if (commentArray.length() > 0) {
                                parentWarnInfo.setCommentAvator(commentArray.optJSONObject(0).optString("avatar"));
                                parentWarnInfo.setCommentName(commentArray.optJSONObject(0).optString("name"));
                                parentWarnInfo.setCommentContent(commentArray.optJSONObject(0).optString("content"));
                                parentWarnInfo.setCommentTime(commentArray.optJSONObject(0).optString("comment_time"));
                            }
                            JSONArray picArray = jo2.optJSONArray("pic");
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (int j = 0; j < picArray.length(); j++) {
                                JSONObject object = picArray.optJSONObject(j);
                                if(!object.optString("picture_url").equals("")&&!object.optString("picture_url").equals("null")){
                                    arrayList.add(object.optString("picture_url"));
                                }
                            }
                            parentWarnInfo.setPics(arrayList);
                            list.add(parentWarnInfo);
                        }
                        parentWarn_adapter = new ParentWarnListAdapter(list, context);
                        listView.setAdapter(parentWarn_adapter);


                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_open_parent_warn);
        SPUtils.remove(this, "JPUSHTRUST");
        context = this;
        ProgressViewUtil.show(context);
        initView();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHTRUST");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHTRUST");
    }

    private void initView() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        image_add = (ImageView) findViewById(R.id.image_add);
        up_jiantou.setOnClickListener(this);
        image_add.setOnClickListener(this);
        pull_listview = (PullToRefreshListView) findViewById(R.id.pull_parentwarn);
        listView = pull_listview.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
        list = new ArrayList<>();

        pull_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
                        pull_listview.onPullDownRefreshComplete();
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
                        pull_listview.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.image_add:
                Intent intent = new Intent(SpaceClickParentWarnActivity.this, Add_ParentWarn.class);
                startActivity(intent);
                break;
        }

    }

    private void getAllInformation() {
        new SpaceRequest(this, handler).parentWarn();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }
}
