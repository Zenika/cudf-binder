package com.zenika.cudf.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preamble preamble = (Preamble) o;

        if (!properties.equals(preamble.properties)) return false;
        if (reqChecksum != null ? !reqChecksum.equals(preamble.reqChecksum) : preamble.reqChecksum != null)
            return false;
        if (statusChecksum != null ? !statusChecksum.equals(preamble.statusChecksum) : preamble.statusChecksum != null)
            return false;
        if (univChecksum != null ? !univChecksum.equals(preamble.univChecksum) : preamble.univChecksum != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = properties.hashCode();
        result = 31 * result + (univChecksum != null ? univChecksum.hashCode() : 0);
        result = 31 * result + (statusChecksum != null ? statusChecksum.hashCode() : 0);
        result = 31 * result + (reqChecksum != null ? reqChecksum.hashCode() : 0);
        return result;
    }

    public static Preamble getDefaultPreamble() {
        Preamble preamble = new Preamble();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("number", "string");
        properties.put("recommends", "vpkgformula = [true!]");
        properties.put("suggests", "vpkglist = []");
        properties.put("url", "string = [\"\"]");
        properties.put("type", "string");
        preamble.setProperties(properties);
        return preamble;
    }
}
