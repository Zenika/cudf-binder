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

import org.apache.archiva.metadata.model.MetadataFacet;
import org.apache.archiva.metadata.repository.storage.maven2.MavenProjectFacet;

import java.util.Map;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
//TODO: Search best way to link facet adapter with archiva facet. (Maybe annotations system)
public class MetadataFacetAdapterFactory {

    private static final String[] SUPPORTED_FACET_ID = {MavenProjectFacet.FACET_ID};

    public static MetadataFacetAdapter getAdapterByFacets(Map<String, MetadataFacet> facets) {
        for (String facetId : SUPPORTED_FACET_ID) {
            if (facets.containsKey(facetId)) {
                return new MavenMetadataFacetAdapter((MavenProjectFacet) facets.get(facetId));
            }
        }
        throw new IllegalArgumentException("The ProjectVersionMetadata with facets " + facets + " is not supported");
    }

}
