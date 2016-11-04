package cn.xiaocool.wxtparent.net.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.constant.ClassCircle;
import cn.xiaocool.wxtparent.net.request.constant.WebHome;
import cn.xiaocool.wxtparent.utils.LogUtils;

/**
 * Created by wzh on 2016/2/27.
 */
public class WebRequest {
    private Context mContext;
    private Handler handler;
    private UserInfo user;
    public WebRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;
        user = new UserInfo();
        user.readData(mContext);
    }

    public WebRequest(Context context) {
        super();
        this.mContext = context;
        user = new UserInfo();
        user.readData(mContext);

    }
    //通知公告
    public void announcement(){
        LogUtils.d("weixiaotong", "getCircleList");
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                try {
                    String  data = "&stuid=1234567890";
                    LogUtils.d("weixiaotong", data);
                    String result_data =  NetUtil.getResponse(WebHome.NET_GET_ANNOUNCEMENT, data);
                    LogUtils.e("announcement", result_data.toString());
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETANNOUNCEMENT;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            };
        }.start();
    }
}
