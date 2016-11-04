package cn.xiaocool.wxtparent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.FindListAdapter;
import cn.xiaocool.wxtparent.bean.find.IndexNewsListInfo;
import cn.xiaocool.wxtparent.bean.find.NewsImgInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.HttpTool;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.SaveSPUtils;
import cn.xiaocool.wxtparent.utils.find.ACacheUtils;
//import cn.xiaocool.wxtparent.ui.list.IPullToRefresh;
/**
 * Created by mac on 16/1/31.
 */
public class FindNewsFragMent extends Fragment {
    private Activity activity;
    private PullToRefreshListView lv_comprehensive;
    /* 整个资讯 */
    private ListView lv;
    private FindListAdapter mAdapter;
    private TextView detail_loading;
    /* 用以发送消息到handler 告知fragment需要切换 */
    private final static int SET_NEWSLIST = 0;
    /**
     * ‘推荐’推送的资讯
     */
    private static ArrayList<IndexNewsListInfo> indexPushNewsList;
    /**
     * 非 ‘推荐’推送的资讯
     */
    private static ArrayList<IndexNewsListInfo> newIndexPushNewsList;
    /**
     * 普通资讯
     */
    private static ArrayList<IndexNewsListInfo> indexNewsList;
    /**
     * 幻灯片资讯
     */
    private static ArrayList<IndexNewsListInfo> viewPagerList;
    /**
     * 把普通的、推送的合在一起 adapter显示的最终集合
     */
    private ArrayList<IndexNewsListInfo> allList;

    private ArrayList<View> dots = new ArrayList<View>();// 点
    /* 幻灯片标题 */
    private TextView title;
    private ViewPager viewPager;// 用于显示图片
    private MyPagerAdapter myPagerAdapter;// viewPager的适配器
    private LinearLayout layout_view_pager;
    private int currPage = 0;// 当前显示的页
    private int oldPage = 0;// 上一次显示的页
    /* 上拉加载的从第几个数开始加载 */
    private int start_id = 0;
    private int pushStartPage = 0;
    private Context mContext;
    String text, cityId;
//    int channel_id;
    private View viewH = null;
    private View views = null;
    /* 幻灯片*viewpager要显示的图片的控件集合 具体几个由获取到的幻灯片数据长度决定 */
    final ArrayList<ImageView> imageList = new ArrayList<ImageView>();
    private SharedPreferences sp;
    /* 幻灯片的子view ID 实现点击事件使用 */
    private static int ID = 220;

