package com.zenika.cudf.parser.mock;

import com.zenika.cudf.parser.AbstractDeserializer;
import com.zenika.cudf.parser.ParsingException;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class MockDeserializer extends AbstractDeserializer {

    private final CUDFParsedDescriptor parsedDescriptor;

    public MockDeserializer(CUDFParsedDescriptor parsedDescriptor) {
        this.parsedDescriptor = parsedDescriptor;
    }

    @Override
    protected CUDFParsedDescriptor parseCudf() throws ParsingException {
        return parsedDescriptor;
    }
}
