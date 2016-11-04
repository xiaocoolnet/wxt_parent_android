package cn.xiaocool.wxtparent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class BabyFriends implements Serializable{


    /**
     * data : [{"name":"朱","photo":"weixiaotong.png","id":"600"}]
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
         * name : 朱
         * photo : weixiaotong.png
         * id : 600
         */
        private String name;
        private String photo;
        private String id;

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getId() {
            return id;
        }
    }
}
