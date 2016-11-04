package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.HttpTool;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.KeyBoardUtils;
import cn.xiaocool.wxtparent.utils.NetBaseUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.view.WxtApplication;

/**
 * Created by mac on 16/1/23.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private String userid;
    private String result_data, token;
    private Button btn_login;
    private Button btn_close;
    private EditText tx_phonenumber;
    private EditText tx_vertifycode;
    private String phone = null;
    private String password = null;
    private TextView tv_register;
    private TextView tv_forget;
    private Context mContext;
    private UserInfo user;
    private KProgressHUD hud;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int i, String s, Set<String> set) {
            String logs;
            switch (i) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("setAlias", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("setAlias", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    handler.sendMessageDelayed(handler.obtainMessage(MSG_SET_ALIAS, user.getChildId()), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + i;
                    Log.e("setAlias", logs);
            }
        }
    };



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {



                case MSG_SET_ALIAS:
                    Log.d("setAlias", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case CommunalInterfaces.BABY_INFO:

                    JSONObject obj_baby = (JSONObject) msg.obj;
                    String status_baby = obj_baby.optString("status");
                    if (status_baby.equals(CommunalInterfaces._STATE)) {
                        JSONArray array_baby = obj_baby.optJSONArray("data");
                        JSONObject child = array_baby.optJSONObject(0);
                        user.setChildId(child.optString("studentid"));
                        Log.e("childId",child.optString("studentid"));
                        user.setChildName(child.optString("studentname"));
                        user.setChildAvator(child.optString("studentavatar"));
                        JSONObject classObj =child.optJSONArray("classlist").optJSONObject(0);
                        user.setSchoolId(classObj.optString("schoolid"));
                        user.setClassId(classObj.optString("classid"));
                        user.setClassName(classObj.optString("classname"));
                        user.writeData(mContext);
                        Log.e("login", "login");
                    }
                    //判断账号是否注册 若没注册 帮对方注册
                    handler.sendEmptyMessage(0x110);
                    JPushInterface.resumePush(mContext);
                    JPushInterface.setAlias(getBaseContext(), user.getChildId(), mAliasCallback);
                    hud.dismiss();
                    IntentUtils.getIntent(LoginActivity.this, MainActivity.class);
                    finish();
                    break;

                case 3:
                    try {
                        JSONObject json = new JSONObject(result_data);
                        String status = json.getString("status");
                        String data = json.getString("data");
                        if (status.equals("success")) {
                            JSONObject item = new JSONObject(data);
                            Log.i("hou_uid", item.getString("id"));
                            WxtApplication.UID = Integer.parseInt(item.getString("id"));
                            Log.i("Info", WxtApplication.UID + "");
                            userid = item.optString("id");
                            user.setUserId(item.getString("id"));
                            user.setUserName(item.getString("name"));
                            user.setUserImg(item.optString("photo"));
                            user.writeData(mContext);
                            Log.e("xiaocoolget", item.getString("name"));
                            user.setUserImg(item.getString("photo"));
                            new SpaceRequest(getApplicationContext(), handler).BabyInfo();
                            Log.e("userid-------", userid);


                        } else {
                            Toast.makeText(LoginActivity.this, data,
                                    Toast.LENGTH_SHORT).show();
                            hud.dismiss();
                            Log.e("hou", data);
//                        mDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
                    break;
                case 1:
                    hud.dismiss();
                    ToastUtils.ToastShort(mContext, "手机号或密码输入错误！");
                default:
                    break;
            }
        }

    };

    private Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        mContext = this;


        init();


        new SpaceRequest(getApplicationContext(), handler2).BabyInfo();
    }


    private void init() {
        Log.e("login", "init");
        // TODO Auto-generated method stub
//        mDialog = MyProgressDialog.createDialog(LoginActivity.this);
        btn_login = (Button) findViewById(R.id.login_login);
        tx_phonenumber = (EditText) findViewById(R.id.login_phonenum);
        tx_vertifycode = (EditText) findViewById(R.id.login_Password);
        btn_login = (Button) findViewById(R.id.login_login);
        //tv_register = (TextView) findViewById(R.id.tv_login_register);
        tv_forget = (TextView) findViewById(R.id.tv_login_forgetpassword);
        tv_register = (TextView) findViewById(R.id.tv_login_register);
        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        user = new UserInfo();
        user.readData(this);
        if (!user.getUserPhone().equals("")) {
            tx_phonenumber.setText(user.getUserPhone());
        }
        // 切换后将EditText光标置于末尾
        CharSequence charSequence = tx_phonenumber.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        KeyBoardUtils.showKeyBoardByTime(tx_phonenumber, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                if (NetBaseUtils.isConnnected(mContext)){
                    getLogin();
                }else {
                    ToastUtils.ToastShort(mContext,"当前无网络！");
                }
                break;
            case R.id.tv_login_register:// 注册
                //IntentUtils.getIntent(LoginActivity.this, RegisterActivity.class);
                IntentUtils.getIntent(LoginActivity.this, ForgetPasswordActivity.class);
                break;
            case R.id.tv_login_forgetpassword:// 忘记密码

                break;
        }
    }

    private void getLogin() {
//            mDialog.show();
        phone = tx_phonenumber.getText().toString();
        password = tx_vertifycode.getText().toString();
        user.setUserPhone(phone);
        user.setUserPassword(password);
        user.writeData(mContext);

        hud = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("登陆中...")
                .setCancellable(true);
        hud.show();
//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        final String DEVICE_ID = tm.getDeviceId();
        //线程
        new Thread() {
            public void run() {
                //1、获取输入的手机号码
                if (tx_phonenumber.getText().length() == 11 && tx_vertifycode.getText().length() != 0) {
                    String phoneNum = tx_phonenumber.getText().toString();
                    String password = tx_vertifycode.getText().toString();
                    if(password.trim().equals("")||password.trim().length()==0){
                        ToastUtils.ToastShort(LoginActivity.this, "请输入密码！");
                    }else{
                        if (HttpTool.isConnnected(mContext)) {
                            result_data = HttpTool.Login(phoneNum, password ,"");
                            //调用服务器登录函数
                            handler.sendEmptyMessage(3);
                        } else {
                            //输出：网络连接有问题！
                            hud.dismiss();
                            handler.sendEmptyMessage(2);
                        }
                    }
                } else {
                    //输出：手机号或密码不正确！
                    handler.sendEmptyMessage(1);
                }
//                    String phonenum = tx_phonenumber.getText().toString();
//                    //2、获取收入的密码
//                    String vertifycode = tx_vertifycode.getText().toString();
//                    //------逻辑判断
//                    if(phonenum.length()==11) {
//                        if (HttpTool.isConnnected(mContext)) {
//                            result_data = HttpTool.Login(phonenum, vertifycode);
//
//                        } else {
//                            handler.sendEmptyMessage(2);
//                        }
//                        //--调用服务器登录函数
//                        handler.sendEmptyMessage(3);
//                        Log.e("wzh","enter");
//                    }
//                    else{
//                        handler.sendEmptyMessage(1);
//                    }


            }

        }.start();
    }
}
