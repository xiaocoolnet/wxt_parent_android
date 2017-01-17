package cn.xiaocool.wxtparent.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.app.ExitApplication;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.utils.DataCleanManager;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.view.NiceDialog;
import cn.xiaocool.wxtparent.view.WxtApplication;
import cn.xiaocool.wxtparent.view.update.UpdateService;

/**
 * Created by Administrator on 2016/4/2.
 */
public class UserSettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout btn_exit,rl_help,rl_feedback,about_us,rl_version;
    private UserInfo user = new UserInfo();
    private SharedPreferences sp;
    private Activity mContext;
    private TextView tv_quit,clear_text;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private static final int REQUEST_WRITE_STORAGE = 111;
    private String versionId,url,description,newId;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x110:
                    if (msg.obj != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        String state = obj.optString("status");
                        if (state.equals(CommunalInterfaces._STATE)) {
                            JSONObject object = obj.optJSONObject("data");
                            Log.e("data-----",object.toString());
                            url = object.optString("url");
                            description = object.optString("description");
                            newId = object.optString("versionid");
                        }
                    }
                    break;
            }
        }
    };
    private NiceDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.me_information_setting);
        mContext = this;
        sp = mContext.getSharedPreferences("list", mContext.MODE_PRIVATE);
        initView();
        versionId = getResources().getString(R.string.version_id).toString();
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String URL = "http://wxt.xiaocool.net/index.php?g=apps&m=index&a=CheckVersion&type=android&versionid="+versionId;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                JSONObject json = null;
                try {
                    json = new JSONObject(arg0);
                    String status = json.getString("status");
                    String data = json.getString("data");
                    if (status.equals("success")) {
                        JSONObject item = new JSONObject(data);
                        url = item.optString("url");
                        description = item.optString("description");
                        newId = item.optString("versionid");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    handler.sendEmptyMessage(111);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.d("onErrorResponse", arg0.toString());
            }
        });
        mQueue.add(request);
    }

    private void initView() {
        rl_version = (RelativeLayout) findViewById(R.id.rl_version);
        rl_version.setOnClickListener(this);
        about_us = (RelativeLayout) findViewById(R.id.about_us);
        about_us.setOnClickListener(this);
        btn_exit = (RelativeLayout) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        tv_quit = (TextView) findViewById(R.id.quit);
        tv_quit.setOnClickListener(this);
        rl_help = (RelativeLayout) findViewById(R.id.help);
        rl_help.setOnClickListener(this);
        findViewById(R.id.clear_layout).setOnClickListener(this);
        clear_text = (TextView)findViewById(R.id.clear_text);
        try {
            clear_text.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*rl_feedback = (RelativeLayout) findViewById(R.id.feedback);
        rl_feedback.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_layout:
                clearcache();
                break;
            case R.id.btn_exit:
                finish();
                break;
            case R.id.quit:
                getDialog();
                break;
            case R.id.help:
                IntentUtils.getIntent(mContext, MeHelpBackActivity.class);
                break;
            case R.id.about_us:
                IntentUtils.getIntent(mContext, AboutUsActivity.class);
                break;
            //版本更新
            case R.id.rl_version:
                if (newId!=null){
                    if(Integer.parseInt(versionId)<Integer.parseInt(newId)){
                        chechVersion();
                    }else{
                        ToastUtils.ToastShort(mContext,"当前版本是最新版本！");
                    }
                }

                break;
        }
    }
    private KProgressHUD hud;
    /**
     * 清楚缓存
     * */
    private void clearcache() {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("清除中")
                .setCancellable(true);
        hud.show();
        cn.xiaocool.wxtparent.utils.DataCleanManager.clearAllCache(mContext);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    hud.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clear_text.setText("0.0Byte");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void chechVersion() {
        mDialog = new NiceDialog(UserSettingActivity.this);
        mDialog.setTitle("发现新版本");
        mDialog.setContent(description);
        mDialog.setOKButton("立即更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //请求存储权限
                boolean hasPermission = (ContextCompat.checkSelfPermission(UserSettingActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(UserSettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    ActivityCompat.shouldShowRequestPermissionRationale(UserSettingActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    //下载
                    startDownload();
                }

            }
        });
        mDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    startDownload();
                } else {
                    Toast.makeText(UserSettingActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(UserSettingActivity.this, UpdateService.class);
        //下载地址
        it.putExtra("apkUrl", url);
        startService(it);
        mDialog.dismiss();
    }
    /**
     * 退出确认对话框
     */
    private void getDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(mContext);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.setting_dialog, null);

        // 对话框
        final Dialog dialog = new AlertDialog.Builder(mContext)
                .create();
        dialog.show();
        dialog.getWindow().setContentView(layout);

        // 取消按钮
        Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 确定按钮
        Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				user.clearData(mContext);
                JPushInterface.stopPush(mContext);
                user.clearDataExceptPhone(UserSettingActivity.this);
                SharedPreferences.Editor e = sp.edit();
                LogUtils.e("删除前", e.toString());
                e.clear();
                e.commit();
                WxtApplication.UID = 0;
                LogUtils.e("删除后", e.toString());
                IntentUtils.getIntent(UserSettingActivity.this, LoginActivity.class);
                finish();
                ExitApplication.getInstance().exit();
        }
        });
    }
}
