package cn.xiaocool.wxtparent.net.request.constant;

/**
 * Created by wzh on 2016/2/27.
 */
public interface WebHome extends NetBaseConstant {
    /**
     * 发送报名
     */
    String SEND_APPLY = NET_API_HOST2+"a=enterschol";

    String NET_GETTEACHER_INFO = NET_API_HOST2+"a=";
    /**
     * 发送评论
     */
    String SEND_COMMENT = NET_API_HOST2+"a=SetComment";
    /**
     * 点赞
     */
    String NET_SET_PRAISE = NET_API_HOST2+"a=SetLike";
    /**
     * 取消赞
     */
    String NET_DEL_PRAISE = NET_API_HOST2+"a=ResetLike";
    /**
     * 获取通知公告栏
     */
    public static final String NET_GET_ANNOUNCEMENT = NET_API_HOST + "a=GetSeverStatus";

    /**
     * 获取用户绑定的当前孩子信息
     */
    String WEB_GET_BABY_INFO = NET_API_HOST + "a=GetUserRelation";

    /**
     * 获取宝宝家长列表
     */
    String WEB_GET_PARENT_INFO = NET_API_HOST_3 + "a=GetInviteFamily";
    /**
     * 获取到校时间体温，离校时间、体温
     */
    String WEB_GET_TIME_TEM = NET_API_HOST + "a=GetStudentLog";
    /**
     * 获取身高、体重数据
     */
    String WEB_GET_STATURE_WEIGHT = NET_API_HOST + "a=GetChange_sta_wei&stuid";
    /**
     * 获取今日记录
     */
    String TODAY_RECORD = NET_API_HOST + "a=GetStudentLog";
    /**
     * 获取服务状态
     */
    String ME_GET_SERVICE_STATUS = NET_API_HOST + "a=GetSeverStatus";

    /**
     * 写入体重数据
     */
    String SPACE_INPUT_WEIGHT = NET_API_HOST + "a=RecordWeight";
    /**
     * 写入身高数据
     */
    String SPACE_INPUT_HEIGHT = NET_API_HOST + "a=RecordHeight";
    /**
     * 消息界面收到的消息
     */
    String SPACE_RECEIVED_MESSAGE = NET_API_HOST + "a=ReceiveidMessage";
    /**
     * 消息界面发送的消息
     */
    String SPACE_SEND_MESSAGE = NET_API_HOST + "a=SentMessage";
    /**
     * 消息界面发送的消息
     */
    String SPACE_CONTACTS = NET_API_HOST + "a=MessageAddress";

    /**
     * 获取宝宝好友
     */
    String GET_FRIEND = NET_API_HOST_3 + "a=getfriendlist";

    /**
     * 获取宝宝信息
     */
    String GET_BABY_INFO = NET_API_HOST_3 + "a=GetBabyInfo";

    /**
     * 修改爱好
     */
    String CHANGE_HOBBY = NET_API_HOST_3 + "a=UpdateHobby";

    /**
     * 修改头像
     */
    String CHANGE_AVATOR = NET_API_HOST_3 + "a=UpdateUserAvatar";

    /**
     * 修改生日
     */
    String CHANGE_BIRTH = NET_API_HOST_3 + "a=UpdateBirth";

    /**
     * 修改家庭住址
     */
    String CHANGE_ADDRESS = NET_API_HOST_3 + "a=UpdateAddress";

    /**
     * 修改接送人
     */
    String CHANGE_RELIVER = NET_API_HOST_3 + "a=UpdateDelivery";

    /**
     * 修改全家福
     */
    String CHANGE_FAMILY = NET_API_HOST_3 + "a=UpdatePhoto";

    /**
     * 修改妈妈职业
     */
    String CHANGE_MOTHER_JOB = NET_API_HOST_3 + "a=UpdateMoProfession";

    /**
     * 喜欢和妈妈做什么
     */
    String CHANGE_MOTHER_LIKE = NET_API_HOST_3 + "a=UpdateWithMother";

