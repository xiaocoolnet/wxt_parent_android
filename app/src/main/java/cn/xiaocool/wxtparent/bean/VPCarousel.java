package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public class VPCarousel implements Serializable{
    /**
     * status : success
     * data : [{"ap_id":"1","picture_name":"1.png"},{"ap_id":"2","picture_name":"2.png"},{"ap_id":"3","picture_name":"3.png"}]
     */

    private String status;
    /**
     * ap_id : 1
     * picture_name : 1.png
     */

    private List<PicUri> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PicUri> getData() {
        return data;
    }

    public void setData(List<PicUri> data) {
        this.data = data;
    }

    public static class PicUri {
        private String ap_id;
        private String picture_name;

        public String getAp_id() {
            return ap_id;
        }

        public void setAp_id(String ap_id) {
            this.ap_id = ap_id;
        }

        public String getPicture_name() {
            return picture_name;
        }

        public void setPicture_name(String picture_name) {
            this.picture_name = picture_name;
        }
    }
}
