package com.zenika.cudf.adapter.metadata;

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

import org.apache.archiva.metadata.repository.storage.maven2.MavenProjectFacet;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class MavenMetadataFacetAdapter implements MetadataFacetAdapter {

    private final MavenProjectFacet mavenProjectFacet;

    public MavenMetadataFacetAdapter(MavenProjectFacet mavenProjectFacet) {
        this.mavenProjectFacet = mavenProjectFacet;
    }

    @Override
    public String getOrganisation() {
        return mavenProjectFacet.getGroupId();
    }

    @Override
    public String getName() {
        return mavenProjectFacet.getArtifactId();
    }

    @Override
    public String getType() {
        return mavenProjectFacet.getPackaging();
    }
}
