package com.zenika.cudf.parser;

import com.zenika.cudf.model.BinaryId;
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

        ParsedPreamble parsedPreamble = createPreamble();
        parsedDescriptor.setPreamble(parsedPreamble);

        BinaryId binaryId1 = new BinaryId("jar1", "zenika", 1);
        BinaryId binaryId2 = new BinaryId("jar2", "zenika", 1);
        BinaryId binaryId3 = new BinaryId("jar3", "zenika", 2);

        Set<ParsedBinary> parsedBinaries = createPackages(binaryId1, binaryId2, binaryId3);
        parsedDescriptor.setPackages(parsedBinaries);

        ParsedRequest request = createRequest(binaryId1);
        parsedDescriptor.setRequest(request);

        return parsedDescriptor;
    }

    private ParsedPreamble createPreamble() {
        ParsedPreamble parsedPreamble = new ParsedPreamble();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");
        parsedPreamble.setProperties(properties);
        parsedPreamble.setReqChecksum("req");
        parsedPreamble.setStatusChecksum("status");
        parsedPreamble.setUnivChecksum("univ");
        return parsedPreamble;
    }

    private Set<ParsedBinary> createPackages(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3) {
        Set<ParsedBinary> parsedBinaries = new HashSet<ParsedBinary>();
        ParsedBinary binary1 = createPackage(binaryId1, "1.0", "jar");
        binary1.getDependencies().add(binaryId2);
        binary1.getDependencies().add(binaryId3);
        parsedBinaries.add(binary1);

        ParsedBinary binary2 = createPackage(binaryId2, "1.0.0", "jar");
        parsedBinaries.add(binary2);

        ParsedBinary binary3 = createPackage(binaryId3, "1.2-SNAPSHOT", "jar");
        parsedBinaries.add(binary3);
        return parsedBinaries;
    }

    private ParsedBinary createPackage(BinaryId binaryId1, String revision, String type) {
        ParsedBinary binary = new ParsedBinary();
        binary.setBinaryId(binaryId1);
        binary.setRevision(revision);
        binary.setType(type);
        return binary;
    }

    private ParsedRequest createRequest(BinaryId binaryId1) {
        ParsedRequest request = new ParsedRequest();
        Set<BinaryId> install = new HashSet<BinaryId>();
        install.add(binaryId1);
        request.setInstall(install);
        return request;
    }
}
