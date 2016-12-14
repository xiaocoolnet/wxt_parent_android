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
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class WebClickClassActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout up_jiantou;
    private ListView listView;
    private PullToRefreshListView listview_pull;
    //bean 类还是用teacher_style的
    private List<Teacher_style.DataEntity> interesting_class_list;
    //adapter还用他们的
    private Teacher_style_Adapter teacher_style_adapter;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.INTERESTING_CLASS:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");

                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        interesting_class_list.clear();
                        if (Array != null) {
                            for (int i = 0; i < Array.length(); i++) {
                                Teacher_style.DataEntity interesting_class = new Teacher_style().new DataEntity();
                                JSONObject Object = Array.optJSONObject(i);
                                interesting_class.setId(Object.optString("id"));
                                interesting_class.setPost_date(Object.optString("post_date"));
                                interesting_class.setPost_excerpt(Object.optString("post_excerpt"));
                                interesting_class.setPost_keywords(Object.optString("post_keywords"));
                                interesting_class.setPost_title(Object.optString("post_title"));
                                interesting_class.setSchoolid(Object.optString("schoolid"));
                                interesting_class.setSmeta(Object.optString("smeta"));
                                interesting_class.setThumb(Object.optString("thumb"));


                                Log.e("yyyy", Object.optString("post_title"));
                                interesting_class_list.add(interesting_class);

                            }
                        }
                        teacher_style_adapter = new Teacher_style_Adapter(getApplicationContext(), interesting_class_list);
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
        setContentView(R.layout.web_open_class);
        initView();
    }

    private void initView() {

        listview_pull = (PullToRefreshListView) findViewById(R.id.list_pull_interesting_class);
        listView = listview_pull.getRefreshableView();
        interesting_class_list = new ArrayList<>();
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);


        new SpaceRequest(getApplicationContext(), handler).interesting_classw();

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
                Intent intent = new Intent(WebClickClassActivity.this, TeacherStyle_Webview.class);

                intent.putExtra("url", "http://wxt.xiaocool.net/index.php?g=portal&m=article&a=Interest&id="+interesting_class_list.get(position).getId());
                intent.putExtra("title", interesting_class_list.get(position).getPost_title());
                intent.putExtra("content", interesting_class_list.get(position).getPost_excerpt());

                startActivity(intent);


            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
        }

    }

    private void getAllInformation() {
        new SpaceRequest(getApplicationContext(), handler).interesting_classw();

    }

}
