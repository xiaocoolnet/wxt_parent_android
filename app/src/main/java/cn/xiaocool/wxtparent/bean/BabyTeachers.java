package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
public class BabyTeachers implements Serializable{

    /**
     * status : success
     * data : [{"teacherid":"609","teachername":"老王","teacherphone":"18865511108"},{"teacherid":"605","teachername":"潘宁","teacherphone":"15589521956"}]
     */

    private String status;
    /**
     * teacherid : 609
     * teachername : 老王
     * teacherphone : 18865511108
     */

    private List<BabyTeacherBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BabyTeacherBean> getData() {
        return data;
    }

    public void setData(List<BabyTeacherBean> data) {
        this.data = data;
    }

    public static class BabyTeacherBean {
        private String teacherid;
        private String teachername;
        private String teacherphone;

        public String getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public String getTeachername() {
            return teachername;
        }

        public void setTeachername(String teachername) {
            this.teachername = teachername;
        }

        public String getTeacherphone() {
            return teacherphone;
        }

        public void setTeacherphone(String teacherphone) {
            this.teacherphone = teacherphone;
        }
    }
}
