package cn.xiaocool.wxtparent.dao;

/**
 * Created by mac on 16/1/31.
 */

public interface CommunalInterfaces {
    int STATE = 1;
    String _STATE = "success";
    int SEND_APPLY = 0x10;
    int UPDATE_NAME = 0x11;
    int CITY = 0x12;
    int MEETSPECIALIIST_STATE = 0x13;
    int SERVICE_LIST = 0x14;
    int GETSERVICEUSERDATA = 0x16;
    int SOUSUO_LIST = 0x17;
    int ADDMAKESERVICE = 0x18;
    String FIRSTKEY = "6be10ac3ed9ea86c";
    int GETLIST = 0x21;
    int ITEM_LAYOUT_TYPE_COUNT = 2;
    int TYPE_ONE = 0;
    int TYPE_TWO = 1;
    int GETUSERDATA = 0x22;
    int USERCITY = 0x23;
    int SPICALISTCITY = 0x24;
    int AUDITYESSERVICE = 0x25;
    int AUDITNOSERVICE = 0x26;
    int UPDATE_BANKCARD = 0x27;
    int ADDREFERRAL = 0x28;
    int DELMEMBERCARD = 0x29;
    int GETMEMBERREFERRAL = 0x30;
    int GETSPECIALISTIMG = 0x31;
    int GETHTTPHEADIMG = 0x32;
    int GETSERVICECITY = 0x33;
    int GETMEMBERWITHLIST = 0x34;

    /* 资讯-专栏——begin */
    int GETNEWLIST = 0xa;
    int MEMBERCOLUMN = 0xb;
    int INFORMATIONLIST = 0xc;
    int NEWSMETTER = 0xd;
    int NEWSRELATED = 0xe;
    int INDEXNEWSLIST = 0x39;
    int INDEXSLIDENEWSLIST = 0x35;
    int DISCUSS = 0x36;
    int DISCUSSLIST = 0x37;
    int USERDATE = 0x38;
    int INDEXPUSHNEWSLIST = 0xf;
    int GETPUSHNEWSLIST = 16;
    int NETGETSEARCH = 0x40;
    int GETCOLUMN = 0x41;
    int COLUMNLIKE = 0x42;
    int NEWCOLLECT = 0x43;
    int WORKDISCUSS = 0x45;
    /* ——end—— */

    //校网
    //获取通知公告
    int GETANNOUNCEMENT = 0x44;
    int ADDWORKRING = 0x46;
    int GETPROJECTLIST = 0x47;
    int PROJECTCLASS = 0x48;
    //我的界面
    //获取服务状态
    int GETSERVICESTATUS = 0x49;
    int GETINFORMATIONUPWARD = 0x50;
    int HIM_GETNEWCOLLECT = 0x51;
    // 宝宝空间
    //获取身高体重
    int GET_STATURE_WEIGHT = 0x52;
    //获取当前孩子信息
    int GET_TIME_TEM = 0x53;
    //写入体重数据
    int INPUT_WEIGHT = 0x54;
    //写入身高数据
    int INPUT_HEIGHT = 0x55;
    //获取收到的消息
    int RECEIVED_MESSAGE = 0x56;
    //获取发送的消息
    int SEND_MESSAGE = 0x57;
    //获取食谱
    int RECIPES = 0x57;
    //获取食谱(新)
    int RECIPESNEW = 0x57;


    //获取通知公告
    int ANNOUNCEMENT = 0x58;
    //获取通知公告
    int ANNOUNCEMENT_YQ = 0x58;
    //获取新闻动态
    int NEWS_DONGTAI = 0x59;
    //获取老师点评
    int TEACHER_REVIEW = 0x58;
    //获取班级课件
    int CLASS_COURSEWARE = 0x59;
    //获取班级活动
    int CLASS_EVENTS = 0x70;
    //获取育儿知识
    int REARING_CHILD = 0x71;
    //获取开心学堂
    int HAPPY_SCHOOL = 0x72;
    //获取班级课程
    int CLASS_SCHEDULE = 0x73;
    //获取宝宝资料
    int BABY_INFO = 0x74;
    //获取宝宝老师
    int BABY_TEACHER = 0x75;

    //教师风采
    int TEACHER_STYLE = 0X75;
    //宝宝秀场
    int BABY_SHOW = 0X75;
    //校园招聘
    int RECRUIT = 0X75;

    //兴趣班
    int INTERESTING_CLASS = 0X75;
    //发送  入学报名
    int ENROLL = 0X75;


    //获取在线请假
    int LEAVE_REASON = 0x76;


