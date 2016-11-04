package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by THB on 2016/7/23.
 */
public class ClassEvent implements Serializable {
    private String id;
    private String title;
    private String content;
    private String teacherName;
    private String teacherAvator;
    private String time;
    private ArrayList<String> applyid;
    private ArrayList<String> pics;
    private String isapply;
    private String begintime;
    private String endtime;
    private String starttime;
    private String finishtime;
    private String contactName;
    private String contactPhone;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getApplyid() {
        return applyid;
    }

    public void setApplyid(ArrayList<String> applyid) {
        this.applyid = applyid;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public String getIsapply() {
        return isapply;
    }

    public void setIsapply(String isapply) {
        this.isapply = isapply;
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }
}
