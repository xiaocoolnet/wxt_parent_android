package cn.xiaocool.wxtparent.bean.find;

import java.io.Serializable;

/**
 * Created by mac on 16/1/31.
 */
public class NewsImgInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String path;
    private String content;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
        return "NewsImgInfo [path=" + path + ", content=" + content + "]";
    }


}