    //获取在线请假 （新）
    int LEAVE_LEIBIAO = 0x76;

    //获取在线请假 （新） 学生列表
    int LEAVE_STU = 0x76;

    //添加   在线请假
    int LEAVE_ADD = 0x76;

    //在线客服
    int ONLINE_SERVICE = 0x77;




    //家长叮嘱   (即  家长端获取家长叮嘱列表)
    int PARENT_WARN = 0x78;

    //新增家长叮嘱
    int PARENT_WARN_ADD = 0x79;

    //教师回复家长叮嘱
    int PARENT_WARN_TEACHER_REVERSION = 0x80;




    //消息界面的联系人列表
    int CONTACTS_LIST = 0x79;

    //我的作业列表
    int SPACE_MYHOMEWORK = 0x79;

    //获取评论
    int SPACE_SEND_PINGLUN = 0x79;

    //在线请假
    int SPACE_LEAVE = 0x79;


    //通讯录 电话列表
    int MESSAGEADDRESS = 0x79;
    //通讯录
    int TEACHER_ADDRESS = 0x79;
    //发消息
    int SENDTOTEACJER = 0x80;
    //宝宝好友
    int BABY_FRIENDS = 0x80;
    //待接确认
    int CONFIRM_CHILDERN = 0x90;
    /**
     * 6种状态 0=>待审核 1=>未评价 2=>约见成功 3=>审核失败 4=>通过审核 5=>待付款
     */
    String MAKESTATE_CHECKPENDING = "0";
    String MAKESTATE_UNVALUED = "1";
    String MAKESTATE_SUCCESS = "2";
    String MAKESTATE_FAILUREAUDIT = "3";
    String MAKESTATE_STATUSCHECK = "4";
    String MAKESTATE_OBLIGATION = "5";

    //发送验证码
    int SEND_CODE = 0x91;

    //注册 家长手机号+验证码+未激活 验证
    int REGISTER = 0x92;

    //家长激活+设置密码
    int SETTING_PASSWORD = 0x93;

    //忘记密码-家长手机号+验证码+已激活 验证
    int FORGET_PASSWORD = 0x94;

    //忘记密码-修改密码
    int CHANGEPASSWORD = 0x95;

    //获取今日记录
    int TODAYRECORD = 0x96;

    //获取群发消息列表
    int NEWSGROUP = 0x97;

    //处理待接确认
    int HANDLECONFIRM = 0x98;

    //宝宝好友
    int BABYFRIEND = 0x98;

    //宝宝信息
    int BABYINFO = 0x99;

    //修改宝宝兴趣爱好
    int CHANGEHOBBY = 0x100;

    //修改宝宝生日
    int CHANGEBIRTH = 0x101;

    //修改宝宝地址
    int CHANGEADDRESS = 0x102;

    //修改宝宝接送人
    int CHANGERELIVER = 0x103;

    //修改用户头像
    int CHANGEAVATOR = 0x104;

    //修改全家福
    int CHANGEFAMILY = 0x105;

    //修改妈妈职业
    int CHANGEMOTHERJOB = 0x106;

    //修改与妈妈喜欢做什么
    int CHANGEMOTHERLIKE = 0x107;

    //修改爸爸职业
    int CHANGEFATHERJOB = 0x108;

    //修改与爸爸喜欢做什么
    int CHANGEFATHERLIKE = 0x109;

    //报名班级活动
    int APPLYEVENT = 0x110;

    //发送家长叮嘱回复
    int SEND_PARENT_REMARK = 0x111;

    //获取教师风采
    int TEACHERINFO = 0x112;

    //获取老师点评
    int GETTEACOMMENT = 0x112;

    //获取体重
    int GETWEIGHT = 0x113;

    //获取身高
    int GETSTATURE = 0x114;

    //获取宝宝家长列表
    int GETBABYPARENT = 0x115;

    //添加宝宝家长
    int ADDPARENT = 0x116;

    //删除家长
    int SELETEPARENT = 0x117;

    //设置主号
    int SETMAIN = 0x118;

    //获得好友列表
    int GETFRIEND = 0x119;

    //获得同班同学
    int GETSTUDENTLIST = 0x120;

    //加好友
    int ADDFRIEND = 0x121;

    //个人成长日记
    int MYDIARY = 0x122;

    //好友成长日记
    int FRIENDDIARY = 0x123;

    //同班家长列表
    int GETCLASSPARENT = 0x124;

    //补签
    int RESIGN = 0x125;

    //更新全家福
    int UPDATFAMILY = 0x126;

    //在线留言
    int ONLINECOMMENT = 0x127;
}
