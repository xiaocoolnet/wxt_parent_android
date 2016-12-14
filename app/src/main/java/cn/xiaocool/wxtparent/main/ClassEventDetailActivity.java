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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ParWarnImgGridAdapter;
import cn.xiaocool.wxtparent.bean.ClassEvent;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class ClassEventDetailActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private ClassEvent homeworkData;
    private ImageView iv_head, iv_content;
    private TextView tv_name, tv_time, tv_item_context, tv_title, tv_content;
    private TextView tv_start,tv_finish,tv_contact,tv_phone,tv_start_attend,tv_finish_attend;
    private RelativeLayout rl_back,rl_one,rl_two;
    private LinearLayout ll_apply_text;
    private GridView picGridview;
    private ImageLoader imageLoader;
    private TextView tv_apply_count, btn_apply;
    private LinearLayout ll_apply;
    private UserInfo userInfo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.APPLYEVENT:
                    JSONObject object = (JSONObject) msg.obj;
                    if (object.optString("status").equals("success")) {
                        ToastUtils.ToastShort(context, "报名成功！");
                        btn_apply.setText("已报名");
                        btn_apply.setClickable(false);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_class_event_detail);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        imageLoader = ImageLoader.getInstance();
        getData();
        initView();
    }

    private void getData() {
        homeworkData = (ClassEvent) getIntent().getSerializableExtra("classEvent");
    }

    private void initView() {
        rl_one = (RelativeLayout) findViewById(R.id.rl_one);
        rl_two = (RelativeLayout) findViewById(R.id.rl_two);
        tv_apply_count = (TextView) findViewById(R.id.tv_bottom);
        ll_apply_text = (LinearLayout) findViewById(R.id.ll_apply_text);
        tv_apply_count.setText("已报名" + homeworkData.getApplyid().size());
        btn_apply = (TextView) findViewById(R.id.btn_apply);
        Long d = new Date().getTime()/1000;
        if (isApply()) {
            btn_apply.setText("已报名");
            btn_apply.setClickable(false);
        } else if(d<Long.parseLong(homeworkData.getStarttime())||d>Long.parseLong(homeworkData.getFinishtime())){
            btn_apply.setBackgroundResource(R.drawable.cornners_gray);
            btn_apply.setText("不在报名时间区间内");
            btn_apply.setLinksClickable(false);
        }else{
            btn_apply.setOnClickListener(this);
        }


        ll_apply = (LinearLayout) findViewById(R.id.ll_apply);
        if (homeworkData.getIsapply().equals("1")) {
            ll_apply.setVisibility(View.VISIBLE);
            ll_apply_text.setVisibility(View.VISIBLE);
            rl_one.setVisibility(View.VISIBLE);
            rl_two.setVisibility(View.VISIBLE);
        } else if (homeworkData.getIsapply().equals("0")) {
            ll_apply.setVisibility(View.GONE);
            ll_apply_text.setVisibility(View.GONE);
            rl_one.setVisibility(View.GONE);
            rl_two.setVisibility(View.GONE);
        }
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        iv_head = (ImageView) findViewById(R.id.item_head);
        imageLoader.displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + homeworkData.getTeacherAvator(), iv_head);
        iv_content = (ImageView) findViewById(R.id.parent_warn_img);
        picGridview = (GridView) findViewById(R.id.parent_warn_img_gridview);
        if (homeworkData.getPics().size() > 1) {
            iv_content.setVisibility(View.GONE);
            picGridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(homeworkData.getPics(), context);
            picGridview.setAdapter(parWarnImgGridAdapter);
            picGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", homeworkData.getPics());
                    intent.putExtra("type", "newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        } else if (homeworkData.getPics().size() == 1) {
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
                    intent.putExtra("type", "newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        } else {
            picGridview.setVisibility(View.GONE);
            iv_content.setVisibility(View.GONE);

        }
        tv_name = (TextView) findViewById(R.id.item_title);
        tv_name.setText(homeworkData.getTeacherName());
        tv_item_context = (TextView) findViewById(R.id.item_content);
        tv_time = (TextView) findViewById(R.id.item_time);
        Date date = new Date();
        date.setTime(Long.parseLong(homeworkData.getTime()) * 1000);
        tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        tv_title = (TextView) findViewById(R.id.myhomework_title);
        tv_title.setText(homeworkData.getTitle());
        tv_content = (TextView) findViewById(R.id.myhomework_content);
        tv_content.setText(homeworkData.getContent());

        tv_start = (TextView) findViewById(R.id.start_text);
        tv_finish = (TextView) findViewById(R.id.finish_text);
        tv_start_attend = (TextView) findViewById(R.id.start_attend_text);
        tv_finish_attend = (TextView) findViewById(R.id.finish_attend_text);
        tv_contact = (TextView) findViewById(R.id.contact_people);
        tv_phone = (TextView) findViewById(R.id.contact_phone);
        date.setTime(Long.parseLong(homeworkData.getBegintime()) * 1000);
        tv_start.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        date.setTime(Long.parseLong(homeworkData.getEndtime()) * 1000);
        tv_finish.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        date.setTime(Long.parseLong(homeworkData.getStarttime()) * 1000);
        tv_start_attend.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        date.setTime(Long.parseLong(homeworkData.getFinishtime()) * 1000);
        tv_finish_attend.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));

        tv_contact.setText(homeworkData.getContactName());
        tv_phone.setText(homeworkData.getContactPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.btn_apply:
                //报名
                new SpaceRequest(context, handler).applyClassEvent(homeworkData.getId());
                break;
        }
    }

    public boolean isApply() {
        if (homeworkData.getApplyid().size() == 0) {
            return false;
        } else {
            for (int i = 0; i < homeworkData.getApplyid().size(); i++) {
                if (userInfo.getChildId().equals(homeworkData.getApplyid().get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
