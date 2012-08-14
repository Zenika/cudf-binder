package com.zenika.cudf.parser.mock;

import com.zenika.cudf.parser.AbstractSerializer;
import com.zenika.cudf.parser.ParsingException;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class MockSerializer extends AbstractSerializer {

    private CUDFParsedDescriptor parsedDescriptor;

    @Override
    protected void parseCUDF(CUDFParsedDescriptor parsedDescriptor) throws ParsingException {
        this.parsedDescriptor = parsedDescriptor;
    }

    public CUDFParsedDescriptor getParsedDescriptor() {
        return parsedDescriptor;
    }
}
