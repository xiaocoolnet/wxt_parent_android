package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by THB on 2016/7/13.
 */
public class NewsGroup implements Serializable {
    private String message_id;
    private String message_type;
    private String read_time;
    private String id;
    private String send_user_id;
    private String send_user_name;
    private String teacherAvator;
    private String message_content;
    private String message_time;
    private String send_num;
    private ArrayList<String> pics;
    private ArrayList<Reciver> recivers;

    public String getTeacherAvator() {
        return teacherAvator;
    }

    public void setTeacherAvator(String teacherAvator) {
        this.teacherAvator = teacherAvator;
    }

    public ArrayList<Reciver> getRecivers() {
        return recivers;
    }

    public void setRecivers(ArrayList<Reciver> recivers) {
        this.recivers = recivers;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getRead_time() {
        return read_time;
    }

    public void setRead_time(String read_time) {
        this.read_time = read_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(String send_user_id) {
        this.send_user_id = send_user_id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getSend_num() {
        return send_num;
    }

    public void setSend_num(String send_num) {
        this.send_num = send_num;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }


    public String getSend_user_name() {
        return send_user_name;
    }

    public void setSend_user_name(String send_user_name) {
        this.send_user_name = send_user_name;
    }


}
