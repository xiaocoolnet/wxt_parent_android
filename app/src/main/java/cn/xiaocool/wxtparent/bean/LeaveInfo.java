package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by THB on 2016/7/23.
 */
public class LeaveInfo implements Serializable{
    private String id;
    private String studentName;
    private String studentAvator;
    private String content;
    private String state;
    private String feedback;
    private String teacherName;
    private String teacherAvator;
    private String begintime;
    private String endtime;
    private String dealtime;
    private String className;
    private String leaveType;
    private ArrayList<String> pics;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAvator() {
        return studentAvator;
    }

    public void setStudentAvator(String studentAvator) {
        this.studentAvator = studentAvator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAvator() {
        return teacherAvator;
    }

    public void setTeacherAvator(String teacherAvator) {
        this.teacherAvator = teacherAvator;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }
}
