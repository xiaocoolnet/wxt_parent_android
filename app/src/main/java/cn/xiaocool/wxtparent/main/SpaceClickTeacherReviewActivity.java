package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.TeacherReviewDetailAdapter;
import cn.xiaocool.wxtparent.adapter.TeacherReview_Adapter;
import cn.xiaocool.wxtparent.bean.TeacherReview;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.DateUtils;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickTeacherReviewActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout up_jiantou;
    private RelativeLayout last_month, next_month;
    private TextView year_month;
    private int year, month;
    private PullToRefreshListView pull_listview;
    private String begintime,endtime;
    private TeacherReviewDetailAdapter adapter;
    private ArrayList<TeacherReview> teacherReviews;
    private ListView listView;
    private Context mContext;
    private TeacherReview_Adapter teacherReview_adapter;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETTEACOMMENT:
                    JSONObject obj = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        teacherReviews.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < hwArray.length(); i++) {
                            itemObject = hwArray.optJSONObject(i);
                            TeacherReview teacherReview = new TeacherReview();
                            teacherReview.setId(itemObject.optString("comment_id"));
                            teacherReview.setTeacherAvator(itemObject.optString("teacher_photo"));
                            teacherReview.setTeacherName(itemObject.optString("teacher_name"));
                            teacherReview.setComment(itemObject.optString("comment_content"));
                            teacherReview.setTime(itemObject.optString("comment_time"));
                            teacherReview.setLearn(itemObject.optString("learn"));
                            teacherReview.setWork(itemObject.optString("work"));
                            teacherReview.setSing(itemObject.optString("sing"));
                            teacherReview.setLabour(itemObject.optString("labour"));
                            teacherReview.setStrain(itemObject.optString("strain"));
                            teacherReviews.add(teacherReview);
                        }
                        Collections.sort(teacherReviews, new Comparator<TeacherReview>() {
                            @Override
                            public int compare(TeacherReview lhs, TeacherReview rhs) {
                                return (int) (Long.parseLong(rhs.getTime())-Long.parseLong(lhs.getTime()));
                            }
                        });
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new TeacherReviewDetailAdapter(mContext,teacherReviews);
                            listView.setAdapter(adapter);
                        }
                    }else{
                        teacherReviews.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_teacher_review);
        mContext = this;
        ProgressViewUtil.show(mContext);
        SPUtils.remove(this, "JPUSHCOMMENT");
        teacherReviews = new ArrayList<>();
        init();
        begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
        endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year, month)/1000);
        new SpaceRequest(mContext,handler).getTeacherComment(begintime,endtime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHCOMMENT");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHCOMMENT");
    }

    private void init() {
        pull_listview = (PullToRefreshListView) findViewById(R.id.teacher_review_listcontent);
        listView = pull_listview.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        last_month = (RelativeLayout) findViewById(R.id.last_month);
        next_month = (RelativeLayout) findViewById(R.id.next_month);
        year_month = (TextView) findViewById(R.id.year_month);
        teacherReview_adapter=new TeacherReview_Adapter(this);
        //获取今天的年月
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;

        year_month = (TextView) findViewById(R.id.year_month);
        year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
        Log.d("年-月", String.valueOf(year) + String.valueOf(month));
        last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month - 1;
                if (month == 0) {
                    month = 12;
                    year = year - 1;
                }
                year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
                begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
                endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
                new SpaceRequest(mContext,handler).getTeacherComment(begintime, endtime);
            }
        });
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month + 1;
                if (month == 13) {
                    month = 1;
                    year = year + 1;
                }
                year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");
                begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
                endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
                new SpaceRequest(mContext,handler).getTeacherComment(begintime, endtime);
            }
        });

        pull_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
                        pull_listview.onPullDownRefreshComplete();
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
                        pull_listview.onPullUpRefreshComplete();
                    }
                }, 1000);

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
    private void getAllInformation() {
        begintime = String.valueOf(DateUtils.getMonthBeginTimestamp(year,month)/1000);
        endtime = String.valueOf(DateUtils.getMonthEndTimestamp(year,month)/1000);
        new SpaceRequest(mContext,handler).getTeacherComment(begintime, endtime);
    }


}
