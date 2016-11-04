package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class Teacher implements Serializable {
    private String id;
    private String name;
    private String className;
    private String classid;
    private String photo;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
