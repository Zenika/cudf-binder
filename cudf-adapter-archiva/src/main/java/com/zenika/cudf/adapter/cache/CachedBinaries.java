package com.zenika.cudf.adapter.cache;

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

import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class CachedBinaries
        implements Binaries {

    public static final String BINARY_ID_KEY_LIST = "BINARY_KEYS";

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

    @Override
    public Iterator<Binary> iterator() {
        return getAllBinaries().iterator();
    }
}
