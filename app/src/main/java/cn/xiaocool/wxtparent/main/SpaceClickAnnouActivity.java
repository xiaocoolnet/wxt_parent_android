package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Annou_Adapter;
import cn.xiaocool.wxtparent.bean.AnnouncementInfo;
import cn.xiaocool.wxtparent.bean.Reciver;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/3/17.
 */
public class SpaceClickAnnouActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private ArrayList<AnnouncementInfo> list_annou_data;
    private RelativeLayout re1;
    private ListView listView;
    private String TAG = "SpaceClickAnnouActivity";
    private Annou_Adapter annou_adapter;
    private PullToRefreshListView listview_pull;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.ANNOUNCEMENT:
                    JSONObject obj = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray Array = obj.optJSONArray("data");
                        list_annou_data.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < Array.length(); i++) {
                            itemObject = Array.optJSONObject(i);
                            AnnouncementInfo announcementInfo = new AnnouncementInfo();
                            announcementInfo.setId(itemObject.optString("noticeid"));
                            announcementInfo.setReadTime(itemObject.optString("create_time"));
                            JSONObject notice = itemObject.optJSONArray("notice_info").optJSONObject(0);
                            announcementInfo.setTitle(notice.optString("title"));
                            announcementInfo.setContent(notice.optString("content"));
                            announcementInfo.setTeacherName(notice.optString("name"));
                            announcementInfo.setTeacherAvator(notice.optString("photo"));
                            announcementInfo.setTime(notice.optString("create_time"));
                            JSONArray picArray = itemObject.optJSONArray("pic");
                            ArrayList<String> pics = new ArrayList<>();
                            for(int j=0;j<picArray.length();j++){
                                if(!picArray.optJSONObject(j).optString("photo").equals("")&&!picArray.optJSONObject(j).optString("photo").equals("null")){
                                    pics.add(picArray.optJSONObject(j).optString("photo"));
                                }
                            }
                            announcementInfo.setPics(pics);
                            JSONArray receiveArray = itemObject.optJSONArray("receiv_list");
                            ArrayList<Reciver> recivers = new ArrayList<>();
                            for(int j=0;j<receiveArray.length();j++){
                                JSONObject object = receiveArray.optJSONObject(j);
                                Reciver reciver = new Reciver();
                                reciver.setId(object.optString("noticeid"));
                                reciver.setReadTime(object.optString("create_time"));
                                reciver.setReceiverId(object.optString("receiverid"));
                                recivers.add(reciver);
                            }
                            announcementInfo.setRecivers(recivers);
                            list_annou_data.add(announcementInfo);
                        }
                    }
                    Collections.sort(list_annou_data, new Comparator<AnnouncementInfo>() {
                        @Override
                        public int compare(AnnouncementInfo lhs, AnnouncementInfo rhs) {
                            return (int) (Long.parseLong(rhs.getTime())-Long.parseLong(lhs.getTime()));
                        }
                    });
                    if (annou_adapter != null) {
                        annou_adapter.notifyDataSetChanged();
                    } else {
                        annou_adapter = new Annou_Adapter(list_annou_data, mContext);
                        listView.setAdapter(annou_adapter);
                    }
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_click_announcement);
        mContext = this;
        ProgressViewUtil.show(mContext);
        SPUtils.remove(this, "JPUSHNOTICE");
        re1 = (RelativeLayout) findViewById(R.id.up_jiantou);
        listview_pull = (PullToRefreshListView) findViewById(R.id.announcement_list);
        listView = listview_pull.getRefreshableView();
        list_annou_data = new ArrayList<>();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
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
        re1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHNOTICE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHNOTICE");
    }

    @Override
    public void onClick(View v) {

    }

    private void getAllInformation() {
        new SpaceRequest(this, handler).announcement();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }
}
