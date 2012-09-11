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

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public abstract class AbstractSerializer {

    public void serialize(CUDFDescriptor descriptor) throws ParsingException {
        CUDFParsedDescriptor parsedDescriptor = new CUDFParsedDescriptor();

        ParsedPreamble parsedPreamble = processPreamble(descriptor);
        Set<ParsedBinary> parsedBinaries = processBinaries(descriptor);
        ParsedRequest parsedRequest = processRequest(descriptor);

        parsedDescriptor.setPreamble(parsedPreamble);
        parsedDescriptor.setPackages(parsedBinaries);
        parsedDescriptor.setRequest(parsedRequest);

        parseCUDF(parsedDescriptor);
    }

    protected abstract void parseCUDF(CUDFParsedDescriptor parsedDescriptor) throws ParsingException;

    private ParsedPreamble processPreamble(CUDFDescriptor descriptor) {
        ParsedPreamble parsedPreamble = new ParsedPreamble();
        Preamble preamble = descriptor.getPreamble();
        parsedPreamble.setProperties(preamble.getProperties());
        parsedPreamble.setReqChecksum(preamble.getReqChecksum());
        parsedPreamble.setStatusChecksum(preamble.getStatusChecksum());
        parsedPreamble.setUnivChecksum(preamble.getUnivChecksum());
        return parsedPreamble;
    }

    private Set<ParsedBinary> processBinaries(CUDFDescriptor descriptor) {
        Set<ParsedBinary> parsedBinaries = new HashSet<ParsedBinary>();
        Set<Binary> binaries = descriptor.getBinaries().getAllBinaries();
        for (Binary binary : binaries) {
            ParsedBinary parsedBinary = new ParsedBinary();
            parsedBinary.setBinaryId(binary.getBinaryId());
            parsedBinary.setRevision(binary.getRevision());
            parsedBinary.setType(binary.getType());
            parsedBinary.setInstalled(binary.isInstalled());
            Set<BinaryId> binaryIds = new HashSet<BinaryId>();
            for (Binary dependency : binary.getDependencies()) {
                binaryIds.add(dependency.getBinaryId());
            }
            parsedBinary.setDependencies(binaryIds);
            parsedBinaries.add(parsedBinary);
        }
        return parsedBinaries;
    }

    private Set<BinaryId> processBinarySet(Set<Binary> binaries) {
        Set<BinaryId> binaryIds = new HashSet<BinaryId>();
        for (Binary binary : binaries) {
            binaryIds.add(binary.getBinaryId());
        }
        return binaryIds;
    }

    private ParsedRequest processRequest(CUDFDescriptor descriptor) {
        Request request = descriptor.getRequest();
        if (request == null) {
            return null;
        }
        ParsedRequest parsedRequest = new ParsedRequest();
        parsedRequest.setInstall(processBinarySet(request.getInstall()));
        parsedRequest.setRemove(processBinarySet(request.getRemove()));
        parsedRequest.setUpdate(processBinarySet(request.getUpdate()));
        return parsedRequest;
    }
}
