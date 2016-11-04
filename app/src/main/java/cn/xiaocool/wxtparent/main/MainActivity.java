package cn.xiaocool.wxtparent.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jauker.widget.BadgeView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.Constant;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.app.ExitApplication;
import cn.xiaocool.wxtparent.bean.CheckVersionModel;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.fragment.DiaryFragment;
import cn.xiaocool.wxtparent.fragment.FindFragment;
import cn.xiaocool.wxtparent.fragment.MeFragment;
import cn.xiaocool.wxtparent.fragment.ParadiseFragment;
import cn.xiaocool.wxtparent.fragment.SpaceFragment;
import cn.xiaocool.wxtparent.fragment.WebFragment;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.view.NiceDialog;
import cn.xiaocool.wxtparent.view.WxtApplication;
import cn.xiaocool.wxtparent.view.update.UpdateService;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button[] mTabs;
    private MeFragment meFragment;
    private SpaceFragment spaceFragment;
    private WebFragment webFragment;
    private DiaryFragment diaryFragment;
    private ParadiseFragment paradiseFragment;
    private FindFragment findingFragment;
    private Fragment[] fragments;
    private int index;
    private UserInfo user = new UserInfo();
    private SharedPreferences sp;
    // 当前fragment的index
    private int currentTabIndex;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private LinearLayout main;
    private Context context;
    private BadgeView bv_first;
    private static final String JPUSHMESSAGE = "JPUSHMESSAGE";
    private static final String JPUSHHOMEWORK = "JPUSHHOMEWORK";
    private static final String JPUSHTRUST = "JPUSHTRUST";
    private static final String JPUSHNOTICE = "JPUSHNOTICE";
    private static final String JPUSHDAIJIE = "JPUSHDAIJIE";
    private static final String JPUSHLEAVE = "JPUSHLEAVE";
    private static final String JPUSHACTIVITY = "JPUSHACTIVITY";
    private static final String JPUSHCOMMENT = "JPUSHCOMMENT";

    Message m = null;
    private static final int REQUEST_WRITE_STORAGE = 111;
    private NiceDialog mDialog = null;
    private CheckVersionModel versionModel;
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //微信分享平台

    public static final String APP_ID_WEIXIN = "wx1929ca4290231712";
    public static IWXAPI api_weixin;

    public void regToWx() {
        api_weixin = WXAPIFactory.createWXAPI(this, APP_ID_WEIXIN);
        api_weixin.registerApp(APP_ID_WEIXIN);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////.


    public LinearLayout test(){
        main = (LinearLayout) findViewById(R.id.main_bottom);
        return main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        user.readData(MainActivity.this);
        context = this;
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        this.registerReceiver(new Receiver(),filter);
        sp = context.getSharedPreferences("list", context.MODE_PRIVATE);


        JPushInterface.resumePush(this);

        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        //regToWx();

        initView();

        meFragment = new MeFragment();
        spaceFragment = new SpaceFragment();
        findingFragment = new FindFragment();
        diaryFragment = new DiaryFragment();
        paradiseFragment = new ParadiseFragment();
        webFragment = new WebFragment();

        fragments = new Fragment[]{spaceFragment, diaryFragment, findingFragment, paradiseFragment, meFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, spaceFragment).add(R.id.fragment_container, diaryFragment).hide(diaryFragment).show(spaceFragment)
                .commit();

        mDialog = new NiceDialog(MainActivity.this);

        checkVersion();
    }
    /**
     * 检查版本更新
     */
    private void checkVersion() {
        String versionId = getResources().getString(R.string.version_id).toString();

        String url =  "http://wxt.xiaocool.net/index.php?g=apps&m=index&a=CheckVersion_Parent&type=android&versionid="+versionId;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String result) {
                Log.d("onResponse", result);
                if (JSONparser(context,result)){
                    versionModel = getBeanFromJson(result);
                    showDialogByYorNo(versionModel.getVersionid());
                    ProgressViewUtil.dismiss();
                }else {

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                ProgressViewUtil.dismiss();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    //展示dialog
    private void showDialogByYorNo(String versionid) {

        if (Integer.valueOf(versionid)>Integer.valueOf(getResources().getString(R.string.version_id).toString())){
            mDialog.setTitle("发现新版本");
            mDialog.setContent(versionModel.getDescription());
            mDialog.setOKButton("立即更新", new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //请求存储权限
                    boolean hasPermission = (ContextCompat.checkSelfPermission(context,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                        ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else {
                        //下载
                        startDownload();
                    }

                }
            });
            mDialog.show();
        }else {
            mDialog.setTitle("已经是最新版本");
            mDialog.setContent("感谢您的使用！");
            mDialog.setOKButton("确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        }
    }

    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(MainActivity.this, UpdateService.class);
        //下载地址
        it.putExtra("apkUrl", versionModel.getUrl());
        startService(it);
        mDialog.dismiss();
    }
    /**
     * 判断返回成功失败
     *
     * @param context
     * @param result
     * @return
     */
    public static Boolean JSONparser(Context context, String result) {
        Boolean flag = true;
        try {
            JSONObject json = new JSONObject(result);
            if (json.getString("status").equals("success")) {
                flag = true;
            } else if (json.getString("status").equals("error")) {
                flag = false;
            }

        } catch (JSONException e) {
            flag = false;
        }
        return flag;
    }
    /**
     * 字符串转模型
     * @param result
     * @return
     */
    private cn.xiaocool.wxtparent.bean.CheckVersionModel getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<CheckVersionModel>() {
        }.getType());
    }
    private void initView() {
        mTabs = new Button[5];
        mTabs[0] = (Button) findViewById(R.id.btn_space);
        mTabs[1] = (Button) findViewById(R.id.btn_diary);
        mTabs[2] = (Button) findViewById(R.id.btn_news);
        mTabs[3] = (Button) findViewById(R.id.btn_web);
        mTabs[4] = (Button) findViewById(R.id.btn_me);
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
        registerForContextMenu(mTabs[0]);
        bv_first = new BadgeView(this);
        bv_first.setTargetView(findViewById(R.id.btn_container_space));
    }

    /**
     * 设置小红点
     */
    public  void setRedPoint(){
        //从本地取出各个小红点的个数
        int msgNum = (int) SPUtils.get(context, JPUSHMESSAGE, 0);
        int hmkNum = (int) SPUtils.get(context, JPUSHHOMEWORK, 0);
        int trustNum = (int) SPUtils.get(context, JPUSHTRUST, 0);
        int noticeNum = (int) SPUtils.get(context, JPUSHNOTICE, 0);
        int daijieNum = (int) SPUtils.get(context, JPUSHDAIJIE, 0);
        int leaveNum = (int) SPUtils.get(context, JPUSHLEAVE, 0);
        int activityNum = (int) SPUtils.get(context, JPUSHACTIVITY, 0);
        int commentNum = (int) SPUtils.get(context, JPUSHCOMMENT, 0);
        int sum = msgNum+hmkNum+trustNum+noticeNum+daijieNum+leaveNum+activityNum+commentNum;
        if(sum>0){
            bv_first.setVisibility(View.VISIBLE);
            bv_first.setText("...");
            Log.e("setRedPoint", bv_first.getText().toString());
        }else{
            setBadgeView(sum,bv_first);
        }
        Log.e("setRedPoint",sum+"");
    }


    /**
     * 设置背景
     * @param message
     * @param message1
     */
    private void setBadgeView(int message, BadgeView message1) {
        if (message1==null)return;
        if (message==0){
            //message1.setVisibility(View.GONE);
            message1.setBadgeCount(0);
        }else {
            message1.setVisibility(View.VISIBLE);
            message1.setBadgeCount(message);
        }
    }


    /**
     * 接受推送通知并通知页面添加小红点
     */
    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Recevier1", "接收到:");
            setRedPoint();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setRedPoint();
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        Log.e("dddd", String.valueOf(view.getId()));
        switch (view.getId()) {
            case R.id.btn_space:
                index = 0;
                break;
            case R.id.btn_diary:
                index = 1;
                break;
            case R.id.btn_news:
                index = 2;
                break;
            case R.id.btn_web:
                index = 3;
                break;
            case R.id.btn_me:
                index = 4;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    public void onClick(View v) {

    }
    /**
     * show the dialog when user logged into another device
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;


        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle("");
                conflictBuilder.setMessage("");
                conflictBuilder.setPositiveButton("", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        JPushInterface.stopPush(MainActivity.this);
                        conflictBuilder = null;
                        user.clearDataExceptPhone(MainActivity.this);
                        SharedPreferences.Editor e = sp.edit();
                        LogUtils.e("删除前", e.toString());
                        e.clear();
                        e.commit();
                        WxtApplication.UID = 0;
                        LogUtils.e("删除后", e.toString());
                        IntentUtils.getIntent(MainActivity.this, LoginActivity.class);
                        finish();
                        ExitApplication.getInstance().exit();
                       /* EMClient.getInstance().login(user.getUserId(), user.getUserId(), new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        Log.d("main", "登陆聊天服务器成功！");

                                    }
                                });
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("main", "登陆聊天服务器失败！");
                            }
                        });*/

                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();

            } catch (Exception e) {
//                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }



    }

    /**
     * show the dialog if user account is removed
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        String st5 = "";
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage("聊天账号已被移除");
                accountRemovedBuilder.setPositiveButton("", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();

            } catch (Exception e) {
//                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

}
