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
import cn.xiaocool.wxtparent.adapter.TeacherListAdapter;
import cn.xiaocool.wxtparent.bean.Teacher;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;

public class Leave_tea_liebiao extends Activity {
    private Context context;
    private RelativeLayout up_jiantou;
    private ArrayList<Teacher> teachers;
    private ListView listView;
    private String childId;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.MESSAGEADDRESS:
                    JSONObject object = (JSONObject) msg.obj;
                    if(object.optString("status").equals("success")) {
                        JSONArray array = object.optJSONArray("data");
                        teachers.clear();
                        for(int i = 0;i < array.length();i++){
                            JSONObject object1 = array.optJSONObject(i);
                            JSONArray teacherArray = object1.optJSONArray("teacherlist");
                            for(int j=0;j<teacherArray.length();j++){
                                JSONObject teacherObject = teacherArray.optJSONObject(j);
                                Teacher teacher = new Teacher();
                                teacher.setClassid(object1.optString("classid"));
                                teacher.setClassName(object1.optString("classname"));
                                teacher.setId(teacherObject.optString("id"));
                                teacher.setName(teacherObject.optString("name"));
                                teacher.setPhoto(teacherObject.optString("photo"));
                                teachers.add(teacher);
                            }
                        }
                        TeacherListAdapter adapter = new TeacherListAdapter(context,teachers);
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
        setContentView(R.layout.activity_leave_tea_liebiao);
        context = this;
        childId = getIntent().getStringExtra("childId");
        initView();

    }

    private void initView() {
        teachers = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = teachers.get(position).getName();
                String teacherid = teachers.get(position).getId();
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("teacherid", teacherid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        /*teacher_ex_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String name = teachersAddresses.get(groupPosition).get(childPosition).get("name");
                String teacherid = teachersAddresses.get(groupPosition).get(childPosition).get("id");
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("teacherid", teacherid);
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });*/
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void News() {
        new SpaceRequest(context, handler).addressParentNew(childId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        News();
    }
}
