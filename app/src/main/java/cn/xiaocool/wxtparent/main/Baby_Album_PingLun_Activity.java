package cn.xiaocool.wxtparent.main;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import java.util.List;
import java.util.Objects;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.Baby_Album_Pinglun_Adapter;

public class Baby_Album_PingLun_Activity extends BaseActivity {

    private ListView listView;
    private List<Objects> list;
    private Baby_Album_Pinglun_Adapter baby_album_pinglun_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_baby__album__ping_lun_);

        initData();

        listView.setAdapter(baby_album_pinglun_adapter);


    }

    private void initData() {

        listView= (ListView) findViewById(R.id.baby_album_pinglun_list);
        baby_album_pinglun_adapter=new Baby_Album_Pinglun_Adapter(this,list);


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
        }
    }


}
