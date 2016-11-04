package cn.xiaocool.wxtparent.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class Teacher_style {


    /**
     * data : [{"post_title":"教师风采   薛凌燕","thumb":"/data/upload/57633c0a1f947.jpg","post_date":"2000-01-01 00:00:00","schoolid":"1","post_keywords":"教师风采   薛凌燕1","id":"16","post_excerpt":"教师风采   薛凌燕","smeta":"{\"thumb\":\"57633c0a1f947.jpg\"}"},{"post_title":"教师风采   张艳艳","thumb":"/data/upload/57633c0a1f947.jpg","post_date":"2000-01-01 00:00:00","schoolid":"1","post_keywords":"教师风采   张艳艳","id":"17","post_excerpt":"作为语文教师，我们可能都嫉妒过学生在理科的课堂上，全神贯注的神情、顿悟时的手舞足措、解题时的沉迷忘我。","smeta":"{\"thumb\":\"57633c0a1f947.jpg\"}"}]
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
         * post_title : 教师风采   薛凌燕
         * thumb : /data/upload/57633c0a1f947.jpg
         * post_date : 2000-01-01 00:00:00
         * schoolid : 1
         * post_keywords : 教师风采   薛凌燕1
         * id : 16
         * post_excerpt : 教师风采   薛凌燕
         * smeta : {"thumb":"57633c0a1f947.jpg"}
         */
        private String post_title;
        private String thumb;
        private String post_date;
        private String schoolid;
        private String post_keywords;
        private String id;
        private String post_excerpt;
        private String smeta;

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public void setPost_date(String post_date) {
            this.post_date = post_date;
        }

        public void setSchoolid(String schoolid) {
            this.schoolid = schoolid;
        }

        public void setPost_keywords(String post_keywords) {
            this.post_keywords = post_keywords;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setPost_excerpt(String post_excerpt) {
            this.post_excerpt = post_excerpt;
        }

        public void setSmeta(String smeta) {
            this.smeta = smeta;
        }

        public String getPost_title() {
            return post_title;
        }

        public String getThumb() {
            return thumb;
        }

        public String getPost_date() {
            return post_date;
        }

        public String getSchoolid() {
            return schoolid;
        }

        public String getPost_keywords() {
            return post_keywords;
        }

        public String getId() {
            return id;
        }

        public String getPost_excerpt() {
            return post_excerpt;
        }

        public String getSmeta() {
            return smeta;
        }
    }
}
