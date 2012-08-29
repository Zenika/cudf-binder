package com.zenika.cudf.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class DefaultBinaries implements Binaries {

    private Map<BinaryId, Binary> binaries = new HashMap<BinaryId, Binary>();

    @Override
    public Binary getBinaryById(BinaryId binaryId) {
        return binaries.get(binaryId);
    }

    @Override
    public Set<Binary> getAllBinaries() {
        return new HashSet<Binary>(binaries.values());
    }

    @Override
    public void addBinary(Binary binary) {
        binaries.put(binary.getBinaryId(), binary);
    }

    @Override
    public void setBinaries(Set<Binary> binaries) {
        for (Binary binary : binaries) {
            this.binaries.put(binary.getBinaryId(), binary);
        }
    }
}
