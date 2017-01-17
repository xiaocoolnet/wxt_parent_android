package cn.xiaocool.wxtparent.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.BabyDiaryAdapter;
import cn.xiaocool.wxtparent.adapter.FriendDiaryAdapter;
import cn.xiaocool.wxtparent.adapter.NoBabyContentAdapter;
import cn.xiaocool.wxtparent.adapter.NoDataAdapter;
import cn.xiaocool.wxtparent.bean.ClassCricleInfo;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.main.AddDiaryActivity;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by mac on 16/1/25.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,View.OnLayoutChangeListener {
    private PullToRefreshListView lv_homework;
    private String data = null;
    private ListView lv;
    private ArrayList<ClassCricleInfo> CricleList = new ArrayList<>();
    private ArrayList<ClassCricleInfo> friendDiary = new ArrayList<>();
    private BabyDiaryAdapter mAdapter;
    private FriendDiaryAdapter adapter;
    private Context mContext;
    private UserInfo userInfo;
    private LinearLayout commentView;
    private static final int HOMEWORK_PRAISE_KEY = 104;
    private static final int DEL_HOMEWORK_PRAISE_KEY = 105;
    private String TAG = "SpaceBabyAlbumActivity";
    private ArrayList<ArrayList<LikeBean>> likeBeanList = new ArrayList<>();
    private View viewH = null;
    private DisplayImageOptions options;
    private SliderLayout sliderLayout;
    private ImageView iv_add;
    private RadioButton[] mTabs;
    private View line_laoshi, line_haoyou;
    private int index = 1;
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
                            for (int i = items.length() - 1; i >= 0; i--) {
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
                            Collections.sort(CricleList, new Comparator<ClassCricleInfo>() {
                                @Override
                                public int compare(ClassCricleInfo lhs, ClassCricleInfo rhs) {
                                    return (int) (Long.parseLong(rhs.getAddtime())-Long.parseLong(lhs.getAddtime()));
                                }
                            });
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter = new BabyDiaryAdapter(CricleList, mContext, handler, commentView, "7");
                                lv.setAdapter(mAdapter);
                                adapter = null;

                            }

                        } else {
                            lv.setAdapter(new NoBabyContentAdapter(mContext));
                            mAdapter = null;
                            adapter = null;
                        }
                    } catch (JSONException e) {
                        LogUtils.d("weixiaotong", "JSONException" + e.getMessage());
                        e.printStackTrace();
                    }

                    break;
                case CommunalInterfaces.FRIENDDIARY:
                    JSONObject obj1 = (JSONObject) msg.obj;
                    try {
                        String state = obj1.getString("status");
                        if (state.equals(CommunalInterfaces._STATE)) {
                            friendDiary.clear();
                            JSONArray items = obj1.getJSONArray("data");
                            JSONObject itemObject;
                            for (int i = items.length() - 1; i >= 0; i--) {
                                if (!items.optJSONObject(i).getString("microblog_info").equals("[]")) {
                                    Log.e("friend", items.optJSONObject(i).getString("microblog_info"));
                                    for (int n = 0; n < items.optJSONObject(i).optJSONArray("microblog_info").length(); n++) {
                                        itemObject = items.optJSONObject(i).optJSONArray("microblog_info").optJSONObject(n);
                                        ClassCricleInfo cricle = new ClassCricleInfo();
                                        cricle.setId(itemObject.getString("mid"));
                                        cricle.setMatter(itemObject.getString("content"));
                                        String workPraise = itemObject.getString("like");
                                        cricle.setMemberName(items.optJSONObject(i).getString("name"));
                                        cricle.setMemberImg(items.optJSONObject(i).getString("photo"));
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
                                        friendDiary.add(cricle);
                                    }
                                }
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            } else {
                                if(friendDiary.size()==0){
                                    lv.setAdapter(new NoDataAdapter(mContext));
                                    mAdapter = null;
                                    adapter = null;
                                }else{
                                    Collections.sort(friendDiary, new Comparator<ClassCricleInfo>() {
                                        @Override
                                        public int compare(ClassCricleInfo lhs, ClassCricleInfo rhs) {
                                            return (int) (Long.parseLong(rhs.getAddtime())-Long.parseLong(lhs.getAddtime()));
                                        }
                                    });
                                    adapter = new FriendDiaryAdapter(friendDiary, mContext, handler, commentView, "7");
                                    lv.setAdapter(adapter);
                                    mAdapter = null;
                                }
                            }

                        }

                    } catch (JSONException e) {
                        LogUtils.d("weixiaotong", "JSONException" + e.getMessage());
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        userInfo = new UserInfo(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_diary, null);
        lv_homework = (PullToRefreshListView) view
                .findViewById(R.id.lv_comprehensive);
        lv_homework.setPullLoadEnabled(true);
        lv_homework.setScrollLoadEnabled(false);
        lv_homework.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (NetUtil.isConnnected(mContext) == true) {
                    Log.e("hello----", "刷新");
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
                        Log.e("hello", "run");
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
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.diary_slide_image,
                null);
        mTabs = new RadioButton[2];
        mTabs[0] = (RadioButton) viewH.findViewById(R.id.btn_laoshi);
        mTabs[1] = (RadioButton) viewH.findViewById(R.id.btn_haoyou);
        mTabs[0].setSelected(true);
        line_laoshi = viewH.findViewById(R.id.line_laoshi);
        line_haoyou = viewH.findViewById(R.id.line_haoyou);
        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
        iv_add = (ImageView) view.findViewById(R.id.image_add);
        iv_add.setOnClickListener(this);
        sliderLayout = (SliderLayout) viewH.findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.ll1);
        file_maps.put("Big Bang Theory", R.drawable.ll2);
        file_maps.put("House of Cards", R.drawable.ll4);
        file_maps.put("Game of Thrones", R.drawable.ll4);
        showViewPager(file_maps);
        lv.addHeaderView(viewH);// 幻灯片是添加到ListView的头部
        activityRootView = view.findViewById(R.id.layout_root);
        return view;
    }

    //轮播图片

    private void getAllInformation() {
        userInfo.readData(mContext);
        if (index == 1) {
            Log.e("hello", "type1");
            new SpaceRequest(mContext, handler).getDiary(userInfo.getChildId());
        } else if (index == 2) {
            new SpaceRequest(mContext, handler).getFriendDiary();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityRootView.addOnLayoutChangeListener(this);
        refresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_laoshi:
                index = 1;
                mTabs[0].setTextColor(getResources().getColor(R.color.title_bg_color));
                mTabs[1].setTextColor(getResources().getColor(R.color.black));
                line_laoshi.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                line_haoyou.setBackgroundColor(getResources().getColor(R.color.zsq_bg_color));
                getAllInformation();
                break;
            case R.id.btn_haoyou:
                index = 2;
                mTabs[0].setTextColor(getResources().getColor(R.color.black));
                mTabs[1].setTextColor(getResources().getColor(R.color.title_bg_color));
                line_laoshi.setBackgroundColor(getResources().getColor(R.color.zsq_bg_color));
                line_haoyou.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                getAllInformation();
                break;
            case R.id.image_add:
                IntentUtils.getIntent(getActivity(), AddDiaryActivity.class);
                break;
        }
    }

    public void refresh() {
        // TODO Auto-generated method stub
        getAllInformation();
    }

    private void showViewPager(HashMap<String, Integer> file_maps) {
        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取屏幕高度
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
    }

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);


        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

            if (mAdapter!=null&&mAdapter.commentPopupWindow!=null&&mAdapter.commentPopupWindow.isShowing()){
                mAdapter.commentPopupWindow.dismiss();
            }
            if (adapter!=null&&adapter.commentPopupWindow!=null&&adapter.commentPopupWindow.isShowing()){
                adapter.commentPopupWindow.dismiss();
            }
        }

    }
}
