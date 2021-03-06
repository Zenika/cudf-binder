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
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.DefaultBinaries;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Adrien Lecharpentier <adrien.lecharpentier@zenika.com>
 */
public abstract class AbstractDeserializer {

    public CUDFDescriptor deserialize() throws ParsingException {
        CUDFParsedDescriptor parsedDescriptor = parseCudf();
        CUDFDescriptor cudfDescriptor = new CUDFDescriptor();

        Preamble preamble = processPreamble(parsedDescriptor);
        Binaries binaries = processParsedBinaries(parsedDescriptor);
        Request request = processParsedRequest(parsedDescriptor, binaries);

        cudfDescriptor.setPreamble(preamble);
        cudfDescriptor.setBinaries(binaries);
        cudfDescriptor.setRequest(request);

        return cudfDescriptor;
    }

    protected abstract CUDFParsedDescriptor parseCudf() throws ParsingException;

    private Preamble processPreamble(CUDFParsedDescriptor parsedDescriptor) {
        ParsedPreamble parsedPreamble = parsedDescriptor.getPreamble();
        Preamble preamble = new Preamble();
        preamble.setProperties(parsedPreamble.getProperties());
        preamble.setUnivChecksum(parsedPreamble.getUnivChecksum());
        preamble.setStatusChecksum(parsedPreamble.getStatusChecksum());
        preamble.setReqChecksum(parsedPreamble.getReqChecksum());
        return preamble;
    }

    private Binaries processParsedBinaries(CUDFParsedDescriptor parsedDescriptor) throws ParsingException {
        Set<ParsedBinary> parsedBinaries = parsedDescriptor.getPackages();
        Map<BinaryId, ParsedBinary> parsedRevisions = buildParsedRevisions(parsedBinaries);

        Binaries binaries = new DefaultBinaries();
        for (ParsedBinary parsedBinary : parsedBinaries) {
            Binary binary = new Binary(parsedBinary.getBinaryId());
            binary.setRevision(parsedBinary.getRevision());
            binary.setInstalled(parsedBinary.isInstalled());
            binary.setType(parsedBinary.getType());
            Set<Binary> dependencies = processParsedBinaryDependencies(parsedBinary, parsedRevisions);
            binary.setDependencies(dependencies);
            binaries.addBinary(binary);
        }
        return binaries;
    }

    private Map<BinaryId, ParsedBinary> buildParsedRevisions(Set<ParsedBinary> parsedBinaries) {
        HashMap<BinaryId, ParsedBinary> revisions = new HashMap<BinaryId, ParsedBinary>();
        for (ParsedBinary parsedBinary : parsedBinaries) {
            revisions.put(parsedBinary.getBinaryId(), parsedBinary);
        }
        return revisions;
    }

    private Set<Binary> processParsedBinaryDependencies(ParsedBinary parsedBinary, Map<BinaryId, ParsedBinary> parsedRevisions) throws ParsingException {
        Set<Binary> dependencies = new HashSet<Binary>();
        for (BinaryId binaryId : parsedBinary.getDependencies()) {
            validateDependency(binaryId);
            ParsedBinary parsedDependency = parsedRevisions.get(binaryId);
            if (parsedDependency == null) {
                parsedDependency = new ParsedBinary();
                parsedDependency.setBinaryId(binaryId);
            }
            Binary dependency = new Binary(parsedDependency.getBinaryId());
            dependency.setRevision(parsedDependency.getRevision());
            dependency.setInstalled(parsedDependency.isInstalled());
            dependency.setType(parsedDependency.getType());
            dependencies.add(dependency);
        }
        return dependencies;
    }

    private void validateDependency(BinaryId binaryId) throws ParsingException {
//        if (binaryId.getVersion()  <= 0) {
//            throw new ParsingException("The dependency binary id version of " + binaryId.getOrganisation() + ParsedBinary.SEPARATOR + binaryId.getName() + " must be either than 0");
//        }
    }

    private Request processParsedRequest(CUDFParsedDescriptor parsedDescriptor, Binaries binaries) {
        Request request = new Request();
        ParsedRequest parsedRequest = parsedDescriptor.getRequest();
        if (parsedRequest != null) {
            Map<BinaryId, Binary> revisions = buildRevisionMap(binaries.getAllBinaries());
            request.setInstall(processBinaryIdSet(revisions, parsedRequest.getInstall()));
            request.setUpdate(processBinaryIdSet(revisions, parsedRequest.getUpdate()));
            request.setRemove(processBinaryIdSet(revisions, parsedRequest.getRemove()));
        }
        return request;
    }

    private HashMap<BinaryId, Binary> buildRevisionMap(Set<Binary> binaries) {
        HashMap<BinaryId, Binary> revisions = new HashMap<BinaryId, Binary>();
        for (Binary binary : binaries) {
            revisions.put(binary.getBinaryId(), binary);
        }
        return revisions;
    }

    private Set<Binary> processBinaryIdSet(Map<BinaryId, Binary> revisions, Set<BinaryId> parsedInstall) {
        Set<Binary> binaries = new HashSet<Binary>();
        for (BinaryId binaryId : parsedInstall) {
            Binary binary = revisions.get(binaryId);
            binaries.add(binary);
        }
        return binaries;
    }
}
