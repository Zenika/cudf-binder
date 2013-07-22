package com.zenika.cudf.parser;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import com.zenika.cudf.parser.model.ParsedBinary;
import com.zenika.cudf.parser.model.ParsedPreamble;
import com.zenika.cudf.parser.model.ParsedRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class PDFSerializer extends AbstractSerializer {

    private File pdf;

    public PDFSerializer(File pdf) {
        this.pdf = pdf;
    }

    @Override
    protected void parseCUDF(CUDFParsedDescriptor parsedDescriptor) throws ParsingException {
        Document document = null;
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdf));
            document.open();
            document.add(convertPreambleToPdf(parsedDescriptor.getPreamble()));
            document.add(Chunk.NEWLINE);
            document.add(convertParsedBinariesToPdf(parsedDescriptor.getPackages()));
            document.add(convertRequestToPdf(parsedDescriptor.getRequest()));
        } catch (DocumentException e) {
            throw new ParsingException("Unable to create PDF", e);
        } catch (FileNotFoundException e) {
            throw new ParsingException("Unable to create file: " + pdf, e);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private Paragraph convertPreambleToPdf(ParsedPreamble preamble) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(CUDFParsedDescriptor.PREAMBLE_START_LINE));
        paragraph.add(convertPreamblePropertiesToPdf(preamble.getProperties()));
        if (preamble.getReqChecksum() != null) {
            paragraph.add(convertReqChecksumToPdf(preamble.getReqChecksum()));
        }
        if (preamble.getStatusChecksum() != null) {
            paragraph.add(convertStatusChecksumToPdf(preamble.getStatusChecksum()));
        }
        if (preamble.getUnivChecksum() != null) {
            paragraph.add(convertUnivChecksumToPdf(preamble.getUnivChecksum()));
        }
        return paragraph;
    }

    private Paragraph convertPreamblePropertiesToPdf(Map<String, String> properties) {
        Paragraph propertiesLine = new Paragraph();
        propertiesLine.add(ParsedPreamble.PROPERTY_START_LINE);
        Iterator<Map.Entry<String, String>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> property = iterator.next();
            propertiesLine.add(property.getKey());
            propertiesLine.add(": ");
            propertiesLine.add(property.getValue());
            if (iterator.hasNext()) {
                propertiesLine.add(", ");
            }
        }
        return propertiesLine;
    }

    private Paragraph convertReqChecksumToPdf(String reqChecksum) {
        Paragraph reqChecksumLine = new Paragraph();
        reqChecksumLine.add(ParsedPreamble.REQ_CHECKSUM_START_LINE);
        reqChecksumLine.add(reqChecksum);
        return reqChecksumLine;
    }

    private Paragraph convertStatusChecksumToPdf(String statusChecksum) {
        Paragraph statusChecksumLine = new Paragraph();
        statusChecksumLine.add(ParsedPreamble.STATUS_CHECKSUM_START_LINE);
        statusChecksumLine.add(statusChecksum);
        return statusChecksumLine;
    }

    private Paragraph convertUnivChecksumToPdf(String univChecksum) {
        Paragraph univChecksumLine = new Paragraph();
        univChecksumLine.add(ParsedPreamble.UNIV_CHECKSUM_START_LINE);
        univChecksumLine.add(univChecksum);
        return univChecksumLine;
    }

    private Paragraph convertParsedBinariesToPdf(Set<ParsedBinary> parsedBinaries) {
        Paragraph binariesParagraph = new Paragraph();
        for (ParsedBinary parsedBinary : parsedBinaries) {
            BinaryId binaryId = parsedBinary.getBinaryId();
            binariesParagraph.add(convertPackageToPdf(binaryId));
            binariesParagraph.add(convertVersionToPdf(binaryId));
            binariesParagraph.add(convertTypeToPdf(parsedBinary));
            binariesParagraph.add(convertNumberToPdf(parsedBinary));
            if (parsedBinary.isInstalled()) {
                binariesParagraph.add(convertInstallToPdf(parsedBinary));
            }
            if (parsedBinary.getDependencies().size() > 0) {
                binariesParagraph.add(convertDependenciesToPdf(parsedBinary));
            }
            binariesParagraph.add(Chunk.NEWLINE);
        }
        return binariesParagraph;
    }

    private Paragraph convertPackageToPdf(BinaryId binaryId) {
        Paragraph packageLine = new Paragraph();
        packageLine.add(CUDFParsedDescriptor.PACKAGE_START_LINE);
        packageLine.add(encodeOrganisationWithName(binaryId.getOrganisation(), binaryId.getName()));
        return packageLine;
    }

    private Paragraph convertVersionToPdf(BinaryId binaryId) {
        Paragraph versionLine = new Paragraph();
        versionLine.add(ParsedBinary.VERSION_START_LINE);
        versionLine.add(String.valueOf(binaryId.getVersion()));
        return versionLine;
    }

    private Paragraph convertTypeToPdf(ParsedBinary parsedBinary) {
        Paragraph typeLine = new Paragraph();
        typeLine.add(ParsedBinary.TYPE_START_LINE);
        typeLine.add(parsedBinary.getType());
        return typeLine;
    }

    private Paragraph convertNumberToPdf(ParsedBinary parsedBinary) {
        Paragraph numberLine = new Paragraph();
        numberLine.add(ParsedBinary.NUMBER_START_LINE);
        numberLine.add(parsedBinary.getRevision());
        return numberLine;
    }

    private Paragraph convertInstallToPdf(ParsedBinary parsedBinary) {
        Paragraph installLine = new Paragraph();
        installLine.add(ParsedBinary.INSTALLED_START_LINE);
        installLine.add(String.valueOf(parsedBinary.isInstalled()));
        return installLine;
    }

    private Paragraph convertDependenciesToPdf(ParsedBinary parsedBinary) {
        Paragraph dependencyLine = new Paragraph();
        dependencyLine.add(ParsedBinary.DEPENDS_START_LINE);
        Iterator<BinaryId> iterator = parsedBinary.getDependencies().iterator();
        while (iterator.hasNext()) {
            BinaryId dependency = iterator.next();
            dependencyLine.add(encodeOrganisationWithName(dependency.getOrganisation(), dependency.getName()));
            dependencyLine.add(" = ");
            dependencyLine.add(String.valueOf(dependency.getVersion()));
            if (iterator.hasNext()) {
                dependencyLine.add(", ");
            }
        }
        return dependencyLine;
    }

    private Paragraph convertRequestToPdf(ParsedRequest request) {
        Paragraph requestLine = new Paragraph();
        if (request != null) {
            requestLine.add(new Paragraph(CUDFParsedDescriptor.REQUEST_START_LINE));
            if (request.getInstall().size() > 0) {
                requestLine.add(convertRequestInstallToPdf(request));
            }
            if (request.getUpdate().size() > 0) {
                requestLine.add(convertUpdateToPdf(request));
            }
            if (request.getRemove().size() > 0) {
                requestLine.add(convertRemoveToPdf(request));
            }
        } else {
            requestLine.add("");
        }
        return requestLine;
    }

    private Paragraph convertRequestInstallToPdf(ParsedRequest request) {
        Paragraph installLine = new Paragraph();
        installLine.add(ParsedRequest.INSTALL_START_LINE);
        installLine.add(convertBinariesToPdf(request.getInstall()));
        return installLine;
    }

    private Paragraph convertUpdateToPdf(ParsedRequest request) {
        Paragraph updateLine = new Paragraph();
        updateLine.add(ParsedRequest.UPDATE_START_LINE);
        updateLine.add(convertBinariesToPdf(request.getUpdate()));
        return updateLine;
    }

    private Paragraph convertRemoveToPdf(ParsedRequest request) {
        Paragraph removeLine = new Paragraph();
        removeLine.add(ParsedRequest.REMOVE_START_LINE);
        removeLine.add(convertBinariesToPdf(request.getRemove()));
        return removeLine;
    }

    private Phrase convertBinariesToPdf(Set<BinaryId> binaries) {
        Phrase line = new Phrase();
        Iterator<BinaryId> iterator = binaries.iterator();
        while (iterator.hasNext()) {
            BinaryId binaryId = iterator.next();
            line.add(encodeOrganisationWithName(binaryId.getOrganisation(), binaryId.getName()));
            line.add(" = ");
            line.add(String.valueOf(binaryId.getVersion()));
            if (iterator.hasNext()) {
                line.add(", ");
            }
        }
        return line;
    }
}
