package com.zenika.cudf.parser.model;

/*
 * Copyright 2012 Zenika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
