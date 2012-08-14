package com.zenika.cudf.parser.mock;

import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.parser.AbstractDeserializer;
import com.zenika.cudf.parser.ParsingException;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class MockDeserializer extends AbstractDeserializer {

    @Override
    protected CUDFParsedDescriptor parseCudf() throws ParsingException {
        CUDFParsedDescriptor parsedDescriptor = new CUDFParsedDescriptor();

        BinaryId binaryId1 = new BinaryId("jar1", "zenika", 1);
        BinaryId binaryId2 = new BinaryId("jar2", "zenika", 1);
        BinaryId binaryId3 = new BinaryId("jar3", "zenika", 2);

        ParsedPreamble parsedPreamble = createParsedPreamble();
        Set<ParsedBinary> parsedBinaries = createParsedBinaries(binaryId1, binaryId2, binaryId3);
        ParsedRequest request = createParsedRequest(binaryId1);

        parsedDescriptor.setPreamble(parsedPreamble);
        parsedDescriptor.setPackages(parsedBinaries);
        parsedDescriptor.setRequest(request);

        return parsedDescriptor;
    }

    private ParsedPreamble createParsedPreamble() {
        ParsedPreamble parsedPreamble = new ParsedPreamble();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");
        parsedPreamble.setProperties(properties);
        parsedPreamble.setReqChecksum("req");
        parsedPreamble.setStatusChecksum("status");
        parsedPreamble.setUnivChecksum("univ");
        return parsedPreamble;
    }

    private Set<ParsedBinary> createParsedBinaries(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3) {
        Set<ParsedBinary> parsedBinaries = new HashSet<ParsedBinary>();
        ParsedBinary binary1 = createParsedBinary(binaryId1, "1.0", "jar");
        binary1.getDependencies().add(binaryId2);
        binary1.getDependencies().add(binaryId3);
        parsedBinaries.add(binary1);

        ParsedBinary binary2 = createParsedBinary(binaryId2, "1.0.0", "jar");
        parsedBinaries.add(binary2);

        ParsedBinary binary3 = createParsedBinary(binaryId3, "1.2-SNAPSHOT", "jar");
        parsedBinaries.add(binary3);
        return parsedBinaries;
    }

    private ParsedBinary createParsedBinary(BinaryId binaryId1, String revision, String type) {
        ParsedBinary binary = new ParsedBinary();
        binary.setBinaryId(binaryId1);
        binary.setRevision(revision);
        binary.setType(type);
        return binary;
    }

    private ParsedRequest createParsedRequest(BinaryId binaryId1) {
        ParsedRequest request = new ParsedRequest();
        request.getInstall().add(binaryId1);
        return request;
    }
}
