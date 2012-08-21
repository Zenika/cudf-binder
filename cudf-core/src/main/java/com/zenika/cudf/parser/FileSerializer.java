package com.zenika.cudf.parser;

import com.zenika.cudf.parser.model.CUDFParsedDescriptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class FileSerializer extends AbstractSerializer {

    private File file;

    public FileSerializer(File file) {
        this.file = file;
    }

    @Override
    protected void parseCUDF(CUDFParsedDescriptor parsedDescriptor) throws ParsingException {
        try {
            DefaultSerializer serializer = new DefaultSerializer(new FileWriter(file));
            serializer.parseCUDF(parsedDescriptor);
        } catch (IOException e) {
            throw new ParsingException("An error was occur when trying to write the file", e);
        }
    }
}
