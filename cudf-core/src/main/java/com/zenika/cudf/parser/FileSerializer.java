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
