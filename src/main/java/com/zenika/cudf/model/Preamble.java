package com.zenika.cudf.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class Preamble {

    private Map<String, String> properties = new HashMap<String, String>();
    private String univChecksum;
    private String statusChecksum;
    private String reqChecksum;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getUnivChecksum() {
        return univChecksum;
    }

    public void setUnivChecksum(String univChecksum) {
        this.univChecksum = univChecksum;
    }

    public String getStatusChecksum() {
        return statusChecksum;
    }

    public void setStatusChecksum(String statusChecksum) {
        this.statusChecksum = statusChecksum;
    }

    public String getReqChecksum() {
        return reqChecksum;
    }

    public void setReqChecksum(String reqChecksum) {
        this.reqChecksum = reqChecksum;
    }
}
