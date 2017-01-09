package cn.xiaocool.wxtparent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bugtags.library.Bugtags;

import cn.jpush.android.api.JPushInterface;
import cn.xiaocool.wxtparent.app.ExitApplication;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.LoginActivity;
import cn.xiaocool.wxtparent.main.MainActivity;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.SPUtils;
import cn.xiaocool.wxtparent.view.NiceDialog;


/**
 * Created by wzh on 2016/2/20.
 */
public class BaseActivity extends FragmentActivity{


    private BaseReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BaseReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    public class BaseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.i("BaseReceiver", "接收到:");
            String loginFromOther = intent.getStringExtra("loginOther");
            if (loginFromOther.equals("loginFromOther")){
                final NiceDialog mDialog = new NiceDialog(BaseActivity.this);
                mDialog.setCancelable(false);
                mDialog.setTitle("提示");
                mDialog.setContent("您的账号在另一地点登录！");
                mDialog.setOKButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new UserInfo(context).clearDataExceptPhone(context);
                        JPushInterface.stopPush(context);
                        SPUtils.remove(context, "newsGroupRecive");
                        SPUtils.remove(context,"receiveParentWarn");
                        SPUtils.remove(context,"noticeRecive");
                        SPUtils.remove(context,"backlogData");
                        SPUtils.remove(context,"teacherCommunication");
                        SPUtils.remove(context,"homeWork");

                        finish();
                        IntentUtils.getIntents(context, LoginActivity.class);
                        ExitApplication.getInstance().exit();
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
            }
        }

    }
}