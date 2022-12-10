package pers.roger.purezafu.model.bean;

import java.util.List;

public class AppDataBean {
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

    public class DataBean {
        List<CatAppsBean> catApps;
        List<AppsBean> latestUseApps;
        List<AppsBean> myapps;

        public List<CatAppsBean> getCatApps() {
            return catApps;
        }

        public List<AppsBean> getLatestUseApps() {
            return latestUseApps;
        }

        public List<AppsBean> getMyapps() {
            return myapps;
        }
    }

    public class CatAppsBean {
        List<AppsBean> apps;
        String name;
        int catId;

        public List<AppsBean> getApps() {
            return apps;
        }

        public String getName() {
            return name;
        }

        public int getCatId() {
            return catId;
        }
    }

    public static class AppsBean {
        String shortName;
        String icon;
        int appType;
        String url;
        String ssourl;
        int id;

        public String getShortName() {
            return shortName;
        }

        public String getIcon() {
            return icon;
        }

        public int getAppType() {
            return appType;
        }

        public String getUrl() {
            return url;
        }

        public String getSsourl() {
            return ssourl;
        }

        public int getId() {
            return id;
        }
    }
}
