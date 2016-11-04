package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.fragment.FamilyPhotoFragment;
import cn.xiaocool.wxtparent.fragment.LifePhotoFragment;
import cn.xiaocool.wxtparent.fragment.VoiceBroadcastFragment;

public class SwingCardSettingActivity extends Activity implements View.OnClickListener {
    private Context context;
    private RelativeLayout btn_exit;
    private Fragment[] fragments;
    private LifePhotoFragment lifePhotoFragment;
    private FamilyPhotoFragment familyPhotoFragment;
    private VoiceBroadcastFragment voiceBroadcastFragment;
    private FragmentManager fragmentManager;
    private TextView[] btns;
    private int index;
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_swing_card_setting);
        context = this;
        initView();
        lifePhotoFragment = new LifePhotoFragment();
        familyPhotoFragment = new FamilyPhotoFragment();
        voiceBroadcastFragment = new VoiceBroadcastFragment();
        fragments = new Fragment[]{lifePhotoFragment,familyPhotoFragment,voiceBroadcastFragment};
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, lifePhotoFragment);
        fragmentTransaction.commit();
        fragmentManager = getFragmentManager();
    }

    private void initView() {
        btn_exit = (RelativeLayout) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        btns = new TextView[3];
        btns[0] = (TextView) findViewById(R.id.btn1);
        btns[0].setOnClickListener(this);
        btns[1] = (TextView) findViewById(R.id.btn2);
        btns[1].setOnClickListener(this);
        btns[2] = (TextView) findViewById(R.id.btn3);
        btns[2].setOnClickListener(this);


        btns[0].setSelected(true);
        btns[0].setBackgroundColor(Color.parseColor("#9BE5B4"));
        btns[0].setTextColor(Color.parseColor("#ffffff"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn1:
                index = 0;
                break;
            case R.id.btn2:
                index = 1;
                break;
            case R.id.btn3:
                index = 2;
                break;
        }
        if (currentIndex != index){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()){
                transaction.add(R.id.fragment_container,fragments[index]);
            }
            transaction.show(fragments[index]);
            transaction.commit();
        }
        btns[currentIndex].setSelected(false);
        btns[currentIndex].setTextColor(Color.parseColor("#000000"));
        btns[currentIndex].setBackgroundColor(Color.parseColor("#FFFFFF"));
        btns[index].setSelected(true);
        btns[index].setBackgroundColor(Color.parseColor("#9BE5B4"));
        btns[index].setTextColor(Color.parseColor("#ffffff"));
        currentIndex = index;
    }
}
