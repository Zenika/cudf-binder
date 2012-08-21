package com.zenika.cudf.parser;

import com.zenika.cudf.parser.model.CUDFParsedDescriptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Logger;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class FileDeserializer extends AbstractDeserializer {

    private static final Logger LOG = Logger.getLogger(FileDeserializer.class.getName());

    private final File file;

    public FileDeserializer(File file) {
        this.file = file;
    }

    public CUDFParsedDescriptor parseCudf() throws ParsingException {
        try {
            DefaultDeserializer deserializer = new DefaultDeserializer(new FileReader(file));
            return deserializer.parseCudf();
        } catch (FileNotFoundException e) {
            throw new ParsingException("Unable to find file to parsing", e);
        }
    }

}
