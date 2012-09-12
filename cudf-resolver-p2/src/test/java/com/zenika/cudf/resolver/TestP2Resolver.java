package com.zenika.cudf.resolver;/*
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
import com.zenika.cudf.model.CUDFDescriptor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zenika.cudf.resolver.TestP2ResolverUtils.BINARY_ID_1;
import static com.zenika.cudf.resolver.TestP2ResolverUtils.BINARY_ID_2;
import static com.zenika.cudf.resolver.TestP2ResolverUtils.BINARY_ID_3;
import static com.zenika.cudf.resolver.TestP2ResolverUtils.createDescriptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestP2Resolver {

    private VersionResolver versionResolver;
    private P2Resolver p2Resolver;

    @Before
    public void setUp() {
        versionResolver = new VersionResolverMock();
        p2Resolver = new P2Resolver(versionResolver);
    }

    @Test
    public void testResolver() {
        CUDFDescriptor descriptor = createDescriptor();
        CUDFDescriptor result = p2Resolver.resolve(descriptor);

        assertEquals(3, result.getBinaries().getAllBinaries().size());

        assertContainsBinaryId(expectedBinaryIds(BINARY_ID_1, BINARY_ID_2, BINARY_ID_3), in(getAllBinaryIds(result.getBinaries())));
    }

    private void assertContainsBinaryId(List<BinaryId> expectedBinaryIds, List<BinaryId> actualBinaryIds) {
        for (BinaryId expectedBinaryId : expectedBinaryIds) {
            assertTrue("The expected binary: " + expectedBinaryId + System.lineSeparator() + "is not in the actual binaries" + actualBinaryIds,
                    actualBinaryIds.contains(expectedBinaryId));
        }
    }

    private List<BinaryId> getAllBinaryIds(Binaries binaries) {
        List<BinaryId> binaryIds = new ArrayList<BinaryId>();
        for (Binary binary : binaries) {
            binaryIds.add(binary.getBinaryId());
        }
        return binaryIds;
    }

    private List<BinaryId> expectedBinaryIds(BinaryId... binaryIds) {
        return Arrays.asList(binaryIds);
    }

    private <T> T in(T t) {
        return t;
    }
}
