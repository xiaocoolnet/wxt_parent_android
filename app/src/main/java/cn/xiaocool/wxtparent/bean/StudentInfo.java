package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class StudentInfo implements Serializable {
    private String id;
    private String name;
    private String sex;
    private String photo;
    private int isFriend;

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String phone) {
        this.photo = phone;
    }
}
