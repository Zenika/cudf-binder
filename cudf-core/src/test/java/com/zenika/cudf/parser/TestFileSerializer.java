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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestFileSerializer extends AbstractTestParser {

    private static final Logger LOG = Logger.getLogger(TestFileSerializer.class.getName());

    private File outputFile;

    @Before
    public void setUp() {
        outputFile = new File("target/tmp-cudf-output.cudf");
    }

    @Test
    public void testFileSerializer() throws Exception {
        FileSerializer serializer = new FileSerializer(outputFile);
        CUDFParsedDescriptor parsedDescriptor = createParsedDescriptor();
        serializer.parseCUDF(parsedDescriptor);
        String actualGeneration = readFile(outputFile);
        String expectedGeneration = readFile(new File("src/test/resources/com/zenika/cudf/resource/expected-generation.cudf"));
        assertEquals(expectedGeneration, actualGeneration);
    }

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder(100);
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            stringBuilder.append(currentLine).append("\n");
        }
        return stringBuilder.toString();
    }

    @After
    public void tearDown() {
        if (!outputFile.delete()) {
            LOG.log(Level.WARNING, "Unable to delete ");
        }
    }
}
