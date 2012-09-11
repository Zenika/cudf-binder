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

import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import com.zenika.cudf.parser.mock.MockDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestDeserializer extends AbstractTestParser {

    @Test
    public void testDeserializer() throws Exception {
        AbstractDeserializer deserializer = new MockDeserializer(createParsedDescriptor());
        CUDFDescriptor actualDescriptor = deserializer.deserialize();
        CUDFDescriptor expectedDescriptor = createDescriptor();

        assertNotNull(actualDescriptor);

        assertPreamble(expectedDescriptor.getPreamble(), actualDescriptor.getPreamble());
        assertBinaries(expectedDescriptor.getBinaries(), actualDescriptor.getBinaries());
        assertRequest(expectedDescriptor.getRequest(), actualDescriptor.getRequest());
    }

    private void assertPreamble(Preamble expectedPreamble, Preamble actualPreamble) {
        assertEquals(expectedPreamble.getProperties(), actualPreamble.getProperties());
        assertEquals(expectedPreamble.getReqChecksum(), actualPreamble.getReqChecksum());
        assertEquals(expectedPreamble.getStatusChecksum(), actualPreamble.getStatusChecksum());
        assertEquals(expectedPreamble.getUnivChecksum(), actualPreamble.getUnivChecksum());
    }

    private void assertBinaries(Binaries expectedBinaries, Binaries actualBinaries) {
        Binary actualBinary1 = actualBinaries.getBinaryById(binaryId1);
        Binary actualBinary2 = actualBinaries.getBinaryById(binaryId2);
        Binary actualBinary3 = actualBinaries.getBinaryById(binaryId3);

        Binary expectedBinary1 = expectedBinaries.getBinaryById(binaryId1);
        Binary expectedBinary2 = expectedBinaries.getBinaryById(binaryId2);
        Binary expectedBinary3 = expectedBinaries.getBinaryById(binaryId3);

        assertBinary(expectedBinary1, actualBinary1);
        assertBinary(expectedBinary2, actualBinary2);
        assertBinary(expectedBinary3, actualBinary3);
    }

    private void assertBinary(Binary expectedBinary, Binary actualBinary) {
        assertEquals(expectedBinary.getBinaryId(), actualBinary.getBinaryId());
        assertEquals(expectedBinary.getRevision(), actualBinary.getRevision());
        assertEquals(expectedBinary.getType(), actualBinary.getType());
        assertEquals(expectedBinary.isInstalled(), actualBinary.isInstalled());
        assertEquals(expectedBinary.getDependencies(), actualBinary.getDependencies());
    }

    private void assertRequest(Request expectedRequest, Request actualRequest) {
        assertEquals(expectedRequest.getInstall(), actualRequest.getInstall());
        assertEquals(expectedRequest.getRemove(), actualRequest.getRemove());
        assertEquals(expectedRequest.getUpdate(), actualRequest.getUpdate());
    }
}
