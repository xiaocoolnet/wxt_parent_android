package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ConfirmChildrenAdapter;
import cn.xiaocool.wxtparent.bean.ConfirmChildren;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;

/**
 * Created by Administrator on 2016/6/17 0017.
 */
public class Fragment_confim_ygq extends Fragment {

    private ListView listView_confim_dwc;
    private List<ConfirmChildren.ConfirmChildrenData> confirmChildrenDataList;
    private ConfirmChildrenAdapter confirmChildrenAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case CommunalInterfaces.CONFIRM_CHILDERN:

                    Log.e("qq", CommunalInterfaces.CONFIRM_CHILDERN + "");
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        try {
                            String state = obj.getString("status");
                            Log.e("qq", "1111");
                            if (state.equals(CommunalInterfaces._STATE)) {
                                Log.e("qq", "2222");
                                JSONArray dataArray = obj.getJSONArray("data");
                                confirmChildrenDataList.clear();
                                JSONObject itemObject;
                                for (int i = 0; i < dataArray.length(); i++) {

                                    itemObject = dataArray.getJSONObject(i);
                                    ConfirmChildren.ConfirmChildrenData confirmChildrenData = new ConfirmChildren.ConfirmChildrenData();
                                    confirmChildrenData.setId(itemObject.getString("id"));
                                    confirmChildrenData.setTeacherid(itemObject.getString("teacherid"));
                                    confirmChildrenData.setUserid(itemObject.getString("userid"));
                                    confirmChildrenData.setPhoto(itemObject.getString("photo"));
                                    confirmChildrenData.setDelivery_time(itemObject.getString("delivery_time"));
                                    confirmChildrenData.setDelivery_status(itemObject.getString("delivery_status"));
                                    confirmChildrenData.setParentid(itemObject.getString("parentid"));
                                    confirmChildrenData.setParenttime(itemObject.getString("parenttime"));
                                    confirmChildrenData.setTeachername(itemObject.getString("teachername"));
                                    confirmChildrenData.setTeacheravatar(itemObject.getString("teacheravatar"));
                                    confirmChildrenData.setTeacherphone(itemObject.getString("teacherphone"));
                                    confirmChildrenDataList.add(confirmChildrenData);
                                }
                                Log.e("qq", "3333");
                                //confirmChildrenAdapter = new ConfirmChildrenAdapter(confirmChildrenDataList, getActivity(),handler);
                                listView_confim_dwc.setAdapter(confirmChildrenAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.confim_dwc_fragment, container, false);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        confirmChildrenDataList = new ArrayList<>();

        listView_confim_dwc = (ListView) getView().findViewById(R.id.listView_confim_dwc);

        new SpaceRequest(getActivity(), handler).confirmChildren();
//        list_data_01 = new ArrayList<>();
//        list_data_02 = new ArrayList<>();
        News();

    }

    private void News() {
        new SpaceRequest(getActivity(), handler).addressParent();
    }

}
