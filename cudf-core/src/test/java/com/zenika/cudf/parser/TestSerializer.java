package com.zenika.cudf.parser;

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

import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.parser.mock.MockSerializer;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestSerializer extends AbstractTestParser {

    @Test
    public void testSerializer() throws Exception {
        MockSerializer serializer = new MockSerializer();

        CUDFDescriptor descriptor = createDescriptor();
        serializer.serialize(descriptor);

        CUDFParsedDescriptor expectedParsedDescriptor = serializer.getParsedDescriptor();
        CUDFParsedDescriptor actualParsedDescriptor = createParsedDescriptor();

        assertNotNull(expectedParsedDescriptor);

        assertParsedPreamble(expectedParsedDescriptor.getPreamble(), actualParsedDescriptor.getPreamble());
        assertParsedBinaries(expectedParsedDescriptor.getPackages(), actualParsedDescriptor.getPackages());
        assertParsedRequest(expectedParsedDescriptor.getRequest(), actualParsedDescriptor.getRequest());
    }

    private void assertParsedPreamble(ParsedPreamble actualParsedPreamble, ParsedPreamble expectedParsedPreamble) {
        assertEquals(expectedParsedPreamble.getProperties(), actualParsedPreamble.getProperties());
        assertEquals(expectedParsedPreamble.getReqChecksum(), actualParsedPreamble.getReqChecksum());
        assertEquals(expectedParsedPreamble.getStatusChecksum(), actualParsedPreamble.getStatusChecksum());
        assertEquals(expectedParsedPreamble.getUnivChecksum(), actualParsedPreamble.getUnivChecksum());
    }

    private void assertParsedBinaries(Set<ParsedBinary> expectedParsedBinaries, Set<ParsedBinary> actualParsedBinaries) {
        ParsedBinary expectedParsedBinary1 = findParsedBinaryByBinaryId(binaryId1, expectedParsedBinaries);
        ParsedBinary expectedParsedBinary2 = findParsedBinaryByBinaryId(binaryId2, expectedParsedBinaries);
        ParsedBinary expectedParsedBinary3 = findParsedBinaryByBinaryId(binaryId3, expectedParsedBinaries);

        ParsedBinary actualParsedBinary1 = findParsedBinaryByBinaryId(binaryId1, actualParsedBinaries);
        ParsedBinary actualParsedBinary2 = findParsedBinaryByBinaryId(binaryId2, actualParsedBinaries);
        ParsedBinary actualParsedBinary3 = findParsedBinaryByBinaryId(binaryId3, actualParsedBinaries);

        assertParsedBinary(expectedParsedBinary1, actualParsedBinary1);
        assertParsedBinary(expectedParsedBinary2, actualParsedBinary2);
        assertParsedBinary(expectedParsedBinary3, actualParsedBinary3);
    }

    private void assertParsedBinary(ParsedBinary expectedParsedBinary, ParsedBinary actualParsedBinary) {
        assertNotNull(actualParsedBinary);
        assertEquals(expectedParsedBinary.getRevision(), actualParsedBinary.getRevision());
        assertEquals(expectedParsedBinary.getType(), actualParsedBinary.getType());
        assertEquals(expectedParsedBinary.isInstalled(), actualParsedBinary.isInstalled());
        assertEquals(expectedParsedBinary.getDependencies(), actualParsedBinary.getDependencies());
    }

    private void assertParsedRequest(ParsedRequest actualParsedRequest, ParsedRequest expectedParsedRequest) {
        assertEquals(expectedParsedRequest.getInstall(), actualParsedRequest.getInstall());
        assertEquals(expectedParsedRequest.getRemove(), actualParsedRequest.getRemove());
        assertEquals(expectedParsedRequest.getUpdate(), actualParsedRequest.getUpdate());
    }
}
