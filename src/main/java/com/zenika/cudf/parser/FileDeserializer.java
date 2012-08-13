package com.zenika.cudf.parser;

import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class FileDeserializer extends AbstractDeserializer {

    private static final Logger LOG = Logger.getLogger(FileDeserializer.class.getName());

    private static final String PREAMBLE_START_LINE = "preamble:";
    private static final String UNIV_CHECKSUM_START_LINE = "univ-checksum: ";
    private static final String STATUS_CHECKSUM_START_LINE = "status-checksum: ";
    private static final String REQ_CHECKSUM_START_LINE = "req-checksum: ";

    private static final String PROPERTY_START_LINE = "property: ";
    private static final String PACKAGE_START_LINE = "package: ";
    private static final String NUMBER_START_LINE = "number: ";
    private static final String TYPE_START_LINE = "type: ";
    private static final String VERSION_START_LINE = "version: ";
    private static final String INSTALLED_START_LINE = "installed: ";
    private static final String DEPENDS_START_LINE = "depends: ";

    private static final String REQUEST_START_LINE = "request:";
    private static final String INSTALL_START_LINE = "install: ";
    private static final String UPDATE_START_LINE = "update: ";
    private static final String REMOVE_START_LINE = "remove: ";

    private static final String SEPARATOR = "%3a";

    private final File file;

    public FileDeserializer(File file) {
        this.file = file;
    }

    public CUDFParsedDescriptor parseCudf() throws ParsingException {
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(file));
            String currentLine;

            ParsedPreamble preamble = null;
            Set<ParsedBinary> packages = new HashSet<ParsedBinary>();
            ParsedRequest request = null;

            while ((currentLine = buffer.readLine()) != null) {
                if (currentLine.startsWith(PREAMBLE_START_LINE)) {
                    preamble = parsePreamble(buffer);
                } else if (currentLine.startsWith(PACKAGE_START_LINE)) {
                    packages.add(parsePackage(currentLine, buffer));
                } else if (currentLine.startsWith(REQUEST_START_LINE)) {
                    request = parseRequest(buffer);
                }
            }
            CUDFParsedDescriptor descriptor = new CUDFParsedDescriptor();
            descriptor.setPreamble(preamble);
            descriptor.setPackages(packages);
            descriptor.setRequest(request);
            return descriptor;
        } catch (FileNotFoundException e) {
            throw new ParsingException("Unable to find file to parsing", e);
        } catch (IOException e) {
            throw new ParsingException("An error was occur when trying to read the file", e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Unable to close reader buffer", e);
                }
            }
        }
    }

    private ParsedPreamble parsePreamble(BufferedReader buffer) throws IOException {
        ParsedPreamble preamble = new ParsedPreamble();
        String currentLine;
        while (!(currentLine = buffer.readLine()).equals("")) {
            if (currentLine.startsWith(PROPERTY_START_LINE)) {
                preamble.setProperties(parsePreambleProperties(buffer, currentLine));
            } else if (currentLine.startsWith(UNIV_CHECKSUM_START_LINE)) {
                preamble.setUnivChecksum(currentLine.substring(UNIV_CHECKSUM_START_LINE.length()).trim());
            } else if (currentLine.startsWith(STATUS_CHECKSUM_START_LINE)) {
                preamble.setStatusChecksum(currentLine.substring(STATUS_CHECKSUM_START_LINE.length()).trim());
            } else if (currentLine.startsWith(REQ_CHECKSUM_START_LINE)) {
                preamble.setReqChecksum(currentLine.substring(REQ_CHECKSUM_START_LINE.length()).trim());
            } else {
                throw new RuntimeException("Unknown property " + currentLine + " in preamble");
            }
        }
        return preamble;
    }

    private Map<String, String> parsePreambleProperties(BufferedReader buffer, String currentLine) throws IOException {
        currentLine = currentLine.substring(PROPERTY_START_LINE.length()).trim();
        Map<String, String> properties = new HashMap<String, String>();
        boolean endOfLogicalLine;
        do {
            for (String property : currentLine.split(",")) {
                String[] propertyTab = property.trim().split(":");
                properties.put(propertyTab[0], propertyTab[1].trim());
            }
            endOfLogicalLine = isEndOfLogicalLine(currentLine);
            if (!endOfLogicalLine) {
                currentLine = buffer.readLine();
            }
        } while (!endOfLogicalLine);
        return properties;
    }

    private boolean isEndOfLogicalLine(String currentLine) {
        return !currentLine.endsWith(",");
    }

    private ParsedBinary parsePackage(String currentLine, BufferedReader buffer) throws IOException {
        ParsedBinary binary = new ParsedBinary();
        String packageLine = currentLine.substring(PACKAGE_START_LINE.length()).trim();
        String[] packageTab = packageLine.split(SEPARATOR);
        String organisation = packageTab[0];
        String name = packageTab[1];
        while (!(currentLine = buffer.readLine()).equals("")) {
            if (currentLine.startsWith(VERSION_START_LINE)) {
                String versionLine = currentLine.substring(VERSION_START_LINE.length()).trim();
                int version = Integer.parseInt(versionLine);
                BinaryId binaryId = new BinaryId(name, organisation, version);
                binary.setBinaryId(binaryId);
            } else if (currentLine.startsWith(NUMBER_START_LINE)) {
                String numberLine = currentLine.substring(NUMBER_START_LINE.length()).trim();
                binary.setRevision(numberLine);
            } else if (currentLine.startsWith(TYPE_START_LINE)) {
                String typeLine = currentLine.substring(TYPE_START_LINE.length()).trim();
                binary.setType(typeLine);
            } else if (currentLine.startsWith(INSTALLED_START_LINE)) {
                String installedLine = currentLine.substring(INSTALL_START_LINE.length()).trim();
                boolean installed = Boolean.parseBoolean(installedLine);
                binary.setInstalled(installed);
            } else if (currentLine.startsWith(DEPENDS_START_LINE)) {
                String dependsLine = currentLine.substring(DEPENDS_START_LINE.length()).trim();
                binary.setDependencies(parsePackageList(dependsLine));
            } else {
                throw new RuntimeException("Unknown property " + currentLine + " in package: " + packageLine);
            }
        }
        return binary;
    }

    private ParsedRequest parseRequest(BufferedReader buffer) throws IOException {
        ParsedRequest request;
        String currentLine;
        request = new ParsedRequest();
        while ((currentLine = buffer.readLine()) != null && !currentLine.equals("")) {
            if (currentLine.startsWith(INSTALL_START_LINE)) {
                String installLine = currentLine.substring(INSTALL_START_LINE.length()).trim();
                request.setInstall(parsePackageList(installLine));
            } else if (currentLine.startsWith(UPDATE_START_LINE)) {
                String updateLine = currentLine.substring(UPDATE_START_LINE.length()).trim();
                request.setUpdate(parsePackageList(updateLine));
            } else if (currentLine.startsWith(REMOVE_START_LINE)) {
                String removeLine = currentLine.substring(REMOVE_START_LINE.length()).trim();
                request.setRemove(parsePackageList(removeLine));
            }
        }
        return request;
    }

    private Set<BinaryId> parsePackageList(String packageListLine) {
        Set<BinaryId> binaryIds = new HashSet<BinaryId>();
        for (String aPackage : packageListLine.split(", ")) {
            String[] packageTab = aPackage.split(" = ");
            String infoPackage = packageTab[0];
            String packageVersion = packageTab[1];
            String[] infoPackageTab = infoPackage.split(SEPARATOR);
            BinaryId binaryId = new BinaryId(infoPackageTab[1], infoPackageTab[0],
                    Integer.parseInt(packageVersion));
            binaryIds.add(binaryId);
        }
        return binaryIds;
    }
}
