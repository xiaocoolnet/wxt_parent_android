package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.SuggestListAdapter;
import cn.xiaocool.wxtparent.bean.SuggestModel;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.JsonParser;
import cn.xiaocool.wxtparent.utils.VolleyUtil;


public class SuggestListActivity extends BaseActivity {

    private ListView listView;
    private PullToRefreshListView pullToRefreshListView;
    private List<SuggestModel> suggestModels;
    private SuggestListAdapter suggestListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_suggest_list);
        suggestModels = new ArrayList<>();
        //back
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //add
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestListActivity.this,OnlineCommentActivity.class);
                startActivity(intent);
            }
        });


        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.sug_lv);
        listView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshListView.onPullUpRefreshComplete();
                    }
                }, 1000);
            }
        });
    }

    /**
     * 获取数据
     */
    private void initData() {
        String url = "http://wxt.xiaocool.net/index.php?g=apps&m=index&a=GetLeaveMessageBySelf&userid="+new UserInfo(this).getUserId();
        VolleyUtil.VolleyGetRequest(this, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                pullToRefreshListView.onPullDownRefreshComplete();
                if (JsonParser.JSONparser(SuggestListActivity.this, result)) {
                    suggestModels.clear();
                    suggestModels.addAll(JsonParser.getBeanFromJsonSuggestModel(result));
                    setAdapter();
                }
            }

            @Override
            public void onError() {
                pullToRefreshListView.onPullDownRefreshComplete();
            }
        });
    }

    private void setAdapter() {
        if (suggestListAdapter==null){
            suggestListAdapter = new cn.xiaocool.wxtparent.adapter.SuggestListAdapter(suggestModels,this);
            listView.setAdapter(suggestListAdapter);
        }else {
            suggestListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
