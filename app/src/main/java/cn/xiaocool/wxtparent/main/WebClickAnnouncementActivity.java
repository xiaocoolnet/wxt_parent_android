package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Teacher_style_Adapter;
import cn.xiaocool.wxtparent.bean.Teacher_style;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/2/25.
 */

public class WebClickAnnouncementActivity extends BaseActivity implements View.OnClickListener {

//    private Context mContext;
//    private List<Announcement.AnnouncementData> list_web_introduce_data;
//    private RelativeLayout re1;
//    private ListView listView;
//    private String TAG = "SpaceClickAnnouActivity";
//    private WebIntroduce_Adapter webIntroduce_Adapter;
//
//    private static final int ANNOU_PRAISE_KEY = 104;
//    private static final int DEL_ANNOU_PRAISE_KEY = 105;
//    private PullToRefreshListView listview_pull;
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//
//                case ANNOU_PRAISE_KEY:
//                    if (msg.obj != null) {
//                        JSONObject jo = (JSONObject) msg.obj;
//                        try {
//                            String state = jo.getString("status");
//                            String result = jo.getString("data");
//                            if (state.equals(CommunalInterfaces._STATE)) {
//                                ToastUtils.ToastShort(getApplicationContext(), "点赞成功");
//                                getAllInformation();
//                            } else {
//                                ToastUtils.ToastShort(getApplicationContext(), result);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    break;
//
//
//                case DEL_ANNOU_PRAISE_KEY:
//                    if (msg.obj != null) {
//                        Log.e(TAG, "取消赞" + msg.obj);
//                        try {
//                            JSONObject json = (JSONObject) msg.obj;
//                            String state = json.getString("status");
//                            String result = json.getString("data");
//                            if (state.equals(CommunalInterfaces._STATE)) {
//                                ToastUtils.ToastShort(getApplicationContext(), "已取消");
//                                getAllInformation();
//                            } else {
//                                ToastUtils.ToastShort(getApplicationContext(), result);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//
//
//                case CommunalInterfaces.ANNOUNCEMENT:
//
//                    JSONObject obj = (JSONObject) msg.obj;
//                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
//                        JSONArray Array = obj.optJSONArray("data");
//                        list_web_introduce_data.clear();
//                        JSONObject itemObject;
//                        for (int i = 0; i < Array.length(); i++) {
//                            itemObject = Array.optJSONObject(i);
//                            Announcement.AnnouncementData announcementData = new Announcement.AnnouncementData();
//                            announcementData.setId(itemObject.optString("id"));
//                            announcementData.setUserid(itemObject.optString("userid"));
//                            announcementData.setTitle(itemObject.optString("title"));
//
//                            Log.e("yyyy", itemObject.optString("title"));
//
//                            announcementData.setContent(itemObject.optString("content"));
//                            announcementData.setCreate_time(itemObject.optString("create_time"));
//                            announcementData.setUsername(itemObject.optString("username"));
//                            announcementData.setReadcount(itemObject.optInt("readcount"));
//                            announcementData.setAllreader(itemObject.optInt("allreader"));
//                            announcementData.setReadtag(itemObject.optInt("readtag"));
//                            announcementData.setPhoto(itemObject.optString("photo"));
//                            JSONArray likeArray = itemObject.optJSONArray("like");
//                            if (likeArray != null) {
//                                ArrayList<LikeBean> likeBeanList = new ArrayList<>();
//                                for (int j = 0; j < likeArray.length(); j++) {
//                                    JSONObject likeObject = likeArray.optJSONObject(j);
//                                    LikeBean likeBean = new LikeBean();
//                                    likeBean.setUserid(likeObject.optString("userid"));
//                                    likeBean.setName(likeObject.optString("name"));
//                                    likeBean.setAvatar(likeObject.optString("avatar"));
//                                    likeBeanList.add(likeBean);
//                                }
//                                announcementData.setWorkPraise(likeBeanList);
//                            }
//                            JSONArray commentArray = itemObject.optJSONArray("comment");
//                            if (commentArray.length() > 0) {
//                                ArrayList<Comments> commentList = new ArrayList<>();
//                                for (int j = 0; j < commentArray.length(); j++) {
//                                    JSONObject commentObject = commentArray.optJSONObject(j);
//                                    Comments comments = new Comments();
//                                    comments.setUserid(commentObject.optString("userid"));
//                                    comments.setName(commentObject.optString("name"));
//                                    comments.setAvatar(commentObject.optString("avatar"));
//                                    comments.setComment_time(commentObject.optString("comment_time"));
//                                    commentList.add(comments);
//                                }
//                                announcementData.setComment(commentList);
//                            }
//                            list_web_introduce_data.add(announcementData);
//
//                        }
//                        if (webIntroduce_Adapter != null) {
//                            webIntroduce_Adapter.notifyDataSetChanged();
//                        } else {
//                            webIntroduce_Adapter = new WebIntroduce_Adapter(list_web_introduce_data, getApplicationContext(), handler);
//                            webIntroduce_Adapter.notifyDataSetChanged();
//                            listView.setAdapter(webIntroduce_Adapter);
//                        }
//
//                    }
//
//
//                    break;
//
//            }
//
//
//        }
//    };


