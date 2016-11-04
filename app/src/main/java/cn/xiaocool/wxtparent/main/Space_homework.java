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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Homework_Adapter;
import cn.xiaocool.wxtparent.bean.Homework;
import cn.xiaocool.wxtparent.bean.Reciver;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshBase;
import cn.xiaocool.wxtparent.ui.list.PullToRefreshListView;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class Space_homework extends Activity implements View.OnClickListener {

    private ListView listView;
    private PullToRefreshListView listView_pull;
    private Homework_Adapter homework_adapter;
    private ImageView exit;
    private Context context;
    private ArrayList<Homework.HomeworkData> list_home_data;
    private String TAG = "Space_homework";


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.SPACE_MYHOMEWORK:
                    ProgressViewUtil.dismiss();
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        list_home_data.clear();
                        JSONObject itemObject;
                        for (int i = hwArray.length()-1; i >=0 ; i--) {
                            itemObject = hwArray.optJSONObject(i);
                            Homework.HomeworkData homeworkData = new Homework.HomeworkData();
                            homeworkData.setId(itemObject.optString("homework_id"));
                            homeworkData.setReadTime(itemObject.optString("read_time"));
                            JSONObject object= itemObject.optJSONArray("homework_info").optJSONObject(0);
                            homeworkData.setTitle(object.optString("title"));
                            homeworkData.setContent(object.optString("content"));
                            homeworkData.setSubject(object.optString("subject"));
                            homeworkData.setCreate_time(object.optString("create_time"));
                            homeworkData.setTeacherName(object.optString("name"));
                            homeworkData.setTeacherAvator(object.optString("photo"));
                            JSONArray picArray = itemObject.optJSONArray("picture");
                            ArrayList<String> pics = new ArrayList<>();
                            for(int j=0;j<picArray.length();j++){
                                if(!picArray.optJSONObject(j).optString("picture_url").equals("")&&!picArray.optJSONObject(j).optString("picture_url").equals("null")){
                                    pics.add(picArray.optJSONObject(j).optString("picture_url"));
                                }
                            }
                            homeworkData.setPics(pics);
                            JSONArray receiveArray = itemObject.optJSONArray("receive_list");
                            ArrayList<Reciver> recivers = new ArrayList<>();
                            for(int j=0;j<receiveArray.length();j++){
                                JSONObject object1 = receiveArray.optJSONObject(j);
                                Reciver reciver = new Reciver();
                                reciver.setReadTime(object1.optString("read_time"));
                                reciver.setReceiverId(object1.optString("receiverid"));
                                recivers.add(reciver);
                            }
                            homeworkData.setRecivers(recivers);
                            list_home_data.add(homeworkData);

                        }
                        Collections.sort(list_home_data, new Comparator<Homework.HomeworkData>() {
                            @Override
                            public int compare(Homework.HomeworkData lhs, Homework.HomeworkData rhs) {
                                return (int) (Long.parseLong(rhs.getCreate_time())-Long.parseLong(lhs.getCreate_time()));
                            }
                        });
                        if (homework_adapter != null) {
                            homework_adapter.notifyDataSetChanged();
                        } else {
                            homework_adapter = new Homework_Adapter(list_home_data, context, handler);
                            listView.setAdapter(homework_adapter);
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
        setContentView(R.layout.activity_space_homework);
        context = this;
        ProgressViewUtil.show(context);
        SPUtils.remove(this, "JPUSHHOMEWORK");
        initData();
        new SpaceRequest(this, handler).myHomework();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHHOMEWORK");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHHOMEWORK");
    }

    private void initData() {
        listView_pull = (PullToRefreshListView) findViewById(R.id.list_space_MyHomework);
        listView = listView_pull.getRefreshableView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#f2f2f2")));
        listView.setDividerHeight(15);
        list_home_data = new ArrayList<>();

        //退出按钮
        exit = (ImageView) findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView_pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
                        listView_pull.onPullDownRefreshComplete();
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
                        listView_pull.onPullUpRefreshComplete();
                    }
                }, 1000);

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


    }

    @Override
    public void onClick(View v) {

    }

    private void getAllInformation() {
        new SpaceRequest(this, handler).myHomework();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }
}
