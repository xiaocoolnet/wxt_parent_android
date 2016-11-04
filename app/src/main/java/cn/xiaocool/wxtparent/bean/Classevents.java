package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class Classevents implements Serializable{

    /**
     * status : success
     * data : [{"activity_title":"清明节扫墓春游","activity_content":"烈士陵园，纪念祖国先烈清明节扫墓春游","releasename":"刘老师","activity_pic":"chunyou.jpg","activity_time":"1458196885"},{"activity_title":"海边游泳","activity_content":"海边游泳","releasename":"老王","activity_pic":"youyong.jpg","activity_time":"1458132885"}]
     */

    private String status;
    /**
     * activity_title : 清明节扫墓春游
     * activity_content : 烈士陵园，纪念祖国先烈清明节扫墓春游
     * releasename : 刘老师
     * activity_pic : chunyou.jpg
     * activity_time : 1458196885
     */

    private List<ClassEventData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ClassEventData> getData() {
        return data;
    }

    public void setData(List<ClassEventData> data) {
        this.data = data;
    }

    public static class ClassEventData {
        private String activity_title;
        private String activity_content;
        private String releasename;
        private String activity_pic;
        private String activity_time;

        public String getActivity_title() {
            return activity_title;
        }

        public void setActivity_title(String activity_title) {
            this.activity_title = activity_title;
        }

        public String getActivity_content() {
            return activity_content;
        }

        public void setActivity_content(String activity_content) {
            this.activity_content = activity_content;
        }

        public String getReleasename() {
            return releasename;
        }

        public void setReleasename(String releasename) {
            this.releasename = releasename;
        }

        public String getActivity_pic() {
            return activity_pic;
        }

        public void setActivity_pic(String activity_pic) {
            this.activity_pic = activity_pic;
        }

        public String getActivity_time() {
            return activity_time;
        }

        public void setActivity_time(String activity_time) {
            this.activity_time = activity_time;
        }

        @Override
        public String toString() {
            return "ClassEventData{" +
                    "activity_title='" + activity_title + '\'' +
                    ", activity_content='" + activity_content + '\'' +
                    ", releasename='" + releasename + '\'' +
                    ", activity_pic='" + activity_pic + '\'' +
                    ", activity_time='" + activity_time + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Classevents{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
