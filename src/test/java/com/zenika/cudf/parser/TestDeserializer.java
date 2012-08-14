package com.zenika.cudf.parser;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import com.zenika.cudf.parser.mock.MockDeserializer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestDeserializer extends AbstractTestParser {

    @Test
    public void testDeserializer() throws Exception {
        AbstractDeserializer deserializer = new MockDeserializer();
        CUDFDescriptor descriptor = deserializer.deserialize();

        assertNotNull(descriptor);

        assertPreamble(descriptor);
        assertBinaries(descriptor);
        assertRequest(descriptor);
    }

    private void assertPreamble(CUDFDescriptor descriptor) {
        Preamble actualPreamble = descriptor.getPreamble();
        Preamble expectedPreamble = createExpectedPreamble();
        assertEquals(expectedPreamble, actualPreamble);
    }

    private void assertBinaries(CUDFDescriptor descriptor) {
        Binary binary1 = createExpectedBinary(binaryId1, "1.0", "jar");
        Binary binary2 = createExpectedBinary(binaryId2, "1.0.0", "jar");
        Binary binary3 = createExpectedBinary(binaryId3, "1.2-SNAPSHOT", "jar");
        binary1.getDependencies().add(binary2);
        binary1.getDependencies().add(binary3);

        Binary actualBinary1 = findBinaryByBinaryId(binaryId1, descriptor.getPackages());

        assertNotNull(actualBinary1);
        assertTrue(actualBinary1.getDependencies().contains(binary2));
        assertTrue(actualBinary1.getDependencies().contains(binary3));
        assertTrue(descriptor.getPackages().contains(binary2));
        assertTrue(descriptor.getPackages().contains(binary3));
    }

    private void assertRequest(CUDFDescriptor descriptor) {
        Request actualRequest = descriptor.getRequest();
        Request expectedRequest = createExpectedRequest(findBinaryByBinaryId(binaryId1, descriptor.getPackages()));
        assertEquals(expectedRequest, actualRequest);
    }

    private Preamble createExpectedPreamble() {
        Preamble expectedPreamble = new Preamble();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");
        expectedPreamble.setProperties(properties);
        expectedPreamble.setReqChecksum("req");
        expectedPreamble.setStatusChecksum("status");
        expectedPreamble.setUnivChecksum("univ");
        return expectedPreamble;
    }

    private Binary createExpectedBinary(BinaryId binaryId, String revision, String type) {
        Binary binary = new Binary(binaryId);
        binary.setRevision(revision);
        binary.setType(type);
        return binary;
    }

    private Request createExpectedRequest(Binary binary1) {
        Request expectedRequest = new Request();
        expectedRequest.getInstall().add(binary1);
        return expectedRequest;
    }
}
