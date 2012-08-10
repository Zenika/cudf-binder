package com.zenika.cudf.parser.model;

import com.zenika.cudf.model.BinaryId;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ParsedRequest {

    private Set<BinaryId> install = new HashSet<BinaryId>();
    private Set<BinaryId> update  = new HashSet<BinaryId>();
    private Set<BinaryId> remove  = new HashSet<BinaryId>();

    public Set<BinaryId> getInstall() {
        return install;
    }

    public void setInstall(Set<BinaryId> install) {
        this.install = install;
    }

    public Set<BinaryId> getUpdate() {
        return update;
    }

    public void setUpdate(Set<BinaryId> update) {
        this.update = update;
    }

    public Set<BinaryId> getRemove() {
        return remove;
    }

    public void setRemove(Set<BinaryId> remove) {
        this.remove = remove;
    }
}
