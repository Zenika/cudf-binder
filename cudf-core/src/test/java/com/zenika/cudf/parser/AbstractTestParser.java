package com.zenika.cudf.parser;

import com.zenika.cudf.model.*;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public abstract class AbstractTestParser {

    protected BinaryId binaryId1 = new BinaryId("jar1", "zenika", 1);
    protected BinaryId binaryId2 = new BinaryId("jar2", "zenika", 1);
    protected BinaryId binaryId3 = new BinaryId("jar3", "zenika", 2);

    protected Binary findBinaryByBinaryId(BinaryId binaryId, Set<Binary> binaries) {
        for (Binary binary : binaries) {
            if (binary.getBinaryId().equals(binaryId)) {
                return binary;
            }
        }
        return null;
    }

    protected ParsedBinary findParsedBinaryByBinaryId(BinaryId binaryId, Set<ParsedBinary> parsedBinaries) {
        for (ParsedBinary parsedBinary : parsedBinaries) {
            if (parsedBinary.getBinaryId().equals(binaryId)) {
                return parsedBinary;
            }
        }
        return null;
    }

    protected CUDFDescriptor createDescriptor() {
        CUDFDescriptor descriptor = new CUDFDescriptor();

        Preamble preamble = createPreamble();
        Binaries binaries = createBinaries(binaryId1, binaryId2, binaryId3);
        Request request = createRequest(binaries.getBinaryById(binaryId1));

        descriptor.setPreamble(preamble);
        descriptor.setBinaries(binaries);
        descriptor.setRequest(request);

        return descriptor;
    }

    private Preamble createPreamble() {
        Preamble preamble = new Preamble();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");
        preamble.setProperties(properties);
        preamble.setReqChecksum("req");
        preamble.setStatusChecksum("status");
        preamble.setUnivChecksum("univ");
        return preamble;
    }

    private Binaries createBinaries(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3) {
        Binaries binaries = new DefaultBinaries();
        Binary binary1 = createBinary(binaryId1, "1.0", "jar", false);
        Binary binary2 = createBinary(binaryId2, "1.0.0", "jar", false);
        Binary binary3 = createBinary(binaryId3, "1.2-SNAPSHOT", "jar", true);

        binary1.getDependencies().add(binary2);
        binary1.getDependencies().add(binary3);

        binaries.addBinary(binary1);
        binaries.addBinary(binary2);
        binaries.addBinary(binary3);
        return binaries;
    }

    private Binary createBinary(BinaryId binaryId, String revision, String type, boolean installed) {
        Binary binary = new Binary(binaryId);
        binary.setInstalled(installed);
        binary.setRevision(revision);
        binary.setType(type);
        return binary;
    }

    private Request createRequest(Binary binary1) {
        Request request = new Request();
        request.getInstall().add(binary1);
        return request;
    }

    protected CUDFParsedDescriptor createParsedDescriptor() {
        CUDFParsedDescriptor parsedDescriptor = new CUDFParsedDescriptor();

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
        Set<ParsedBinary> parsedBinaries = new LinkedHashSet<ParsedBinary>();
        ParsedBinary binary1 = createParsedBinary(binaryId1, "1.0", "jar", false);
        binary1.getDependencies().add(binaryId2);
        binary1.getDependencies().add(binaryId3);
        parsedBinaries.add(binary1);

        ParsedBinary binary2 = createParsedBinary(binaryId2, "1.0.0", "jar", false);
        parsedBinaries.add(binary2);

        ParsedBinary binary3 = createParsedBinary(binaryId3, "1.2-SNAPSHOT", "jar", true);
        parsedBinaries.add(binary3);
        return parsedBinaries;
    }

    private ParsedBinary createParsedBinary(BinaryId binaryId1, String revision, String type, boolean installed) {
        ParsedBinary binary = new ParsedBinary();
        binary.setBinaryId(binaryId1);
        binary.setRevision(revision);
        binary.setType(type);
        binary.setInstalled(installed);
        return binary;
    }

    private ParsedRequest createParsedRequest(BinaryId binaryId1) {
        ParsedRequest request = new ParsedRequest();
        request.getInstall().add(binaryId1);
        return request;
    }
}
