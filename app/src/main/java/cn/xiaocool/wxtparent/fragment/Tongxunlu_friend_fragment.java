package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.content.Context;
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

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ConfirmChildrenAdapter;
import cn.xiaocool.wxtparent.adapter.FriendAdapter;
import cn.xiaocool.wxtparent.bean.ConfirmChildren;
import cn.xiaocool.wxtparent.bean.Friend;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class Tongxunlu_friend_fragment extends Fragment {
    private ListView list;
    private FriendAdapter adapter;
    private ArrayList<Friend> friends;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.BABYFRIEND:
                    JSONObject object = (JSONObject) msg.obj;
                    if (object.optString("status").equals("success")) {
                        JSONArray jsonArray = object.optJSONArray("data");
                        JSONObject itemObject;
                        friends.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            itemObject = jsonArray.optJSONObject(i);
                            Friend friend = new Friend();
                            friend.setId(itemObject.optString("id"));
                            friend.setName((itemObject.optString("name")));
                            friend.setAvator(itemObject.optString("photo"));
                            friends.add(friend);
                        }
                        adapter = new FriendAdapter(context, friends);
                        list.setAdapter(adapter);
                    }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tongxunlu_friend_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        list = (ListView) view.findViewById(R.id.list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        friends = new ArrayList<>();
        new SpaceRequest(context, handler).getBabyFriend();
    }
}
