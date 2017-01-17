package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.StuParentAdapter;
import cn.xiaocool.wxtparent.bean.StuParent;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;

public class SpaceClickGroupActivity extends BaseActivity {

    private ExpandableListView list;
    private StuParentAdapter adapter;
    private ArrayList<StuParent> stuParents;
    private Context context;
    private UserInfo userInfo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETCLASSPARENT:
                    ProgressViewUtil.dismiss();
                    JSONObject object = (JSONObject) msg.obj;
                    if (object.optString("status").equals("success")) {
                        JSONArray jsonArray = object.optJSONArray("data").optJSONObject(0).optJSONArray("stu_par_list");
                        JSONObject itemObject;
                        stuParents.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            itemObject = jsonArray.optJSONObject(i);
                            StuParent stuParent = new StuParent();
                            stuParent.setName(itemObject.optString("name"));
                            stuParent.setPhoto(itemObject.optString("photo"));
                            List<StuParent.ParentInfoBean> parentInfoBeans = new ArrayList<>();
//                            JSONArray parentArray = itemObject.optJSONArray("parent_info");
//                            for(int j = 0;j<parentArray.length();j++){
//                                JSONObject parentObject = parentArray.optJSONObject(j);
//                                StuParent.ParentInfoBean parentInfoBean = new StuParent.ParentInfoBean();
//                                parentInfoBean.setName(parentObject.optString("name"));
//                                parentInfoBean.setPhoto(parentObject.optString("photo"));
//                                parentInfoBean.setAppellation(parentObject.optString("appellation"));
//                                parentInfoBean.setPhone(parentObject.optString("phone"));
//                                parentInfoBeans.add(parentInfoBean);
//                            }
                            stuParent.setParent_info(parentInfoBeans);
                            if(!userInfo.getChildId().equals(itemObject.optString("id"))){
                                stuParents.add(stuParent);
                            }
                        }
                        adapter = new StuParentAdapter(context, stuParents);
                        list.setAdapter(adapter);
                        list.setGroupIndicator(null);
                    }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_space_click_group);

        context = this;
        ProgressViewUtil.show(context);
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        stuParents = new ArrayList<>();
        list = (ExpandableListView) findViewById(R.id.list);
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SpaceRequest(context,handler).getClassParent();
    }
}
