package cn.xiaocool.wxtparent.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.HashMap;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.CircleListAdapter;
import cn.xiaocool.wxtparent.adapter.NoDataAdapter;
import cn.xiaocool.wxtparent.bean.ClassCricleInfo;
import cn.xiaocool.wxtparent.bean.Comments;
import cn.xiaocool.wxtparent.bean.LikeBean;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.main.AddTrendActivity;
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
public class FindFragment extends Fragment implements OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private PullToRefreshListView lv_homework;
    private String data = null;
    private LinearLayout commentView;
    private ListView lv;
    private ArrayList<ClassCricleInfo> CricleList = new ArrayList<>();
    private CircleListAdapter mAdapter;
    private Context mContext;
    private UserInfo userInfo;
    private static final int HOMEWORK_PRAISE_KEY = 104;
    private static final int DEL_HOMEWORK_PRAISE_KEY = 105;
    private static final int GET_CIRCLE_LIST_KEY = 2;
    private String TAG = "SpaceBabyAlbumActivity";
    private ArrayList<ArrayList<LikeBean>> likeBeanList = new ArrayList<>();
    private View viewH = null;
    private DisplayImageOptions options;
    private SliderLayout sliderLayout;
    private ImageView iv_add;
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
//                                ToastUtils.ToastShort(mContext, result);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GET_CIRCLE_LIST_KEY:
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
                                if(CricleList.size()==0){
                                    lv.setAdapter(new NoDataAdapter(mContext));
                                    lv.setDivider(new ColorDrawable(Color.parseColor("#FFFFFF")));
                                    mAdapter = null;
                                }else{
                                    mAdapter = new CircleListAdapter(CricleList,mContext,handler,commentView,"1");
                                    lv.setAdapter(mAdapter);
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

    //1:页面的创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
    }

    //2:加载布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.information_listview, null);
        lv_homework = (PullToRefreshListView) view
                .findViewById(R.id.lv_comprehensive);
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
                        new SpaceRequest(mContext, handler).getCircleList(userInfo.getChildId(), String.valueOf(CricleList.size()), "1", "1", "1", GET_CIRCLE_LIST_KEY);
                        lv_homework.onPullUpRefreshComplete();
                    }
                }, 1000);
            }
        });
        lv = lv_homework.getRefreshableView();
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.web_slide_image,
                null);
        iv_add = (ImageView) view.findViewById(R.id.image_add);
        iv_add.setOnClickListener(this);
        sliderLayout = (SliderLayout) viewH.findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.ll1);
        file_maps.put("Big Bang Theory", R.drawable.ll2);
        file_maps.put("House of Cards", R.drawable.ll4);
        file_maps.put("Game of Thrones", R.drawable.ll4);
        showViewPager(file_maps);
        lv.addHeaderView(viewH);// 幻灯片是添加到ListView的头部
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getAllInformation();
    }

    //轮播图片
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

    private void getAllInformation() {
        new SpaceRequest(mContext, handler).getCircleList(userInfo.getChildId(), null, "1", "1", "1", GET_CIRCLE_LIST_KEY);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_add:
                IntentUtils.getIntent(getActivity(), AddTrendActivity.class);
                break;
        }
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
    public void onSliderClick(BaseSliderView slider) {

    }

}
