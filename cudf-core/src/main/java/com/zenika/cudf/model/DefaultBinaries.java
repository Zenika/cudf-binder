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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class DefaultBinaries implements Binaries {

    private Map<BinaryId, Binary> binaries = new HashMap<BinaryId, Binary>();

    public DefaultBinaries() {
    }

    public DefaultBinaries(Set<Binary> binaries) {
        setBinaries(binaries);
    }

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

    @Override
    public Iterator<Binary> iterator() {
        return binaries.values().iterator();
    }
}
