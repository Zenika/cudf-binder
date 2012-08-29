package com.zenika.cudf.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class Binary implements Serializable {

    private final BinaryId binaryId;
    private String revision;
    private boolean installed;
    private String type;
    private Set<Binary> dependencies = new HashSet<Binary>();

    public Binary(BinaryId binaryId) {
        this.binaryId = binaryId;
    }

    public BinaryId getBinaryId() {
        return binaryId;
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

    public Set<Binary> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<Binary> dependencies) {
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

        Binary binary = (Binary) o;

        if (installed != binary.installed) return false;
        if (!binaryId.equals(binary.binaryId)) return false;
        if (type != null ? !type.equals(binary.type) : binary.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return binaryId.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Binary");
        sb.append("{binaryId=").append(binaryId);
        sb.append(", revision='").append(revision).append('\'');
        sb.append(", installed=").append(installed);
        sb.append(", type='").append(type).append('\'');
        sb.append(", dependencies=").append(dependencies);
        sb.append('}');
        return sb.toString();
    }
}
