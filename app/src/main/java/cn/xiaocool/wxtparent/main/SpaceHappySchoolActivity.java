package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.HappySchoolAdapter;
import cn.xiaocool.wxtparent.bean.HappySchool;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.RearingChildListView;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SpaceHappySchoolActivity extends Activity {
    private ImageView btn_exit;
    private RearingChildListView happySchoolLv;
    private HappySchoolAdapter happySchoolAdapter;
    private List<HappySchool.HappySchoolData> happySchoolDataList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CommunalInterfaces.HAPPY_SCHOOL:
                    if (msg.obj!=null){
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            String status = obj.getString("status");
                            if (status.equals(CommunalInterfaces._STATE)){
                                JSONArray dataArray = obj.getJSONArray("data");
                                JSONObject itemObject;
                                for (int i = 0; i < dataArray.length(); i++) {
                                    itemObject = dataArray.getJSONObject(i);
                                    HappySchool.HappySchoolData happySchoolData = new HappySchool.HappySchoolData();
                                    happySchoolData.setReleasename(itemObject.getString("releasename"));
                                    happySchoolData.setHappy_content(itemObject.getString("happy_content"));
                                    happySchoolData.setHappy_pic(itemObject.getString("happy_pic"));
                                    happySchoolData.setHappy_title(itemObject.getString("happy_title"));
                                    happySchoolData.setHappy_time(itemObject.getString("happy_time"));
                                    happySchoolDataList.add(happySchoolData);
                                }
                                happySchoolAdapter = new HappySchoolAdapter(SpaceHappySchoolActivity.this,happySchoolDataList);
                                happySchoolLv.setAdapter(happySchoolAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        setContentView(R.layout.space_happy_school_activity);
        initView();
    }

    private void initView() {
        happySchoolDataList = new ArrayList<>();
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        happySchoolLv = (RearingChildListView) findViewById(R.id.space_happySchoolLv);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new SpaceRequest(SpaceHappySchoolActivity.this,handler).happySchool();
        happySchoolLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SpaceHappySchoolActivity.this,SpaceHappySchoolSpecificActivity.class);
                intent.putExtra("happy_title", happySchoolDataList.get(position).getHappy_title());
                intent.putExtra("happy_pic", happySchoolDataList.get(position).getHappy_pic());
                intent.putExtra("happy_content", happySchoolDataList.get(position).getHappy_content());
                intent.putExtra("releasename", happySchoolDataList.get(position).getReleasename());
                startActivity(intent);
            }
        });

    }
}
