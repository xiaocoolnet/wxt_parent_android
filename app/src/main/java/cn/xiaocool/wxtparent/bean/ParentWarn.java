package cn.xiaocool.wxtparent.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class ParentWarn {


    /**
     * status : success
     * data : [{"id":"32","userid":"12","studentid":"22","teacherid":"605","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1463450780","feedback":"iv减肥","feed_time":"1463456127","username":"","studentname":"","teachername":"潘宁","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"27","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1463390356","feedback":"保护","feed_time":"1463391103","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"17","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1462330167","feedback":"你换个","feed_time":"1463385132","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"16","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1461999741","feedback":"","feed_time":"0","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"15","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1461999740","feedback":"","feed_time":"0","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"14","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1461999739","feedback":"","feed_time":"0","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"13","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1461999651","feedback":"","feed_time":"0","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"12","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1461918991","feedback":"","feed_time":"0","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""},{"id":"11","userid":"12","studentid":"22","teacherid":"597","content":"孩子有点感冒，让中午吃药","photo":"","create_time":"1461916835","feedback":"","feed_time":"0","username":"","studentname":"","teachername":"李春波12312","teacheravatar":"weixiaotong.png","studentavatar":""}]
     */

    private String status;
    /**
     * id : 32
     * userid : 12
     * studentid : 22
     * teacherid : 605
     * content : 孩子有点感冒，让中午吃药
     * photo :
     * create_time : 1463450780
     * feedback : iv减肥
     * feed_time : 1463456127
     * username :
     * studentname :
     * teachername : 潘宁
     * teacheravatar : weixiaotong.png
     * studentavatar :
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String userid;
        private String studentid;
        private String teacherid;
        private String content;
        private String photo;
        private String create_time;
        private String feedback;
        private String feed_time;
        private String username;
        private String studentname;
        private String teachername;
        private String teacheravatar;
        private String studentavatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getStudentid() {
            return studentid;
        }

        public void setStudentid(String studentid) {
            this.studentid = studentid;
        }

        public String getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getFeed_time() {
            return feed_time;
        }

        public void setFeed_time(String feed_time) {
            this.feed_time = feed_time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStudentname() {
            return studentname;
        }

        public void setStudentname(String studentname) {
            this.studentname = studentname;
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

        public String getStudentavatar() {
            return studentavatar;
        }

        public void setStudentavatar(String studentavatar) {
            this.studentavatar = studentavatar;
        }
    }
}
