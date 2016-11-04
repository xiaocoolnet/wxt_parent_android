package cn.xiaocool.wxtparent.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class StuParent {

    /**
     * id : 665
     * name : 朱晟超
     * photo : weixiaotong.png
     * parent_info : [{"id":"734","name":"吴纯纯","photo":"20160909171603735.png","phone":"18358334203","appellation":"母亲"},{"id":"735","name":"陈伟","photo":"20160909182734735.png","phone":"18267892065","appellation":"父亲"}]
     */

    private String id;
    private String name;
    private String photo;
    /**
     * id : 734
     * name : 吴纯纯
     * photo : 20160909171603735.png
     * phone : 18358334203
     * appellation : 母亲
     */

    private List<ParentInfoBean> parent_info;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ParentInfoBean> getParent_info() {
        return parent_info;
    }

    public void setParent_info(List<ParentInfoBean> parent_info) {
        this.parent_info = parent_info;
    }

    public static class ParentInfoBean {
        private String id;
        private String name;
        private String photo;
        private String phone;
        private String appellation;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAppellation() {
            return appellation;
        }

        public void setAppellation(String appellation) {
            this.appellation = appellation;
        }
    }
}
