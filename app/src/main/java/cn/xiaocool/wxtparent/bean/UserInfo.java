package cn.xiaocool.wxtparent.bean;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import cn.xiaocool.wxtparent.db.sp.UserSp;

/**
 * Created by mac on 16/1/23.
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;// 用户ID
    private String userIdTemp;
    private String userPassword;// 用户密码
    private String userImg;// 用户头像
    private String userPhone;// 用户手机号
    private String userCode;// 验证码
    private String userName;// 姓名
    private String schoolId;//学校id
    private String classId;//班级id
    //当前宝宝的id
    private String childId;
    private String childName;
    private String childAvator;
    private String className;

    private String userGender;// 性别
    private String userCompany;// 公司
    private String userPosition;// 职务
    private String userCityId;// 城市ID
    private String userCity;// 城市名
    private String userEase;// 环信名
    private String userEasePassword;// 环信密码
    private ArrayList<TagInfo> userTags;// 用户的标签
    private ArrayList<TagInfo> userCategoryTags;// 用户的业态标签
    private ArrayList<TagInfo> userSkillsTags;// 用户的技能标签
    private ArrayList<TagInfo> userPersonalTags;// 用户的行业标签
    private ArrayList<MyBaby> user_mybaby;


    private String isKa;// 0不是，1是

    public UserInfo() {

    }

    public UserInfo(Context context) {
        readData(context);
    }

    public void readData(Context context) {
        UserSp sp = new UserSp(context);
        sp.read(this);
    }

    public void writeData(Context context) {
        UserSp sp = new UserSp(context);
        sp.write(this);
    }

    public void clearData(Context context) {
        UserSp sp = new UserSp(context);
        sp.clear();
    }

    public void clearDataExceptPhone(Context mContext) {
        // TODO Auto-generated method stub
        UserSp sp = new UserSp(mContext);
        UserInfo user = new UserInfo();
        user.setUserPhone(sp.read().getUserPhone());
        clearData(mContext);
        user.writeData(mContext);
    }

    /**
     * 已登录过得，自动进入
     *
     * @return
     */
    public boolean isLogined() {
        if (this.getUserId().equals("")) {
            return false;
        }
        return true;
    }


    public String getUserId() {
        if (userId == null) {
            return "";
        } else if (userId.equals("null")) {
            return "";
        }
        Log.e("aaaaaaaaaaaaaaaaaaaaaa", userId);
        return userId;
    }

    public String getSchoolId() {
        if (schoolId == null) {
            return "";
        } else if (schoolId.equals("null")) {
            return "";
        }
        Log.e("this id is", "aaa");
        return schoolId;
    }

    public String getClassId() {
        if (classId == null) {
            return "";
        } else if (classId.equals("null")) {
            return "";
        }
        Log.e("this id is", "aaa");
        return classId;
    }


    public void setUserId(String userId) {
        Log.e("set userid ok", userId);
        this.userId = userId;
        Log.e("this.userId is", this.userId);
    }

    public String getUserIdTemp() {
        if (userIdTemp == null) {
            return "";
        } else if (userIdTemp.equals("null")) {
            return "";
        }
        return userIdTemp;
    }

    public void setUserIdTemp(String userIdTemp) {
        this.userIdTemp = userIdTemp;
    }

    public String getUserPhone() {
        if (userPhone == null) {
            return "";
        } else if (userPhone.equals("null")) {
            return "";
        }
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCode() {
        if (userCode == null) {
            return "";
        } else if (userCode.equals("null")) {
            return "";
        }
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        if (userName == null) {
            return "null";
        } else if (userName.equals("null")) {
            return "null";
        }
        return userName;
    }

    public void setUserName(String userName) {
        Log.e("setname", "begin");
        Log.e("setname", userName);
        this.userName = userName;
        Log.e("this.userName is", this.userName);

    }

    public void setSchoolId(String schoolId) {
        Log.e("set_school_id", "begin");
        Log.e("school_id", schoolId);
        this.schoolId = schoolId;
        Log.e("schoolID", this.schoolId);
    }

    public void setClassId(String classId) {
        Log.e("set_class_id", "begin");
        Log.e("class_id", classId);
        this.classId = classId;
        Log.e("schoolID", this.classId);
    }


    public String getUserGender() {
        if (userGender == null) {
            return "";
        } else if (userGender.equals("null")) {
            return "";
        }
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserCompany() {
        if (userCompany == null) {
            return "";
        } else if (userCompany.equals("null")) {
            return "";
        }
        return userCompany;
    }

    public String getChildId() {
        if (childId == null) {
            return "";
        } else if (childId.equals("null")) {
            return "";
        }
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        if (childName == null) {
            return "";
        } else if (childName.equals("null")) {
            return "";
        }
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildAvator() {
        if (childAvator == null) {
            return "";
        } else if (childAvator.equals("null")) {
            return "";
        }
        return childAvator;
    }

    public void setChildAvator(String childAvator) {
        this.childAvator = childAvator;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserPosition() {
        if (userPosition == null) {
            return "";
        } else if (userPosition.equals("null")) {
            return "";
        }
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserCityId() {
        if (userCityId == null) {
            return "";
        } else if (userCityId.equals("null")) {
            return "";
        }
        return userCityId;
    }

    public void setUserCityId(String userCityId) {
        this.userCityId = userCityId;
    }

    public String getUserCity() {
        if (userCity == null) {
            return "";
        } else if (userCity.equals("null")) {
            return "";
        }
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getUserPassword() {
        if (userPassword == null) {
            return "";
        } else if (userPassword.equals("null")) {
            return "";
        }
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getClassName() {
        if (className == null) {
            return "";
        } else if (className.equals("null")) {
            return "";
        }
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserImg() {
        if (userImg == null) {
            return "";
        } else if (userImg.equals("null")) {
            return "";
        }
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserEase() {
        if (userEase == null) {
            return "";
        } else if (userEase.equals("null")) {
            return "";
        }
        return userEase;
    }

    public void setUserEase(String userEase) {
        this.userEase = userEase;
    }

    public String getUserEasePassword() {
        return "1234567890";
    }

    public void setUserEasePassword(String userEasePassword) {
        this.userEasePassword = "1234567890";
    }

    public ArrayList<TagInfo> getUserTags() {
        if (userTags == null) {
            return new ArrayList<TagInfo>();
        } else if (userTags.equals("null")) {
            return new ArrayList<TagInfo>();
        }
        return userTags;
    }

    public void setUserTags(ArrayList<TagInfo> userTags) {
        this.userTags = userTags;
    }

    public ArrayList<TagInfo> getUserCategoryTags() {
        if (userCategoryTags == null) {
            return new ArrayList<TagInfo>();
        } else if (userCategoryTags.equals("null")) {
            return new ArrayList<TagInfo>();
        }
        return userCategoryTags;
    }

    public void setUserCategoryTags(ArrayList<TagInfo> userCategoryTags) {
        this.userCategoryTags = userCategoryTags;
    }

    public ArrayList<TagInfo> getUserSkillsTags() {
        if (userSkillsTags == null) {
            return new ArrayList<TagInfo>();
        } else if (userSkillsTags.equals("null")) {
            return new ArrayList<TagInfo>();
        }
        return userSkillsTags;
    }

    public void setUserSkillsTags(ArrayList<TagInfo> userSkillsTags) {
        this.userSkillsTags = userSkillsTags;
    }

    public ArrayList<TagInfo> getUserPersonalTags() {
        if (userPersonalTags == null) {
            return new ArrayList<TagInfo>();
        } else if (userPersonalTags.equals("null")) {
            return new ArrayList<TagInfo>();
        }
        return userPersonalTags;
    }

    public void setUserPersonalTags(ArrayList<TagInfo> userPersonalTags) {
        this.userPersonalTags = userPersonalTags;
    }


    public String getIsKa() {
        if (isKa == null) {
            return "";
        } else if (isKa.equals("null")) {
            return "";
        }
        return isKa;
    }

    public void setIsKa(String isKa) {
        this.isKa = isKa;
    }

    @Override
    public String toString() {
        return "UserInfo [userId=" + userId + ", userIdTemp=" + userIdTemp + ", userPassword=" + userPassword + ", userImg=" + userImg + ", userPhone=" + userPhone + ", userCode=" + userCode
                + ", userName=" + userName + ", userGender=" + userGender + ", userCompany=" + userCompany + ", userPosition=" + userPosition + ", userCityId=" + userCityId + ", userCity=" + userCity
                + ", userEase=" + userEase + ", userEasePassword=" + userEasePassword + ", userTags=" + userTags + ", userCategoryTags=" + userCategoryTags + ", userSkillsTags=" + userSkillsTags
                + ", userPersonalTags=" + userPersonalTags + ", isKa=" + isKa + "]";
    }

}