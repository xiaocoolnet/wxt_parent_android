package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.fragment.Fragment_confim_dwc;
import cn.xiaocool.wxtparent.fragment.Fragment_confim_ygq;
import cn.xiaocool.wxtparent.fragment.Fragment_confim_ywc;
import cn.xiaocool.wxtparent.utils.SPUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickConfimActivity extends Activity implements View.OnClickListener {

    private ImageView btn_exit;
    private int index;
    private int currentTabIndex;
    private Fragment_confim_dwc fragment_confim_dwc;
    private Fragment_confim_ygq fragment_confim_ygq;
    private Fragment_confim_ywc fragment_confim_ywc;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;
    private RadioButton[] mTabs;
    private View line_daichuli, line_yiwancheng, line_yiguoqi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_confirm_click);
        SPUtils.remove(this,"JPUSHDAIJIE");
        initView();
        initData();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SPUtils.remove(this, "JPUSHDAIJIE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "JPUSHDAIJIE");
    }

    private void initData() {


    }

    private void initView() {
        fragment_confim_dwc = new Fragment_confim_dwc();
        fragment_confim_ywc = new Fragment_confim_ywc();
        fragment_confim_ygq = new Fragment_confim_ygq();
        fragmentManager = getFragmentManager();
        fragments = new Fragment[]{fragment_confim_dwc, fragment_confim_ywc};
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.re_fragment, fragment_confim_dwc);
        transaction.commit();
        mTabs = new RadioButton[2];
        mTabs[0] = (RadioButton) findViewById(R.id.btn_daichuli);
        mTabs[1] = (RadioButton) findViewById(R.id.btn_yiwancheng);
        //mTabs[2] = (RadioButton) findViewById(R.id.btn_yiguoqi);
        mTabs[0].setSelected(true);
        line_daichuli = findViewById(R.id.line_daichuli);
        line_yiwancheng = findViewById(R.id.line_yiwancheng);
        //line_yiguoqi = findViewById(R.id.line_yiguoqi);


        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
       //mTabs[2].setOnClickListener(this);
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);


//        lv_confirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(SpaceClickConfimActivity.this, SpaceClickSpecificConfirmActivity.class);
//                intent.putExtra("confirm_id", confirmChildrenDataList.get(position).getId());
//                intent.putExtra("confirm_teacher_id", confirmChildrenDataList.get(position).getTeacherid());
//                intent.putExtra("confirm_user_id", confirmChildrenDataList.get(position).getUserid());
//                intent.putExtra("confirm_photo", confirmChildrenDataList.get(position).getPhoto());
//                intent.putExtra("confirm_delivery_time", confirmChildrenDataList.get(position).getDelivery_time());
//                intent.putExtra("confirm_delivery_status", confirmChildrenDataList.get(position).getDelivery_status());
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_daichuli:
                index = 0;
                mTabs[0].setTextColor(getResources().getColor(R.color.title_bg_color));
                mTabs[1].setTextColor(getResources().getColor(R.color.black));
                //mTabs[2].setTextColor(getResources().getColor(R.color.black));
                line_daichuli.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                line_yiwancheng.setBackgroundColor(getResources().getColor(R.color.zsq_bg_color));
                //line_yiguoqi.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_yiwancheng:
                index = 1;
                mTabs[0].setTextColor(getResources().getColor(R.color.black));
                mTabs[1].setTextColor(getResources().getColor(R.color.title_bg_color));
                //mTabs[2].setTextColor(getResources().getColor(R.color.black));
                line_daichuli.setBackgroundColor(getResources().getColor(R.color.zsq_bg_color));
                line_yiwancheng.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                //line_yiguoqi.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            /*case R.id.btn_yiguoqi:
                index = 2;
                mTabs[0].setTextColor(getResources().getColor(R.color.black));
                mTabs[1].setTextColor(getResources().getColor(R.color.black));
                mTabs[2].setTextColor(getResources().getColor(R.color.title_bg_color));
                line_daichuli.setBackgroundColor(getResources().getColor(R.color.white));
                line_yiwancheng.setBackgroundColor(getResources().getColor(R.color.white));
                line_yiguoqi.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                break;*/

        }

        if (currentTabIndex != index)

        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                transaction.add(R.id.re_fragment, fragments[index]);
            }
            transaction.show(fragments[index]);
            transaction.commit();

        }

        mTabs[currentTabIndex].

                setSelected(false);

        mTabs[index].

                setSelected(true);

        currentTabIndex = index;


    }
}
