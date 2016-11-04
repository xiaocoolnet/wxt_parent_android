package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.MyExpandableListViewAdapter;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.LogUtils;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MyAddressFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private ExpandableListView address_class;
    private List<Map<String,String>> classAddresses = new ArrayList<>();
    private List<List<Map<String,String>>> teachersAddresses = new ArrayList<>();
    private String teacherID;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CommunalInterfaces.TEACHER_ADDRESS:
                    if (msg.obj!=null){
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            String state = obj.getString("status");
                            if (state.equals(CommunalInterfaces._STATE)){
                                address_class = (ExpandableListView) getView().findViewById(R.id.address_class);
                                address_class.setGroupIndicator(null);
                                JSONArray dataArray = obj.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    Map<String, String> title = new HashMap<>();
                                    title.put("group", dataArray.getJSONObject(i).getString("classname"));
                                    classAddresses.add(title);
                                    JSONArray teachersPhoneArray = dataArray.getJSONObject(i).getJSONArray("teacherinfo");
                                    List<Map<String, String>> teacherInfo = new ArrayList<>();
                                    for (int j = 0; j < teachersPhoneArray.length(); j++) {
                                        Map<String, String> title_1_content_1 = new HashMap<String, String>();
                                        title_1_content_1.put("teacherName", teachersPhoneArray.getJSONObject(j).getString("name"));
                                        title_1_content_1.put("teacherPhone",teachersPhoneArray.getJSONObject(j).getString("phone"));
                                        title_1_content_1.put("teacherID",teachersPhoneArray.getJSONObject(j).getString("id"));
                                        teacherID = teachersPhoneArray.getJSONObject(j).getString("id");
                                        LogUtils.e("111", title_1_content_1.toString());
                                        teacherInfo.add(title_1_content_1);
                                    }
                                    teachersAddresses.add(teacherInfo);
                                }
                                // 加入列表
                                MyExpandableListViewAdapter sela = new MyExpandableListViewAdapter(classAddresses,teachersAddresses,context);
                                address_class.setAdapter(sela);
                                int groupCount = address_class.getCount();
                                for (int i=0; i<groupCount; i++) {
                                    address_class.expandGroup(i);
                                }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_teacher,null);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        new SpaceRequest(context,handler).teacherAddress();
    }
    @Override
    public void onClick(View v) {
    }
}
