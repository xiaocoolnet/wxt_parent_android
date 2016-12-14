package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/4/21.
 */
public class HotNewsReceiveActivity extends BaseActivity implements View.OnClickListener {
    private TextView news_hot_news1_sender;
    private TextView news_hot_news1_sendtime;
    private TextView news_hot_news1_message_details;
    private ImageView quit;
    private String send_user_id;
    private TextView tvRevert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_hot_news_receive);
        initView();
    }

    private void initView() {
        news_hot_news1_sender = (TextView) findViewById(R.id.news_hot_news1_sneder);
        news_hot_news1_sendtime = (TextView) findViewById(R.id.news_hot_news1_sendtime);
        news_hot_news1_message_details = (TextView) findViewById(R.id.news_hot_news1_message_details);
        tvRevert = (TextView) findViewById(R.id.tvRevert);
        tvRevert.setOnClickListener(this);
        init_hot_news();
        quit = (ImageView) findViewById(R.id.quit);
        quit.setOnClickListener(this);

    }

    private void init_hot_news() {
//        new SpaceRequest(this,handler).HotNews();
        Intent intent = getIntent();
        news_hot_news1_sender.setText(intent.getStringExtra("name"));

        news_hot_news1_message_details.setText(intent.getStringExtra("content"));
        send_user_id = intent.getStringExtra("sendID");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit:
                finish();
                break;
            case R.id.tvRevert:
                Intent intent = new Intent(this, MySendMessageToTeacherActivity.class);
                intent.putExtra("teacherID", send_user_id);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
