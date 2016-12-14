package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.FriendAdapter;
import cn.xiaocool.wxtparent.bean.Friend;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;

/**
 * Created by Administrator on 2016/3/25.
 */
public class SpaceFriendsActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private ListView listView;
    private RelativeLayout rl_back;
    private LinearLayout ll_add_friend;
    private ArrayList<Friend> friends;
    private FriendAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETFRIEND:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    ProgressViewUtil.dismiss();
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject babyObject;
                        friends.clear();
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            babyObject = (JSONObject) jsonArray.get(i);
                            Friend friend = new Friend();
                            friend.setId(babyObject.optString("id"));
                            friend.setName(babyObject.optString("name"));
                            friend.setAvator(babyObject.optString("photo"));
                            friend.setNumber(babyObject.optString("blog_coutn"));
                            friends.add(friend);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = new FriendAdapter(context, friends);
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_baby_friends);
        context = this;
        ProgressViewUtil.show(context);
        friends = new ArrayList<>();
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.my_baby_content);
        ll_add_friend = (LinearLayout) findViewById(R.id.add_child);
        ll_add_friend.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SpaceFriendsActivity.this,FriendDiaryActivity.class);
                intent.putExtra("id",friends.get(position).getId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.add_child:
                Intent intent = new Intent(SpaceFriendsActivity.this,AddFriendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",friends);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SpaceRequest(context,handler).getFriend();
    }
}
