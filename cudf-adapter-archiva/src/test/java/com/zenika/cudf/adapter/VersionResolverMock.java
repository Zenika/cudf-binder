package com.zenika.cudf.adapter;

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

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.resolver.VersionResolver;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class VersionResolverMock implements VersionResolver {

    private int numberOfResolveToCUDFCall = 0;
    private int numberOfResolveFromCUDFCall = 0;

    @Override
    public Binary resolveToCUDF(Binary binary) {
        numberOfResolveToCUDFCall++;
        return binary;
    }

    @Override
    public Binary resolveFromCUDF(Binary binary) {
        numberOfResolveFromCUDFCall++;
        return binary;
    }

    public int getNumberOfResolveToCUDFCall() {
        return numberOfResolveToCUDFCall;
    }

    public int getNumberOfResolveFromCUDFCall() {
        return numberOfResolveFromCUDFCall;
    }
}
