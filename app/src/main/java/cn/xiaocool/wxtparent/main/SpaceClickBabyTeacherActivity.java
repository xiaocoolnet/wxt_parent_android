package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.BabyTeacherAdapter;
import cn.xiaocool.wxtparent.bean.BabyTeachers;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickBabyTeacherActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private String data;

    private EditText baby_teacher_edit;

    private ListView lv_baby_teacher;
    private BabyTeacherAdapter babyTeacherAdapter;
    private List<BabyTeachers.BabyTeacherBean> babyTeacherBeanList;
    private List<BabyTeachers.BabyTeacherBean> babyTeacherBeanList_02;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Runnable eChanged = new Runnable() {
//
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    String data = baby_teacher_edit.getText().toString();
//
//                    babyTeacherBeanList.clear();
//
//                    getmDataSub(babyTeacherBeanList, data);
//
//                    babyTeacherAdapter.notifyDataSetChanged();
//
//                }
//            };


            switch (msg.what) {
                case CommunalInterfaces.BABY_TEACHER:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            String state = obj.getString("status");
                            if (state.equals(CommunalInterfaces._STATE)) {
                                JSONArray dataArray = obj.getJSONArray("data");
                                JSONObject itemObject;
                                for (int i = 0; i < dataArray.length(); i++) {
                                    itemObject = dataArray.getJSONObject(i);
                                    BabyTeachers.BabyTeacherBean babyTeacherBean = new BabyTeachers.BabyTeacherBean();
                                    babyTeacherBean.setTeachername(itemObject.getString("teachername"));
                                    babyTeacherBean.setTeacherphone(itemObject.getString("teacherphone"));
                                    babyTeacherBeanList.add(babyTeacherBean);
                                    babyTeacherBeanList_02.add(babyTeacherBean);
                                }

                                babyTeacherAdapter = new BabyTeacherAdapter(babyTeacherBeanList, SpaceClickBabyTeacherActivity.this);
                                lv_baby_teacher.setAdapter(babyTeacherAdapter);
                                babyTeacherAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.space_baby_teacher);
        initView();
    }

    private void initView() {
        babyTeacherBeanList_02 = new ArrayList<>();
        babyTeacherBeanList = new ArrayList<>();
        baby_teacher_edit = (EditText) findViewById(R.id.baby_teacher_edit);


        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        lv_baby_teacher = (ListView) findViewById(R.id.lv_baby_teacher);


        baby_teacher_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void afterTextChanged(Editable s) {
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
//                data = baby_teacher_edit.getText().toString().trim();
//                Log.e("data", data);
                handler.post(eChanged);


            }
        });


        new SpaceRequest(SpaceClickBabyTeacherActivity.this, handler).babyTeacher();
    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            data = baby_teacher_edit.getText().toString().trim();
            Log.e("data", data);
//            babyTeacherBeanList_02 = babyTeacherBeanList;

            babyTeacherBeanList.clear();//先要清空，不然会叠加

            getmDataSub(data);//获取更新数据

            babyTeacherAdapter.notifyDataSetChanged();//更新

        }
    };

    private void getmDataSub(String data) {
        int length = babyTeacherBeanList_02.size();
        Log.e("data1", "length" + length);
        for (int i = 0; i < length; ++i) {

            //搜索电话
            //  || babyTeacherBeanList_02.get(i).getTeacherphone().contains(data)
            if (babyTeacherBeanList_02.get(i).getTeachername().contains(data) ) {

                babyTeacherBeanList.add(babyTeacherBeanList_02.get(i));

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
        }

    }


}
