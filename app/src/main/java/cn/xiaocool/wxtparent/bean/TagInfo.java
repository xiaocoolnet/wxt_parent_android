package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;

/**
 * Created by mac on 16/1/23.
 */

public class TagInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tagName;
    private String tagId;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "TagInfo [tagName=" + tagName + ", tagId=" + tagId + "]";
    }

}
