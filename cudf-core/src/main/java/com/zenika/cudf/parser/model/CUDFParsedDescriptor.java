package com.zenika.cudf.parser.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class CUDFParsedDescriptor {

    public static final String PREAMBLE_START_LINE = "preamble: ";
    public static final String PACKAGE_START_LINE = "package: ";
    public static final String REQUEST_START_LINE = "request: ";

    private ParsedPreamble preamble;
    private ParsedRequest request;
    private Set<ParsedBinary> packages = new HashSet<ParsedBinary>();

    public ParsedPreamble getPreamble() {
        return preamble;
    }

    public void setPreamble(ParsedPreamble preamble) {
        this.preamble = preamble;
    }

    public ParsedRequest getRequest() {
        return request;
    }

    public void setRequest(ParsedRequest request) {
        this.request = request;
    }

    public Set<ParsedBinary> getPackages() {
        return packages;
    }

    public void setPackages(Set<ParsedBinary> packages) {
        this.packages = packages;
    }
}
