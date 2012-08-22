package com.zenika.cudf.adapter.metadata;

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
