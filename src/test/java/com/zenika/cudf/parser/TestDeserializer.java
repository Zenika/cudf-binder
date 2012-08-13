package com.zenika.cudf.parser;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestDeserializer {

    @Test
    public void testDeserializer() throws Exception {
        AbstractDeserializer deserializer = new MockDeserializer();
        CUDFDescriptor descriptor = deserializer.deserialize();
        Preamble actualPreamble = descriptor.getPreamble();
        Preamble expectedPreamble = createExpectedPreamble();
        assertEquals(expectedPreamble, actualPreamble);

        BinaryId binaryId1 = new BinaryId("jar1", "zenika", 1);
        BinaryId binaryId2 = new BinaryId("jar2", "zenika", 1);
        BinaryId binaryId3 = new BinaryId("jar3", "zenika", 2);

        Binary binary1 = createExpectedBinary(binaryId1, "1.0", "jar");
        Binary binary2 = createExpectedBinary(binaryId2, "1.0.0", "jar");
        Binary binary3 = createExpectedBinary(binaryId3, "1.2-SNAPSHOT", "jar");
        binary1.getDependencies().add(binary2);
        binary1.getDependencies().add(binary3);

        Set<Binary> binaries = descriptor.getPackages();
        Binary actualBinary1 = findBinaryByBinaryId(binaryId1, binaries);
        assertNotNull(actualBinary1);
        assertTrue(actualBinary1.getDependencies().contains(binary2));
        assertTrue(actualBinary1.getDependencies().contains(binary3));

        assertTrue(binaries.contains(binary2));
        assertTrue(binaries.contains(binary3));

        Request actualRequest = descriptor.getRequest();
        Request expectedRequest = createExpectedRequest(binary1);

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

    private Binary findBinaryByBinaryId(BinaryId binaryId1, Set<Binary> binaries) {
        Binary actualBinary1 = null;
        for (Binary binary : binaries) {
            if (binary.getBinaryId().equals(binaryId1)) {
                actualBinary1 = binary;
            }
        }
        return actualBinary1;
    }

    private Request createExpectedRequest(Binary binary1) {
        Request expectedRequest = new Request();
        expectedRequest.getInstall().add(binary1);
        return expectedRequest;
    }
}
