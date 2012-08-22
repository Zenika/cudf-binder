package com.zenika.cudf.adapter.metadata;

import org.apache.archiva.metadata.model.MetadataFacet;
import org.apache.archiva.metadata.repository.storage.maven2.MavenProjectFacet;

import java.util.Map;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
//TODO: Search best way to link facet adapter with archiva facet. (Maybe annotations system)
public class MetadataFacetAdapterFactory {

    private static final String[] SUPPORTED_FACET_ID = {MavenProjectFacet.FACET_ID};

    public static MetadataFacetAdapter getAdapterByFacet(Map<String, MetadataFacet> facets) {
        for (String facetId : SUPPORTED_FACET_ID) {
            if (facets.containsKey(facetId)) {
                return new MavenMetadataFacetAdapter((MavenProjectFacet) facets.get(facetId));
            }
        }
        throw new IllegalArgumentException("The ProjectVersionMetadata with facets " + facets + " is not supported");
    }

}
