package cn.xiaocool.wxtparent.net.request.constant;

/**
 * Created by mac on 16/1/26.
 */

public interface NetBaseConstant {
    /**
     * H5拼接地址
     */
    public final static String NET_H5_HOST = "http://wxt.xiaocool.net/index.php?g=portal&m=article";
    /**
     * 网址
     */
    // public final static String NET_BASE_HOST = "http://182.92.112.97";
    // 本地服务
    // public final static String NET_BASE_HOST = "http://192.168.1.130";
    /**
     * 网络请求IP
     */
    // public final static String NET_BASE_HOST = "http://182.92.112.97:16896";
    /**
     * 网络请求IP
     */
    public final static String NET_BASE_HOST = "http://wxt.xiaocool.net";
    public final static String NET_HOST_BASE = "http://wxt.xiaocool.net/index.php?g=apps";
    /**
     * 咨询图片加载IP
     */
    public final static String NET_HOST = "http://182.92.112.97:16897";
    /**
    *API接口地址
    */
    public final static String NET_API_HOST = "http://wxt.xiaocool.net/index.php?g=apps&m=index&";
    public final static String NET_API_HOST2 = "http://wxt.xiaocool.net/index.php?g=apps&m=school&";

    /**
     *
     * 点赞、取消点赞、发送评论、通知公告 等
     *
     *
     * */
    public final static String NET_API_HOST_1 = "http://wxt.xiaocool.net/index.php?g=apps&m=school&";


    /*
    *
    *
    * 班级活动
    *
    * */

    public final static String NET_API_HOST_2 = "http://wxt.xiaocool.net/index.php?g=apps&m=teacher&";


    /*
      *
      *
      * 在线请假 （新的接口）
      *
      * */
    public final static String NET_API_HOST_3= "http://wxt.xiaocool.net/index.php?g=apps&m=student&";



    /**
     * 头像图片地址
     *
     */
    public final static String NET_AVATAR_HOST = "http://wxt.xiaocool.net/uploads/avatar";
    /**
     * 动态圈广告图片地址
     */
    public final static String NET_VIEWADAPTER_HOST = "http://wxt.xiaocool.net/uploads/Viwepager/";
    /**
     * 动态圈日志图片地址
     *
     */
    public final static String NET_CIRCLEPIC_HOST = "http://wxt.xiaocool.net/uploads/microblog/";
    /**
     * 食谱图片地址
     */
    String NET_RECIPES_HOST = "http://wxt.xiaocool.net/uploads/recipe/";
    //开心学堂图片
    public final static String NET_HAPPYSCHOOL_HOST = "http://wxt.xiaocool.net/uploads/happyschool/";
    //班级活动
    public final static String NET_CLASSEVENT_HOST = "http://wxt.xiaocool.net/uploads/ClassAction/";
}
