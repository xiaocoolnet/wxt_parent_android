package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.CoursewareAdapter;
import cn.xiaocool.wxtparent.bean.CourseInfo;
import cn.xiaocool.wxtparent.bean.Courseware;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;

public class CoursewareActivity extends Activity implements View.OnClickListener {
    private Context context;
    private ArrayList<Courseware> coursewares;
    private RelativeLayout rl_back;
    private ListView listView;
    private CoursewareAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x110:
                    JSONObject obj = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = obj.optJSONArray("data");
                        coursewares.clear();
                        JSONObject itemObject;
                        for (int i = 0; i < hwArray.length(); i++) {
                            itemObject = hwArray.optJSONObject(i);
                            Courseware courseware = new Courseware();
                            courseware.setId(itemObject.optString("id"));
                            courseware.setSubject(itemObject.optString("subject"));
                            JSONArray courseArray = itemObject.optJSONArray("courseware_info");
                            ArrayList<CourseInfo> courseInfos = new ArrayList<>();
                            JSONObject courseObject;
                            for (int j = 0; j < courseArray.length(); j++) {
                                courseObject = courseArray.optJSONObject(j);
                                CourseInfo courseInfo = new CourseInfo();
                                courseInfo.setId(courseObject.optString("courseware_id"));
                                courseInfo.setSubjectId(courseObject.optString("subjectid"));
                                courseInfo.setTitle(courseObject.optString("courseware_title"));
                                courseInfo.setContent(courseObject.optString("courseware_content"));
                                courseInfo.setTime(courseObject.optString("courseware_time"));
                                courseInfo.setTeacherName(courseObject.optString("teacher_name"));
                                courseInfo.setTeacherAvatar(courseObject.optString("teacher_photo"));
                                courseInfo.setDesignation(courseObject.optString("teacher_duty"));
                                ArrayList<String> pics = new ArrayList<>();
                                JSONArray picArray = courseObject.optJSONArray("pic");
                                for (int k = 0; k < picArray.length(); k++) {
                                    if(!picArray.optJSONObject(k).optString("picture_url").equals("")&&!picArray.optJSONObject(k).optString("picture_url").equals("null")){
                                        pics.add(picArray.optJSONObject(k).optString("picture_url"));
                                    }
                                }
                                courseInfo.setPics(pics);
                                courseInfos.add(courseInfo);
                            }
                            courseware.setCourseInfos(courseInfos);
                            coursewares.add(courseware);
                        }
                        adapter = new CoursewareAdapter(context,coursewares);
                        listView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_courseware);
        context = this;
        ProgressViewUtil.show(context);
        initView();
    }

    private void initView() {
        coursewares = new ArrayList<>();
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CoursewareActivity.this,CourseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",coursewares.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
        }
    }

    private void getAllInformation(){
        new SpaceRequest(context,handler).getCourseware(0X110);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllInformation();
    }
}
