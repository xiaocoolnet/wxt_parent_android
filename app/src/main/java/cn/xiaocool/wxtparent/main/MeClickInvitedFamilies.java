package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/3/23.
 */
public class MeClickInvitedFamilies extends BaseActivity {
    private Button invitefamilyBtn;
    private ListView invitefamilyListView;
    private String familyphone = "",familyname = "";
    private List<String> familyphoneList;
    private List<String> familynameList;
    private HashMap<String,String> map;
    private ImageView btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invitedfamilies);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        invitefamilyBtn = (Button) findViewById(R.id.invitefamilyBtn);
        invitefamilyListView = (ListView) findViewById(R.id.invitefamilyList);
        invitefamilyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeClickInvitedFamilies.this,MeClickInviteFamily.class);
                startActivityForResult(intent, 1);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            switch (resultCode){
                case RESULT_OK:
                    familyphone = data.getStringExtra("telephone");
                    familyname = data.getStringExtra("name");
                    break;
            }
        }
    }
    class InvitedFamiliesAdapter extends BaseAdapter{
        LayoutInflater inflater;
        public InvitedFamiliesAdapter(List<String> familyphoneList,List<String> familynameList) {
            this.inflater = LayoutInflater.from(MeClickInvitedFamilies.this);
            MeClickInvitedFamilies.this.familyphoneList = familyphoneList;
            MeClickInvitedFamilies.this.familynameList = familynameList;
        }

        @Override
        public int getCount() {
            return familyphoneList.size();
        }

        @Override
        public Object getItem(int position) {
            return familyphoneList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return convertView;
        }
        class ViewHolder {
            public ViewHolder(View convertView) {

            }
        }
    }
}
