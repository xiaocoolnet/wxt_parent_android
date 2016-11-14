package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.AddressAdapter;
import cn.xiaocool.wxtparent.bean.Address;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class Tongxunlu_teacher_fragment extends Fragment {
    private ArrayList<Address> addresses;
    private ListView listView;
    private UserInfo userInfo;
    private SQLiteDatabase db;  //数据库对象
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.MESSAGEADDRESS:
                    ProgressViewUtil.dismiss();
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if (jsonObject.optString("status").equals(CommunalInterfaces._STATE)) {
                        JSONArray hwArray = jsonObject.optJSONArray("data");
                        JSONArray array = hwArray.optJSONObject(0).optJSONArray("teacherlist");
                        addresses.clear();
                        Log.e("hello-----", array.toString());
                        JSONObject itemObject;
                        for (int i = 0; i < array.length(); i++) {
                            itemObject = array.optJSONObject(i);
                            if (itemObject != null) {
                                Address address = new Address();
                                address.setId(itemObject.optString("id"));
                                address.setName(itemObject.optString("name"));
                                address.setPhone(itemObject.optString("phone"));
                                address.setAvator(itemObject.optString("photo"));
                                addresses.add(address);
//                                insertDataToTable(itemObject.optString("id"), itemObject.optString("name"), NetBaseConstant.NET_CIRCLEPIC_HOST+itemObject.optString("photo"));
                            }
                        }

                    }
                    AddressAdapter adapter = new AddressAdapter(addresses,getActivity());
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tongxunlu_teacher_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        addresses = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.list_address);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProgressViewUtil.show(getActivity());
        userInfo = new UserInfo(getActivity());
        userInfo.readData(getActivity());
//        //打开或者创建数据库, 这里是创建数据库
//        db = SQLiteDatabase.openOrCreateDatabase(getActivity().getFilesDir().toString() + "/users.db", null);
//        System.out.println(getActivity().getFilesDir().toString() + "/users.db");
//
//        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table';", null);
//        while (cursor.moveToNext()) {
//            //遍历出表名
//            String name = cursor.getString(0);
//            Log.i("System.out", name);
//        }
//        insertData(userInfo.getUserId(),userInfo.getUserName(),NetBaseConstant.NET_CIRCLEPIC_HOST+userInfo.getUserImg());
        getNews();

    }

    private void getNews() {
        new SpaceRequest(getActivity(), handler).addressParent();
    }

    /*
     * 插入或更新数据到数据库中
     */
    public void insertDataToTable(String userid, String username, String useravatar) {


        try {
            db = SQLiteDatabase.openOrCreateDatabase(getActivity().getFilesDir().toString() + "/users.db", null);
            Cursor cursor = db.rawQuery("select * from users_table where user_id=?", new String[]{userid});
            if (cursor.moveToFirst()) {
                updateData(userid, username, useravatar);
            } else {
                insertData(userid, username, useravatar);
            }
        } catch (SQLiteException exception) {
            db.execSQL("create table users_table (" +
                    "_id integer primary key autoincrement, " + "user_id varchar(50)," +
                    "user_name varchar(50), " +
                    "user_avatar varchar(500))");
            Log.e("数据库操作异常","生生世世是生生世世是三三三三三三三");
            insertData(userid, username, useravatar);

        }

    }

    /*
    * 向数据库中更新数据
    */
    private void updateData(String userid, String username, String useravatar) {
        db.execSQL("update users_table set user_name=? , user_avatar=? where user_id=?", new Object[]{username, useravatar, userid});

    }


    /*
     * 向数据库中插入数据
	 */
    private void insertData(String userid, String username, String useravatar) {
        db.execSQL("insert into users_table values(null,?, ?, ?)", new String[]{userid, username, useravatar});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (db.isOpen())db.close();
    }
}