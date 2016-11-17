package cn.xiaocool.wxtparent.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.videogo.openapi.EZOpenSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ParentInfo;
import cn.xiaocool.wxtparent.camera.DeviceInfoListActivity;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.main.AddChildActivity;
import cn.xiaocool.wxtparent.main.BabyInformationActivity;
import cn.xiaocool.wxtparent.main.CoursewareActivity;
import cn.xiaocool.wxtparent.main.ParentDetailActivity;
import cn.xiaocool.wxtparent.main.SpaceBabyAlbumActivity;
import cn.xiaocool.wxtparent.main.SpaceClickAnnouActivity;
import cn.xiaocool.wxtparent.main.SpaceClickAttendanceActivity;
import cn.xiaocool.wxtparent.main.SpaceClickClassActivity;
import cn.xiaocool.wxtparent.main.SpaceClickClassEventActivity;
import cn.xiaocool.wxtparent.main.SpaceClickConfimActivity;
import cn.xiaocool.wxtparent.main.SpaceClickHeightActivity;
import cn.xiaocool.wxtparent.main.SpaceClickLeaveActivity;
import cn.xiaocool.wxtparent.main.SpaceClickNewsActivity;
import cn.xiaocool.wxtparent.main.SpaceClickParentWarnActivity;
import cn.xiaocool.wxtparent.main.SpaceClickRecipesActivity;
import cn.xiaocool.wxtparent.main.SpaceClickTeacherReviewActivity;
import cn.xiaocool.wxtparent.main.SpaceClickWeightActivity;
import cn.xiaocool.wxtparent.main.SpaceFriendsActivity;
import cn.xiaocool.wxtparent.main.SpeaceSchoolWebActivity;
import cn.xiaocool.wxtparent.net.request.MeRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.utils.DateUtils;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;


/**
 * Created by mac on 16/1/25.
 */
