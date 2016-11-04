package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class AnnouncementInfo implements Serializable {
    private String id;
    private String teacherName;
    private String teacherAvator;
    private String time;
    private String title;
    private String content;
    private ArrayList<String> pics;
    private String readTime;
    private ArrayList<Reciver> recivers;

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public ArrayList<Reciver> getRecivers() {
        return recivers;
    }

    public void setRecivers(ArrayList<Reciver> recivers) {
        this.recivers = recivers;
    }
}
