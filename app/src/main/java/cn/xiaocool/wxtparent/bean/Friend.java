package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by THB on 2016/7/21.
 */
public class Friend implements Serializable{
    private String id;
    private String name;
    private String avator;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }
}
