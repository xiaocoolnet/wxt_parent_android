package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class Courseware implements Serializable {
    private String id;
    private String subject;
    private ArrayList<CourseInfo> courseInfos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<CourseInfo> getCourseInfos() {
        return courseInfos;
    }

    public void setCourseInfos(ArrayList<CourseInfo> courseInfos) {
        this.courseInfos = courseInfos;
    }
}
