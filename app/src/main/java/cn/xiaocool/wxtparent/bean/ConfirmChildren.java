package cn.xiaocool.wxtparent.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ConfirmChildren {

    // 原来的接口
    /**
     * status : success
     * data : [{"id":"1","teacherid":"609","userid":"28","photo":"","delivery_time":"1457596464","delivery_status":"1"},{"id":"2","teacherid":"609","userid":"28","photo":"","delivery_time":"1457596466","delivery_status":"1"}]
     */

    //现在的接口
            /**
             *
             *status":"success","data":[{"id":"1","teacherid":"609","userid":"28","photo":"","delivery_time":"1457596464","delivery_status":"1","parentid":"122","parenttime":"1462415788","teachername":"\u8001\u738b","teacheravatar":"weixiaotong.png","teacherphone":"18865511108"
             * {"id":"2","teacherid":"609","userid":"28","photo":"","delivery_time":"1457596466","delivery_status":"1","parentid":"0","parenttime":"0","teachername":"\u8001\u738b","teacheravatar":"weixiaotong.png","teacherphone":"18865511108"}]}
             *
             *
             * 多了几个字段
             *parentid
             * parenttime
             *teachername
             * teacheravatar
             * teacherphone
             *
             *
             * */



    private String status;
    /**
     * id : 1
     * teacherid : 609
     * userid : 28
     * photo :
     * delivery_time : 1457596464
     * delivery_status : 1
     */

    private List<ConfirmChildrenData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ConfirmChildrenData> getData() {
        return data;
    }

    public void setData(List<ConfirmChildrenData> data) {
        this.data = data;
    }

    public static class ConfirmChildrenData {
        private String id;
        private String teacherid;
        private String userid;
        private String photo;
        private String content;
        private String delivery_time;
        private String delivery_status;
        private String parentid;
        private String parenttime;
        private String teachername;
        private String teacheravatar;
        private String teacherphone;
        private String teacherDuty;

        public String getTeacherDuty() {
            return teacherDuty;
        }

        public void setTeacherDuty(String teacherDuty) {
            this.teacherDuty = teacherDuty;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getDelivery_status() {
            return delivery_status;
        }

        public void setDelivery_status(String delivery_status) {
            this.delivery_status = delivery_status;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getParenttime() {
            return parenttime;
        }

        public void setParenttime(String parenttime) {
            this.parenttime = parenttime;
        }

        public String getTeachername() {
            return teachername;
        }

        public void setTeachername(String teachername) {
            this.teachername = teachername;
        }

        public String getTeacheravatar() {
            return teacheravatar;
        }

        public void setTeacheravatar(String teacheravatar) {
            this.teacheravatar = teacheravatar;
        }

        public String getTeacherphone() {
            return teacherphone;
        }

        public void setTeacherphone(String teacherphone) {
            this.teacherphone = teacherphone;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
