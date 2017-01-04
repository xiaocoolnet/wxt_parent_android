package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.MyDiaryAdapter;
import cn.xiaocool.wxtparent.adapter.NoFriendContentAdapter;
import cn.xiaocool.wxtparent.bean.ClassCricleInfo;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.view.WxtApplication;

public class FriendDiaryActivity extends BaseActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private PullToRefreshListView lv_homework;
    private String id;
    private ListView lv;
    private String data=null;
    private LinearLayout commentView;
    private ArrayList<ClassCricleInfo> CricleList = new ArrayList<>();
    private MyDiaryAdapter mAdapter;
    private Context mContext;
    private UserInfo userInfo;
    private String TAG = "SpaceBabyAlbumActivity";
    private static final int HOMEWORK_PRAISE_KEY = 104;
    private static final int DEL_HOMEWORK_PRAISE_KEY = 105;
    private static final int GET_CIRCLE_LIST_KEY = 2;
    private ImageView iv_nodata;
    private View viewH = null;
    private SliderLayout sliderLayout;
    private RelativeLayout rl_back;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.SEND_PARENT_REMARK:
                    Log.d("是否成功", "======");

                    if (msg.obj != null) {
                        JSONObject obj2 = (JSONObject) msg.obj;
                        String state = obj2.optString("status");
                        LogUtils.e("HomeworkCommentActivity", obj2.optString("data"));
                        Log.d("是否成功", state);
                        if (state.equals(CommunalInterfaces._STATE)) {
                            data = obj2.optString("data");
                            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                            getAllInformation();
                        } else {
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;

                case HOMEWORK_PRAISE_KEY:
                    if (msg.obj != null) {
                        LogUtils.i(TAG, "点赞" + msg.obj);
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            String state = json.getString("status");
                            String result = json.getString("data");
                            if (state.equals(CommunalInterfaces._STATE)) {
                                getAllInformation();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case DEL_HOMEWORK_PRAISE_KEY:
                    if (msg.obj != null) {
                        LogUtils.i(TAG, "取消赞" + msg.obj);
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            String state = json.getString("status");
                            String result = json.getString("data");
                            if (state.equals(CommunalInterfaces._STATE)) {
                                getAllInformation();
                            } else {
                                ToastUtils.ToastShort(mContext, result);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CommunalInterfaces.MYDIARY:
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        String state = obj.getString("status");
                        if (state.equals(CommunalInterfaces._STATE)) {
                            CricleList.clear();
                            JSONArray items = obj.getJSONArray("data");
                            JSONObject itemObject;
                            for (int i = 0; i < items.length(); i++) {
                                itemObject = (JSONObject) items.get(i);
                                ClassCricleInfo cricle = new ClassCricleInfo();
                                cricle.setId(itemObject.getString("mid"));
                                cricle.setMatter(itemObject.getString("content"));
                                String workPraise = itemObject.getString("like");
                                cricle.setMemberName(itemObject.getString("name"));
                                cricle.setMemberImg(itemObject.getString("photo"));
                                cricle.setAddtime(itemObject.getString("write_time"));
                                cricle.setDay(Long.parseLong(itemObject.getString("write_time")));
                                String jsonImg = itemObject.getString("pic");
                                JSONArray imgList = new JSONArray(jsonImg);
                                ArrayList<String> imgs = new ArrayList<String>();
                                for (int k = 0; k < imgList.length(); k++) {
                                    JSONObject imgobject = (JSONObject) imgList.get(k);
                                    imgs.add(imgobject.getString("pictureurl"));
                                }
                                cricle.setWorkImgs(imgs);
                                if (workPraise != null && !workPraise.equals("null")) {
                                    JSONArray jsonWorkPraiseArray = new JSONArray(workPraise);
                                    ArrayList<LikeBean> workPraises = new ArrayList<>();
                                    for (int k = 0; k < jsonWorkPraiseArray.length(); k++) {
                                        JSONObject jsonPraise = jsonWorkPraiseArray.getJSONObject(k);
                                        LikeBean praise = new LikeBean();
                                        praise.setUserid(jsonPraise.getString("userid"));
                                        praise.setName(jsonPraise.getString("name"));
                                        workPraises.add(praise);
                                    }
                                    cricle.setWorkPraise(workPraises);
                                }
                                JSONArray commentArray = itemObject.optJSONArray("comment");
                                if (commentArray.length() > 0) {
                                    ArrayList<Comments> commentList = new ArrayList<>();
                                    for (int j = 0; j < commentArray.length(); j++) {
                                        JSONObject commentObject = commentArray.optJSONObject(j);
                                        Comments comments = new Comments();
                                        comments.setUserid(commentObject.optString("userid"));
                                        comments.setName(commentObject.optString("name"));
                                        comments.setAvatar(commentObject.optString("avatar"));
                                        comments.setComment_time(commentObject.optString("comment_time"));
                                        comments.setContent(commentObject.optString("content"));
                                        commentList.add(comments);
                                    }
                                    cricle.setComment(commentList);
                                }
                                CricleList.add(cricle);
                            }


                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }else{
                                Collections.sort(CricleList, new Comparator<ClassCricleInfo>() {
                                    @Override
                                    public int compare(ClassCricleInfo lhs, ClassCricleInfo rhs) {
                                        return (int) (Long.parseLong(rhs.getAddtime())-Long.parseLong(lhs.getAddtime()));
                                    }
                                });
                                ClassCricleInfo classCricleInfo = new ClassCricleInfo();
                                classCricleInfo.setAddtime(String.valueOf(new Date().getTime() / 1000 + 24 * 60 * 60));
                                if(Long.parseLong(classCricleInfo.getAddtime())-Long.parseLong(CricleList.get(0).getAddtime())>24 * 60 * 60*15){
                                    classCricleInfo.setId("today");
                                    CricleList.add(classCricleInfo);
                                }
                                Collections.sort(CricleList, new Comparator<ClassCricleInfo>() {
                                    @Override
                                    public int compare(ClassCricleInfo lhs, ClassCricleInfo rhs) {
                                        return (int) (Long.parseLong(rhs.getAddtime())-Long.parseLong(lhs.getAddtime()));
                                    }
                                });
                                mAdapter = new MyDiaryAdapter(CricleList,mContext,handler,commentView,"7");
                                lv.setAdapter(mAdapter);
                            }

                        }else{
                            lv.setAdapter(new NoFriendContentAdapter(mContext));
                            lv.setDivider(new ColorDrawable(Color.parseColor("#FFFFFF")));
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    dialog();
                                }
                            });
                            mAdapter = null;
                        }
                    } catch (JSONException e) {
                        LogUtils.d("weixiaotong", "JSONException" + e.getMessage());
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    /**
     * 催一催分享弹出框
     */
    private void dialog() {
        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);    // 系统默认Dialog没有输入框
        // 获取自定义的布局
        View alertDialogView = View.inflate(mContext, R.layout.confirm_weixin, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);
        TextView tv_queding = (TextView) alertDialogView.findViewById(R.id.queding);
        TextView tv_quxiao = (TextView) alertDialogView.findViewById(R.id.quxiao);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                tempDialog.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        tempDialog.show();
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = tempDialog.getWindow().getAttributes();
        layoutParams.width = width-100;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tempDialog.getWindow().setAttributes(layoutParams);
    }

    private void share() {
        WXTextObject textObject = new WXTextObject();
        textObject.text = "您的宝宝最近没有发表成长日记，快来智校园发送吧~";
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = "微校通家长端";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "weixin";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        WxtApplication wxtApplication = WxtApplication.getInstance();
        wxtApplication.api.sendReq(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_diary);
        mContext = this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        id = getIntent().getStringExtra("id");
        initView();
    }
    public void initView(){
        iv_nodata = (ImageView) findViewById(R.id.nodata);
        commentView = (LinearLayout) findViewById(R.id.edit_and_send);
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        lv_homework = (PullToRefreshListView)findViewById(R.id.lv_comprehensive);
        lv_homework.setPullLoadEnabled(true);
        lv_homework.setScrollLoadEnabled(false);
        lv_homework.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (NetUtil.isConnnected(mContext) == true) {
                    getAllInformation();

                } else {
                    ToastUtils.ToastShort(mContext, "暂无网络");
                }
                /**
                 * 过1秒结束下拉刷新
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv_homework.onPullDownRefreshComplete();
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
                        lv_homework.onPullUpRefreshComplete();
                    }
                }, 1000);
            }
        });
        lv = lv_homework.getRefreshableView();
        lv.setDividerHeight(0);
        viewH = LayoutInflater.from(FriendDiaryActivity.this).inflate(R.layout.diary_slide_pic,
                null);
        sliderLayout = (SliderLayout) viewH.findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.ll1);
        file_maps.put("Big Bang Theory", R.drawable.ll2);
        file_maps.put("House of Cards", R.drawable.ll4);
        file_maps.put("Game of Thrones", R.drawable.ll4);
        showViewPager(file_maps);
        lv.addHeaderView(viewH);// 幻灯片是添加到ListView的头部
    }

    private void getAllInformation() {
        new SpaceRequest(mContext,handler).getDiary(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }

    private void showViewPager(HashMap<String, Integer> file_maps) {
        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
