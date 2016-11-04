package cn.xiaocool.wxtparent.fragment;

/**
 * Created by mac on 16/1/24.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.app.ExitApplication;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.main.MeClickMyBabyActivity;
import cn.xiaocool.wxtparent.main.MeClickMyParentActivity;
import cn.xiaocool.wxtparent.main.QRCODEActivity;
import cn.xiaocool.wxtparent.main.SuggestListActivity;
import cn.xiaocool.wxtparent.main.SwingCardSettingActivity;
import cn.xiaocool.wxtparent.main.TeacherStyleActivity;
import cn.xiaocool.wxtparent.main.UserSettingActivity;
import cn.xiaocool.wxtparent.main.WebClickEditActivity;
import cn.xiaocool.wxtparent.net.request.MeRequest;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;
import cn.xiaocool.wxtparent.utils.HeadPicture;
import cn.xiaocool.wxtparent.utils.IntentUtils;

public class MeFragment extends Fragment implements OnClickListener {
    private IntentFilter myIntentFilter;
    private RelativeLayout myService;
    private RelativeLayout myBaby;
    private RelativeLayout invitefamily;
    private RelativeLayout me_qrCode;
    private RelativeLayout me_apply_expert;
    private RelativeLayout online_comment;
    private RelativeLayout erweima;
    private RelativeLayout shuaka;
    private RelativeLayout me_info,system;
    private TextView tv_name;
    private TextView tvServiceTime,tv_phone;
    private ImageView iv_head;
    private Activity mContext;
    private UserInfo user;
    private ArrayList<String> imgS = new ArrayList<String>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private SharedPreferences sp;
    private TextView tvStatus;
    private static final int GET_USET_WORKLIST_IMG_KEY = 2;
    private static final int SPECIAL_KEY = 3;
    private String phoneNum;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETSERVICESTATUS:
                    JSONObject objServiceStatus = (JSONObject) msg.obj;
                    try {
                        String status = objServiceStatus.getString("status");
                        String data = objServiceStatus.getString("data");
                        if (status.equals("success")){
                                JSONObject serviceStatus = new JSONObject(data);
                                String endTime = serviceStatus.getString("endtime");
                                String getStatus= serviceStatus.getString("status");
                                if(getStatus.equals("0")){
                                    tvStatus.setText("未开通");
                                    tvServiceTime.setText("无服务时间");
                                }else if (getStatus.equals("1")){
                                    tvStatus.setText("已开通");
                                    tvServiceTime.setText(endTime);
                                }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x520:
                    JSONObject jsonObject1 = (JSONObject) msg.obj;
                    try {
                        String status = jsonObject1.getString("status");
                        if (status.equals("success")){
                            JSONObject object = (JSONObject) jsonObject1.get("data");
                            phoneNum = object.getString("phone");
                            tv_phone.setText(phoneNum);
                            Log.e("phoneNum is",phoneNum);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private TextView me_btn_setting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        initView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ExitApplication.getInstance().removeActivity(mContext);
    }

    private void initView() {
        system = (RelativeLayout) getView().findViewById(R.id.system);
        system.setOnClickListener(this);
        me_info = (RelativeLayout) getView().findViewById(R.id.me_info);
        me_info.setOnClickListener(this);
        shuaka = (RelativeLayout) getView().findViewById(R.id.shuaka);
        shuaka.setOnClickListener(this);
        erweima = (RelativeLayout) getView().findViewById(R.id.erweima);
        erweima.setOnClickListener(this);
        online_comment = (RelativeLayout) getView().findViewById(R.id.online_comment);
        online_comment.setOnClickListener(this);
        iv_head = (ImageView) getView().findViewById(R.id.iv_me_fragment_avatar);
        me_btn_setting = (TextView)getView().findViewById(R.id.me_btn_setting);
        me_btn_setting.setOnClickListener(this);
        tv_name = (TextView) getView().findViewById(R.id.tv_me_fragment_name);
        myService = (RelativeLayout)getView().findViewById(R.id.me_service);
        myService.setOnClickListener(this);
        //我的宝宝
        myBaby = (RelativeLayout) getView().findViewById(R.id.me_mybaby);
        myBaby.setOnClickListener(this);
        //我的宝宝
        //初始化服务时间的tv
        tvServiceTime = (TextView) getView().findViewById(R.id.service_time);
        //初始化服务状态tv
        tvStatus = (TextView) getView().findViewById(R.id.tv_me_service_state);
        //生成二维码
       /* me_qrCode = (RelativeLayout)getView().findViewById(R.id.me_qrCode);
        me_qrCode.setOnClickListener(this);*/
        //扫描二维码
        /*me_apply_expert = (RelativeLayout) getView().findViewById(R.id.me_apply_expert);
        me_apply_expert.setOnClickListener(this);*/
        //邀请家人
        invitefamily = (RelativeLayout) getView().findViewById(R.id.me_myfamily);
        invitefamily.setOnClickListener(this);
        initServiceStatus();
        user = new UserInfo();
        // 显示图片的配置
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_square)
                .showImageOnFail(R.drawable.default_square).cacheInMemory(true)
                .cacheOnDisc(true).build();
        tv_phone = (TextView) getView().findViewById(R.id.tv_phone);
    }

    private void initServiceStatus() {
        new MeRequest(mContext,handler).serviceStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("xiaocool", "begingetdata");
        // 获取数据，网络获取数据并保存
        getData();
        // 数据显示
        displayData();
        new SpaceRequest(mContext,handler).getServicePhone(0x520);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转到用户设置
            case R.id.me_btn_setting:
                IntentUtils.getIntent(getActivity(), UserSettingActivity.class);
                break;
            case R.id.me_info:
                IntentUtils.getIntent(getActivity(), WebClickEditActivity.class);
                break;
            case R.id.system:
                Intent intent1 = new Intent(mContext,TeacherStyleActivity.class);
                intent1.putExtra("type","9");
                intent1.putExtra("title","系统通知");
                startActivity(intent1);
                break;
            case R.id.me_service:
                serviceDialog();
                break;
            case R.id.erweima:
                IntentUtils.getIntent(mContext, QRCODEActivity.class);
                break;
            case R.id.shuaka:
                IntentUtils.getIntent(mContext, SwingCardSettingActivity.class);
                break;
            case R.id.online_comment:
                IntentUtils.getIntent(mContext, SuggestListActivity.class);
                break;
            case R.id.me_mybaby:
                IntentUtils.getIntent(mContext, MeClickMyBabyActivity.class);
                break;
            case R.id.me_myfamily:
                IntentUtils.getIntent(mContext, MeClickMyParentActivity.class);
                break;
            /*case R.id.me_qrCode:
                IntentUtils.getIntent(mContext, GenerateQrCodeActivity.class);
                break;*/
            //扫描二维码
            /*case R.id.me_apply_expert:
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;*/
            default:
                break;
        }
    }

    /**
     * 数据的显示
     */
    private void displayData() {
        Log.e("xiaocool","displayData");
        user.readData(mContext);
        if (!user.getUserImg().equals("")) {
            imageLoader.displayImage(
                    NetBaseConstant.NET_CIRCLEPIC_HOST + "/" + user.getUserImg(),
                    iv_head, options);
        } else {
            new HeadPicture().getHeadPicture(iv_head);
        }
        Log.e("xiaocool",user.getUserName());
        tv_name.setText(user.getUserName());
    }
    /**
     * 拨打客服电话确认对话框
     */

    private void serviceDialog() {
        //获取客服电话
        new MeRequest(mContext,handler).onlineService();
        LayoutInflater inflaterDl = LayoutInflater.from(mContext);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.setting_service_dialog, null);

        // 对话框
        final Dialog dialog = new AlertDialog.Builder(mContext)
                .create();
        dialog.show();
        dialog.getWindow().setContentView(layout);

        // 取消按钮
        Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 确定按钮
        Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }
    /**
     * 获取数据，网络获取数据并保存
     */
    private void getData() {
        //获取服务状态
        getservicestate();
    }
    //获取个人的服务状态
    private void getservicestate(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mContext.RESULT_OK) {
            Bundle bundle = data.getExtras();
            //获取得到的结果
            String scanResult = bundle.getString("result");
        }
    }
}
