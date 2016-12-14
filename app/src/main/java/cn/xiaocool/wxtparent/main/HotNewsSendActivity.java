package cn.xiaocool.wxtparent.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;


/**
 * Created by 潘 on 2016/3/31.
 */
public class HotNewsSendActivity extends BaseActivity implements View.OnClickListener {
    private TextView news_hot_news1_sender;
    private TextView news_hot_news1_sendtime;
    private TextView news_hot_news1_message_details;
    private ImageView quit;
    private String send_user_id;
    //    private Handler handler = new Handler() {
//        private long times;
//
//        public void handleMessage(android.os.Message msg){
//            switch (msg.what){
//                case CommunalInterfaces.SEND_MESSAGE:
//                    if(msg.obj!=null){
//                        JSONObject obj = (JSONObject)msg.obj;
//                        try{
//                            String status = obj.getString("status");
//                            if (status.equals("success")){
//                                JSONArray jsonArray = obj.getJSONArray("data");
//                                    news_hot_news1_sender.setText(jsonArray.getJSONObject(0).getString("send_user_name"));
//                                    String message_time = jsonArray.getJSONObject(0).getString("message_time");
//                                    times = Long.parseLong(message_time)*1000;
//                                    Date date = new Date(times);
//                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//                                    news_hot_news1_sendtime.setText(dateFormat.format(date));
//                                    news_hot_news1_message_details.setText(jsonArray.getJSONObject(0).getString("message_content"));
//                                    send_user_id = jsonArray.getJSONObject(0).getString("send_user_id");
//
//                            }
//                        }catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//
//            }
//        }
//
//    };
    private TextView tvRevert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_hot_news_send);
        initView();
    }

    private void initView() {
        news_hot_news1_sender = (TextView) findViewById(R.id.news_hot_news1_sneder);
        news_hot_news1_sendtime = (TextView) findViewById(R.id.news_hot_news1_sendtime);
        news_hot_news1_message_details = (TextView) findViewById(R.id.news_hot_news1_message_details);
        init_hot_news();
        quit = (ImageView) findViewById(R.id.quit);
        quit.setOnClickListener(this);

    }

    private void init_hot_news() {
//        new SpaceRequest(this,handler).HotNews();
        Intent intent = getIntent();
        news_hot_news1_sender.setText(intent.getStringExtra("name"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        news_hot_news1_sendtime.setText(dateFormat.format(new Date(Long.parseLong(intent.getStringExtra("time")) * 1000)));
        news_hot_news1_message_details.setText(intent.getStringExtra("content"));
        send_user_id = intent.getStringExtra("receiveID");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit:
                finish();
                break;
//            case R.id.tvRevert:
//                Intent intent = new Intent(this, MySendMessageToTeacherActivity.class);
//                intent.putExtra("teacherID", send_user_id);
//                startActivity(intent);
//                break;
            default:
                break;
        }
    }
}
