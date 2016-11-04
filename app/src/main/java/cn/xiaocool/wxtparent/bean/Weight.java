package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class Weight implements Serializable {
    private String weight;
    private String logdate;
    private String age;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLogdate() {
        return logdate;
    }

    public void setLogdate(String logdate) {
        this.logdate = logdate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
