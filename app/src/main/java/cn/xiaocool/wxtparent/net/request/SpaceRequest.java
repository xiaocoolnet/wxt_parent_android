package cn.xiaocool.wxtparent.net.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.main.SpaceClickHeightActivity;
import cn.xiaocool.wxtparent.main.SpaceClickWeightActivity;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.constant.WebHome;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.NetBaseUtils;

/**
 * Created by wzh on 2016/2/27.
 */
public class SpaceRequest {
    private Context mContext;
    private Handler handler;
    private UserInfo user;
    public static String id;
    public static String childId;
    private String etWeight;
    private String etHeight;

    public SpaceRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;
        user = new UserInfo();
        user.readData(mContext);
        id = user.getUserId();
        childId = user.getChildId();
    }

    public void post(final String url, final List<NameValuePair> params, final int KEY){
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String result_data = NetBaseUtils.getResponseForPost(url, params, mContext);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }


    //获取当前绑定的宝宝信息
    public void BabyInfo() {
        id = user.getUserId();
        Log.e("id is1", id);
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


    //发送验证码
    public void SendVerifyCode(final String phoNum) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&phone=" + phoNum;
                String result_data = NetUtil.getResponse(WebHome.SEND_CODE, data);
                Log.e("result data is", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SEND_CODE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //注册
    public void register(final String phoNum, final String verifyCode) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&phone=" + phoNum + "&code=" + verifyCode;
                String result_data = NetUtil.getResponse(WebHome.REGISTER, data);
                Log.e("successful", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.REGISTER;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //忘记密码-家长手机号+验证码+已激活 验证
    public void forgetPassword(final String phoNum, final String verifyCode) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&phone=" + phoNum + "&code=" + verifyCode;
                String result_data = NetUtil.getResponse(WebHome.FORGET_PASSWORD, data);
                Log.e("forget", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.FORGET_PASSWORD;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取学生今日记录
    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=GetStudentLog&stuid=599
    public void TodayRecord() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "&stuid=" + "597";
                    String result_data = NetUtil.getResponse(WebHome.TODAY_RECORD, data);
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TODAYRECORD;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取身高体重
    public void StatureWeight() {
        LogUtils.d("weixiaotong", "getCircleList");
        id = user.getUserId();
        Log.e("id is1", id);
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "&stuid=" + childId;
                    String result_data = NetUtil.getResponse(WebHome.WEB_GET_STATURE_WEIGHT, data);
                    LogUtils.e("announcement", result_data.toString());
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GET_STATURE_WEIGHT;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    Log.e("enter catch success", "");
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取到校时间
    public void ArriveTimeTem(final String time) {

        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&stuid=" + childId + "&time=" + time;
                String result_data = NetUtil.getResponse(WebHome.WEB_GET_TIME_TEM, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GET_TIME_TEM;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //写入数据库体重数据
    public void InputWeight() {
        Log.e("输入体重id", id);
        etWeight = SpaceClickWeightActivity.input_text;
        Log.e("进入", etWeight);
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&stuid=" + childId + "&weight=" + etWeight;
                String result_data = NetUtil.getResponse(WebHome.SPACE_INPUT_WEIGHT, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.INPUT_WEIGHT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }

    //写入数据库身高数据
    public void InputHeight() {
        etHeight = SpaceClickHeightActivity.input_text;
        Log.e("进入", etHeight);
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&stuid=" + childId + "&stature=" + etHeight;
                String result_data = NetUtil.getResponse(WebHome.SPACE_INPUT_HEIGHT, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.INPUT_HEIGHT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }


    //点开消息界面获取用户已经收到的消息
    public void ReceivedMessage() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_RECEIVED_MESSAGE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.RECEIVED_MESSAGE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }


    //点开消息界面获取用户已发送的消息
    public void sendMessage() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_SEND_MESSAGE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SEND_MESSAGE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }

    /*
    * 添加动态
    *
    *
    * */
    public void send_trend(final String content, final String picname, final int
            KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", user.getUserId()));
                params.add(new BasicNameValuePair("schoolid", user.getSchoolId
                        ()));
                params.add(new BasicNameValuePair("type", "1"));
                params.add(new BasicNameValuePair("content", content));
                params.add(new BasicNameValuePair("classid", user.getClassId
                        ()));
                if (picname != null) {
                    params.add(new BasicNameValuePair("picurl", picname));
                }
                String result_data = NetBaseUtils.getResponseForPost
                        (WebHome.SEND_TRENDS, params, mContext);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    /**
     * 我的作业
     */

    //点开消息界面获取 我的作业 列表中的 数据
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=gethomeworkmessage&receiverid=661
    public void myHomework() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "receiverid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.SPACE_MYHOMEWORK, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SPACE_MYHOMEWORK;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }

    //获取群发消息列表
    //http://wxt.xiaocool.net/index.php?g=Apps&m=Message&a=user_reception_message&receiver_user_id=597
    public void getNewGroup() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&receiver_user_id=" + childId;
                String result_data = NetUtil.getResponse(WebHome.SPACE_NEWSGROUP, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.NEWSGROUP;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }

    //点开消息界面获取 我的作业 列表中的 点赞
    public void myHomework_set_like(final String workBindId, final int workPraiseKey, final String type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&id=" + workBindId + "&type=" + type + "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_MYHOMEWORK_SET_LIKE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = workPraiseKey;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }


    //点开消息界面获取 我的作业 列表中的 取消点赞
    public void myHomework_del_like(final String workBindId, final int workPraiseKey, final String type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "&id=" + workBindId + "&type=" + type + "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_MYHOMEWORK_DEL_LIKE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = workPraiseKey;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }
    //获取评论列表

    /**
     * 获取评论列表
     * 接口地址：m=school&a=GetCommentlist 入参：userid,refid,type  出参：list
     */

    public void get_comments(final String id, final String refid, final String type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                Log.e("ayay", "user.getUserId():" + user.getUserId() + "---------" + "id:" + id);
                String data = "&refid=" + refid + "&type=" + type + "&userid=" + user.getUserId();
                String result_data = NetUtil.getResponse(WebHome.SPACE_SEND_PINGLUN, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SPACE_SEND_PINGLUN;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }


        }.start();
    }


    //发送 评论 post请求
    public void send_pinglun(final String type, final String zy_id, final String contents, final String picname, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", user.getUserId()));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("content", contents));
                params.add(new BasicNameValuePair("id", zy_id));
                if (picname != null) {
                    params.add(new BasicNameValuePair("photo", picname));
                }
//                params.add(new BasicNameValuePair("reciveid","12"));
                String result = NetBaseUtils.getResponseForPost(WebHome.SPACE_SEND_PINGLUN2, params, mContext);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    msg.what = KEY;
                    msg.obj = object;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }


    //通讯录
    public void contactsList() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_CONTACTS, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CONTACTS_LIST;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }


    /*
    *
    * 通讯录 电话的列表
    * */
    //http://wxt.xiaocool.net/index.php?g=apps&m=teacher&a=getstudentclasslistandteacherlist&studentid=507
    public void addressParent() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.MESSAGEADDRESS,
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.MESSAGEADDRESS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void addressParentNew(final String stuId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + stuId;
                String result_data = NetUtil.getResponse(WebHome.MESSAGEADDRESS,
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.MESSAGEADDRESS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=getfriendlist&studentid=661
    public void getFriend() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=getfriendlist",
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETFRIEND;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getStudentlistByClassid&classid=1
    public void getStudentList() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&classid=" + user.getClassId();
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=school&a=getStudentlistByClassid",
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETSTUDENTLIST;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=addfriend&studentid=597&friendid=605
    public void addFriend(final String friendId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&friendid=" + friendId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=addfriend",
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ADDFRIEND;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=resign&userid=597,605&schoolid=1
    public void resign() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + childId + "&schoolid=" + "1";
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=resign",
                        data);
                Log.e("hello-----", WebHome.MESSAGEADDRESS + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.RESIGN;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetSelfGrow&studentid=661
    public void getDiary(final String friendId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + friendId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetSelfGrow",
                        data);
                LogUtils.e("hello", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.MYDIARY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetFriendsGrow&studentid=661
    public void getFriendDiary() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetFriendsGrow",
                        data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.FRIENDDIARY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=FairylandUrl&name=营养食谱
    public void getParadise(final String name,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String data;
                data = "&name=" + name;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=FairylandUrl",
                        data);
                Log.e("hello-----", WebHome.ADDPARENT + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=AddInviteFamily&studentid=597&parentname=随便&appellation=大爷&phone=123123123
    public void addParent(final String parentname, final String appellation, final String phone, final String photo) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data;
                if (photo.equals("1")) {
                    data = "&studentid=" + childId + "&parentname=" + parentname + "&appellation=" + appellation + "&phone=" + phone;
                } else {
                    data = "&studentid=" + childId + "&parentname=" + parentname + "&appellation=" + appellation + "&phone=" + phone + "&photo=" + photo;
                }
                String result_data = NetUtil.getResponse(WebHome.ADDPARENT,
                        data);
                Log.e("hello-----", WebHome.ADDPARENT + data);
                LogUtils.e("announce", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ADDPARENT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取食谱(新)    传入schoolid begindate enddate
    public void getRecipes(final String begindate, final String enddate) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + "1" + "&begindate=" + begindate + "&enddate=" + enddate;
                String result_data = NetUtil.getResponse(WebHome.RECIPESNEW, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.RECIPESNEW;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    /*
    *
    * 通知公告
    *
    * */
    //获取通知公告
    //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=get_receive_notice&receiverid=661
    public void announcement() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&receiverid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.SPACE_ANNOUNCEMENT, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ANNOUNCEMENT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //删除家长
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=DeleteFamily&studentid=661&userid=693
    public void deleteParent(final String parentId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&userid=" + parentId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=DeleteFamily", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SELETEPARENT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //设置主号
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=SetMainParent&studentid=597&userid=605
    public void setMainParent(final String parentId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&userid=" + parentId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=SetMainParent", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SETMAIN;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    // 通知公告  列表中的 点赞
    public void annou_set_like(final String workBindId, final int workPraiseKey, final String type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                //workBindId=75,type=2

//                String data = "&id="+workBindId+"&type="+type+"&userid="+id;
                String data = "&id=" + workBindId + "&type=" + type + "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_MYHOMEWORK_SET_LIKE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = workPraiseKey;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }

    // 通知公告 取消赞
    public void annou_del_like(final String workBindId, final int workPraiseKey, final String type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "&id=" + workBindId + "&type=" + type + "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.SPACE_MYHOMEWORK_DEL_LIKE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = workPraiseKey;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }






/*
*
* 班级活动
*
* */

    //获取 活动列表
//http://wxt.xiaocool.net/index.php?g=apps&m=student&a=getactivitylist&receiverid=661
    public void classEvents() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String data = "&receiverid=" + childId;
                Log.e("classEvent-----", WebHome.CLASS_EVENTS + data);
                String result_data = NetUtil.getResponse(WebHome.CLASS_EVENTS, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CLASS_EVENTS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取老师点评
    public void teacherReview() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&stuid=" + "599";
                String result_data = NetUtil.getResponse(WebHome.TEACHER_REVIEW, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TEACHER_REVIEW;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取 教师风采
    public void teacher_style() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.TEACHER_STYLE, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TEACHER_STYLE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取 ANNOUNCEMENT_园区
    public void announcement_yuanqu() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.ANNOUNCEMENT_YQ, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ANNOUNCEMENT_YQ;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取 新闻动态
    public void news_dongtai() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.NEWS_DONGTAI, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.NEWS_DONGTAI;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取 育儿知识
    public void rearing_child() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.REARING_CHILD, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);
                Log.e("yyyy", "WebHome.REARING_CHILD");

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.REARING_CHILD;
                    msg.obj = obj;
                    Log.e("yyyy", "WebHome.REARING_CHILD");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取 校园招聘
    public void recruit() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.RECRUIT, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.RECRUIT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取 宝宝秀场
    public void baby_show() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.BABY_SHOW, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.BABY_SHOW;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取 兴趣班
    public void interesting_classw() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse(WebHome.INTERESTING_CLASS, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.INTERESTING_CLASS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //发送  入学报名  借口中暂时缺少图片的字段   final String photo        + "&photo=" + photo
    public void enroll(final String name, final String sex, final String birthday, final String address, final String classname, final String phone, final String qq, final String weixin, final String education, final String school, final String remark) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId() + "&username=" + name + "&sex=" + sex + "&birthday=" + birthday + "&address=" + address + "&classname=" + classname + "&phone=" + phone + "&qq=" + qq + "&weixin=" + weixin + "&education=" + education + "&remark=" + remark + "&school=" + school;
                String result_data = NetUtil.getResponse(WebHome.ENROLL, data);

                Log.e("yyyy", data);
                Log.e("yyyy", result_data);

                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ENROLL;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取班级课件
    public void classCourseware() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=1&classid=1";
                String result_data = NetUtil.getResponse(WebHome.CLASS_COURSEWARE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CLASS_COURSEWARE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

//    public void classEvents() {
//        new Thread() {
//            Message msg = Message.obtain();
//
//            public void run() {
//                String data = "&schoolid=1&classid=1";
//                String result_data = NetUtil.getResponse(WebHome.CLASS_EVENTS, data);
//                try {
//                    JSONObject obj = new JSONObject(result_data);
//                    msg.what = CommunalInterfaces.CLASS_EVENTS;
//                    msg.obj = obj;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                    handler.sendMessage(msg);
//                }
//
//            }
//        }.start();
//
//    }

//    public void rearingChild() {
//        new Thread() {
//            Message msg = Message.obtain();
//
//            public void run() {
//                String data = "&schoolid=1";
//                String result_data = NetUtil.getResponse(WebHome.REARING_CHILD, data);
//                try {
//                    JSONObject obj = new JSONObject(result_data);
//                    msg.what = CommunalInterfaces.REARING_CHILD;
//                    msg.obj = obj;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                    handler.sendMessage(msg);
//                }
//            }
//        }.start();
//
//    }

    public void happySchool() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=1";
                String result_data = NetUtil.getResponse(WebHome.HAPPY_SCHOOL, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.HAPPY_SCHOOL;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void classSchedule() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "schoolid=1&classid=1";
                String result_data = NetUtil.getResponse(WebHome.CLASS_SCHEDULE, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CLASS_SCHEDULE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void inputPhoneNumAndName() {

    }

    public void babyTeacher() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "schoolid=" + user.getSchoolId() + "&classid=" + user.getClassId();

                String result_data = NetUtil.getResponse(WebHome.BABY_TEACHER, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.BABY_TEACHER;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    public void leaveReason(final String user_leave_reason) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id + "&teacherid=" + 1 + "&content=" + user_leave_reason;
                String result_data = NetUtil.getResponse(WebHome.LEAVE_REASON, data);
                Log.i("Info_resultData", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.LEAVE_REASON;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //在线请假 (新)
    public void getLeave_liebiao() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.LEAVE_LEIBIAO, data);
                Log.e("reason", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.LEAVE_LEIBIAO;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //在线请假的学生列表
    public void getLeave_stu() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.LEAVE_STU, data);
                Log.e("reason", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.LEAVE_STU;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //添加在线请假
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=addleave&teacherid=605&parentid=681&studentid=661&begintime=1469170284&endtime=1469170284&reason=&picture_url=newsgroup2021469263354727.jpg,newsgroup2021469263354727.jpg
    public void add_leave(final String begintime, final String endtime, final String reason, final String pics, final String teacherid, final String stuId, final String leaveType) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&begintime=" + begintime + "&endtime=" + endtime + "&parentid=" + id +
                        "&reason=" + reason + "&studentid=" + stuId + "&teacherid=" + teacherid + "&picture_url=" + pics + "&leavetype=" + leaveType;
                String result_data = NetUtil.getResponse(WebHome.LEAVE_ADD, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.LEAVE_ADD;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void getTeacherAttendance(final String begintime, final String endtime, final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + childId + "&begintime=" + begintime + "&endtime=" + endtime;

                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=teacher&a=GetTeacherAttendanceList", data);

                Log.e("getTeacherAttendance", "http://wxt.xiaocool.net/index.php?g=apps&m=teacher&a=GetTeacherAttendanceList" + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //家长叮嘱（新的，在原来的方法上改动）
    public void parentWarn() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id;
                String result_data = NetUtil.getResponse(WebHome.PARENT_WARN, data);
                Log.e("parentWarn", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.PARENT_WARN;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //添加家长叮嘱（新的，在原来的方法上改动）
    public void parentWarn_add(final String studentid, final String teacherid, final String content, final String pics) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id + "&studentid=" + studentid + "&teacherid=" + teacherid + "&content=" + content + "&picture_url=" + pics;
                String result_data = NetUtil.getResponse(WebHome.PARENT_WARN_ADD, data);
                Log.e("parentWarn  data", data);
                Log.e("parentWarn", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.PARENT_WARN_ADD;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    public void teacherAddress() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId();
                String result_data = NetUtil.getResponse(WebHome.TEACHER_ADDRESS, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TEACHER_ADDRESS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=UpdatePhoto&studentid=661&photo=asdaw.png
    public void updateFamily(final String photo) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&photo=" + photo;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=UpdatePhoto", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.UPDATFAMILY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }

    public void sendMessageToTeacher(final String sendMessage, final String teacherID) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&sendid=597&receiveid=" + teacherID + "&content=" + sendMessage;
                LogUtils.i("Info_id_message", teacherID + sendMessage);
                String result_data = NetUtil.getResponse(WebHome.SENDTOTEACJER, data);
                Log.i("Info", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SENDTOTEACJER;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取宝宝好友
    public void getBabyFriends() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + id;
                Log.e("getBabyFriends", "data" + data);
                String result_data = NetUtil.getResponse(WebHome.BABY_FRIENDS, data);
                Log.e("getBabyFriends", "result_data" + result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);

                    msg.what = CommunalInterfaces.BABY_FRIENDS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void diaryViewPager() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + id;
                String result_data = NetUtil.getResponse(WebHome.BABY_FRIENDS, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.BABY_FRIENDS;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void updataUserImg(final String img, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                Map<String, String> textParams = new HashMap<>();
                Map<String, File> fileparams = new HashMap<>();
                String result = NetUtil.getResponse("", "");
            }
        }.start();
//    public void run() {
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("MemberImg", img));
//        params.add(new BasicNameValuePair("memberId", user.getUserIdTemp()));
//        String result = NetBaseUtils.getResponseForImg(UserNetConstant.NET_USER_UPLOAD_HEAD_IMG, params, mContext);
//        LogUtils.i(TAG, "修改用户的头像--->" + result);
//        msg.what = KEY;
//        msg.obj = result;
//        handler.sendMessage(msg);
//    };
    }

    public void confirmChildren() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.CONFIRM_CHILDERN, data);
                Log.e("result",result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CONFIRM_CHILDERN;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void handleConfirm(final String transportid, final String state) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "transportid=" + transportid + "&parentid=" + id + "&status=" + state;
                String result_data = NetUtil.getResponse(WebHome.HANDLE_CONFIRM, data);
                Log.e("result",result_data);
                Log.e("daijie",WebHome.HANDLE_CONFIRM+data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.HANDLECONFIRM;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void HotNews() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId();
                String result_data = NetUtil.getResponse(WebHome.SPACE_SEND_MESSAGE, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SEND_MESSAGE;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=getfriendlist&studentid=661
    public void getBabyFriend() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.GET_FRIEND, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.BABYFRIEND;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetParentList&studentid=661
    public void getClassParent() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetParentList", data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETCLASSPARENT;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=SchoolCourseware&schoolid=1&classid=1
    public void getCourseware(final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + user.getSchoolId() + "&classid=" + user.getClassId();
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=SchoolCourseware", data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=Apps&m=Message&a=read_message&message_id=457&receiver_user_id=661
    public void readNewGroup(final String msgId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&message_id=" + msgId + "&receiver_user_id=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=Apps&m=Message&a=read_message", data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = 1;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=school&a=read_notice&noticeid=177&receiverid=661
    public void readAnnocement(final String msgId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&noticeid=" + msgId + "&receiverid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=school&a=read_notice", data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = 1;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=read_homework&receiverid=599&homework_id=11
    public void readHomeWork(final String msgId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&homework_id=" + msgId + "&receiverid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=student&a=read_homework", data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = 1;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetBabyInfo&studentid=661
    public void getBabyInformation() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId;
                String result_data = NetUtil.getResponse(WebHome.GET_BABY_INFO, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.BABYINFO;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //爱好--http://wxt.xiaocool.net/index.php?g=apps&m=student&a=UpdateBirth&studentid=661&hobby=音乐
    public void changeHobby(final String hobby) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&hobby=" + hobby;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_HOBBY, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEHOBBY;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改宝宝生日
    public void changeBirth(final String birth) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&birthday=" + birth;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_BIRTH, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEBIRTH;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改头像
    public void changeAvator(final String avator) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&avator=" + avator;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_AVATOR, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEAVATOR;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改家庭住址
    public void changeAddress(final String address) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&address=" + address;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_ADDRESS, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEADDRESS;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改接送人
    public void changeReliver(final String delivery) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&delivery=" + delivery;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_RELIVER, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGERELIVER;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改全家福
    public void changeFamily(final String photo) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&photo=" + photo;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_FAMILY, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEFAMILY;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改妈妈职业
    public void changeMotherJob(final String motherpro) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&motherpro=" + motherpro;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_MOTHER_JOB, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEMOTHERJOB;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改与妈妈喜欢做什么
    public void changeMotherLike(final String withmother) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&withmother=" + withmother;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_MOTHER_LIKE, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEMOTHERLIKE;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改爸爸职业
    public void changeFatherJob(final String fatherpro) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&fatherpro=" + fatherpro;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_FATHER_JOB, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEFATHERJOB;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //修改与爸爸喜欢做什么
    public void changeFatherLike(final String withfather) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&withfather=" + withfather;
                String result_data = NetUtil.getResponse(WebHome.CHANGE_FATHER_LIKE, data);
                LogUtils.e("announcement", result_data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CHANGEFATHERLIKE;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //报名班级活动
    public void applyClassEvent(final String eventId) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + childId + "&activityid=" + eventId;
                String result_data = NetUtil.getResponse(WebHome.APPLY_EVENT, data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.APPLYEVENT;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取体重
    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=GetChange_weight&stuid=661
    public void getWeight() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&stuid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=GetChange_weight", data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETWEIGHT;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=service_phone&schoolid=1
    public void getServicePhone(final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String data = "&schoolid=" + user.getSchoolId();
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=service_phone", data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=forgetPass_Activate&userid=28&pass=123
    public void changePsw(final String psw,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String data = "&userid=" + user.getUserId()+"&pass=" + psw;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=forgetPass_Activate", data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取身高
    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=GetChange_stature&stuid=599
    public void getStature() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&stuid=" + childId;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=GetChange_stature", data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETSTATURE;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    //获取老师点评
    //http://wxt.xiaocool.net/index.php?g=apps&m=student&a=GetTeacherComment&studentid=661&begintime=0&endtime=1469863987
    public void getTeacherComment(final String begintime, final String endtime) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&studentid=" + childId + "&begintime=" + begintime + "&endtime=" + endtime;
                Log.e("teacherComment-----", WebHome.GET_TEACOMMENT + data);
                String result_data = NetUtil.getResponse(WebHome.GET_TEACOMMENT, data);
                try {
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.GETTEACOMMENT;
                    msg.obj = jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void getCircleList(final String userid, final String begin_id, final String school_id, final String class_id, final String type, final int KEY) {
        LogUtils.d("weixiaotong", "getCircleList");
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "&userid=" + userid + "&classid=" + user.getClassId() + "&schoolid=" + user.getSchoolId() + "&type=" + type + "&beginid=" + begin_id;
                    LogUtils.d("weixiaotong", data);
                    String result_data = NetUtil.getResponse(WebHome.NET_GET_CIRCLELIST, data);
                    LogUtils.e("getIndexSlideNewsList", WebHome.NET_GET_CIRCLELIST + data);
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

            ;
        }.start();
    }

    public void send_remark(final String id, final String content, final String type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId() + "&id=" + id + "&content=" + content + "&type=" + type;
                Log.d("final String id", data);
                String result_data = NetUtil.getResponse(WebHome.SEND_COMMENT, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SEND_PARENT_REMARK;
                    msg.obj = obj;
                    Log.d("是否成功++", obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }


        }.start();
    }

    /**
     * 作业点赞
     */
    public void Praise(final String workBindId, final int workPraiseKey, final String type) {
        LogUtils.d("weixiaotong", "getCircleList");
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "&userid=" + user.getUserId() + "&id=" + workBindId + "&type=" + type;//!!!!!!!!!!!!!!!!!!!!!!!!!参数舛错了!
                    LogUtils.d("weixiaotong", data);
                    String result_data = NetUtil.getResponse(WebHome.NET_SET_PRAISE, data);
                    LogUtils.e("getIndexSlideNewsList", result_data.toString());
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = workPraiseKey;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 作业取消点赞
     */
    public void DelPraise(final String id, final int KEY, final String type) {
        LogUtils.d("weixiaotong", "getCircleList");
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                try {
                    String data = "userid=" + user.getUserId() + "&id=" + id + "&type=" + type;
                    LogUtils.d("weixiaotong", data);
                    String result_data = NetUtil.getResponse(WebHome.NET_DEL_PRAISE, data);

                    LogUtils.e("getIndexSlideNewsList", result_data.toString());
                    JSONObject jsonObject = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }

            ;
        }.start();
    }

    public void send_trend(final String content, final String picname, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", user.getUserId()));
                params.add(new BasicNameValuePair("schoolid", user.getSchoolId()));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("content", content));
                params.add(new BasicNameValuePair("classid", user.getClassId()));
                if (picname != null) {
                    if (!picname.equals("null")) {
                        params.add(new BasicNameValuePair("picurl", picname));
                    }

                }
                Log.e("addTrend-------", WebHome.SEND_TRENDS + params.toString());
                String result_data = NetBaseUtils.getResponseForPost(WebHome.SEND_TRENDS, params, mContext);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void send_child_diary(final String content, final String picname, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", user.getChildId()));
                params.add(new BasicNameValuePair("schoolid", "1"));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("content", content));
                params.add(new BasicNameValuePair("classid", "1"));
                if (picname != null) {
                    if (!picname.equals("null")) {
                        params.add(new BasicNameValuePair("picurl", picname));
                    }

                }
                Log.e("addTrend-------", WebHome.SEND_TRENDS + params.toString());
                String result_data = NetBaseUtils.getResponseForPost(WebHome.SEND_TRENDS, params, mContext);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取校网通知公告、新闻动态、育儿知识list
     */
    public void getSchoolListInfo(final String key, final int what) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + "1";
                String result_data = NetUtil.getResponse(WebHome.NET_GETTEACHER_INFO + key, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = what;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取教师风采..................部分h5上一级页面
     */
    public void getTeacherInfo(final String key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + "1";
                String result_data = NetUtil.getResponse(WebHome.NET_GETTEACHER_INFO + key, data);
                Log.e("getTeacherInfo", WebHome.NET_GETTEACHER_INFO + key + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TEACHERINFO;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取系统通知
     */
    public void getArticleInfo() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&term_id=3";
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=getSystemMessages", data);
                Log.e("getArticleInfo", "http://wxt.xiaocool.net/index.php?g=apps&m=index&a=" + data);
                Log.e("getArticleInfo", result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TEACHERINFO;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 发送入学报名
     */
//    schoolid,username,sex(0,1),birthday(1999-05-21),address,classname(与账号所在班级无关，用户手动填写),
//    phone,qq,weixin(微信名称),education(学历),school(毕业学校),remark,photo(先调用上传图片接口，此处填写文件名)
    public void send_apply(final String username, final String sex, final String birthday, final String address,
                           final String classname, final String phone, final String qq, final String weixin, final String education,
                           final String school, final String remark, final String photo) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("schoolid", "1"));
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("sex", sex));
                params.add(new BasicNameValuePair("birthday", birthday));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("classname", classname));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("qq", qq));
                params.add(new BasicNameValuePair("weixin", weixin));
                params.add(new BasicNameValuePair("education", education));
                params.add(new BasicNameValuePair("school", school));
                params.add(new BasicNameValuePair("remark", remark));

                if (photo != null) {
                    params.add(new BasicNameValuePair("photo", photo));
                }
//                String data = "&schoolid="+user.getSchoolId()+"&username="+username+"&sex="+sex+"&birthday="+birthday+"&address="+address+
//                        "&classname"+classname+"&phone="+phone+ "&qq="+qq+"&weixin="+weixin+"&education="+education+"&school="+school+"&remark="+remark+
//                "&photo="+photo;
//                String result_data = NetUtil.getResponse(WebHome.SEND_APPLY, data);
//                Log.e("sdsd","jingbujing");
                String result_data = NetBaseUtils.getResponseForPost(WebHome.SEND_APPLY, params, mContext);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.SEND_APPLY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=LeaveMessage&userid=597&message=
    public void onlineComment(final String message) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId() + "&message=" + message;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=LeaveMessage", data);
                Log.e("result_data", WebHome.CLASS_SCHEDULE + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.ONLINECOMMENT;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //获取宝宝课成
    public void classScheduleNew() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&schoolid=" + "1" + "&classid=" + user.getClassId();
                String result_data = NetUtil.getResponse(WebHome.CLASS_SCHEDULE, data);
                Log.e("result_data", WebHome.CLASS_SCHEDULE + data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.CLASS_SCHEDULE;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 更新头像
     */
    public void updateHeadImg(final String avatar, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId() + "&avatar=" + avatar;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=UpdateUserAvatar", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 更新头像(通用)
     */
    public void updateAvatar(final String id, final String avatar, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + id + "&avatar=" + avatar;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=UpdateUserAvatar", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 更新名字
     */
    public void updateName(final String avatar, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId() + "&nicename=" + avatar;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=UpdateUserName", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 更新性别
     */
    public void updateSex(final int avatar, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + user.getUserId() + "&sex=" + avatar;
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=UpdateUserSex", data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //http://wxt.xiaocool.net/index.php?g=apps&m=index&a=CheckVersion&versionid=10&type=android
    /**
     * 获取版本号
     */
    public void checkVersion(final String versionid, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&versionid=" + "10";
                String result_data = NetUtil.getResponse("http://wxt.xiaocool.net/index.php?g=apps&m=index&a=CheckVersion&type=android", data);
                Log.e("hello","http://wxt.xiaocool.net/index.php?g=apps&m=index&a=CheckVersion&type=android"+data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void send_chat(final List<NameValuePair> params, final int i) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                String result_data = NetBaseUtils.getResponseForPost("http://wxt.xiaocool.net/index.php?g=apps&m=message&a=SendChatData", params, mContext);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = i;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
