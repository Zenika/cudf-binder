package com.zenika.cudf.parser.model;

import com.zenika.cudf.model.BinaryId;

import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ParsedBinary {

    private BinaryId binaryId;
    private String revision;
    private boolean installed;
    private String type;
    private Set<BinaryId> dependencies;

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
}
