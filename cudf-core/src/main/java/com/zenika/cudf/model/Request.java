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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (!install.equals(request.install)) return false;
        if (!remove.equals(request.remove)) return false;
        if (!update.equals(request.update)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = install.hashCode();
        result = 31 * result + update.hashCode();
        result = 31 * result + remove.hashCode();
        return result;
    }
}
