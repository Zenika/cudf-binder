package com.zenika.cudf.adapter.cache;

import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class CachedBinaries implements Binaries {

    private static final String BINARY_ID_KEY_LIST = "BINARY_KEYS";

    private final Cache cache;

    public CachedBinaries(Cache cache) {
        this.cache = cache;
        this.cache.put(BINARY_ID_KEY_LIST, new HashSet<BinaryId>());
    }

    @Override
    public Binary getBinaryById(BinaryId binaryId) {
        return (Binary) cache.get(binaryId);
    }

    //TODO: This method may thrown an OutOfMemory exception about heap space.
    @Override
    public Set<Binary> getAllBinaries() {
        Set<BinaryId> binaryIds = (Set<BinaryId>) cache.get(BINARY_ID_KEY_LIST);
        Set<Binary> binaries = new HashSet<Binary>();
        for (BinaryId binaryId : binaryIds) {
            binaries.add((Binary) cache.get(binaryId));
        }
        return binaries;
    }

    @Override
    public void addBinary(Binary binary) {
        cache.put(binary.getBinaryId(), binary);
        Set<BinaryId> binaryIds = (Set<BinaryId>) cache.get(BINARY_ID_KEY_LIST);
        binaryIds.add(binary.getBinaryId());
    }

    @Override
    public void setBinaries(Set<Binary> binaries) {
        for (Binary binary : binaries) {
            addBinary(binary);
        }
    }
}
