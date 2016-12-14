package cn.xiaocool.wxtparent.main;

import android.content.Intent;
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
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Teacher_style_Adapter;
import cn.xiaocool.wxtparent.bean.Teacher_style;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/2/25.
 */
public class WebClickRearingChildActivity extends BaseActivity implements View.OnClickListener {

//    private ImageView btn_exit;
//    private RearingChildListView rearingChildLv;
//    private List<RearingChild.RearingChildData> rearingChildDataList;
//    private RearingChildAdapter rearingChildAdapter;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case CommunalInterfaces.REARING_CHILD:
//                    if (msg.obj!=null){
//                        JSONObject obj = (JSONObject) msg.obj;
//                        try {
//                            String status = obj.getString("status");
//                            if (status.equals(CommunalInterfaces._STATE)){
//                                JSONArray dataArray = obj.getJSONArray("data");
//                                JSONObject itemObject;
//                                for (int i = 0; i < dataArray.length(); i++) {
//                                    itemObject = dataArray.getJSONObject(i);
//                                    RearingChild.RearingChildData rearingChildData = new RearingChild.RearingChildData();
//                                    rearingChildData.setReleasename(itemObject.getString("releasename"));
//                                    rearingChildData.setHappy_content(itemObject.getString("happy_content"));
//                                    rearingChildData.setHappy_pic(itemObject.getString("happy_pic"));
//                                    rearingChildData.setHappy_title(itemObject.getString("happy_title"));
//                                    rearingChildData.setHappy_time(itemObject.getString("happy_time"));
//                                    rearingChildDataList.add(rearingChildData);
//                                }
//                                rearingChildAdapter = new RearingChildAdapter(WebClickRearingChildActivity.this,rearingChildDataList);
//                                rearingChildLv.setAdapter(rearingChildAdapter);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//            }
//        }
//    };


    private ListView listView;
    private PullToRefreshListView listview_pull;
    private List<Teacher_style.DataEntity> rearing_child_list;
    private Teacher_style_Adapter teacher_style_adapter;
    private RelativeLayout up_jiantou;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.REARING_CHILD:
                    JSONObject obj = (JSONObject) msg.obj;
                    LogUtils.e("WebClickTeacherStyleActivity", obj.optString("data"));
                    String status = obj.optString("status");

                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        rearing_child_list.clear();
                        if (Array != null) {
                            for (int i = 0; i < Array.length(); i++) {
                                Teacher_style.DataEntity tea_style = new Teacher_style().new DataEntity();
                                JSONObject Object = Array.optJSONObject(i);
                                tea_style.setId(Object.optString("id"));
                                tea_style.setPost_date(Object.optString("post_date"));
                                tea_style.setPost_excerpt(Object.optString("post_excerpt"));
                                tea_style.setPost_keywords(Object.optString("post_keywords"));
                                tea_style.setPost_title(Object.optString("post_title"));
                                tea_style.setSchoolid(Object.optString("schoolid"));
                                tea_style.setSmeta(Object.optString("smeta"));
                                tea_style.setThumb(Object.optString("thumb"));


                                Log.e("yyyy", Object.optString("post_title"));
                                rearing_child_list.add(tea_style);

                            }
                        }
                        teacher_style_adapter = new Teacher_style_Adapter(getApplicationContext(), rearing_child_list);
                        listView.setAdapter(teacher_style_adapter);
                    }
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_click_child_rearing_list);
//        initView();
        initView();

    }

    private void initView() {

        up_jiantou= (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);


        listview_pull = (PullToRefreshListView) findViewById(R.id.web_reaing_list);
        listView = listview_pull.getRefreshableView();
        rearing_child_list = new ArrayList<>();
        new SpaceRequest(getApplicationContext(), handler).announcement_yuanqu();

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WebClickRearingChildActivity.this, TeacherStyle_Webview.class);

                intent.putExtra("url","http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getParentsThings&schoolid=" + rearing_child_list.get(position).getId());
                intent.putExtra("title", rearing_child_list.get(position).getPost_title());
                intent.putExtra("content", rearing_child_list.get(position).getPost_excerpt());

                startActivity(intent);


            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
        }
    }

//    private void getAllInformation() {
//        new SpaceRequest(this, handler).announcement();
//
//    }

    private void getAllInformation() {
        new SpaceRequest(getApplicationContext(), handler).announcement_yuanqu();

    }
//    private void initView() {
//        rearingChildDataList = new ArrayList<>();
//        btn_exit = (ImageView) findViewById(R.id.btn_exit);
//        btn_exit.setOnClickListener(this);
//        rearingChildLv = (RearingChildListView) findViewById(R.id.web_rearingChildLv);
//        new SpaceRequest(WebClickRearingChildActivity.this,handler).rearingChild();
//        rearingChildLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(WebClickRearingChildActivity.this, WebClickRearingChildSpecificActivity.class);
//                intent.putExtra("happy_title", rearingChildDataList.get(position).getHappy_title());
//                intent.putExtra("happy_pic", rearingChildDataList.get(position).getHappy_pic());
//                intent.putExtra("happy_content", rearingChildDataList.get(position).getHappy_content());
//                intent.putExtra("releasename", rearingChildDataList.get(position).getReleasename());
//                startActivity(intent);
//            }
//        });
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_exit:
//                finish();
//        }
//
//    }


}