public class SpaceFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout building, people, nearby;
    private TextView tv_red;
    //上边八个按钮
    private RelativeLayout news;
    private RelativeLayout baby_album;
    private RelativeLayout confirm;
    private RelativeLayout leave;
    private RelativeLayout recipes;
    private ImageView parent1,parent2,parent3,parent4,parent5;
    private ImageView[] imageViews;
    private ArrayList<ParentInfo> arrayList;
    private int length;
    private Button btn_daka;
    private boolean isRecorded_Weigth,isRecorded_Height;
    private TextView tv1,tv2,tv3,tv4,tv5;
    private TextView[] textViews;
    //定义到校时间、体温，离校时间、体温
    private TextView tv_arrive_time, tv_arrive_tem, tv_leave_time, tv_leave_tem;
    private DisplayImageOptions option;
    //记录体重身高按钮
    private Button btn_record_weight, btn_recoed_height;
    private static final String TAG = "JPush";
    private static final String JPUSHMESSAGE = "JPUSHMESSAGE";
    private static final String JPUSHHOMEWORK = "JPUSHHOMEWORK";
    private static final String JPUSHTRUST = "JPUSHTRUST";
    private static final String JPUSHNOTICE = "JPUSHNOTICE";
    private static final String JPUSHDAIJIE = "JPUSHDAIJIE";
    private static final String JPUSHLEAVE = "JPUSHLEAVE";
    private static final String JPUSHACTIVITY = "JPUSHACTIVITY";
    private static final String JPUSHCOMMENT = "JPUSHCOMMENT";

    private RelativeLayout rl_xiaoxi;
    private RelativeLayout rl_daijie;
    private RelativeLayout rl_qingjia,rl_dingzhu,rl_gonggao,rl_huodong,rl_dianping;
    private BadgeView bv_daijie,bv_qingjia,bv_dingzhu,bv_gonggao,bv_huodong,bv_dianping,bv_xiaoxi;

    //下边八个按钮
    private RelativeLayout baby_courseware, btnClass, teacherReview, babyTeacher, announcement, classevents, space_park,space_files;
    //space_park是原来的”宝宝乐园“，现改为校网，id等不变；

    private FragmentActivity mContext;
    private TextView tv_stature, tv_weight;//身高体重
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.BABY_INFO:
                    JSONObject object = (JSONObject) msg.obj;
                    try {
                        String status = object.getString("status");
                        if (status.equals("success")) {
                            JSONArray data = (JSONArray) object.get("data");
                            JSONObject babyObject = (JSONObject) data.get(0);
                            String babyName = (String) babyObject.get("studentname");
                            String babySchool = (String) babyObject.get("school_name");
                            String sex = (String) babyObject.get("sex");
                            if(sex.equals("0")){
                                baby_sex.setImageResource(R.drawable.icon_woman);
                            }else if(sex.equals("1")){
                                baby_sex.setImageResource(R.drawable.icon_man);
                            }
                            ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST + babyObject.optString("studentavatar"), baby_imgae);
                            baby_name.setText(babyName);
                            baby_school_name.setText(babySchool);
                            baby_class.setText(babyObject.optJSONArray("classlist").optJSONObject(0).optString("classname"));
                        } else {
                            baby_name.setText("无信息");
                            baby_school_name.setText("无信息");
                            baby_class.setText("无信息");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
                case CommunalInterfaces.GET_STATURE_WEIGHT:
                    JSONObject obj_stature_weight = (JSONObject) msg.obj;
                    try {
                        String statusssss = obj_stature_weight.getString("status");
                        Log.e("data is ", obj_stature_weight.getString("data").toString());
                        Log.e("status is is is this", statusssss);
                        if (statusssss.equals("success")) {

                            String data = obj_stature_weight.getString("data");
                            JSONObject stature_weight = new JSONObject(data);
                            String stature = stature_weight.getString("new_stature");
                            String weight = stature_weight.getString("new_weight");
                            tv_stature.setText(stature);
                            tv_weight.setText(weight);
                        } else {
                            Log.e("errorerror", "error");
                            tv_stature.setText("00");
                            tv_weight.setText("00");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //获取到校时间和体温
                    break;
                case CommunalInterfaces.GET_TIME_TEM:
                    JSONObject obj_time_tem = (JSONObject) msg.obj;
                    String status = obj_time_tem.optString("status");
                    JSONArray data = obj_time_tem.optJSONArray("data");
                    if (status.equals("success")) {
                        JSONObject time_tem = data.optJSONObject(0);
                        String arriveTime = time_tem.optString("arrivetime");
                        String arriveTemperature = time_tem.optString("arrivetemperature");
                        String leaveTime = time_tem.optString("leavetime");
                        String leaveTemperature = time_tem.optString("learntemperature");
                        Log.e("info-----",arriveTime+"-"+arriveTemperature+"-"+leaveTime+"-"+leaveTemperature);
                        if (arriveTime.equals("null")||arriveTime.equals("")) {
                            tv_arrive_time.setText("");
                        }else {
                            tv_arrive_time.setText(DateUtils.getDate(arriveTime));
                        }
                        if (arriveTemperature.equals("null")||arriveTemperature.equals("")) {
                            tv_arrive_tem.setText("");
                        }else {
                            tv_arrive_tem.setText("");
                        }
                        if (leaveTime.equals("null")||leaveTime.equals("")) {
                            tv_leave_time.setText("");
                        }else {
                            tv_leave_time.setText(DateUtils.getDate(leaveTime));
                        }
                        if (leaveTemperature.equals("null")||leaveTemperature.equals("")) {
                            tv_leave_tem.setText("");
                        }else {
                            tv_leave_tem.setText("");
                        }
                        String weight = time_tem.optString("weight");
                        String stature = time_tem.optString("stature");
                        if (weight.equals("null")) {
                            tv_weight.setText("");
                        }else {
                            tv_weight.setText(weight);
                        }
                        if (stature.equals("null")) {
                            tv_stature.setText("");
                        }else {
                            tv_stature.setText(stature);
                        }
                        Log.e("weight",weight);
                        Log.e("stature---",stature);
                        if(weight.length()==0||weight==null||weight.equals("null")||weight.isEmpty()){
                            isRecorded_Weigth = false;
                        }else{
                            isRecorded_Weigth = true;
                        }
                        if (stature.length()==0||stature==null||stature.equals("null")||stature.isEmpty()){
                            isRecorded_Height = false;
                        }else{
                            isRecorded_Height = true;
                        }
                    }
                    break;
                case CommunalInterfaces.GETBABYPARENT:
                    JSONObject parent = (JSONObject) msg.obj;
                    Log.e("hello", parent.toString());
                    if (parent.optString("status").equals("success")) {
                        JSONArray jsonArray = parent.optJSONArray("data");
                        JSONObject babyObject;
                        arrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            babyObject = jsonArray.optJSONObject(i);
                            ParentInfo parentInfo = new ParentInfo();
                            parentInfo.setId(babyObject.optString("userid"));
                            parentInfo.setAppellation(babyObject.optString("appellation"));
                            parentInfo.setType(babyObject.optString("type"));
                            parentInfo.setName(babyObject.optJSONArray("parent_info").optJSONObject(0).optString("name"));
                            parentInfo.setPhone(babyObject.optJSONArray("parent_info").optJSONObject(0).optString("phone"));
                            parentInfo.setPhoto(babyObject.optJSONArray("parent_info").optJSONObject(0).optString("photo"));
                            arrayList.add(parentInfo);
                        }
                        showParent();
                    }
                    break;
            }
        }
    };

    private void showParent() {
        for(int i=0;i<arrayList.size();i++){
            ImageLoader.getInstance().displayImage(NetBaseConstant.NET_CIRCLEPIC_HOST+arrayList.get(i).getPhoto(),imageViews[i],option);
            textViews[i].setVisibility(View.VISIBLE);
            textViews[i].setText(arrayList.get(i).getAppellation());
            final int j=i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ParentDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("parent",arrayList.get(j));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
        if(arrayList.size()<5) {
            imageViews[arrayList.size()].setVisibility(View.VISIBLE);
            imageViews[arrayList.size()].setImageResource(R.drawable.type_select_btn_nor);
            textViews[arrayList.size()].setVisibility(View.GONE);
            imageViews[arrayList.size()].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.getIntent(mContext, AddChildActivity.class);
                }
            });
        }
        for (int i=1;i<5-arrayList.size();i++){
            imageViews[i+arrayList.size()].setVisibility(View.GONE);
            textViews[i+arrayList.size()].setVisibility(View.GONE);
        }
    }

    private LinearLayout baby_information;
    private ImageView baby_imgae;
    private TextView baby_class;
    private TextView baby_name;
    private ImageView baby_sex;
    private TextView baby_school_name;
    private RelativeLayout space_friends;
    private RelativeLayout space_parents,space_check;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_space, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        option = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_avatar).showImageOnFail(R.drawable.default_avatar)
                .cacheInMemory(true).cacheOnDisc(true).build();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        getActivity().registerReceiver(new Receiver(),filter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
        Log.e("onActivityCreated enter", "ok");
    }

    private void initView() {
        //待接
        rl_daijie = (RelativeLayout) getView().findViewById(R.id.rl_daijie);
        bv_daijie = new BadgeView(mContext);
        bv_daijie.setTargetView(rl_daijie);
        //请假
        rl_qingjia = (RelativeLayout) getView().findViewById(R.id.rl_qingjia);
        bv_qingjia = new BadgeView(mContext);
        bv_qingjia.setTargetView(rl_qingjia);
        //消息
        rl_xiaoxi = (RelativeLayout) getView().findViewById(R.id.rl_xiaoxi);
        bv_xiaoxi = new BadgeView(mContext);
        bv_xiaoxi.setTargetView(rl_xiaoxi);

        //叮嘱
        rl_dingzhu = (RelativeLayout) getView().findViewById(R.id.rl_dingzhu);
        bv_dingzhu = new BadgeView(mContext);
        bv_dingzhu.setTargetView(rl_dingzhu);
        //公告
        rl_gonggao = (RelativeLayout) getView().findViewById(R.id.rl_gonggao);
        bv_gonggao = new BadgeView(mContext);
        bv_gonggao.setTargetView(rl_gonggao);
        //活动
        rl_huodong = (RelativeLayout) getView().findViewById(R.id.rl_huodong);
        bv_huodong = new BadgeView(mContext);
        bv_huodong.setTargetView(rl_huodong);
        //点评
        rl_dianping = (RelativeLayout) getView().findViewById(R.id.rl_dianping);
        bv_dianping = new BadgeView(mContext);
        bv_dianping.setTargetView(rl_dianping);
        arrayList = new ArrayList<>();
        //五个家长
        parent1 = (ImageView) getView().findViewById(R.id.parent1);
        parent2 = (ImageView) getView().findViewById(R.id.parent2);
        parent3 = (ImageView) getView().findViewById(R.id.parent3);
        parent4 = (ImageView) getView().findViewById(R.id.parent4);
        parent5 = (ImageView) getView().findViewById(R.id.parent5);
        tv1 = (TextView) getView().findViewById(R.id.parent_name1);
        tv2 = (TextView) getView().findViewById(R.id.parent_name2);
        tv3 = (TextView) getView().findViewById(R.id.parent_name3);
        tv4 = (TextView) getView().findViewById(R.id.parent_name4);
        tv5 = (TextView) getView().findViewById(R.id.parent_name5);
        textViews = new TextView[]{tv1,tv2,tv3,tv4,tv5};
        imageViews = new ImageView[]{parent1,parent2,parent3,parent4,parent5};
        //宝宝资料
        baby_information = (LinearLayout) getView().findViewById(R.id.baby_information);
        baby_information.setOnClickListener(this);
        //宝宝头像
        baby_imgae = (ImageView) getView().findViewById(R.id.baby_img);
        baby_imgae.setOnClickListener(this);
        //宝宝班级
        baby_class = (TextView) getView().findViewById(R.id.baby_class);
        //宝宝名字
        baby_name = (TextView) getView().findViewById(R.id.baby_name);
        //性别
        baby_sex = (ImageView) getView().findViewById(R.id.baby_sex);
        //宝宝学校
        baby_school_name = (TextView) getView().findViewById(R.id.baby_school_name);
        //上面八个按钮
        baby_album = (RelativeLayout) getView().findViewById(R.id.space_album);
        baby_album.setOnClickListener(this);
        confirm = (RelativeLayout) getView().findViewById(R.id.space_confirm);
        confirm.setOnClickListener(this);
        //在线请假
        leave = (RelativeLayout) getView().findViewById(R.id.space_leave);
        leave.setOnClickListener(this);
        btn_daka = (Button) getView().findViewById(R.id.main_daka);
        btn_daka.setOnClickListener(this);
        //家长叮嘱
        space_parents = (RelativeLayout) getView().findViewById(R.id.space_parents);
        space_parents.setOnClickListener(this);

        recipes = (RelativeLayout) getView().findViewById(R.id.space_recipes);
        recipes.setOnClickListener(this);
        news = (RelativeLayout) getView().findViewById(R.id.space_news);
        news.setOnClickListener(this);

        btn_record_weight = (Button) getView().findViewById(R.id.btn_record_weight);
        btn_record_weight.setOnClickListener(this);
        btn_recoed_height = (Button) getView().findViewById(R.id.btn_record_height);
        btn_recoed_height.setOnClickListener(this);

        //初始化到校时间、体温，离校时间、体温
        tv_arrive_time = (TextView) getView().findViewById(R.id.arrive_time);
        tv_arrive_tem = (TextView) getView().findViewById(R.id.arrive_tem);
        tv_leave_time = (TextView) getView().findViewById(R.id.leave_time);
        tv_leave_tem = (TextView) getView().findViewById(R.id.leave_tem);

        //初始化身高体重的TV
        tv_stature = (TextView) getView().findViewById(R.id.space_stature);
        tv_weight = (TextView) getView().findViewById(R.id.space_weight);


        //下面八个按钮
        baby_courseware = (RelativeLayout) getView().findViewById(R.id.space_courseware);
        baby_courseware.setOnClickListener(this);
        btnClass = (RelativeLayout) getView().findViewById(R.id.space_class);
        btnClass.setOnClickListener(this);
        teacherReview = (RelativeLayout) getView().findViewById(R.id.space_review);
        teacherReview.setOnClickListener(this);
        space_check = (RelativeLayout) getView().findViewById(R.id.space_check);
        space_check.setOnClickListener(this);
        //babyTeacher = (RelativeLayout) getView().findViewById(R.id.space_teacher);
        //babyTeacher.setOnClickListener(this);
        announcement = (RelativeLayout) getView().findViewById(R.id.space_announcement);
        announcement.setOnClickListener(this);
        //宝宝好友
        space_friends = (RelativeLayout) getView().findViewById(R.id.space_friends);
        space_friends.setOnClickListener(this);
        classevents = (RelativeLayout) getView().findViewById(R.id.space_activity);
        classevents.setOnClickListener(this);


        //校网   （原来的宝宝乐园）
        space_park = (RelativeLayout) getView().findViewById(R.id.space_park);
        space_park.setOnClickListener(this);

        space_files = (RelativeLayout) getView().findViewById(R.id.space_files);
        space_files.setOnClickListener(this);
        getView().findViewById(R.id.main_baobaoshipin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EZOpenSDK.getInstance().openLoginPage();
                Intent intent = new Intent(mContext, DeviceInfoListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initBabyInfo() {
        new SpaceRequest(mContext, handler).BabyInfo();
    }

    private void init_time_tem() {
        new SpaceRequest(mContext, handler).ArriveTimeTem(String.valueOf(DateUtils.lastDayWholePointDate(new Date()).getTime() / 1000));
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart enter", "ok");
    }

    @Override
    public void onResume() {
        super.onResume();
        //更改数据后刷新数据
        setRedPoint();
        Log.e("onResume enter", "ok");
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause enter", "ok");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop enter", "ok");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView is ", "ok");
    }


    /**
     * 设置小红点
     */
    public  void setRedPoint(){
        //从本地取出各个小红点的个数
        int msgNum = (int) SPUtils.get(mContext, JPUSHMESSAGE, 0);
        int hmkNum = (int) SPUtils.get(mContext, JPUSHHOMEWORK, 0);
        int trust = (int) SPUtils.get(mContext,JPUSHTRUST,0);
        int notice = (int) SPUtils.get(mContext,JPUSHNOTICE,0);
        int daijieNum = (int) SPUtils.get(mContext,JPUSHDAIJIE,0);
        int leaveNum = (int) SPUtils.get(mContext,JPUSHLEAVE,0);
        int activityNum = (int) SPUtils.get(mContext, JPUSHACTIVITY, 0);
        int commentNum = (int) SPUtils.get(mContext, JPUSHCOMMENT, 0);
        setBadgeView(daijieNum,bv_daijie);
        setBadgeView(leaveNum,bv_qingjia);
        setBadgeView(trust,bv_dingzhu);
        setBadgeView(notice,bv_gonggao);
        setBadgeView(activityNum,bv_huodong);
        setBadgeView(commentNum,bv_dianping);
        setBadgeView(msgNum+hmkNum,bv_xiaoxi);
        /*setBadgeView(trust,newsFragment.trust);
        setBadgeView(notice,newsFragment.notice);
        setBadgeView(daijieNum,functionFragment.btn_daijie);
        setBadgeView(leaveNum,functionFragment.btn_leave);*/

    }


    /**
     * 设置背景
     * @param message
     * @param message1
     */
    private void setBadgeView(int message, BadgeView message1) {
        if (message1==null)return;
        if (message==0){
            message1.setVisibility(View.GONE);
        }else {
            message1.setVisibility(View.VISIBLE);
            message1.setBadgeCount(message);
        }
    }


    /**
     * 接受推送通知并通知页面添加小红点
     */
    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Recevier1", "接收到:");
            setRedPoint();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //宝宝信息
            case R.id.baby_img:
                IntentUtils.getIntent(mContext, BabyInformationActivity.class);
                break;
            //消息中心
            case R.id.space_news:
                IntentUtils.getIntent(mContext, SpaceClickNewsActivity.class);
                break;
            //宝宝相册
            case R.id.space_album:
                IntentUtils.getIntent(mContext, SpaceBabyAlbumActivity.class);
                break;
            //待接确认
            case R.id.space_confirm:
                IntentUtils.getIntent(mContext, SpaceClickConfimActivity.class);
                break;
            //在线请假
            case R.id.space_leave:
                IntentUtils.getIntent(mContext, SpaceClickLeaveActivity.class);
                break;
            //家长叮嘱
            case R.id.space_parents:
                IntentUtils.getIntent(mContext, SpaceClickParentWarnActivity.class);
                break;
            //今日食谱
            case R.id.space_recipes:
                IntentUtils.getIntent(mContext, SpaceClickRecipesActivity.class);
                break;
            //考勤
            case R.id.space_check:
                IntentUtils.getIntent(mContext, SpaceClickAttendanceActivity.class);
                break;
            //宝宝课件
            case R.id.space_courseware:
                IntentUtils.getIntent(mContext, CoursewareActivity.class);
                break;
//            //育儿知识
//            case R.id.space_child_rearing:
//                IntentUtils.getIntent(mContext, WebClickRearingChildActivity.class);
//                break;
            //班级课程
            case R.id.space_class:
                IntentUtils.getIntent(mContext, SpaceClickClassActivity.class);
                break;
            //老师点评
            case R.id.space_review:
                IntentUtils.getIntent(mContext, SpaceClickTeacherReviewActivity.class);
                break;
            //宝宝教师
           /* case R.id.space_teacher:
                IntentUtils.getIntent(mContext, SpaceClickBabyTeacherActivity.class);
                break;*/
            //体重
            case R.id.btn_record_weight:
                int type_weight;
                if(isRecorded_Weigth){
                    type_weight=1;
                }else{
                    type_weight=0;
                }
                Log.e("type1-------",type_weight+"");
                Intent intent1 = new Intent(mContext,SpaceClickWeightActivity.class);
                intent1.putExtra("type_weight",type_weight);
                startActivity(intent1);
                break;
            //身高
            case R.id.btn_record_height:
                int type_height;
                if(isRecorded_Height){
                    type_height=1;
                }else{
                    type_height=0;
                }
                Log.e("type2-------", type_height + "");
                Intent intent2 = new Intent(mContext,SpaceClickHeightActivity.class);
                intent2.putExtra("type_height", type_height);
                startActivity(intent2);
                break;
            //通知公告
            case R.id.space_announcement:
                IntentUtils.getIntent(mContext, SpaceClickAnnouActivity.class);
                break;
            //班级活动
            case R.id.space_activity:
                IntentUtils.getIntent(mContext, SpaceClickClassEventActivity.class);
                break;
//            //快乐学堂
//            case R.id.space_happy_school_ll:
//                IntentUtils.getIntent(mContext, SpaceHappySchoolActivity.class);
//                break;
            //宝宝好友
            case R.id.space_friends:
                IntentUtils.getIntent(mContext, SpaceFriendsActivity.class);
                break;
            //新添加的校网的 activity (把原来的校网fragment中的东西放到这个新的宝宝乐园中)
            case R.id.space_park:
                IntentUtils.getIntent(mContext, SpeaceSchoolWebActivity.class);
                break;
            case R.id.main_daka:
                IntentUtils.getIntent(mContext, SpaceClickAttendanceActivity.class);
                break;
            //成长档案
            case R.id.space_files:
                ToastUtils.ToastShort(mContext,"该功能尚未上线！");
                break;
            default:
                break;
        }
    }

    public void refresh() {
        initBabyInfo();
        new MeRequest(mContext, handler).MyParentAll();
        init_time_tem();
    }
}