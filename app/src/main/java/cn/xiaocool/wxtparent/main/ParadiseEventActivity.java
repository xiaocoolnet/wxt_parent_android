package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.PradiseEventsAdapter;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;


public class ParadiseEventActivity extends BaseActivity {


    private RelativeLayout up_jiantou;
    private PullToRefreshListView event_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_paradise_event);
        initview();
    }

    private void initview() {

        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        event_list = (PullToRefreshListView) findViewById(R.id.event_list);
        event_list.getRefreshableView().setAdapter(new PradiseEventsAdapter(this));
    }
}
