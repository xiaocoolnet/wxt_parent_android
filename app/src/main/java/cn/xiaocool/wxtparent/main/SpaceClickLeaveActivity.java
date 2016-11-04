package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Leave_lb_Adapter;
import cn.xiaocool.wxtparent.bean.LeaveInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickLeaveActivity extends Activity implements View.OnClickListener {
    private ImageView leave_add;
    private RelativeLayout up_jiantou;
    private ListView listView;
    private Context context;
    private PullToRefreshListView listView_pull_leave;
    private ArrayList<LeaveInfo> list_leave_lb;
    private Leave_lb_Adapter leave_lb_adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.LEAVE_LEIBIAO:
                    JSONObject obj = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    String status = obj.optString("status");
                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        list_leave_lb.clear();
                        if (Array != null) {
                            for (int i = 0; i < Array.length(); i++) {
                                JSONObject object = Array.optJSONObject(i);
                                LeaveInfo leaveInfo = new LeaveInfo();
                                leaveInfo.setId(object.optString("id"));
                                leaveInfo.setBegintime(object.optString("begintime"));
                                leaveInfo.setEndtime(object.optString("endtime"));
                                leaveInfo.setContent(object.optString("reason"));
                                leaveInfo.setState(object.optString("status"));
                                leaveInfo.setFeedback(object.optString("feedback"));
                                leaveInfo.setDealtime(object.optString("deal_time"));
                                leaveInfo.setClassName(object.optString("classname"));
                                leaveInfo.setTeacherName(object.optString("teachername"));
                                leaveInfo.setLeaveType(object.optString("leavetype"));
                                leaveInfo.setTeacherAvator(object.optString("teacheravatar"));
                                leaveInfo.setStudentAvator(object.optString("studentavatar"));
                                leaveInfo.setStudentName(object.optString("studentname"));
                                JSONArray picArray = object.optJSONArray("pic");
                                ArrayList<String> pics = new ArrayList<>();
                                for(int j=0;j<picArray.length();j++){
                                    if(!picArray.optJSONObject(j).optString("picture_url").equals("")&&!picArray.optJSONObject(j).optString("picture_url").equals("null")){
                                        pics.add(picArray.optJSONObject(j).optString("picture_url"));
                                    }
                                }
                                leaveInfo.setPics(pics);
                                list_leave_lb.add(leaveInfo);
                            }
                        }
                        leave_lb_adapter = new Leave_lb_Adapter(context, list_leave_lb);
                        listView.setAdapter(leave_lb_adapter);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.leave_online);
        SPUtils.remove(this, "JPUSHLEAVE");
        context = this;
        ProgressViewUtil.show(context);
        init();
        //new SpaceRequest(context, handler).getLeave_liebiao();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHLEAVE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHLEAVE");
    }

    private void init() {
        leave_add = (ImageView) findViewById(R.id.leave_add);
        list_leave_lb = new ArrayList<>();
        listView_pull_leave = (PullToRefreshListView) findViewById(R.id.listView_pull_leave);
        listView = listView_pull_leave.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView_pull_leave.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
                        listView_pull_leave.onPullDownRefreshComplete();
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
                        listView_pull_leave.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });
        leave_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Leave_add_activity.class);
                startActivity(intent);


            }
        });
    }
    @Override
    public void onClick(View v) {

    }

    private void getAllInformation() {
        new SpaceRequest(context, handler).getLeave_liebiao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }
}
