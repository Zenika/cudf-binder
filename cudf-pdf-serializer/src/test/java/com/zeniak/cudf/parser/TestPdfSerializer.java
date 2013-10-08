package com.zeniak.cudf.parser;

import com.zenika.cudf.parser.PDFSerializer;
import com.zenika.cudf.parser.ParsingException;
import com.zenika.cudf.parser.model.CUDFParsedDescriptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestPdfSerializer extends AbstractTestParser{

    private static final Logger LOG = Logger.getLogger(TestPdfSerializer.class.getName());

    private File outputPdf;
    
    @Before
    public void setUp() {
        outputPdf = new File("target/tmp-cudf.pdf");
    }
    
    @Test
    public void testPdfSerialization() throws ParsingException {
        PDFSerializer pdfSerializer = new PDFSerializer(outputPdf);
        pdfSerializer.serialize(createDescriptor());
    }

    @After
    public void tearDown() {
        if (!outputPdf.delete()) {
            LOG.log(Level.WARNING, "Unable to delete ");
        }
    }
}
