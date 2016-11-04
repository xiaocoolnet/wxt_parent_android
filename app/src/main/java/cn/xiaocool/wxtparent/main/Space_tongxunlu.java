package cn.xiaocool.wxtparent.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.TongXunLu_Adapter;
import cn.xiaocool.wxtparent.fragment.Tongxunlu_parent_fragment;
import cn.xiaocool.wxtparent.fragment.Tongxunlu_teacher_fragment;
import cn.xiaocool.wxtparent.fragment.Tongxunlu_friend_fragment;

public class Space_tongxunlu extends BaseActivity implements View.OnClickListener {

    private ImageView exit;
    private TongXunLu_Adapter tongXunLu_adapter;
    private List<String> list = new ArrayList<>();
    private Tongxunlu_teacher_fragment tongxunlu_teacher_fragment;
    private Tongxunlu_parent_fragment tongxunlu_friend_fragment;
    private FragmentManager fragmentManager;
    private RadioButton[] mTabs;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private View line_laoshi, line_haoyou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_space_tongxunlu);
        init();
    }
    private void init() {
        tongxunlu_teacher_fragment = new Tongxunlu_teacher_fragment();
        tongxunlu_friend_fragment = new Tongxunlu_parent_fragment();
        fragments = new Fragment[]{tongxunlu_teacher_fragment, tongxunlu_friend_fragment};
        fragmentManager = getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.rel_tongxunlu, tongxunlu_teacher_fragment);
        transaction.commit();
        initView();
        //退出按钮
        exit = (ImageView) findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mTabs = new RadioButton[2];
        mTabs[0] = (RadioButton) findViewById(R.id.btn_laoshi);
        mTabs[1] = (RadioButton) findViewById(R.id.btn_haoyou);
        mTabs[0].setSelected(true);
        line_laoshi = findViewById(R.id.line_laoshi);
        line_haoyou = findViewById(R.id.line_haoyou);
        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_laoshi:
                index = 0;
                mTabs[0].setTextColor(getResources().getColor(R.color.title_bg_color));
                mTabs[1].setTextColor(getResources().getColor(R.color.black));
                line_laoshi.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                line_haoyou.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_haoyou:
                index = 1;
                mTabs[0].setTextColor(getResources().getColor(R.color.black));
                mTabs[1].setTextColor(getResources().getColor(R.color.title_bg_color));
                line_laoshi.setBackgroundColor(getResources().getColor(R.color.white));
                line_haoyou.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                break;

        }

        if (currentTabIndex != index)

        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                transaction.add(R.id.rel_tongxunlu, fragments[index]);

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

