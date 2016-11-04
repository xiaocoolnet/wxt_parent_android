package cn.xiaocool.wxtparent.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.main.SpaceClickRecipesActivity;
import cn.xiaocool.wxtparent.main.WebClickAnnouncementActivity;
import cn.xiaocool.wxtparent.main.WebClickClassActivity;
import cn.xiaocool.wxtparent.main.WebClickIntroduceActivity;
import cn.xiaocool.wxtparent.main.WebClickLeaveActivity;
import cn.xiaocool.wxtparent.main.WebClickRearingChildActivity;
import cn.xiaocool.wxtparent.main.WebClickRecruirActivity;
import cn.xiaocool.wxtparent.main.WebClickTeacherStyleActivity;
import cn.xiaocool.wxtparent.utils.IntentUtils;

/**
 * Created by mac on 16/1/25.
 */
public class WebFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout building, people, nearby;
    private TextView tv_red;
    //定义八个按钮的布局
    private RelativeLayout rlIntroduce,rlStyle,teacher_style,rlRecruit,rlClass,rlLeave;
    private RelativeLayout web_recipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        //初始化RL按钮组件
//        rlIntroduce = (RelativeLayout) getView().findViewById(R.id.web_introduce);
//        rlIntroduce.setOnClickListener(this);
//        rlStyle = (RelativeLayout) getView().findViewById(R.id.web_teacher_style);
//        rlStyle.setOnClickListener(this);
//        teacher_style = (RelativeLayout) getView().findViewById(R.id.web_teacher_style);
//        teacher_style.setOnClickListener(this);
//        web_recipes = (RelativeLayout)getView().findViewById(R.id.web_recipes);
//        web_recipes.setOnClickListener(this);
//        rlRecruit = (RelativeLayout) getView().findViewById(R.id.web_recruit);
//        rlRecruit.setOnClickListener(this);
//        rlClass = (RelativeLayout) getView().findViewById(R.id.web_class);
//        rlClass.setOnClickListener(this);
//        rlLeave = (RelativeLayout) getView().findViewById(R.id.web_leave);
//        rlLeave.setOnClickListener(this);

//        ImageView img_yuansuojieshao = (ImageView)getView().findViewById(R.id.web_introduce);
//        img_yuansuojieshao.setOnClickListener(this);
//        ImageView img_jiaoshifengcai = (ImageView)getView().findViewById(R.id.web_teacher_style);
//        img_jiaoshifengcai.setOnClickListener(this);
//        ImageView img_meizhoushipu = (ImageView)getView().findViewById(R.id.web_recipes);
//        img_meizhoushipu.setOnClickListener(this);
//        ImageView img_xiaoyuanzhaopin = (ImageView)getView().findViewById(R.id.web_recruit);
//        img_xiaoyuanzhaopin.setOnClickListener(this);
//        ImageView img_xingquban = (ImageView)getView().findViewById(R.id.web_class);
//        img_xingquban.setOnClickListener(this);
//        ImageView img_zaixianqingjia = (ImageView)getView().findViewById(R.id.web_leave);
//        img_zaixianqingjia.setOnClickListener(this);
//        RelativeLayout rl_announcement = (RelativeLayout) getView().findViewById(R.id.announcement);
//        rl_announcement.setOnClickListener(this);
//        RelativeLayout rl_rearing_child = (RelativeLayout) getView().findViewById(R.id.rearing_child);
//        rl_rearing_child.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.web_introduce:
//                IntentUtils.getIntent(getActivity(), WebClickIntroduceActivity.class);
//                break;
//            case R.id.web_teacher_style:
//                IntentUtils.getIntent(getActivity(), WebClickTeacherStyleActivity.class);
//                break;
//            case R.id.web_recipes:
//                IntentUtils.getIntent(getActivity(), SpaceClickRecipesActivity.class);
//                break;
//            case R.id.web_recruit:
//                IntentUtils.getIntent(getActivity(), WebClickRecruirActivity.class);
//                break;
//            case R.id.web_class:
//                IntentUtils.getIntent(getActivity(), WebClickClassActivity.class);
//                break;
//            case R.id.web_leave:
//                IntentUtils.getIntent(getActivity(), WebClickLeaveActivity.class);
//                break;
//            case R.id.announcement:
//                IntentUtils.getIntent(getActivity(), WebClickAnnouncementActivity.class);
//                break;
//            case R.id.rearing_child:
//                IntentUtils.getIntent(getActivity(), WebClickRearingChildActivity.class);
//                break;
//
//        }
    }

    public void refresh() {
        // TODO Auto-generated method stub
//        if (WxtApplication.professionCircleCount > 0) {
//            tv_red.setVisibility(View.VISIBLE);
//        } else {
//            tv_red.setVisibility(View.INVISIBLE);
//        }

    }
}