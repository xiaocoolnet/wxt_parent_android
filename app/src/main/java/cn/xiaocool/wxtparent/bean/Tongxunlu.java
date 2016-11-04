package cn.xiaocool.wxtparent.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class Tongxunlu {


    /**
     * data : [{"classid":"2","classname":"小二班","teacherinfo":[{"phone":"18363866803","name":"李春波12312","id":"597"},{"phone":"15888708668","name":"郑敬概","id":"614"}],"schoolid":"1","schoolname":"北京市第一幼儿园"},{"classid":"2","classname":"小二班","teacherinfo":[{"phone":"18363866803","name":"李春波12312","id":"597"},{"phone":"15888708668","name":"郑敬概","id":"614"}],"schoolid":"1","schoolname":"北京市第一幼儿园"}]
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
         * classid : 2
         * classname : 小二班
         * teacherinfo : [{"phone":"18363866803","name":"李春波12312","id":"597"},{"phone":"15888708668","name":"郑敬概","id":"614"}]
         * schoolid : 1
         * schoolname : 北京市第一幼儿园
         */
        private String classid;
        private String classname;
        private List<TeacherinfoEntity> teacherinfo;
        private String schoolid;
        private String schoolname;

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public void setTeacherinfo(List<TeacherinfoEntity> teacherinfo) {
            this.teacherinfo = teacherinfo;
        }

        public void setSchoolid(String schoolid) {
            this.schoolid = schoolid;
        }

        public void setSchoolname(String schoolname) {
            this.schoolname = schoolname;
        }

        public String getClassid() {
            return classid;
        }

        public String getClassname() {
            return classname;
        }

        public List<TeacherinfoEntity> getTeacherinfo() {
            return teacherinfo;
        }

        public String getSchoolid() {
            return schoolid;
        }

        public String getSchoolname() {
            return schoolname;
        }

        public class TeacherinfoEntity {
            /**
             * phone : 18363866803
             * name : 李春波12312
             * id : 597
             */
            private String phone;
            private String name;
            private String id;

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }
        }
    }
}
