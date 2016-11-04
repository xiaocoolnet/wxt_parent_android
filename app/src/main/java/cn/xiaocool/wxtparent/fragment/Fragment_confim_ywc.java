package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.CompletedConfirmAdapter;
import cn.xiaocool.wxtparent.bean.ConfirmChildren;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;

/**
 * Created by Administrator on 2016/6/17 0017.
 */
public class Fragment_confim_ywc extends Fragment {

    private ListView listView_confim_dwc;
    private Context context;
    private List<ConfirmChildren.ConfirmChildrenData> confirmChildrenDataList;
    private CompletedConfirmAdapter confirmChildrenAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.CONFIRM_CHILDERN:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        ProgressViewUtil.dismiss();
                        Log.e("hello-------", obj.toString());
                        try {
                            String state = obj.getString("status");
                            if (state.equals(CommunalInterfaces._STATE)) {
                                JSONArray dataArray = obj.getJSONArray("data");
                                confirmChildrenDataList.clear();
                                JSONObject itemObject;
                                for (int i = 0; i < dataArray.length(); i++) {
                                    itemObject = dataArray.getJSONObject(i);
                                    ConfirmChildren.ConfirmChildrenData confirmChildrenData = new ConfirmChildren.ConfirmChildrenData();
                                    confirmChildrenData.setId(itemObject.optString("id"));
                                    confirmChildrenData.setTeacherid(itemObject.optString("teacherid"));
                                    confirmChildrenData.setUserid(itemObject.optString("userid"));
                                    confirmChildrenData.setPhoto(itemObject.optString("photo"));
                                    confirmChildrenData.setDelivery_time(itemObject.optString("delivery_time"));
                                    confirmChildrenData.setDelivery_status(itemObject.optString("delivery_status"));
                                    confirmChildrenData.setContent(itemObject.optString("content"));
                                    confirmChildrenData.setParentid(itemObject.optString("parentid"));
                                    confirmChildrenData.setParenttime(itemObject.optString("parenttime"));
                                    confirmChildrenData.setTeachername(itemObject.optString("teachername"));
                                    confirmChildrenData.setTeacherDuty(itemObject.optString("teacher_duty"));
                                    confirmChildrenData.setTeacheravatar(itemObject.optString("teacheravatar"));
                                    confirmChildrenData.setTeacherphone(itemObject.optString("teacherphone"));
                                    if (!confirmChildrenData.getDelivery_status().equals("0")) {
                                        confirmChildrenDataList.add(confirmChildrenData);
                                    }
                                }
                                Collections.sort(confirmChildrenDataList, new Comparator<ConfirmChildren.ConfirmChildrenData>() {
                                    @Override
                                    public int compare(ConfirmChildren.ConfirmChildrenData lhs, ConfirmChildren.ConfirmChildrenData rhs) {
                                        return Integer.parseInt(rhs.getId())-Integer.parseInt(lhs.getId());
                                    }
                                });
                                confirmChildrenAdapter = new CompletedConfirmAdapter(confirmChildrenDataList, context, handler);
                                listView_confim_dwc.setAdapter(confirmChildrenAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.confim_dwc_fragment, container, false);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        ProgressViewUtil.show(context);
        confirmChildrenDataList = new ArrayList<>();
        listView_confim_dwc = (ListView) getView().findViewById(R.id.listView_confim_dwc);
        new SpaceRequest(getActivity(), handler).confirmChildren();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh(){
        new SpaceRequest(getActivity(), handler).confirmChildren();
    }

}
