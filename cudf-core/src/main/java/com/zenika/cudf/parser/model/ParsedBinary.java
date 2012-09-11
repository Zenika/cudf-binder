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

import com.zenika.cudf.model.BinaryId;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ParsedBinary {

    public static final String NUMBER_START_LINE = "number: ";
    public static final String TYPE_START_LINE = "type: ";
    public static final String VERSION_START_LINE = "version: ";
    public static final String INSTALLED_START_LINE = "installed: ";
    public static final String DEPENDS_START_LINE = "depends: ";

    public static final String SEPARATOR = "%3a";

    private BinaryId binaryId;
    private String revision;
    private boolean installed;
    private String type;
    private Set<BinaryId> dependencies = new HashSet<BinaryId>();

    public BinaryId getBinaryId() {
        return binaryId;
    }

    public void setBinaryId(BinaryId binaryId) {
        this.binaryId = binaryId;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public Set<BinaryId> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<BinaryId> dependencies) {
        this.dependencies = dependencies;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsedBinary binary = (ParsedBinary) o;

        if (binaryId != null ? !binaryId.equals(binary.binaryId) : binary.binaryId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return binaryId != null ? binaryId.hashCode() : 0;
    }
}
