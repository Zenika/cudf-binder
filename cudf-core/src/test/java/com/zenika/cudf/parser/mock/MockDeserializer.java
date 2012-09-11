package com.zenika.cudf.parser.mock;

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
