package com.zenika.cudf.model;

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
