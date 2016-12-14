package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;

/**
 * Created by wzh on 2016/1/29.
 */

/**
 * 这个类没有用到
 */
public class SpaceClickNewsActivity2 extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private LinearLayout newsListItem;
    private LinearLayout newsListContent;
    private Context mContext = this;
    private TextView name,content;
    private String [] names;
    private String [] contents;
    private Handler handler = new Handler(){
      public void handleMessage(android.os.Message msg){
          switch (msg.what){
              case CommunalInterfaces.RECEIVED_MESSAGE:
                  JSONObject rm_obj = (JSONObject) msg.obj;
                  try {
                      String status = rm_obj.getString("status");
                      JSONArray items = rm_obj.getJSONArray("data");
                      JSONObject itemObject;
//                      newsListContent = new LinearLayout(mContext);
                        names = new String[items.length()];
                        contents = new String[items.length()];

                      //绑定ListView
                      ListView list = (ListView) findViewById(R.id.news_list_view);
                      //生成动态数组，加入数据
                      ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String, Object>>();


                      for (int i = 0;i <items.length();i ++){
                          itemObject = (JSONObject) items.get(i);
                          String receiveName = itemObject.getString("receive_user_name");
                          Log.e("receive_name",receiveName);
                          String messageContent = itemObject.getString("message_content");
                          Log.e("message_content",messageContent);
                          names[i] = receiveName;
                          contents[i] = messageContent;

                          HashMap<String,Object> map = new HashMap<String,Object>();
                          map.put("news_name",names[i]);
                          map.put("news_content", contents[i]);
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

                          SimpleAdapter listItemAdapter = new SimpleAdapter(SpaceClickNewsActivity2.this,listItem,R.layout.space_news_click_teacher0,
                                  new String[]{"news_name","news_content"},
                                  new int[] {R.id.news_name,R.id.news_content} );
                          list.setAdapter(listItemAdapter);
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
          }
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_news_click);
        mContext = this;
        initView();
        initList();
    }

    private void initList() {

    }


    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        //每条消息
        newsListItem = (LinearLayout) findViewById(R.id.space_news_list);
        //装消息的布局
        newsListContent = (LinearLayout) findViewById(R.id.news_list_content);
        name = (TextView) findViewById(R.id.news_name);
        content = (TextView) findViewById(R.id.news_content);
        receivedMessage();
    }

    private void receivedMessage() {
            new SpaceRequest(mContext,handler).ReceivedMessage();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
        }

    }


}
