package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class Leave_lb implements Serializable {


    /**
     * data : [{"reason":"Dressed","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1466179200","parentphone":"18363866803","begintime":"1465920000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"45","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Dressed","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1466179200","parentphone":"18363866803","begintime":"1465920000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"44","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Dressed","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1466179200","parentphone":"18363866803","begintime":"1465920000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"43","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Dressed","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1466179200","parentphone":"18363866803","begintime":"1465920000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"42","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Sass","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462550400","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"40","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Added","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462636800","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"39","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Added","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462636800","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"38","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Added","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462636800","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"37","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Added","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462636800","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"36","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Added","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462636800","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"35","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"},{"reason":"Added","parentname":"李春波12312","studentavatar":"weixiaotong.png","create_time":"0","studentname":"李春波12312","endtime":"1462636800","parentphone":"18363866803","begintime":"1462464000","deal_time":"0","parentid":"597","studentid":"599","feedback":"","teacherid":"599","teachername":"李小东","parentavatar":"weixiaotong.png","id":"34","teacheravatar":"weixiaotong.png","teacherphone":"18865511107","status":"1"}]
     * status : success
     */
    private List<DataEntity> data;
    private String status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public class DataEntity {
        /**
         * reason : Dressed
         * parentname : 李春波12312
         * studentavatar : weixiaotong.png
         * create_time : 0
         * studentname : 李春波12312
         * endtime : 1466179200
         * parentphone : 18363866803
         * begintime : 1465920000
         * deal_time : 0
         * parentid : 597
         * studentid : 599
         * feedback :
         * teacherid : 599
         * teachername : 李小东
         * parentavatar : weixiaotong.png
         * id : 45
         * teacheravatar : weixiaotong.png
         * teacherphone : 18865511107
         * status : 1
         */
        private String reason;
        private String parentname;
        private String studentavatar;
        private String create_time;
        private String studentname;
        private String endtime;
        private String parentphone;
        private String begintime;
        private String deal_time;
        private String parentid;
        private String studentid;
        private String feedback;
        private String teacherid;
        private String teachername;
        private String parentavatar;
        private String id;
        private String teacheravatar;
        private String teacherphone;
        private String status;

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setParentname(String parentname) {
            this.parentname = parentname;
        }

        public void setStudentavatar(String studentavatar) {
            this.studentavatar = studentavatar;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setStudentname(String studentname) {
            this.studentname = studentname;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public void setParentphone(String parentphone) {
            this.parentphone = parentphone;
        }

        public void setBegintime(String begintime) {
            this.begintime = begintime;
        }

        public void setDeal_time(String deal_time) {
            this.deal_time = deal_time;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public void setStudentid(String studentid) {
            this.studentid = studentid;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public void setTeachername(String teachername) {
            this.teachername = teachername;
        }

        public void setParentavatar(String parentavatar) {
            this.parentavatar = parentavatar;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTeacheravatar(String teacheravatar) {
            this.teacheravatar = teacheravatar;
        }

        public void setTeacherphone(String teacherphone) {
            this.teacherphone = teacherphone;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public String getParentname() {
            return parentname;
        }

        public String getStudentavatar() {
            return studentavatar;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getStudentname() {
            return studentname;
        }

        public String getEndtime() {
            return endtime;
        }

        public String getParentphone() {
            return parentphone;
        }

        public String getBegintime() {
            return begintime;
        }

        public String getDeal_time() {
            return deal_time;
        }

        public String getParentid() {
            return parentid;
        }

        public String getStudentid() {
            return studentid;
        }

        public String getFeedback() {
            return feedback;
        }

        public String getTeacherid() {
            return teacherid;
        }

        public String getTeachername() {
            return teachername;
        }

        public String getParentavatar() {
            return parentavatar;
        }

        public String getId() {
            return id;
        }

        public String getTeacheravatar() {
            return teacheravatar;
        }

        public String getTeacherphone() {
            return teacherphone;
        }

        public String getStatus() {
            return status;
        }
    }
}
