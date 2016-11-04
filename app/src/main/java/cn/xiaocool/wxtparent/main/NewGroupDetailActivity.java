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
import cn.xiaocool.wxtparent.bean.NewsGroup;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class NewGroupDetailActivity extends Activity implements View.OnClickListener {
    private Context context;
    private NewsGroup newsGroup;
    private TextView tv_time,tv_content,tv_zongfa,tv_yidu,tv_weidu,tv_name;
    private RelativeLayout rl_back;
    private ImageLoader imageLoader;
    private GridView picGridview;
    private ImageView iv_head,iv_content;
    private RelativeLayout rl_one;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context,"读取成功！");
                    }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_group_detail);
        context = this;
        imageLoader = ImageLoader.getInstance();
        newsGroup = (NewsGroup) getIntent().getSerializableExtra("newGroup");
        new SpaceRequest(context,handler).readNewGroup(newsGroup.getMessage_id());
        initView();
    }

    private void initView() {
        rl_one = (RelativeLayout) findViewById(R.id.rl_one);
        rl_one.setVisibility(View.GONE);
        iv_head = (ImageView) findViewById(R.id.item_head);
        imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/"+newsGroup.getTeacherAvator(),iv_head);
        rl_back = (RelativeLayout) findViewById(R.id.quit);
        rl_back.setOnClickListener(this);
        tv_time = (TextView) findViewById(R.id.item_time);
        Date date = new Date();
        date.setTime(Long.parseLong(newsGroup.getMessage_time()) * 1000);
        tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        tv_content = (TextView) findViewById(R.id.myhomework_content);
        tv_content.setText(newsGroup.getMessage_content());
        tv_name = (TextView) findViewById(R.id.item_title);
        tv_name.setText(newsGroup.getSend_user_name());
        picGridview = (GridView) findViewById(R.id.parent_warn_img_gridview);
        iv_content = (ImageView) findViewById(R.id.parent_warn_img);
        if (newsGroup.getPics().size()>1){
            iv_content.setVisibility(View.GONE);
            picGridview.setVisibility(View.VISIBLE);
            ParWarnImgGridAdapter parWarnImgGridAdapter = new ParWarnImgGridAdapter(newsGroup.getPics(),context);
            picGridview.setAdapter(parWarnImgGridAdapter);
            picGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                    // 图片浏览
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", newsGroup.getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", a);
                    context.startActivity(intent);
                }
            });

        }else if (newsGroup.getPics().size()==1){
            picGridview.setVisibility(View.GONE);
            iv_content.setVisibility(View.VISIBLE);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + newsGroup.getPics().get(0), iv_content);
            iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ImgDetailActivity.class);
                    intent.putStringArrayListExtra("Imgs", newsGroup.getPics());
                    intent.putExtra("type","newsgroup");
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            });
        }else {
            picGridview.setVisibility(View.GONE);
            iv_content.setVisibility(View.GONE);
        }
        tv_zongfa = (TextView) findViewById(R.id.total_send);
        tv_yidu = (TextView) findViewById(R.id.alread_text);
        tv_weidu = (TextView) findViewById(R.id.noalread_text);
        if(newsGroup.getRecivers().size()!=0){
            int total,already,noalready=0;
            total=newsGroup.getRecivers().size();
            for(int i=0;i<newsGroup.getRecivers().size();i++){
                if(newsGroup.getRecivers().get(i).getReadTime().equals("null")||newsGroup.getRecivers().get(i).getReadTime().length()==0){
                    noalready++;
                }
            }
            already=total-noalready;
            tv_zongfa.setText("总发"+total);
            tv_yidu.setText("已阅读"+already);
            tv_weidu.setText("未读"+noalready);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quit:
                finish();
                break;
        }
    }
}
