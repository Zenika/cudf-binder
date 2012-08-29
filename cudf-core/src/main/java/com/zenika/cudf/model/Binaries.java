package com.zenika.cudf.model;

import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public interface Binaries {

    Binary getBinaryById(BinaryId binaryId);

    Set<Binary> getAllBinaries();

    void addBinary(Binary binary);

    void setBinaries(Set<Binary> binaries);

}
