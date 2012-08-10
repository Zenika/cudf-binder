package com.zenika.cudf.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
}
