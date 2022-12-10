package pers.roger.purezafu.model.bean;

import java.io.Serializable;

public class UpgradeBean implements Serializable {
    private String downloadUrl;
    private String updateLog;
    private String forceUpdateType;
    private boolean updateRequired;

    public void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }

    public void setForceUpdateType(String forceUpdateType) {
        this.forceUpdateType = forceUpdateType;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public String getForceUpdateType() {
        return forceUpdateType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean getUpdateRequired() {
        return updateRequired;
    }
}
