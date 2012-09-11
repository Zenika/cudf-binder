package com.zenika.cudf.resolver;

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

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
//TODO: Maybe should be in core ?
public interface VersionResolver {

    /**
     * Resolve the number version in binary to CUDF version in binary id (ex: 1.2.0 -> 3)
     *
     * @param binary
     * @return the same binary with the CUDF version fixed
     */
    Binary resolveToCUDF(Binary binary);

    /**
     * Resolve the CUDF version in binary id to number version in binary (ex: 3 -> 1.2.0)
     *
     * @param binary
     * @return the same binary with the number version fixed
     */
    Binary resolveFromCUDF(Binary binary);
}
