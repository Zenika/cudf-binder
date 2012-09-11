package com.zenika.cudf.adapter;

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

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.resolver.VersionResolver;

import static com.zenika.cudf.adapter.TestP2DescriptorUtils.DEFAULT_ORGANISATION;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class VersionResolverMock implements VersionResolver {

    private int numberOfResolveToCUDFCall = 0;
    private int numberOfResolveFromCUDFCall = 0;

    @Override
    public Binary resolveToCUDF(Binary binary) {
        numberOfResolveToCUDFCall++;
        if (binary.getBinaryId().getOrganisation().equals(DEFAULT_ORGANISATION) && binary.getBinaryId().getName().equals("jar1")) {
            return createBinary(binary, 1);
        } else if (binary.getBinaryId().getOrganisation().equals(DEFAULT_ORGANISATION) && binary.getBinaryId().getName().equals("jar2")) {
            return createBinary(binary, 1);
        } else if (binary.getBinaryId().getOrganisation().equals(DEFAULT_ORGANISATION) && binary.getBinaryId().getName().equals("jar3")) {
            return createBinary(binary, 2);
        }
        throw new IllegalArgumentException("Unknown CUDF version for: " + binary);
    }

    private Binary createBinary(Binary binary, int version) {
        BinaryId binaryId = new BinaryId(binary.getBinaryId().getName(), binary.getBinaryId().getOrganisation(), version);
        Binary newBinary = new Binary(binaryId);
        newBinary.setInstalled(binary.isInstalled());
        newBinary.setType(binary.getType());
        newBinary.setRevision(binary.getRevision());
        newBinary.setDependencies(binary.getDependencies());
        return newBinary;
    }

    @Override
    public Binary resolveFromCUDF(Binary binary) {
        numberOfResolveFromCUDFCall++;
        if (binary.getBinaryId().getOrganisation().equals(DEFAULT_ORGANISATION) && binary.getBinaryId().getName().equals("jar1")) {
            return createBinary(binary, "1.0");
        } else if (binary.getBinaryId().getOrganisation().equals(DEFAULT_ORGANISATION) && binary.getBinaryId().getName().equals("jar2")) {
            return createBinary(binary, "1.0.0");
        } else if (binary.getBinaryId().getOrganisation().equals(DEFAULT_ORGANISATION) && binary.getBinaryId().getName().equals("jar3")) {
            return createBinary(binary, "1.2-SNAPSHOT");
        }
        throw new IllegalArgumentException("Unknown revision for: " + binary);
    }

    private Binary createBinary(Binary binary, String revision) {
        binary.setRevision(revision);
        return binary;
    }

    public int getNumberOfResolveToCUDFCall() {
        return numberOfResolveToCUDFCall;
    }

    public int getNumberOfResolveFromCUDFCall() {
        return numberOfResolveFromCUDFCall;
    }
}
