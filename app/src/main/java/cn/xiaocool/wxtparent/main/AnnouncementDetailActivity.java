package cn.xiaocool.wxtparent.main;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ParWarnImgGridAdapter;
import cn.xiaocool.wxtparent.bean.AnnouncementInfo;
import cn.xiaocool.wxtparent.bean.Reciver;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class AnnouncementDetailActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private AnnouncementInfo homeworkData;
    private ImageView iv_head,iv_content;
    private TextView tv_name,tv_time,tv_item_context,tv_title,tv_content,tv_zongfa,tv_yidu,tv_weidu;
    private RelativeLayout rl_back;
    private GridView picGridview;
    private ImageLoader imageLoader;
    private AnnouncementInfo announcementInfo;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context, "读取成功！");
                        new SpaceRequest(context, handler).announcement();
                    }
                    break;
                case CommunalInterfaces.ANNOUNCEMENT:
                    JSONObject obj1 = (JSONObject) msg.obj;
                    if (obj1.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj1.optJSONArray("data");
                        JSONObject itemObject;
                        for (int i = 0; i < Array.length(); i++) {
                            itemObject = Array.optJSONObject(i);
                            if(homeworkData.getId().equals(itemObject.optString("noticeid"))){
                                announcementInfo = new AnnouncementInfo();
                                JSONArray receiveArray = itemObject.optJSONArray("receiv_list");
                                ArrayList<Reciver> recivers = new ArrayList<>();
                                for (int j = 0; j < receiveArray.length(); j++) {
                                    JSONObject object = receiveArray.optJSONObject(j);
                                    Reciver reciver = new Reciver();
                                    reciver.setId(object.optString("noticeid"));
                                    reciver.setReadTime(object.optString("create_time"));
                                    reciver.setReceiverId(object.optString("receiverid"));
                                    recivers.add(reciver);
                                }
                                announcementInfo.setRecivers(recivers);
                            }
                        }
                    }
                    if(announcementInfo.getRecivers().size()!=0){
                        int total,already,noalready=0;
                        total=homeworkData.getRecivers().size();
                        for(int j=0;j<announcementInfo.getRecivers().size();j++){
                            if(announcementInfo.getRecivers().get(j).getReadTime().equals("0")){
                                noalready++;
                            }
                        }
                        already=total-noalready;
                        tv_zongfa.setText("总发"+total);
                        tv_yidu.setText("已阅读"+already);
                        tv_weidu.setText("未读"+noalready);
                    }
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_announcement_detail);
        context = this;
        imageLoader = ImageLoader.getInstance();
        getData();
        new SpaceRequest(context,handler).readAnnocement(homeworkData.getId());
        initView();
    }

    private void getData() {
        homeworkData = (AnnouncementInfo) getIntent().getSerializableExtra("announcement");
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        iv_head = (ImageView) findViewById(R.id.item_head);
        imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/"+homeworkData.getTeacherAvator(),iv_head);
        iv_content = (ImageView) findViewById(R.id.parent_warn_img);
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
        date.setTime(Long.parseLong(homeworkData.getTime()) * 1000);
        tv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
        tv_title = (TextView) findViewById(R.id.myhomework_title);
        tv_title.setText(homeworkData.getTitle());
        tv_content = (TextView) findViewById(R.id.myhomework_content);
        tv_content.setText(homeworkData.getContent());
        tv_zongfa = (TextView) findViewById(R.id.zongfa);
        tv_yidu = (TextView) findViewById(R.id.yidu);
        tv_weidu = (TextView) findViewById(R.id.weidu);
        if(homeworkData.getRecivers().size()!=0){
            int total,already,noalready=0;
            total=homeworkData.getRecivers().size();
            for(int i=0;i<homeworkData.getRecivers().size();i++){
                if(homeworkData.getRecivers().get(i).getReadTime().equals("0")){
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
            case R.id.up_jiantou:
                finish();
                break;
        }
    }
}
