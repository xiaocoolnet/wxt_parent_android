package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by THB on 2016/7/19.
 */
public class Address implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String avator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
