package pers.roger.purezafu.model.bean;

import java.io.Serializable;

public class UserLoginBean implements Serializable {
    private String type;
    private String code;
    private String content;
    private String anchor;
    private DataBean data;

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnchor() {
        return anchor;
    }

    public String getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public DataBean getData() {
        return data;
    }


    public static class DataBean {
        private boolean isTeacher;
        private int uid;
        private String token;
        private String name;
        private String userName;

        public void setName(String name) {
            this.name = name;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setTeacher(boolean teacher) {
            isTeacher = teacher;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public String getToken() {
            return token;
        }

        public String getUserName() {
            return userName;
        }

        public boolean isTeacher() {
            return isTeacher;
        }
    }
}