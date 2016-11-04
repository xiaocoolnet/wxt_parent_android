package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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

public class WebClickBabyShowActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private ListView listView;
    private PullToRefreshListView listview_pull;
    //bean 类还是用teacher_style的
    private List<Teacher_style.DataEntity> baby_show_list;
    //adapter还用他们的
    private Teacher_style_Adapter teacher_style_adapter;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.BABY_SHOW:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");

                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        baby_show_list.clear();
                        if (Array != null) {
                            for (int i = 0; i < Array.length(); i++) {
                                Teacher_style.DataEntity baby_show = new Teacher_style().new DataEntity();
                                JSONObject Object = Array.optJSONObject(i);
                                baby_show.setId(Object.optString("id"));
                                baby_show.setPost_date(Object.optString("post_date"));
                                baby_show.setPost_excerpt(Object.optString("post_excerpt"));
                                baby_show.setPost_keywords(Object.optString("post_keywords"));
                                baby_show.setPost_title(Object.optString("post_title"));
                                baby_show.setSchoolid(Object.optString("schoolid"));
                                baby_show.setSmeta(Object.optString("smeta"));
                                baby_show.setThumb(Object.optString("thumb"));


                                Log.e("yyyy", Object.optString("post_title"));
                                baby_show_list.add(baby_show);

                            }
                        }
                        teacher_style_adapter = new Teacher_style_Adapter(getApplicationContext(), baby_show_list);
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
        setContentView(R.layout.activity_web_click_baby_show);

        initView();

    }

    private void initView() {
        listview_pull = (PullToRefreshListView) findViewById(R.id.list_pull_baby_show);
        listView = listview_pull.getRefreshableView();
        baby_show_list = new ArrayList<>();
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        new SpaceRequest(getApplicationContext(), handler).baby_show();

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
                Intent intent = new Intent(WebClickBabyShowActivity.this, TeacherStyle_Webview.class);

                intent.putExtra("url", "http://wxt.xiaocool.net/index.php?g=portal&m=article&a=baby&id="+baby_show_list.get(position).getId());
                intent.putExtra("title", baby_show_list.get(position).getPost_title());
                intent.putExtra("content", baby_show_list.get(position).getPost_excerpt());

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
        new SpaceRequest(getApplicationContext(), handler).baby_show();

    }

}
