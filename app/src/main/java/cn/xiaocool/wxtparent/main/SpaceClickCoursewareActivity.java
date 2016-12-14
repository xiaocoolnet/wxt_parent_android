package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickCoursewareActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private ListView listView;
    private Context mContext;
    private int length;
    private String [] title;
    private String [] content;
    private String [] name;
    private String [] date;
    private SimpleAdapter simpleAdapter;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case CommunalInterfaces.CLASS_COURSEWARE:
                    JSONObject courseObj = (JSONObject) msg.obj;
                    try {
                        JSONArray courseArr = courseObj.getJSONArray("data");
                        length = courseArr.length();
                        JSONObject itemsObj;
                        title = new String[length];
                        content = new String[length];
                        name = new String [length];
                        date = new String [length];
                        ArrayList<HashMap<String,Object>> courseList = new ArrayList<HashMap<String,Object>>();
                        for (int i = 0;i < length;i ++){
                            itemsObj = (JSONObject) courseArr.get(i);
                            String itemTitle = itemsObj.getString("courseware_title");
                            String itemContent = itemsObj.getString("courseware_content");
                            String itemName = itemsObj.getString("releasename");
                            String itemDate = itemsObj.getString("courseware_time");
                            title[i] = itemTitle;
                            content[i] = itemContent;
                            name[i] = itemName;
                            date[i] = itemDate;
                            HashMap<String,Object> map = new HashMap<String,Object>();
                            map.put("title",title[i]);
                            map.put("content",content[i]);
                            map.put("name",name[i]);
                            map.put("date",date[i]);
                            courseList.add(map);
                         }
                        simpleAdapter = new SimpleAdapter(mContext,courseList,R.layout.space_baby_courseware_listitem,
                                new String []{"title","content","name","date"},new int[]{R.id.title,R.id.content,
                                R.id.name,R.id.date});
                        listView.setAdapter(simpleAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_baby_courseware);
        mContext = this;
        initView();
        //绑定ListView
        listView = (ListView) findViewById(R.id.baby_course_listcontent);
        //调用网络请求
        new SpaceRequest(mContext,handler).classCourseware();
    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
        }

    }


}
