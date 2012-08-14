package com.zenika.cudf.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class CUDFDescriptor {

    private Preamble preamble;
    private Request request;
    private Set<Binary> packages = new HashSet<Binary>();

    public Preamble getPreamble() {
        return preamble;
    }

    public void setPreamble(Preamble preamble) {
        this.preamble = preamble;
    }

    public Set<Binary> getPackages() {
        return packages;
    }

    public void setPackages(Set<Binary> packages) {
        this.packages = packages;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CUDFDescriptor that = (CUDFDescriptor) o;

        if (!packages.equals(that.packages)) return false;
        if (preamble != null ? !preamble.equals(that.preamble) : that.preamble != null) return false;
        if (request != null ? !request.equals(that.request) : that.request != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = preamble != null ? preamble.hashCode() : 0;
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + packages.hashCode();
        return result;
    }
}