    @SuppressWarnings("static-access")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
//        Bundle args = getArguments();
//        text = args != null ? args.getString("text") : "推荐";
//		/* 栏目ID */
//        channel_id = args != null ? args.getInt("id", 211) : 211;
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
		/* 城市ID */
        cityId = sp.getString("cityId", "");
//        LogUtils.e("channel_id=传参", channel_id + "");
//        LogUtils.e("text=传参", text);
//        LogUtils.e("cityId=传参", cityId + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.find_listview, null);
        detail_loading = (TextView) view.findViewById(R.id.detail_loading);
        lv_comprehensive = (PullToRefreshListView) view
                .findViewById(R.id.lv_comprehensive);
        lv_comprehensive.setPullLoadEnabled(true);
        lv_comprehensive.setScrollLoadEnabled(false);
        lv = lv_comprehensive.getRefreshableView();
        viewPagerList = new ArrayList<IndexNewsListInfo>();
        indexPushNewsList = new ArrayList<IndexNewsListInfo>();
        newIndexPushNewsList = new ArrayList<IndexNewsListInfo>();
        indexNewsList = new ArrayList<IndexNewsListInfo>();
        allList = new ArrayList<IndexNewsListInfo>();
        // 幻灯片整体
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.view_pager,
                null);
        // 圆点部分
        layout_view_pager = (LinearLayout) viewH
                .findViewById(R.id.layout_view_pager);
        // 图片的标题
        title = (TextView) viewH.findViewById(R.id.title);
        // 显示图片的VIew
        viewPager = (ViewPager) viewH.findViewById(R.id.vp);
        lv.addHeaderView(viewH);// 幻灯片是添加到ListView的头部
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    /** 此方法意思为fragment是否可见 ,可见时候加载数据 */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {// fragment可见时加载数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.obtainMessage(SET_NEWSLIST).sendToTarget();
                }
            }).start();
        } else {
            // fragment不可见时不执行操作

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 规则说明： 当栏目等于‘推荐’的时候 只有幻灯片和推荐的资讯 没有普通 否则：全部都有（幻灯片、推送[非‘推荐’栏目的]、普通资讯）
     *
     * @param startPage
     * @param indexSlideNewsId
     */
    private void getAllInformation(String pushStartPage, String startPage,
                                   String indexSlideNewsId, String indexPushNewsId, String indexNewsId) {
//        if (channel_id == 211) {
			/* 幻灯片 */
//            new SpeciaColumnRequest(activity, handler).getIndexSlideNewsList(
//                    "", cityId, indexSlideNewsId);
//			/* ‘推荐’推送 */
//            new SpeciaColumnRequest(activity, handler).getIndexPushNewsList("",
//                    cityId, pushStartPage, "10");
//        } else {
			/* 幻灯片 */
//            new SpeciaColumnRequest(activity, handler).getIndexSlideNewsList(""
//                    + channel_id, cityId, indexSlideNewsId);
//			/* 非‘推荐’推送 */
//            new SpeciaColumnRequest(activity, handler).getNewIndexPushNewsList(
//                    "" + channel_id, startPage, indexPushNewsId);
//			/* 普通 */
//            new SpeciaColumnRequest(activity, handler).getIndexNewsList(""
//                    + channel_id, "", startPage, "10", indexNewsId);
//        }
    }

    /**
     * 获取 网络图片 在viewpager上显示
     *
     * @param viewPagerList
     *            获取的网络数据
     */
    private void getViewPager(final ArrayList<IndexNewsListInfo> viewPagerList) {
        imageList.clear();
        dots.clear();
        for (int i = 0; i < viewPagerList.size(); i++) {
            ImageView image = new ImageView(activity);
            image.setId(ID + i);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 当返回的数据为null或是 " "时候 给viewpager里的 imageView 设置一张默认图片
            if (viewPagerList.size() > 0) {
                if (HttpTool.isConnnected(mContext) == true) {
                    String imgUrl = NetBaseConstant.NET_HOST
                            + viewPagerList.get(i).getNewsImg().get(0)
                            .getPath();
                    ImageLoader.getInstance().displayImage(imgUrl, image);
                } else {
                    image.setImageResource(R.drawable.article_img);
                }
            } else {
                image.setImageResource(R.drawable.article_img);
            }
            imageList.add(image);
            imageList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < imageList.size(); j++) {
                        // 把ImageView的对应ID 产生的 点击事件 Id 值 匹配
                        if (v.getId() == imageList.get(j).getId()) {
                            int newsPicState = Integer.parseInt(viewPagerList
                                    .get(j).getNewsPicState());
                            getCleckToClass(viewPagerList, j, newsPicState);
                        }
                    }
                }

                /**
                 * 点击当前的这张图片分别对应的跳转 资讯详情（0:单图文 1：多图文 2：视频）
                 */
                private void getCleckToClass(
                        final ArrayList<IndexNewsListInfo> viewPagerList,
                        int j, int newsPicState) {
                    switch (newsPicState) {
                        case 0:
                            /**
                             * viewPagerList:传进来的数据 j：代表当前的下标值 article：
                             * bundle传过去的唯一key值 以下相同
                             */
//                            getBundle(viewPagerList, j, "article",
//                                    ShareActivity.class, "普通资讯");
                            break;
                        case 1:
//                            getBundle(viewPagerList, j, "picture",
//                                    ImagesInformationItemActivity.class, "多图文");
                            break;
                        case 2:
//                            getBundle(viewPagerList, j, "video",
//                                    InformationRecommendVideoActivity.class, "视频");
                            break;
                    }
                }
            });
        }

        // 获取显示的点（即文字下方的点，表示当前是第几张）
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
        for (int w = 0; w < viewPagerList.size(); w++) {
            views = new View(getActivity());
            params.setMargins(5, 0, 5, 0);
            dots.add(views);
            layout_view_pager.addView(views, params);
        }
        for (int p = 0; p < dots.size(); p++) {
            if (p == 0) {
                dots.get(0).setBackgroundResource(R.drawable.viewpager_focused);
            } else {
                dots.get(p).setBackgroundResource(R.drawable.viewpager_normal);
            }
        }

        // 图片的标题
        title.setText(viewPagerList.get(0).getNewsTitle());
        if (myPagerAdapter == null) {
        } else {
            myPagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 带参跳转使用的方法
     */
    @SuppressWarnings("rawtypes")
    public void getBundle(ArrayList<IndexNewsListInfo> viewPagerList,
                          final int position, String key, Class clazz, String str) {
        IndexNewsListInfo indexNewsList = viewPagerList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, indexNewsList);
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * ViewPager每次仅最多加载三张图片（有利于防止发送内存溢出）
     */
    private class MyPagerAdapter extends PagerAdapter {
        private ArrayList<ImageView> imageList;

        public MyPagerAdapter(ArrayList<ImageView> imageList) {
            if (imageList == null) {
                imageList = new ArrayList<ImageView>();
            }
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // 判断将要显示的图片是否和现在显示的图片是同一个
            // arg0为当前显示的图片，arg1是将要显示的图片
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 销毁该图片
            if (position < imageList.size()) {
                container.removeView(imageList.get(position));
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageList.get(position));
            return imageList.get(position);
        }
    }

    /**
     * 监听ViewPager的变化
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener {
        private ArrayList<IndexNewsListInfo> viewPagerList;

        public MyPageChangeListener(ArrayList<IndexNewsListInfo> viewPagerList) {
            this.viewPagerList = viewPagerList;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @SuppressWarnings("static-access")
        @Override
        public void onPageSelected(int position) {
            // 当显示的图片发生变化之后
            // 设置标题
            if (viewPagerList.size() > 0) {
                // for (int i = 0; i < viewPagerList.size(); i++) {
                title.setText(viewPagerList.get(position).getNewsTitle());
                // }
                // 改变点的状态
                dots.get(position).setBackgroundResource(
                        R.drawable.viewpager_focused);
                dots.get(oldPage).setBackgroundResource(
                        R.drawable.viewpager_normal);
                // 记录的页面
                oldPage = position;
                currPage = position;
            } else {
                ArrayList<IndexNewsListInfo> saveViewPager;
                if (ACacheUtils.get(getActivity(),
                        "viewpagerlist" + ".txt").getAsString(
                        "viewPagerList") != null) {
                    try {
						/* 从缓存中取值出来 转成集合数据 */
                        saveViewPager = SaveSPUtils.getInstance()
                                .String2SceneList(
                                        ACacheUtils.get(
                                                getActivity(),
                                                "viewpagerlist"
                                                        + ".txt").getAsString(
                                                "viewPagerList"));
                        title.setText(saveViewPager.get(position)
                                .getNewsTitle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
					/* 缓存也没有的话 什么也不做 */
                }
            }
            // for (int i = 0; i < viewPagerList.size(); i++) {
            // dots.get(i).setBackgroundResource(R.drawable.viewpager_focused);
            // }
            // if (oldPage < dots.size()) {
            // dots.get(oldPage).setBackgroundResource(
            // R.drawable.viewpager_normal);
            // }
        }
    }

    /**
     * 发消息给handler来更新页面图片
     */
    private class ViewPagerTask implements Runnable {
        private ArrayList<IndexNewsListInfo> viewPagerList;

        public ViewPagerTask(ArrayList<IndexNewsListInfo> viewPagerList) {
            this.viewPagerList = viewPagerList;
        }

        @Override
        public void run() {
            // 改变当前页面的值
            currPage = (currPage + 1) % viewPagerList.size();
            // 发送消息给UI线程
            handler.sendEmptyMessage(3);
        }
    }

    /* 摧毁视图 adapter置空 */
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        myPagerAdapter = null;
        mAdapter = null;
    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case SET_NEWSLIST:
                    getFragmentDataJson();
                    break;
                case CommunalInterfaces.INDEXPUSHNEWSLIST:
                    getIndexPushNewsListJson(msg);
                    break;
                case CommunalInterfaces.GETPUSHNEWSLIST:
                    getNewIndexPushNewsListJson(msg);
                    break;
                case CommunalInterfaces.INDEXNEWSLIST:
                    getIndexNewsListJson(msg);
                    break;
                case CommunalInterfaces.INDEXSLIDENEWSLIST:
                    getIndexSlidenewsListJson(msg);
                    break;
                case 3:
                    // 接收到消息后，更新页面
                    // viewPager.setCurrentItem(currPage);
                    break;
            }
        };
    };

    /**
     * 根据栏目 切换fragmeng 并获取数据 比较复杂 有网络的时候去获取请求 没网络的时候 读取缓存中的数据
     */
    @SuppressWarnings({ "deprecation" })
    private void getFragmentDataJson() {
        detail_loading.setVisibility(View.GONE);
//        LogUtils.e("channel_id", channel_id + "=handler");
        LogUtils.e("text", text + "=handler");
        allList.clear();
        if (HttpTool.isConnnected(mContext) == true) {
            if (allList != null && allList.size() != 0
                    && viewPagerList.size() != 0) {
            } else {
                viewPagerList.clear();
                getAllInformation(0 + "", start_id + "", " ", " ", " ");
            }
        } else {
            imageList.clear();
            dots.clear();
            layout_view_pager.removeAllViews();
            allList.clear();
//            if (channel_id == 211) {
				/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                getACaheViewPager();
				/* 【'推荐'推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                getACheIndexNewsPush();
//            } else {
//				/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                getACaheViewPager();
//				/* 【非'推荐'推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                getACheNewIndexNewsPush();
//				/* 【普通资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                getACheIndexNews();
//            }

        }
        if (mAdapter == null) {
            mAdapter = new FindListAdapter(activity, allList);
        }
        lv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        lv_comprehensive.setOnScrollListener(new PauseOnScrollListener(
                ImageLoader.getInstance(), false, false));
        if (myPagerAdapter == null) {
            // 为viewPager设置适配器
            myPagerAdapter = new MyPagerAdapter(imageList);
        }
        viewPager.setAdapter(myPagerAdapter);
        // 为viewPager添加监听器，该监听器用于当图片变换时，标题和点也跟着变化
        viewPager.setOnPageChangeListener(new MyPageChangeListener(
                viewPagerList));
        // 开启定时器，每隔2秒自动播放下一张（通过调用线程实现）（与Timer类似，可使用Timer代替）
        ScheduledExecutorService scheduled = Executors
                .newSingleThreadScheduledExecutor();
        // 设置一个线程，该线程用于通知UI线程变换图片
        ViewPagerTask pagerTask = new ViewPagerTask(viewPagerList);
        scheduled.scheduleAtFixedRate(pagerTask, 1, 4, TimeUnit.SECONDS);
		/* 设置刷新的监听器 */
        lv_comprehensive
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        allList.clear();
                        indexPushNewsList.clear();
                        dots.clear();
                        layout_view_pager.removeAllViews();
                        String viewPagerNewsId = "";
                        String indexPushNewsId = "";
                        String indexNewsId = "";
                        // TODO 下拉刷新
                        for (int i = 0; i < viewPagerList.size(); i++) {
                            viewPagerNewsId += viewPagerList.get(i).getNewsId()
                                    + ",";
                        }
                        viewPagerList.clear();
                        for (int k = 0; k < indexNewsList.size(); k++) {
                            indexNewsId += indexNewsList.get(k).getNewsId()
                                    + ",";
                        }
                        start_id = indexNewsList.size();
                        indexNewsList.clear();
//                        if (channel_id != 211) {
                            for (int j = 0; j < newIndexPushNewsList.size(); j++) {
                                indexPushNewsId += newIndexPushNewsList.get(j)
                                        .getNewsId() + ",";
                            }
                            newIndexPushNewsList.clear();
//                        }
                        if (HttpTool.isConnnected(mContext) == true) {
                            getAllInformation(pushStartPage + "",
                                    start_id + "", viewPagerNewsId,
                                    indexPushNewsId, indexNewsId);
                        } else {
                            allList.clear();
//                            if (channel_id == 211) {
								/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                                getACaheViewPager();
								/* 【'推荐'推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                                getACheIndexNewsPush();
//                            } else {
//								/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                                getACaheViewPager();
//								/* 【非'推荐'推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                                getACheNewIndexNewsPush();
//								/* 【普通资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                                getACheIndexNews();
//                            }
                        }
                        /**
                         * 过1秒结束下拉刷新
                         */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv_comprehensive.onPullDownRefreshComplete();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        // TODO 上拉加载
                        start_id = indexNewsList.size();
                        String indexNewsId = "";
                        dots.clear();
                        layout_view_pager.removeAllViews();
                        for (int i = 0; i < indexNewsList.size(); i++) {
                            indexNewsId += indexNewsList.get(i).getNewsId()
                                    + ",";
                        }
                        pushStartPage = allList.size();
                        allList.clear();
                        if (HttpTool.isConnnected(mContext) == true) {
                            getAllInformation(pushStartPage + "",
                                    start_id + "", "", "", indexNewsId);
                        } else {
                            allList.clear();
//                            if (channel_id == 211) {
								/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                                getACaheViewPager();
								/* 【'推荐'推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                                getACheIndexNewsPush();
//                            } else {
//								/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                                getACaheViewPager();
//								/* 【非'推荐'推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                                getACheNewIndexNewsPush();
//								/* 【普通资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
//                                getACheIndexNews();
//                            }
                        }

                        /**
                         * 过1秒后 结束向上加载
                         */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv_comprehensive.onPullUpRefreshComplete();
                            }
                        }, 1000);
                    }
                });
    }

    /**
     * 返回 幻灯片 JSON
     *
     * @param msg
     */
    @SuppressWarnings("static-access")
    private void getIndexSlidenewsListJson(Message msg) {
        if (msg.obj != null) {
            JSONObject obj = (JSONObject) msg.obj;
            LogUtils.e("json=viewPagerList", obj.toString());
            try {
                String state = obj.getString("state");
                if (state.equals(CommunalInterfaces._STATE)) {
                    viewPagerList.clear();
                    for (int i = 0; i < obj.length() - 1; i++) {
                        IndexNewsListInfo indexInfo = new IndexNewsListInfo();
                        JSONObject json = obj.getJSONObject("" + i);
                        indexInfo.setNewsId(json.getString("NewsId"));
                        indexInfo.setNewsTitle(json.getString("NewsTitle"));
                        indexInfo.setNewsNum(json.getString("NewsNum"));
                        indexInfo.setNewsAuthor(json.getString("NewsAuthor"));
                        indexInfo.setNewsPicState(json
                                .getString("NewsPicState"));
                        indexInfo.setNewsTime(json.getString("NewsTime"));
                        indexInfo.setNewsProject(json.getString("NewsProject"));
                        indexInfo.setNewsRadio(json.getString("NewsRadio"));
                        indexInfo.setNewsRadioPath(json
                                .getString("NewsRadioPath"));
                        indexInfo.setNewsRadioUrl(json
                                .getString("NewsRadioUrl"));
                        indexInfo.setNewsClass(json.getString("NewsClass"));
                        // 图片
                        String newsImg = json.getString("NewsImg");
                        JSONArray newsImgArray = new JSONArray(newsImg);
                        ArrayList<NewsImgInfo> newsImgList = new ArrayList<NewsImgInfo>();

                        for (int j = 0; j < newsImgArray.length(); j++) {
                            NewsImgInfo newsImgInfo = new NewsImgInfo();
                            JSONObject imgJson = newsImgArray.getJSONObject(j);
                            if (imgJson == null) {
                                newsImgInfo.setPath(null);
                            } else {
                                newsImgInfo.setPath(imgJson.getString("path"));
                                newsImgList.add(newsImgInfo);
                            }
                        }
                        indexInfo.setNewsImg(newsImgList);
                        viewPagerList.add(indexInfo);
                    }
                    String viewPagerStr = "";
                    try {
                        /**
                         * 【幻灯片】缓存前先将集合数据转化成字符串
                         */
                        viewPagerStr = SaveSPUtils.getInstance()
                                .SceneList2String(viewPagerList);
                        /**
                         * 以字符串形式进行缓存 无保留时间
                         */
                        ACacheUtils.get(getActivity(),
                                "viewpagerlist"  + ".txt").put(
                                "viewPagerList", viewPagerStr);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    layout_view_pager.removeAllViews();
                    getViewPager(viewPagerList);
                } else {
                    imageList.clear();
                    dots.clear();
                    layout_view_pager.removeAllViews();
                    ArrayList<IndexNewsListInfo> saveViewPager = null;
					/* 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                    if (ACacheUtils.get(getActivity(),
                            "viewpagerlist"  + ".txt").getAsString(
                            "viewPagerList") != null) {
                        try {
							/* 从缓存中取值出来 转成集合数据 */
                            saveViewPager = SaveSPUtils.getInstance()
                                    .String2SceneList(
                                            ACacheUtils.get(
                                                    getActivity(),
                                                    "viewpagerlist"
                                                            + ".txt")
                                                    .getAsString(
                                                            "viewPagerList"));
                            // Log.e("保存幻灯片", saveViewPager.toString());
							/* 从缓存中获取数据 显示==幻灯片 */
                            getViewPager(saveViewPager);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
						/* 缓存也没有的话 什么也不做 */
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    /**
     * 返回 普通资讯 JSON
     *
     * @param msg
     */
    @SuppressWarnings({ "static-access" })
    private void getIndexNewsListJson(Message msg) {
        if (msg.obj != null) {
            JSONObject obj = (JSONObject) msg.obj;
            try {
                String state = obj.getString("state");
                indexNewsList.clear();
                if (state.equals(CommunalInterfaces._STATE)) {
                    for (int i = 0; i < obj.length() - 1; i++) {
                        IndexNewsListInfo indexInfo = new IndexNewsListInfo();
                        JSONObject json = obj.getJSONObject("" + i);
                        indexInfo.setNewsId(json.getString("NewsId"));
                        indexInfo.setNewsTitle(json.getString("NewsTitle"));
                        // indexInfo.setNewsTag(json.getString("NewsTag"));
                        indexInfo.setNewsNum(json.getString("NewsNum"));
                        indexInfo.setNewsAuthor(json.getString("NewsAuthor"));
                        indexInfo.setNewsPicState(json
                                .getString("NewsPicState"));
                        indexInfo.setNewsTime(json.getString("NewsTime"));
                        indexInfo.setNewsProject(json.getString("NewsProject"));
                        indexInfo.setNewsRadio(json.getString("NewsRadio"));
                        indexInfo.setNewsRadioPath(json
                                .getString("NewsRadioPath"));
                        indexInfo.setNewsRadioUrl(json
                                .getString("NewsRadioUrl"));
                        indexInfo.setNewsClass(json.getString("NewsClass"));
                        // 图片
                        String newsImg = json.getString("NewsImg");
                        JSONArray newsImgArray = new JSONArray(newsImg);
                        ArrayList<NewsImgInfo> newsImgList = new ArrayList<NewsImgInfo>();
                        for (int j = 0; j < newsImgArray.length(); j++) {
                            JSONObject imgJson = newsImgArray.getJSONObject(j);
                            if (imgJson == null) {
                                imgJson = null;
                            } else {
                                NewsImgInfo newsImgInfo = new NewsImgInfo();
                                newsImgInfo.setPath(imgJson.getString("path"));
                                newsImgList.add(newsImgInfo);
                            }
                        }
                        indexInfo.setNewsImg(newsImgList);
                        // 多图文内容
                        indexInfo.setNewsPicMatterName(json
                                .getString("NewsPicMatter"));
                        indexNewsList.add(indexInfo);
                    }
                    try {
                        /**
                         * 【普通资讯】缓存前先将集合数据转化成字符串
                         */
                        String indexNews = SaveSPUtils.getInstance()
                                .SceneList2String(indexNewsList);
                        /**
                         * 以字符串形式进行缓存 无保留时间
                         */
                        ACacheUtils.get(getActivity(),
                                "indexNewsas"  + ".txt").put(
                                "indexNewsList", indexNews);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    allList.removeAll(indexNewsList);
                    allList.addAll(indexNewsList);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ArrayList<IndexNewsListInfo> saveindexNews = null;
					/* 【普通资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                    if (ACacheUtils.get(getActivity(),
                            "indexNewsas"  + ".txt").getAsString(
                            "indexNewsList") != null) {
                        try {
							/* 从缓存中取值出来 转成集合数据 */
                            saveindexNews = SaveSPUtils.getInstance()
                                    .String2SceneList(
                                            ACacheUtils.get(
                                                    getActivity(),
                                                    "indexNewsas"  + ".txt")
                                                    .getAsString(
                                                            "indexNewsList"));
							/* 把获取缓存得到的数据放进adapter要显示的集合 */
                            allList.removeAll(saveindexNews);
                            allList.removeAll(indexNewsList);
                            allList.addAll(saveindexNews);
                            if (mAdapter != null) {
                                LogUtils.e("saveindexNews",
                                        saveindexNews.toString());
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
						/* 缓存也没有的话 什么也不做 */
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    /**
     * 返回 ‘推荐’ 推送 资讯列表 JSON
     *
     * @param msg
     */
    @SuppressWarnings("static-access")
    private void getIndexPushNewsListJson(Message msg) {
        if (msg.obj != null) {
            JSONObject obj = (JSONObject) msg.obj;
            try {
                String state = obj.getString("state");
                if (state.equals(CommunalInterfaces._STATE)) {
                    // indexPushNewsList.clear();
                    for (int i = 0; i < obj.length() - 1; i++) {
                        IndexNewsListInfo indexInfo = new IndexNewsListInfo();
                        JSONObject json = obj.getJSONObject("" + i);
                        indexInfo.setNewsId(json.getString("NewsId"));
                        indexInfo.setNewsTitle(json.getString("NewsTitle"));
                        indexInfo.setNewsNum(json.getString("NewsNum"));
                        indexInfo.setNewsAuthor(json.getString("NewsAuthor"));
                        indexInfo.setNewsPicState(json
                                .getString("NewsPicState"));
                        indexInfo.setNewsTime(json.getString("NewsTime"));
                        indexInfo.setNewsProject(json.getString("NewsProject"));
                        indexInfo.setNewsRadio(json.getString("NewsRadio"));
                        indexInfo.setNewsRadioPath(json
                                .getString("NewsRadioPath"));
                        indexInfo.setNewsRadioUrl(json
                                .getString("NewsRadioUrl"));
                        indexInfo.setNewsClass(json.getString("NewsClass"));
                        // 图片
                        String newsImg = json.getString("NewsImg");
                        JSONArray newsImgArray = new JSONArray(newsImg);
                        ArrayList<NewsImgInfo> newsImgList = new ArrayList<NewsImgInfo>();
                        for (int j = 0; j < newsImgArray.length(); j++) {
                            NewsImgInfo newsImgInfo = new NewsImgInfo();
                            JSONObject imgJson = newsImgArray.getJSONObject(j);
                            newsImgInfo.setPath(imgJson.getString("path"));
                            newsImgList.add(newsImgInfo);
                        }
                        indexInfo.setNewsImg(newsImgList);
                        indexInfo.setNewsPicMatterName(json
                                .getString("NewsPicMatter"));
                        indexPushNewsList.add(indexInfo);
                    }
                    try {
                        /**
                         * 【推荐资讯】缓存前先将集合数据转化成字符串
                         */
                        String indexPush = SaveSPUtils.getInstance()
                                .SceneList2String(indexPushNewsList);
                        if (!indexPush.equals("") || indexPush != null) {
                            /**
                             * 以字符串形式进行缓存 无保留时间
                             */
                            ACacheUtils.get(getActivity(),
                                    "indexPushas"  + ".txt").put(
                                    "indexPush", indexPush);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    allList.removeAll(indexPushNewsList);
                    allList.removeAll(newIndexPushNewsList);
                    allList.addAll(indexPushNewsList);
                    if (allList != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ArrayList<IndexNewsListInfo> saveindexPush = null;
					/* 【推荐资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                    if (ACacheUtils.get(getActivity(),
                            "indexPushas"  + ".txt").getAsString(
                            "indexPush") != null) {
                        try {
							/* 从缓存中取值出来 转成集合数据 */
                            saveindexPush = SaveSPUtils.getInstance()
                                    .String2SceneList(
                                            ACacheUtils.get(
                                                    getActivity(),
                                                    "indexPushas"+ ".txt")
                                                    .getAsString("indexPush"));
							/* 把获取缓存得到的数据放进adapter要显示的集合(先卸掉原先的数据不然刷新数据会重复) */
                            allList.removeAll(saveindexPush);
                            allList.removeAll(indexPushNewsList);
                            allList.removeAll(newIndexPushNewsList);
                            allList.addAll(saveindexPush);
                            if (allList != null) {
                                LogUtils.e("保存TJ=allList", allList.toString());
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
						/* 缓存也没有的话 什么也不做 */
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    /**
     * 返回 非‘推荐’ 推送 资讯列表 JSON
     */
    @SuppressWarnings("static-access")
    private void getNewIndexPushNewsListJson(Message msg) {
        if (msg.obj != null) {
            JSONObject obj = (JSONObject) msg.obj;
            LogUtils.e("json=newIndexPushNewsList", obj.toString());
            try {
                String state = obj.getString("state");
                if (state.equals(CommunalInterfaces._STATE)) {
                    newIndexPushNewsList.clear();
                    for (int i = 0; i < obj.length() - 1; i++) {
                        IndexNewsListInfo indexInfo = new IndexNewsListInfo();
                        JSONObject json = obj.getJSONObject("" + i);
                        indexInfo.setNewsId(json.getString("NewsId"));
                        indexInfo.setNewsTitle(json.getString("NewsTitle"));
                        indexInfo.setNewsNum(json.getString("NewsNum"));
                        indexInfo.setNewsAuthor(json.getString("NewsAuthor"));
                        indexInfo.setNewsPicState(json
                                .getString("NewsPicState"));
                        indexInfo.setNewsTime(json.getString("NewsTime"));
                        indexInfo.setNewsProject(json.getString("NewsProject"));
                        indexInfo.setNewsRadio(json.getString("NewsRadio"));
                        indexInfo.setNewsRadioPath(json
                                .getString("NewsRadioPath"));
                        indexInfo.setNewsRadioUrl(json
                                .getString("NewsRadioUrl"));
                        indexInfo.setNewsClass(json.getString("NewsClass"));
                        // 图片
                        String newsImg = json.getString("NewsImg");
                        JSONArray newsImgArray = new JSONArray(newsImg);
                        ArrayList<NewsImgInfo> newsImgList = new ArrayList<NewsImgInfo>();
                        for (int j = 0; j < newsImgArray.length(); j++) {
                            NewsImgInfo newsImgInfo = new NewsImgInfo();
                            JSONObject imgJson = newsImgArray.getJSONObject(j);
                            newsImgInfo.setPath(imgJson.getString("path"));
                            newsImgList.add(newsImgInfo);
                        }
                        indexInfo.setNewsImg(newsImgList);
                        indexInfo.setNewsPicMatterName(json
                                .getString("NewsPicMatter"));
                        newIndexPushNewsList.add(indexInfo);
                    }
                    try {
                        /**
                         * 【推荐资讯】缓存前先将集合数据转化成字符串
                         */
                        String newIndexPush = SaveSPUtils.getInstance()
                                .SceneList2String(newIndexPushNewsList);
                        if (!newIndexPush.equals("") || newIndexPush != null) {
                            /**
                             * 以字符串形式进行缓存 无保留时间
                             */
                            ACacheUtils.get(getActivity(),
                                    "newIndexPush" + ".txt").put(
                                    "indexPush", newIndexPush);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    allList.removeAll(indexPushNewsList);
                    allList.removeAll(newIndexPushNewsList);
                    allList.addAll(newIndexPushNewsList);
                    if (allList != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ArrayList<IndexNewsListInfo> saveNewIndexPush = null;
					/* 【推荐资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值 */
                    if (ACacheUtils.get(getActivity(),
                            "newIndexPush"  + ".txt").getAsString(
                            "indexPush") != null) {
                        try {
							/* 从缓存中取值出来 转成集合数据 */
                            saveNewIndexPush = SaveSPUtils.getInstance()
                                    .String2SceneList(
                                            ACacheUtils.get(
                                                    getActivity(),
                                                    "newIndexPush" + ".txt")
                                                    .getAsString("indexPush"));
							/* 把获取缓存得到的数据放进adapter要显示的集合(先卸掉原先的数据不然刷新数据会重复) */
                            allList.removeAll(saveNewIndexPush);
                            allList.removeAll(indexPushNewsList);
                            allList.removeAll(newIndexPushNewsList);
                            allList.addAll(saveNewIndexPush);
                            if (allList != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
						/* 缓存也没有的话 什么也不做 */
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    /**
     * 【头部图片轮播】没网的时候判断是否缓存过 如果缓存过从缓存中取值
     */
    @SuppressWarnings("static-access")
    private void getACaheViewPager() {
        ArrayList<IndexNewsListInfo> saveViewPager;
        if (ACacheUtils.get(getActivity(),
                "viewpagerlist" + ".txt").getAsString(
                "viewPagerList") != null) {
            try {
				/* 从缓存中取值出来 转成集合数据 */
                saveViewPager = SaveSPUtils.getInstance().String2SceneList(
                        ACacheUtils.get(getActivity(),
                                "viewpagerlist"  + ".txt")
                                .getAsString("viewPagerList"));
				/* 从缓存中获取数据 显示==幻灯片 */
                getViewPager(saveViewPager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
			/* 缓存也没有的话 什么也不做 */
        }
    }

    /**
     * 【'推荐' 推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值
     */
    @SuppressWarnings("static-access")
    private void getACheIndexNewsPush() {
        if (ACacheUtils.get(getActivity(), "indexPushas"  + ".txt")
                .getAsString("indexPush") != null) {
            try {
				/* 从缓存中取值出来 转成集合数据 */
                ArrayList<IndexNewsListInfo> saveindexPush = SaveSPUtils
                        .getInstance().String2SceneList(
                                ACacheUtils.get(getActivity(),
                                        "indexPushas"  + ".txt")
                                        .getAsString("indexPush"));
				/* 把获取缓存得到的数据放进adapter要显示的集合(先卸掉原先的数据不然刷新数据会重复) */
                allList.removeAll(saveindexPush);
                allList.removeAll(indexPushNewsList);
                allList.removeAll(newIndexPushNewsList);
                allList.addAll(saveindexPush);
                // Log.e("allList=saveindexPush", allList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
			/* 缓存也没有的话 什么也不做 */
        }
    }

    /**
     * 【非'推荐' 推送资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值
     */
    @SuppressWarnings("static-access")
    private void getACheNewIndexNewsPush() {
        if (ACacheUtils
                .get(getActivity(), "newIndexPush"  + ".txt")
                .getAsString("indexPush") != null) {
            try {
				/* 从缓存中取值出来 转成集合数据 */
                ArrayList<IndexNewsListInfo> saveNewIndexPush = SaveSPUtils
                        .getInstance().String2SceneList(
                                ACacheUtils.get(getActivity(),
                                        "newIndexPush"  + ".txt")
                                        .getAsString("indexPush"));
				/* 把获取缓存得到的数据放进adapter要显示的集合(先卸掉原先的数据不然刷新数据会重复) */
                allList.removeAll(saveNewIndexPush);
                allList.removeAll(indexPushNewsList);
                allList.removeAll(newIndexPushNewsList);
                allList.addAll(saveNewIndexPush);
                // Log.e("allList=saveindexPush", allList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
			/* 缓存也没有的话 什么也不做 */
        }
    }

    /**
     * 【普通资讯】没网的时候判断是否缓存过 如果缓存过从缓存中取值
     */
    @SuppressWarnings("static-access")
    private void getACheIndexNews() {
        if (ACacheUtils.get(getActivity(), "indexNewsas"  + ".txt")
                .getAsString("indexNewsList") != null) {
            try {
				/* 从缓存中取值出来 转成集合数据 */
                ArrayList<IndexNewsListInfo> saveindexNews = SaveSPUtils
                        .getInstance().String2SceneList(
                                ACacheUtils.get(getActivity(),
                                        "indexNewsas" + ".txt")
                                        .getAsString("indexNewsList"));
				/* 把获取缓存得到的数据放进adapter要显示的集合 */
                allList.removeAll(saveindexNews);
                allList.removeAll(indexNewsList);
                allList.addAll(saveindexNews);
                LogUtils.e("allList=saveindexNews", allList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
			/* 缓存也没有的话 什么也不做 */
        }
    }

}