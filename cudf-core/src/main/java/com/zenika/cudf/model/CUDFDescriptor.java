package com.zenika.cudf.model;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class CUDFDescriptor {

    private Preamble preamble;
    private Request request;
    private Binaries binaries;

    public Preamble getPreamble() {
        return preamble;
    }

    public void setPreamble(Preamble preamble) {
        this.preamble = preamble;
    }

    public Binaries getBinaries() {
        return binaries;
    }

    public void setBinaries(Binaries binaries) {
        this.binaries = binaries;
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

        if (binaries != null ? !binaries.equals(that.binaries) : that.binaries != null) return false;
        if (preamble != null ? !preamble.equals(that.preamble) : that.preamble != null) return false;
        if (request != null ? !request.equals(that.request) : that.request != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = preamble != null ? preamble.hashCode() : 0;
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + (binaries != null ? binaries.hashCode() : 0);
        return result;
    }
}
