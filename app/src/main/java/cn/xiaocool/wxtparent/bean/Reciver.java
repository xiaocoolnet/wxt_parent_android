package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class Reciver implements Serializable {
    private String id;
    private String receiverId;
    private String readTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
