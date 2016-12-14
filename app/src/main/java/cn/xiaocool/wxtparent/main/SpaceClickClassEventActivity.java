package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ClassEvent_2_Adapter;
import cn.xiaocool.wxtparent.bean.ClassEvent;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by Administrator on 2016/3/20.
 */
public class SpaceClickClassEventActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ArrayList<ClassEvent> list_classEvent_data;
    private RelativeLayout re1;
    private ListView listView;
    private ClassEvent_2_Adapter classEvent_2_Adapter;
    private PullToRefreshListView listview_pull;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.CLASS_EVENTS:
                    JSONObject obj = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        list_classEvent_data.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < Array.length(); i++) {
                            itemObject = Array.optJSONObject(i);
                            ClassEvent classEvent = new ClassEvent();
                            classEvent.setId(itemObject.optString("activity_id"));
                            JSONObject object = itemObject.optJSONArray("activity_list").optJSONObject(0);
                            classEvent.setTitle(object.optString("title"));
                            classEvent.setContent(object.optString("content"));
                            classEvent.setBegintime(object.optString("begintime"));
                            classEvent.setEndtime(object.optString("endtime"));
                            classEvent.setStarttime(object.optString("starttime"));
                            classEvent.setFinishtime(object.optString("finishtime"));
                            classEvent.setContactName(object.optString("contactman"));
                            classEvent.setContactPhone(object.optString("contactphone"));
                            classEvent.setIsapply(object.optString("isapply"));
                            classEvent.setTime(object.optString("create_time"));
                            if(object.optJSONArray("user_info").length()!=0){
                                classEvent.setTeacherName(object.optJSONArray("user_info").optJSONObject(0).optString("name"));
                                classEvent.setTeacherAvator(object.optJSONArray("user_info").optJSONObject(0).optString("photo"));
                            }
                            JSONArray picArray = itemObject.optJSONArray("pic");
                            ArrayList<String> pics = new ArrayList<>();
                            for(int j=0;j<picArray.length();j++){
                                if(!picArray.optJSONObject(j).optString("picture_url").equals("null")&&!picArray.optJSONObject(j).optString("picture_url").equals("")){
                                    pics.add(picArray.optJSONObject(j).optString("picture_url"));
                                }
                            }
                            classEvent.setPics(pics);
                            JSONArray applyArray = itemObject.optJSONArray("apply_count");
                            ArrayList<String> apply = new ArrayList<>();
                            if(itemObject.optJSONArray("apply_count")!=null) {
                                for (int j = 0; j < applyArray.length(); j++) {
                                    apply.add(applyArray.optJSONObject(j).optString("userid"));
                                }
                            }
                            classEvent.setApplyid(apply);
                            list_classEvent_data.add(classEvent);
                        }
                        Collections.sort(list_classEvent_data, new Comparator<ClassEvent>() {
                            @Override
                            public int compare(ClassEvent lhs, ClassEvent rhs) {
                                return Long.parseLong(lhs.getTime())<Long.parseLong(rhs.getTime())?1:-1;
                            }
                        });
                        if (classEvent_2_Adapter != null) {
                            classEvent_2_Adapter.notifyDataSetChanged();
                        } else {
                            classEvent_2_Adapter = new ClassEvent_2_Adapter(list_classEvent_data, mContext);
                            listView.setAdapter(classEvent_2_Adapter);
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_class_event);
        ProgressViewUtil.show(this);
        SPUtils.remove(this, "JPUSHACTIVITY");
        initView2();


    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHACTIVITY");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHACTIVITY");
    }

    private void initView2() {
        mContext = this;
        re1 = (RelativeLayout) findViewById(R.id.up_jiantou);
        listview_pull = (PullToRefreshListView) findViewById(R.id.class_events_listcontent);
        listView = listview_pull.getRefreshableView();
        list_classEvent_data = new ArrayList<>();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
        listview_pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
                        listview_pull.onPullDownRefreshComplete();
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
                        listview_pull.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });

        re1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }


    private void getAllInformation() {
        new SpaceRequest(this, handler).classEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }
}