    private ListView listView;
    private PullToRefreshListView listview_pull;
    private List<Teacher_style.DataEntity> announcement_yq_list;
    private Teacher_style_Adapter teacher_style_adapter;
    private RelativeLayout up_jiantou;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.ANNOUNCEMENT_YQ:
                    JSONObject obj = (JSONObject) msg.obj;
                    LogUtils.e("WebClickTeacherStyleActivity", obj.optString("data"));
                    String status = obj.optString("status");

                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        announcement_yq_list.clear();
                        if (Array != null) {
                            for (int i = 0; i < Array.length(); i++) {
                                Teacher_style.DataEntity tea_style = new Teacher_style().new DataEntity();
                                JSONObject Object = Array.optJSONObject(i);
                                tea_style.setId(Object.optString("id"));
                                tea_style.setPost_date(Object.optString("post_date"));
                                tea_style.setPost_excerpt(Object.optString("post_excerpt"));
                                tea_style.setPost_keywords(Object.optString("post_keywords"));
                                tea_style.setPost_title(Object.optString("post_title"));
                                tea_style.setSchoolid(Object.optString("schoolid"));
                                tea_style.setSmeta(Object.optString("smeta"));
                                tea_style.setThumb(Object.optString("thumb"));


                                Log.e("yyyy", Object.optString("post_title"));
                                announcement_yq_list.add(tea_style);

                            }
                        }
                        teacher_style_adapter = new Teacher_style_Adapter(getApplicationContext(), announcement_yq_list);
                        listView.setAdapter(teacher_style_adapter);
                    }
                    break;
            }
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.web_open_introduce);
        setContentView(R.layout.activity_web_introduce);
        initView();




//        mContext = this;
//        re1 = (RelativeLayout) findViewById(R.id.up_jiantou);
//        listview_pull = (PullToRefreshListView) findViewById(R.id.web_introduce_list);
//        listView = listview_pull.getRefreshableView();
//        list_web_introduce_data = new ArrayList<>();
//        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
//        listView.setDividerHeight(10);
//        new SpaceRequest(mContext, handler).announcement();
//        listview_pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                if (NetUtil.isConnnected(getApplicationContext()) == true) {
//                    getAllInformation();
//
//                } else {
//                    ToastUtils.ToastShort(getApplicationContext(), "暂无网络");
//                }
//                /**
//                 * 过1秒结束下拉刷新
//                 */
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        listview_pull.onPullDownRefreshComplete();
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                /**
//                 * 过1秒后 结束向上加载
//                 */
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        listview_pull.onPullUpRefreshComplete();
//                    }
//                }, 1000);
//
//            }
//        });
//
//
//        re1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


    }

    private void initView() {

        up_jiantou= (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);


        listview_pull = (PullToRefreshListView) findViewById(R.id.web_introduce_list);
        listView = listview_pull.getRefreshableView();
        announcement_yq_list = new ArrayList<>();
        new SpaceRequest(getApplicationContext(), handler).announcement_yuanqu();

        listview_pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (NetUtil.isConnnected(getApplicationContext()) == true) {
                    getAllInformation();

                } else {
                    ToastUtils.ToastShort(getApplicationContext(), "暂无网络");
                }
                /**
                 * 过1秒结束下拉刷新
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listview_pull.onPullDownRefreshComplete();
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
                        listview_pull.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WebClickAnnouncementActivity.this, TeacherStyle_Webview.class);

                intent.putExtra("url", "http://wxt.xiaocool.net/index.php?g=portal&m=article&a=notice&id="+announcement_yq_list.get(position).getId());
                intent.putExtra("title", announcement_yq_list.get(position).getPost_title());
                intent.putExtra("content", announcement_yq_list.get(position).getPost_excerpt());

                startActivity(intent);


            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
        }
    }

//    private void getAllInformation() {
//        new SpaceRequest(this, handler).announcement();
//
//    }

    private void getAllInformation() {
        new SpaceRequest(getApplicationContext(), handler).announcement_yuanqu();

    }


}

