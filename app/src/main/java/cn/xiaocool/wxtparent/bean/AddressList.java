package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/29.
 */
public class AddressList implements Serializable{

    /**
     * status : success
     * data : [{"schoolid":"1","classid":"1","schoolname":"北京市第一幼儿园","classname":"一年级一班","teacherinfo":[{"id":"609","name":"老王","phone":"18865511108"},{"id":"605","name":"潘宁","phone":"15589521956"}]},{"schoolid":"1","classid":"2","schoolname":"北京市第一幼儿园","classname":"一年级二班","teacherinfo":[{"id":"609","name":"老王","phone":"18865511108"},{"id":"576","name":"刘老师","phone":"15001312674"}]}]
     */

    private String status;
    /**
     * schoolid : 1
     * classid : 1
     * schoolname : 北京市第一幼儿园
     * classname : 一年级一班
     * teacherinfo : [{"id":"609","name":"老王","phone":"18865511108"},{"id":"605","name":"潘宁","phone":"15589521956"}]
     */

    private List<AddressListData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AddressListData> getData() {
        return data;
    }

    public void setData(List<AddressListData> data) {
        this.data = data;
    }

    public static class AddressListData {
        private String schoolid;
        private String classid;
        private String schoolname;
        private String classname;
        /**
         * id : 609
         * name : 老王
         * phone : 18865511108
         */

        private List<TeacherinfoBean> teacherinfo;

        public String getSchoolid() {
            return schoolid;
        }

        public void setSchoolid(String schoolid) {
            this.schoolid = schoolid;
        }

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public String getSchoolname() {
            return schoolname;
        }

        public void setSchoolname(String schoolname) {
            this.schoolname = schoolname;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public List<TeacherinfoBean> getTeacherinfo() {
            return teacherinfo;
        }

        public void setTeacherinfo(List<TeacherinfoBean> teacherinfo) {
            this.teacherinfo = teacherinfo;
        }

        public static class TeacherinfoBean {
            private String id;
            private String name;
            private String phone;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}
