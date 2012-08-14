package com.zenika.cudf.parser.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ParsedPreamble {

    public static final String PROPERTY_START_LINE = "property: ";
    public static final String UNIV_CHECKSUM_START_LINE = "univ-checksum: ";
    public static final String STATUS_CHECKSUM_START_LINE = "status-checksum: ";
    public static final String REQ_CHECKSUM_START_LINE = "req-checksum: ";

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