    /**
     * 修改爸爸职业
     */
    String CHANGE_FATHER_JOB = NET_API_HOST_3 + "a=UpdateFaProfession";

    /**
     * 喜欢和爸爸做什么
     */
    String CHANGE_FATHER_LIKE = NET_API_HOST_3 + "a=UpdateWithFather";

    /**
     * 报名班级活动
     */
    String APPLY_EVENT = NET_API_HOST_2 + "a=ApplyActivity";

    /**
     * 获取老师点评
     */
    String GET_TEACOMMENT = NET_API_HOST_3 + "a=GetTeacherComment";
     /**
     * 消息界面 我的作业 列表
     */
    String SPACE_MYHOMEWORK = NET_API_HOST_3 + "a=gethomeworkmessage&";

    /**
     * 消息界面 群发消息列表http://wxt.xiaocool.net/index.php?g=apps
     */
    String SPACE_NEWSGROUP = NET_HOST_BASE + "&m=Message&a=user_reception_message";

    /*
    *
    * 添加    发送  动态  宝宝相册  班级相册
    * */
    String SEND_TRENDS = NET_API_HOST + "a=WriteMicroblog";


    /**
     * 消息界面 我的作业 点赞
     */
    String SPACE_MYHOMEWORK_SET_LIKE = NET_API_HOST_1 + "a=SetLike";
    /**
     * 消息界面 我的作业 取消点赞
     */
    String SPACE_MYHOMEWORK_DEL_LIKE = NET_API_HOST_1 + "a=ResetLike";

    /*
    * 消息界面  添加评论
    * */
    String SPACE_SEND_PINGLUN = NET_API_HOST_1 + "a=GetCommentlist";

    /*
    * 消息界面  评论发送
    *
    * */
    String SPACE_SEND_PINGLUN2 = NET_API_HOST_1 + "a=SetComment";
    /*
    * 通讯录 电话的列表
    *
    * */
    String MESSAGEADDRESS = NET_API_HOST_2 + "a=getstudentclasslistandteacherlist";
    /*
        * 添加家长
        *
        * */
    String ADDPARENT = NET_API_HOST_3 + "a=AddInviteFamily";
    /*
    *通知公告
    *
    *
    * */

    String SPACE_ANNOUNCEMENT = NET_API_HOST_1 + "a=get_receive_notice";


    /*
    *
    * 班级活动
    *
    * */

    String CLASS_EVENTS = NET_API_HOST_3 + "a=getactivitylist";


    /**
     * 获取食谱
     */
    String RECIPES = NET_API_HOST + "a=WeekRecipe";


    /**
     * 获取食谱 (新)
     */
    String RECIPESNEW = NET_API_HOST_1 + "a=getCookbookContent";


    /**
     * 获取通知公告条目
     */
    String ANNOUNCEMENT = NET_API_HOST + "a=SchoolNotice";

    /**
     * 获取老师点评
     */
    String TEACHER_REVIEW = NET_API_HOST + "a=TeacherComment";

    /**
     * 获取  教师风采
     */
    String TEACHER_STYLE = NET_API_HOST_1 + "a=getteacherinfos";
    /**
     * 获取  宝宝秀场
     */
    String BABY_SHOW = NET_API_HOST_1 + "a=getbabyinfos";
    /**
     * 获取 校园招聘
     */
    String RECRUIT = NET_API_HOST_1 + "a=getjobs";
    /**
     * 获取  兴趣班
     */
    String INTERESTING_CLASS = NET_API_HOST_1 + "a=getInterestclass";
    /**
     * 发送  入学报名
     */
    String ENROLL = NET_API_HOST_1 + "a=enterschol ";


    /**
     * 获取  教师风采
     */
    String ANNOUNCEMENT_YQ = NET_API_HOST_1 + "a=getSchoolNotices";

    /**
     * 获取  新闻动态
     */
    String NEWS_DONGTAI = NET_API_HOST_1 + "a=getSchoolNews";


