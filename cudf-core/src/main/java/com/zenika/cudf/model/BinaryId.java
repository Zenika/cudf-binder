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

import java.io.Serializable;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class BinaryId implements Serializable {

    private final String name;
    private final String organisation;
    private final int version;

    public BinaryId(String name, String organisation, int version) {
        check(name, "The name must be not null");
        check(organisation, "The organisation must be not null");
        this.name = name;
        this.organisation = organisation;
        this.version = version;
    }

    private void check(String string, String message) {
        if (string == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public String getName() {
        return name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BinaryId binaryId = (BinaryId) o;

        if (version != binaryId.version) {
            return false;
        }
        if (!name.equals(binaryId.name)) {
            return false;
        }
        if (!organisation.equals(binaryId.organisation)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + organisation.hashCode();
        result = 31 * result + version;
        return result;
    }
}
