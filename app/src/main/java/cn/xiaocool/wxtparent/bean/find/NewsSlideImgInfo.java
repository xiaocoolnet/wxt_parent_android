package cn.xiaocool.wxtparent.bean.find;

import java.io.Serializable;

/**
 * Created by mac on 16/1/31.
 */
public class NewsSlideImgInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "NewsImgInfo [path=" + path + "]";
    }
}