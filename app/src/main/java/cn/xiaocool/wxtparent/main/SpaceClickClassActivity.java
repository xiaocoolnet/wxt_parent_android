package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ClassScheduleExAdapter;
import cn.xiaocool.wxtparent.bean.ClassList;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;


/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickClassActivity extends Activity implements View.OnClickListener {

    private RelativeLayout up_jiantou;
    private ExpandableListView class_schedule_list;
    private ArrayList<ArrayList<String>> classlists;
    private ClassScheduleExAdapter classScheduleExAdapter;
    private RequestQueue mQueue;
    private Context mContext;
    private ArrayList<ClassList.ClassListData> arrayList;
    private TextView scca_top_title;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CommunalInterfaces.CLASS_SCHEDULE:
                    if (msg.obj !=null){
                        JSONObject obj = (JSONObject) msg.obj;
                        ProgressViewUtil.dismiss();
                        try {
                            String state = obj.optString("status");
                            if (state.equals(CommunalInterfaces._STATE)){
                                JSONObject jsonObject = obj.optJSONObject("data");

                                JSONArray mon = jsonObject.optJSONArray("mon");
                                ArrayList<String> monArray = new ArrayList<>();
                                for (int i=0;i<mon.length();i++){
                                    JSONObject monObject = mon.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    monArray.add(monObject.optString(pos));
                                }
                                classlists.add(monArray);

                                JSONArray tu = jsonObject.optJSONArray("tu");
                                ArrayList<String> tuArray = new ArrayList<>();
                                for (int i=0;i<tu.length();i++){
                                    JSONObject tuObject = tu.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    tuArray.add(tuObject.optString(pos));
                                }
                                classlists.add(tuArray);

                                JSONArray we = jsonObject.optJSONArray("we");
                                ArrayList<String> weArray = new ArrayList<>();
                                for (int i=0;i<we.length();i++){
                                    JSONObject weObject = we.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    weArray.add(weObject.optString(pos));
                                }
                                classlists.add(weArray);

                                JSONArray th = jsonObject.optJSONArray("th");
                                ArrayList<String> thArray = new ArrayList<>();
                                for (int i=0;i<th.length();i++){
                                    JSONObject thObject = th.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    thArray.add(thObject.optString(pos));
                                }
                                classlists.add(thArray);

                                JSONArray fri = jsonObject.optJSONArray("fri");
                                ArrayList<String> friArray = new ArrayList<>();
                                for (int i=0;i<fri.length();i++){
                                    JSONObject friObject = fri.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    friArray.add(friObject.optString(pos));
                                }
                                classlists.add(friArray);

                                JSONArray sat = jsonObject.optJSONArray("sat");
                                ArrayList<String> satArray = new ArrayList<>();
                                for (int i=0;i<sat.length();i++){
                                    JSONObject satObject = sat.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    satArray.add(satObject.optString(pos));
                                }
                                classlists.add(satArray);

                                JSONArray sun = jsonObject.optJSONArray("sun");
                                ArrayList<String> sunArray = new ArrayList<>();
                                for (int i=0;i<sun.length();i++){
                                    JSONObject sunObject = sun.optJSONObject(i);
                                    String pos = ""+(i+1);
                                    sunArray.add(sunObject.optString(pos));
                                }
                                classlists.add(sunArray);


                                if (classScheduleExAdapter!=null){
                                    classScheduleExAdapter.notifyDataSetChanged();
                                }else {
                                    classScheduleExAdapter = new ClassScheduleExAdapter(classlists,SpaceClickClassActivity.this);
                                    class_schedule_list.setAdapter(classScheduleExAdapter);
                                }

                                int groupCount = class_schedule_list.getCount();
                                for (int i=0; i<groupCount; i++) {
                                    class_schedule_list.expandGroup(i);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("课表数据异常", "dsadsad");
                        }
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_class);
        mContext = this;
        ProgressViewUtil.show(mContext);
        mQueue = Volley.newRequestQueue(mContext);
        arrayList = new ArrayList<>();
        initView();
        new SpaceRequest(SpaceClickClassActivity.this,handler).classScheduleNew();

    }
    private void initView() {
        classlists = new ArrayList<>();
        class_schedule_list = (ExpandableListView) findViewById(R.id.class_schedule_list);
        class_schedule_list.setGroupIndicator(null);

        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        scca_top_title = (TextView) findViewById(R.id.scca_top_title);
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
