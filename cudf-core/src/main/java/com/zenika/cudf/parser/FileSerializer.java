package com.zenika.cudf.parser;

import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class FileSerializer extends AbstractSerializer {

    private static final Logger LOG = Logger.getLogger(FileSerializer.class.getName());

    private File file;

    public FileSerializer(File file) {
        this.file = file;
    }

    @Override
    protected void parseCUDF(CUDFParsedDescriptor parsedDescriptor) throws ParsingException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            ParsedPreamble preamble = parsedDescriptor.getPreamble();
            writeParsedPreamble(writer, preamble);

            Set<ParsedBinary> parsedBinaries = parsedDescriptor.getPackages();
            writeParsedBinaries(writer, parsedBinaries);

            ParsedRequest request = parsedDescriptor.getRequest();
            writeParsedRequest(writer, request);
        } catch (IOException e) {
            throw new ParsingException("An error was occur when trying to write the file", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Unable to close file at :" + file.getAbsolutePath(), e);
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
            writer.append(CUDFParsedDescriptor.PACKAGE_START_LINE).append(binaryId.getOrganisation())
                    .append(ParsedBinary.SEPARATOR).append(binaryId.getName()).append("\n");
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
        }
    }

    private void writeParsedRequest(BufferedWriter writer, ParsedRequest request) throws IOException {
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
        writer.flush();
    }

    private void writeParsedBinaryId(BufferedWriter writer, Set<BinaryId> binaryIds) throws IOException {
        Iterator<BinaryId> iterator1 = binaryIds.iterator();
        while (iterator1.hasNext()) {
            BinaryId dependency = iterator1.next();
            writer.append(dependency.getOrganisation()).append(ParsedBinary.SEPARATOR).append(dependency.getName())
                    .append(" = ").append(String.valueOf(dependency.getVersion()));
            if (iterator1.hasNext()) {
                writer.append(", ");
            } else {
                writer.newLine();
            }
        }
    }
}
