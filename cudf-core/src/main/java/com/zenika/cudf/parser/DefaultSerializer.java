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

import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
//TODO: Refactor exceptions handling
//TODO: Create CUDF validator
//TODO: Compute the preamble properties
public class DefaultSerializer extends AbstractSerializer {

    private static final Logger LOG = Logger.getLogger(FileSerializer.class.getName());

    private final Writer writer;
    private final Map<String, String> illegals = new HashMap<String, String>();

    private final BufferedWriter optionalWriter;

    public DefaultSerializer(Writer writer) {
        this(writer, null);
    }

    public DefaultSerializer(Writer writer, Writer optionalWriter) {
        this.writer = writer;
        if (optionalWriter != null) {
            this.optionalWriter = new BufferedWriter(optionalWriter);
        } else {
            this.optionalWriter = null;
        }
        initiateIllegalsCharatersForCUDF();
    }

    private void initiateIllegalsCharatersForCUDF() {
        illegals.put("_", Integer.toHexString('_'));
        illegals.put(":", Integer.toHexString(':'));
    }

    @Override
    protected void parseCUDF(CUDFParsedDescriptor parsedDescriptor) throws ParsingException {
        BufferedWriter bufferWriter = null;
        try {
            bufferWriter = new BufferedWriter(writer);
            ParsedPreamble preamble = parsedDescriptor.getPreamble();
            writeParsedPreamble(bufferWriter, preamble);

            Set<ParsedBinary> parsedBinaries = parsedDescriptor.getPackages();
            writeParsedBinaries(bufferWriter, parsedBinaries);

            ParsedRequest request = parsedDescriptor.getRequest();
            writeParsedRequest(bufferWriter, request);
        } catch (IOException e) {
            throw new ParsingException("An error was occur when trying to write the file", e);
        } finally {
            if (bufferWriter != null) {
                try {
                    bufferWriter.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Unable to close writer", e);
                }
            }
        }
    }

    private void writeParsedPreamble(BufferedWriter writer, ParsedPreamble preamble) throws IOException {
        writer.append(CUDFParsedDescriptor.PREAMBLE_START_LINE).append("\n");
        writer.append(ParsedPreamble.PROPERTY_START_LINE);
        writeParsedPreambleProperties(writer, preamble.getProperties());
        if (preamble.getReqChecksum() != null) {
            writer.append(ParsedPreamble.REQ_CHECKSUM_START_LINE).append(preamble.getReqChecksum()).append("\n");
        }
        if (preamble.getStatusChecksum() != null) {
            writer.append(ParsedPreamble.STATUS_CHECKSUM_START_LINE).append(preamble.getStatusChecksum()).append("\n");
        }
        if (preamble.getUnivChecksum() != null) {
            writer.append(ParsedPreamble.UNIV_CHECKSUM_START_LINE).append(preamble.getUnivChecksum()).append("\n");
        }
        writer.newLine();
        writer.flush();
    }

    private void writeParsedPreambleProperties(BufferedWriter writer, Map<String, String> properties)
            throws IOException {
        Iterator<Map.Entry<String, String>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> property = iterator.next();
            writer.append(property.getKey()).append(": ").append(property.getValue());
            if (iterator.hasNext()) {
                writer.append(", ");
            } else {
                writer.newLine();
            }
        }
    }

    private void writeParsedBinaries(BufferedWriter writer, Set<ParsedBinary> parsedBinaries) throws IOException {
        for (ParsedBinary parsedBinary : parsedBinaries) {
            BinaryId binaryId = parsedBinary.getBinaryId();
            writer.append(CUDFParsedDescriptor.PACKAGE_START_LINE).append(encodeOrganisationWithName(
                    binaryId.getOrganisation(), binaryId.getName())).append("\n");
            writer.append(ParsedBinary.VERSION_START_LINE).append(String.valueOf(binaryId.getVersion())).append("\n");
            writer.append(ParsedBinary.TYPE_START_LINE).append(parsedBinary.getType()).append("\n");
            writer.append(ParsedBinary.NUMBER_START_LINE).append(parsedBinary.getRevision()).append("\n");
            if (parsedBinary.isInstalled()) {
                writer.append(ParsedBinary.INSTALLED_START_LINE).append(String.valueOf(parsedBinary.isInstalled()))
                        .append("\n");
            }
            if (parsedBinary.getDependencies().size() > 0) {
                writer.append(ParsedBinary.DEPENDS_START_LINE);
                writeParsedBinaryId(writer, parsedBinary.getDependencies());
            }
            writer.newLine();
            writer.flush();

            if (optionalWriter != null) {
                optionalWriter.append(encodeOrganisationWithName(binaryId.getOrganisation(), binaryId.getName()))
                        .append(" ").append(parsedBinary.getRevision()).append(" => ")
                        .append(Integer.toString(binaryId.getVersion()));
                optionalWriter.newLine();
            }
        }
    }

    private void writeParsedRequest(BufferedWriter writer, ParsedRequest request) throws IOException {
        if (request != null) {
            writer.append(CUDFParsedDescriptor.REQUEST_START_LINE).append("\n");
            if (request.getInstall().size() > 0) {
                writer.append(ParsedRequest.INSTALL_START_LINE);
                writeParsedBinaryId(writer, request.getInstall());
            }
            if (request.getRemove().size() > 0) {
                writer.append(ParsedRequest.REMOVE_START_LINE);
                writeParsedBinaryId(writer, request.getRemove());
            }
            if (request.getUpdate().size() > 0) {
                writer.append(ParsedRequest.UPDATE_START_LINE);
                writeParsedBinaryId(writer, request.getUpdate());
            }
        }
        writer.flush();
    }

    private void writeParsedBinaryId(BufferedWriter writer, Set<BinaryId> binaryIds) throws IOException {
        Iterator<BinaryId> iterator1 = binaryIds.iterator();
        while (iterator1.hasNext()) {
            BinaryId dependency = iterator1.next();
            writer.append(encodeOrganisationWithName(dependency.getOrganisation(), dependency.getName()))
                    .append(" = ").append(String.valueOf(dependency.getVersion()));
            if (iterator1.hasNext()) {
                writer.append(", ");
            } else {
                writer.newLine();
            }
        }
    }

    private String encodeOrganisationWithName(String organisation, String name) {
        return encodingString(organisation + ParsedBinary.SEPARATOR + name);
    }

    private String encodingString(String input) {
        for (String illegal : illegals.keySet()) {
            input = input.replaceAll(illegal, '%' + illegals.get(illegal));
        }
        return input;
    }
}
