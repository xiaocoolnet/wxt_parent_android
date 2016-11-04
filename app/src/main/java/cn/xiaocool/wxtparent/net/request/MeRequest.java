package cn.xiaocool.wxtparent.net.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.constant.WebHome;
import cn.xiaocool.wxtparent.utils.LogUtils;

/**
 * Created by wzh on 2016/2/27.
 */
public class MeRequest {
    private Context mContext;
    private Handler handler;
    private UserInfo user;
    public static String childId;
    public static String id;

    public MeRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;
        user = new UserInfo();
        user.readData(mContext);
        id = user.getUserId();
        childId = user.getChildId();
    }

    //    public MeRequest(Context context) {
//        super();
//        this.mContext = context;
//        user = new UserInfo();
//        user.readData(mContext);
//
//    }
    //服务状态
    public void serviceStatus() {
        LogUtils.d("weixiaotong", "getCircleList");
        Log.e("id is this", id);
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "&stuid=" + id;
                    LogUtils.d("weixiaotong", data);
                    String result_data = NetUtil.getResponse(WebHome.ME_GET_SERVICE_STATUS, data);
                    LogUtils.e("announcement", result_data.toString());
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETSERVICESTATUS;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取我的宝宝信息
    public void MyBabyAll() {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                try {
                    String data = "&userid=" + id;
                    String result_data = NetUtil.getResponse(WebHome.WEB_GET_BABY_INFO, data);
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.BABY_INFO;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取我的宝宝信息
    public void MyParentAll() {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                try {
                    String data = "&studentid=" +childId ;
                    String result_data = NetUtil.getResponse(WebHome.WEB_GET_PARENT_INFO, data);
                    JSONObject jsonObject = new JSONObject(result_data);
                    Log.e("hello",WebHome.WEB_GET_PARENT_INFO+data);
                    msg.what = CommunalInterfaces.GETBABYPARENT;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取客服联系方式
    public void onlineService() {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                try {
//                    String data = "&userid=" + id;
                    String data = "userid=" + id;
                    String result_data = NetUtil.getResponse(WebHome.ONLINE_SERVICE, data);
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ONLINE_SERVICE;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
