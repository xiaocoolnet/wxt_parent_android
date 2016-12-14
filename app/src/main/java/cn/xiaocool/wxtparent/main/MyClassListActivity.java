package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.EListAdapter;
import cn.xiaocool.wxtparent.bean.Child;
import cn.xiaocool.wxtparent.bean.Group;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;


/**
 * Created by Administrator on 2016/5/11.
 */
public class MyClassListActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Group> groups;
    private ExpandableListView listView;
    private EListAdapter adapter;
    private CheckBox quan_check;
    private int size;
    private TextView down_selected_num;
    private RelativeLayout up_jiantou, btn_finish;
    private ArrayList<Child> selectedUsers;
    private ArrayList<String> selectedIds, selectedNames;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETCLASSPARENT:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj.optString("status").equals(CommunalInterfaces._STATE)) {
                            groups = new ArrayList<Group>();
                            JSONArray dataArray = obj.optJSONArray("data").optJSONObject(0).optJSONArray("stu_par_list");
                            JSONObject itemObject;
                            for (int i = 0; i < dataArray.length(); i++) {
                                itemObject = dataArray.optJSONObject(i);
                                Group group = new Group(itemObject.optString("id"), itemObject.optString("name"));
                                JSONArray stuArray = itemObject.optJSONArray("parent_info");
                                for (int j = 0; j < stuArray.length(); j++) {
                                    JSONObject object = stuArray.optJSONObject(j);
                                    Child child = new Child(object.optString("id"), object.optString("name"),
                                            itemObject.optString("name"));
                                    group.addChildrenItem(child);
                                }
                                groups.add(group);
                            }
                            adapter = new EListAdapter(MyClassListActivity.this, groups, quan_check, down_selected_num);
                            listView.setAdapter(adapter);
                            listView.setOnChildClickListener(adapter);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        initView();
    }

    private void initView() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        btn_finish = (RelativeLayout) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);
        quan_check = (CheckBox) findViewById(R.id.quan_check);
        quan_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });
        listView = (ExpandableListView) findViewById(R.id.listView);
        listView.setGroupIndicator(null);
        down_selected_num = (TextView) findViewById(R.id.down_selected_num);
        new SpaceRequest(MyClassListActivity.this,handler).getClassParent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.btn_finish:
                getAllMenbers();
                if (selectedIds.size() > 0) {

                    Intent intent = new Intent();
                    intent.putExtra("sss", "ssssss");
                    intent.putStringArrayListExtra("ids", selectedIds);
                    intent.putStringArrayListExtra("names", selectedNames);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {

                    ToastUtils.ToastShort(this, "请选择接收人！");
                }

                break;
        }
    }

    private void checkAll() {
        if (quan_check.isChecked()) {
            size = 0;
            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).setChecked(true);
                size += groups.get(i).getChildrenCount();
                for (int j = 0; j < groups.get(i).getChildrenCount(); j++) {
                    groups.get(i).getChildItem(j).setChecked(true);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setOnChildClickListener(adapter);
            for (int i = 0; i < groups.size(); i++) {
                listView.expandGroup(i);
            }
            down_selected_num.setText("已选择" + size + "人");

        } else {
            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).setChecked(false);
                for (int j = 0; j < groups.get(i).getChildrenCount(); j++) {
                    groups.get(i).getChildItem(j).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setOnChildClickListener(adapter);
            for (int i = 0; i < groups.size(); i++) {
                listView.expandGroup(i);
            }
            down_selected_num.setText("已选择0人");
        }
    }

    /**
     * 获取群组初始人员
     */
    private void getAllMenbers() {
        ArrayList<String> newG = new ArrayList<>();
        selectedUsers = new ArrayList<>();
        selectedIds = new ArrayList<>();
        selectedNames = new ArrayList<>();
        for (int i = 0; i < adapter.getterGroups().size(); i++) {
            for (int j = 0; j < adapter.getterGroups().get(i).getChildrenCount(); j++) {
                if (adapter.getterGroups().get(i).getChildItem(j).getChecked()) {

                    Child child = new Child(adapter.getterGroups().get(i).getChildItem(j).getUserid(), adapter.getterGroups().get(i).getChildItem(j).getFullname(), adapter.getterGroups().get(i).getChildItem(j).getUsername());
                    selectedUsers.add(child);
                    selectedIds.add(adapter.getterGroups().get(i).getChildItem(j).getUserid());
                    selectedNames.add(adapter.getterGroups().get(i).getChildItem(j).getFullname());
                } else {
                    Log.e("checked", String.valueOf(adapter.getterGroups().get(i).getChildItem(j).getChecked()));

                }

            }
        }

    }


}
