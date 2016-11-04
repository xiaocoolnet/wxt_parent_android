package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ParWarnImgGridAdapter;
import cn.xiaocool.wxtparent.bean.Homework;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class HomeworkDetailActivity extends Activity implements View.OnClickListener {
    private Context context;
    private Homework.HomeworkData homeworkData;
    private ImageView iv_head,iv_content;
    private TextView tv_name,tv_time,tv_item_context,tv_title,tv_content,tv_subject;
    private RelativeLayout rl_back;
    private GridView picGridview;
    private ImageLoader imageLoader;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context, "读取成功！");
                    }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homework_detail);
        context = this;
        imageLoader = ImageLoader.getInstance();
        getData();
        new SpaceRequest(context,handler).readHomeWork(homeworkData.getId());
        initView();
    }

    private void getData() {
        homeworkData = (Homework.HomeworkData) getIntent().getSerializableExtra("homework");
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        iv_head = (ImageView) findViewById(R.id.item_head);
        imageLoader.displayImage("http://wxt.xiaocool.net/uploads/avatar/"+homeworkData.getTeacherAvator(),iv_head);
        iv_content = (ImageView) findViewById(R.id.parent_warn_img);
        tv_subject = (TextView) findViewById(R.id.subject);
        tv_subject.setText(homeworkData.getSubject());
        picGridview = (GridView) findViewById(R.id.parent_warn_img_gridview);
        if (homeworkData.getPics().size()>1){
            iv_content.setVisibility(View.GONE);
            picGridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(homeworkData.getPics(),context);
            picGridview.setAdapter(parWarnImgGridAdapter);
            picGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", homeworkData.getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (homeworkData.getPics().size()==1){
            picGridview.setVisibility(View.GONE);
            iv_content.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + homeworkData.getPics().get(0), iv_content);
            iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", homeworkData.getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            picGridview.setVisibility(View.GONE);
            iv_content.setVisibility(View.GONE);

        }
        tv_name = (TextView) findViewById(R.id.item_title);
        tv_name.setText(homeworkData.getTeacherName());
        tv_item_context = (TextView) findViewById(R.id.item_content);
        tv_time = (TextView) findViewById(R.id.item_time);
        Date date = new Date();
        date.setTime(Long.parseLong(homeworkData.getCreate_time()) * 1000);
        tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        tv_title = (TextView) findViewById(R.id.myhomework_title);
        tv_title.setText(homeworkData.getTitle());
        tv_content = (TextView) findViewById(R.id.myhomework_content);
        tv_content.setText(homeworkData.getContent());
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
