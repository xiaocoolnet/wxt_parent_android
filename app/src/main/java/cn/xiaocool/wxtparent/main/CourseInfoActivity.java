package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.CourseInfoAdapter;
import cn.xiaocool.wxtparent.bean.Courseware;

public class CourseInfoActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back;
    private TextView tv_name;
    private Courseware courseware;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course_info);
        context = this;
        getData();
        initView();
    }

    private void getData() {
        courseware = (Courseware) getIntent().getSerializableExtra("data");
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(courseware.getSubject()+"课件");
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new CourseInfoAdapter(context,courseware.getCourseInfos()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
        }
    }
}
