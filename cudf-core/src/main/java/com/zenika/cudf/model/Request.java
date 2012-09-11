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
