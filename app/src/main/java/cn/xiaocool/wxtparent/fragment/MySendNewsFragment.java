package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.main.HotNewsSendActivity;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;

/**
 * Created by wzh on 2016/3/13.
 */
public class MySendNewsFragment extends Fragment implements View.OnClickListener {
    private ImageView btn_exit;
    private LinearLayout newsListItem;
    private LinearLayout newsListContent;
    private FragmentActivity mContext;
    private TextView name, content;
    private String[] names;
    private String[] contents;
    private String[] sendTime;
    private String[] receiveID;
    private ListView list;
    private SimpleAdapter listItemAdapter;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.SEND_MESSAGE:
                    JSONObject rm_obj = (JSONObject) msg.obj;
                    try {
                        String status = rm_obj.getString("status");
                        JSONArray items = rm_obj.getJSONArray("data");
                        JSONObject itemObject;
//                      newsListContent = new LinearLayout(mContext);
                        names = new String[items.length()];
                        contents = new String[items.length()];
                        sendTime = new String[items.length()];
                        receiveID = new String[items.length()];
                        //绑定ListView

                        //生成动态数组，加入数据
                        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();


                        for (int i = 0; i < items.length(); i++) {
                            itemObject = (JSONObject) items.get(i);
                            String sendName = itemObject.getString("send_user_name");
                            Log.e("send_name", sendName);
                            String messageContent = itemObject.getString("message_content");
                            Log.e("send_content", messageContent);
                            String messageTime = itemObject.getString("message_time");
                            String messageReceiveID = itemObject.getString("receive_user_id");
                            names[i] = sendName;
                            contents[i] = messageContent;
                            sendTime[i] = messageTime;
                            receiveID[i] = messageReceiveID;
                            Log.e("fragment2 is enter", "yes");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("news_name", names[i]);
                            map.put("news_content", contents[i]);
                            map.put("news_time", sendTime[i]);
                            map.put("news_receive",receiveID[i]);
                            //添加并显示
                            listItem.add(map);
//                          newsListItem = new LinearLayout(mContext);
//                          name = new TextView(mContext);
//                          content = new TextView(mContext);
//                          newsListContent.removeAllViews();
//                          newsListContent.addView(newsListItem);
//                          name.setText(receiveName);
//                          Log.e("set_text_seccussful","yes");
//                          content.setText(messageContent);
                        }

                        listItemAdapter = new SimpleAdapter(mContext, listItem, R.layout.space_news_click_teacher0,
                                new String[]{"news_name", "news_content"},
                                new int[]{R.id.news_name, R.id.news_content});
                        list.setAdapter(listItemAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.space_news_item_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (FragmentActivity) getActivity();
        initView();
        initList();
        //绑定listview
        list = (ListView) getView().findViewById(R.id.news_list_view);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> map = (HashMap<String, Object>) listItemAdapter.getItem(position);
                String intentNewsName = (String) map.get("news_name");
                String intentNewsContent = (String) map.get("news_content");
                String intentNewsTime = (String) map.get("news_time");
                String intentReceivreID = (String) map.get("news_receive");
                Intent intent = new Intent();
                intent.putExtra("name", intentNewsName);
                intent.putExtra("content", intentNewsContent);
                intent.putExtra("time", intentNewsTime);
                intent.putExtra("receiveID",intentReceivreID);
                intent.setClass(mContext, HotNewsSendActivity.class);
                startActivity(intent);

            }
        });

    }


    private void initList() {

    }


    private void initView() {

        //每条消息
        newsListItem = (LinearLayout) getView().findViewById(R.id.space_news_list);
        //装消息的布局
        newsListContent = (LinearLayout) getView().findViewById(R.id.news_list_content);
        name = (TextView) getView().findViewById(R.id.news_name);
        content = (TextView) getView().findViewById(R.id.news_content);
        receivedMessage();
    }

    private void receivedMessage() {
        new SpaceRequest(mContext, handler).sendMessage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }
}
