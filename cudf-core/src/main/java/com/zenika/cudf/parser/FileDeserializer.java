package com.zenika.cudf.parser;

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
