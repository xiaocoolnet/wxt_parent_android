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

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ConfirmChildrenAdapter;
import cn.xiaocool.wxtparent.bean.ConfirmChildren;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/17 0017.
 */
public class Fragment_confim_dwc extends Fragment {
    private Context context;
    private ListView listView_confim_dwc;
    private List<ConfirmChildren.ConfirmChildrenData> confirmChildrenDataList;
    private ConfirmChildrenAdapter confirmChildrenAdapter;
    private KProgressHUD hud;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.CONFIRM_CHILDERN:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        Log.e("hello-------",obj.toString());
                        ProgressViewUtil.dismiss();
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
                                    confirmChildrenData.setTeacheravatar(itemObject.optString("teacheravatar"));
                                    confirmChildrenData.setTeacherphone(itemObject.optString("teacherphone"));
                                    confirmChildrenData.setTeacherDuty(itemObject.optString("teacher_duty"));
                                    if (confirmChildrenData.getDelivery_status().equals("0")) {
                                        confirmChildrenDataList.add(confirmChildrenData);
                                    }
                                }
                                Collections.sort(confirmChildrenDataList, new Comparator<ConfirmChildren.ConfirmChildrenData>() {
                                    @Override
                                    public int compare(ConfirmChildren.ConfirmChildrenData lhs, ConfirmChildren.ConfirmChildrenData rhs) {
                                        return Integer.parseInt(rhs.getDelivery_time())-Integer.parseInt(lhs.getDelivery_time());
                                    }
                                });
                                confirmChildrenAdapter = new ConfirmChildrenAdapter(confirmChildrenDataList, context, handler,hud);
                                listView_confim_dwc.setAdapter(confirmChildrenAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case CommunalInterfaces.HANDLECONFIRM:
                    JSONObject object = (JSONObject) msg.obj;
                    String state = object.optString("status");
                    if (state.equals(CommunalInterfaces._STATE)) {
                        String data = object.optString("data");
                        if(data.equals("success")){
                            hud.dismiss();
                            ToastUtils.ToastShort(context,"处理成功");
                            refresh();
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
        confirmChildrenDataList = new ArrayList<>();
        context = getActivity();
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        listView_confim_dwc = (ListView) getView().findViewById(R.id.listView_confim_dwc);
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
