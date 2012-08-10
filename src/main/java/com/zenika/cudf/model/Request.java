package com.zenika.cudf.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class Request {

    private Set<Binary> install = new HashSet<Binary>();
    private Set<Binary> update = new HashSet<Binary>();
    private Set<Binary> remove = new HashSet<Binary>();

    public Set<Binary> getInstall() {
        return install;
    }

    public void setInstall(Set<Binary> install) {
        this.install = install;
    }

    public Set<Binary> getUpdate() {
        return update;
    }

    public void setUpdate(Set<Binary> update) {
        this.update = update;
    }

    public Set<Binary> getRemove() {
        return remove;
    }

    public void setRemove(Set<Binary> remove) {
        this.remove = remove;
    }
}
