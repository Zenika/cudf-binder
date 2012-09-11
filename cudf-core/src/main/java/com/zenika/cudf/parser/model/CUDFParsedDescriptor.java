package com.zenika.cudf.parser.model;

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