    /**
     * 获取班级课件
     */
    String CLASS_COURSEWARE = NET_API_HOST + "a=SchoolCourseware";

    /**
     * 获取在线请假
     */
    String LEAVE_REASON = NET_API_HOST + "a=OnlineLeave";


    /**
     * 获取在线请假（新的接口）
     */
    String LEAVE_LEIBIAO = NET_API_HOST_3 + "a=getleavelist";

    /**
     * 获取在线请假（新的接口）获得孩子列表
     */
    String LEAVE_STU = NET_API_HOST + "a=GetUserRelation";

    //添加 在线请假
    String LEAVE_ADD = NET_API_HOST_3 + "a=addleave";


    /**
     * 获取家长叮嘱(旧的，post传值，已经弃用)
     //    String PARENT_WARN = NET_API_HOST + "a=PatriarchEnjoin";
     */


    /**
     * 新增家长叮嘱
     */
    String PARENT_WARN_ADD = NET_API_HOST_3 + "a=addentrust";

    /*
     *
     * 家长端获取家长叮嘱列表
     */
    String PARENT_WARN = NET_API_HOST_3 + "a=getentrustlist";
    /*
    *
    * 教师回复家长叮嘱

    * */
    String PARENT_WARN_TEACHER_REVERSION = NET_API_HOST_1 + "a=feedbackentrust";


    /**
     * 获取班级活动
     */
//    String CLASS_EVENTS = NET_API_HOST+"a=ClassActivity";
    /**
     * 获取育儿知识
     */
    String REARING_CHILD = NET_API_HOST + "a=getParentsThings";
    /**
     * 获取开心学堂
     */
    String HAPPY_SCHOOL = NET_API_HOST + "a=HappySchool";
    /**
     * 发送家长叮嘱
     */
    String PARENTS_TOLD = NET_API_HOST + "a=PatriarchEnjoin";
    /**
     * 获取班级课表
     */
    String CLASS_SCHEDULE = NET_API_HOST_1 + "a=ClassSyllabus";


    /**
     * 获取宝宝老师
     */
    String BABY_TEACHER = NET_API_HOST + "a=OnlineLeaveTeacher";


    /**
     * 获取宝宝好友列表
     */
    String BABY_FRIENDS = NET_API_HOST + "m=student&a=getfriendlist";
    /**
     * 处理待接确认
     */
    String HANDLE_CONFIRM = NET_API_HOST_3 + "a=confirmtransport&";
    /**
     * 获取待接列表
     */
    String CONFIRM_CHILDERN = NET_HOST_BASE + "&m=student&a=gettransportconfirmation";//原来的接口有问题这是才改的，个人感觉好像所有家长端的接口都有问题
    /**
     * 获取在线客服联系方式
     */
    String ONLINE_SERVICE = NET_API_HOST + "a=service";
    String TEACHER_ADDRESS = NET_API_HOST + "a=MessageAddress";
    /**
     * 获取发送消息
     */
    String SENDTOTEACJER = NET_API_HOST + "a=SendMessage";

    /**
     * 获取动态圈
     */
    public static final String NET_GET_CIRCLELIST = NET_API_HOST + "a=GetMicroblog";


    /*
      *发送  验证码
      * */
    public static final String SEND_CODE = NET_API_HOST + "a=SendMobileCode";
    /*
    *注册  （家长手机号+验证码+未激活 验证）
    * */
    public static final String REGISTER = NET_API_HOST + "a=UserVerify ";
    /*
  * 家长激活+设置密码

  * */
    public static final String SETTING_PASSWORD = NET_API_HOST + "a=UserActivate ";

    /*
    * 忘记密码-家长手机号+验证码+已激活 验证
    * */
    public static final String FORGET_PASSWORD = NET_API_HOST + "a=forgetPass_Verify";

    /*
    * 忘记密码-修改密码

    * */
    public static final String CHANGEPASSWORD = NET_API_HOST + "a=forgetPass_Activate";


}
