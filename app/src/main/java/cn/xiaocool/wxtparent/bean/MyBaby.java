package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyBaby implements Serializable{

    /**
     * status : success
     * data : [{"id":"2","studentid":"599","userid":"597","appellation":"父亲","bind_status":"1","relation_rank":"1","preferred":"1","time":"1453282437"},{"id":"1","studentid":"605","userid":"597","appellation":"舅舅","bind_status":"0","relation_rank":"2","preferred":"0","time":"1453282437"}]
     */

    private String status;
    /**
     * id : 2
     * studentid : 599
     * userid : 597
     * appellation : 父亲
     * bind_status : 1
     * relation_rank : 1
     * preferred : 1
     * time : 1453282437
     */

    private List<MyBabyData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MyBabyData> getData() {
        return data;
    }

    public void setData(List<MyBabyData> data) {
        this.data = data;
    }

    public static class MyBabyData {
        private String id;
        private String studentid;
        private String userid;
        private String appellation;
        private String bind_status;
        private String relation_rank;
        private String preferred;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStudentid() {
            return studentid;
        }

        public void setStudentid(String studentid) {
            this.studentid = studentid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAppellation() {
            return appellation;
        }

        public void setAppellation(String appellation) {
            this.appellation = appellation;
        }

        public String getBind_status() {
            return bind_status;
        }

        public void setBind_status(String bind_status) {
            this.bind_status = bind_status;
        }

        public String getRelation_rank() {
            return relation_rank;
        }

        public void setRelation_rank(String relation_rank) {
            this.relation_rank = relation_rank;
        }

        public String getPreferred() {
            return preferred;
        }

        public void setPreferred(String preferred) {
            this.preferred = preferred;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
