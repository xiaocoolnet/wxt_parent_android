package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Homework_pl_adapter;
import cn.xiaocool.wxtparent.adapter.Teacher_style_Adapter;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.Homework;
import cn.xiaocool.wxtparent.bean.Teacher_style;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class WebClickTeacherStyleActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private ListView listView;
    private PullToRefreshListView listview_pull;
    private List<Teacher_style.DataEntity> tea_style_list;
    private Teacher_style_Adapter teacher_style_adapter;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.TEACHER_STYLE:
                    JSONObject obj = (JSONObject) msg.obj;
                    LogUtils.e("WebClickTeacherStyleActivity", obj.optString("data"));
                    String status = obj.optString("status");

                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        tea_style_list.clear();
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
                                tea_style_list.add(tea_style);

                            }
                        }
                        teacher_style_adapter = new Teacher_style_Adapter(getApplicationContext(), tea_style_list);
                        listView.setAdapter(teacher_style_adapter);
                    }
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_open_teacher_style);
        initView();

    }

    private void initView() {

        listview_pull = (PullToRefreshListView) findViewById(R.id.list_pull_teacher_style);
        listView = listview_pull.getRefreshableView();
        tea_style_list = new ArrayList<>();
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        new SpaceRequest(getApplicationContext(), handler).teacher_style();

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
                Intent intent = new Intent(WebClickTeacherStyleActivity.this, TeacherStyle_Webview.class);


                intent.putExtra("url", "http://wxt.xiaocool.net/index.php?g=portal&m=article&a=teacher&id=" + tea_style_list.get(position).getId());


                intent.putExtra("title", tea_style_list.get(position).getPost_title());
                intent.putExtra("content", tea_style_list.get(position).getPost_excerpt());

                startActivity(intent);


            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
        }

    }

    private void getAllInformation() {
        new SpaceRequest(getApplicationContext(), handler).teacher_style();

    }
}
