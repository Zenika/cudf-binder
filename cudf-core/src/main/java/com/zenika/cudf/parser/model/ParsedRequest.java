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
public class ParsedRequest {

    public static final String INSTALL_START_LINE = "install: ";
    public static final String UPDATE_START_LINE = "update: ";
    public static final String REMOVE_START_LINE = "remove: ";

    private Set<BinaryId> install = new HashSet<BinaryId>();
    private Set<BinaryId> update = new HashSet<BinaryId>();
    private Set<BinaryId> remove = new HashSet<BinaryId>();

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
