package com.zenika.cudf.adapter;

import com.zenika.cudf.adapter.metadata.MetadataFacetAdapter;
import com.zenika.cudf.adapter.metadata.MetadataFacetAdapterFactory;
import com.zenika.cudf.adapter.resolver.CUDFVersionResolver;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import org.apache.archiva.metadata.model.Dependency;
import org.apache.archiva.metadata.model.ProjectVersionMetadata;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ArchivaBinaryAdapter implements BinaryAdapter<ProjectVersionMetadata> {

    private CUDFVersionResolver versionResolver;

    @Override
    public ProjectVersionMetadata fromCUDF(Binary binary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Binary toCUDF(ProjectVersionMetadata projectVersionMetadata) {
        check();
        MetadataFacetAdapter metadataFacetAdapter =
                MetadataFacetAdapterFactory.getAdapterByFacets(projectVersionMetadata.getFacets());
        BinaryId binaryId = new BinaryId(metadataFacetAdapter.getName(), metadataFacetAdapter.getOrganisation(), 0);
        Binary binary = new Binary(binaryId);
        binary.setRevision(projectVersionMetadata.getVersion());
        binary.setType(metadataFacetAdapter.getType());
        binary.setDependencies(convertArchivaDependencies(projectVersionMetadata.getDependencies()));
        binary = versionResolver.resolve(binary);
        return binary;
    }

    private void check() {
        if (versionResolver == null) {
            throw new IllegalStateException("The versionResolver must be set");
        }
    }

    private Set<Binary> convertArchivaDependencies(List<Dependency> archivaDependencies) {
        Set<Binary> dependencies = new HashSet<Binary>();
        for (Dependency archivaDependency : archivaDependencies) {
            BinaryId dependencyId = new BinaryId(archivaDependency.getArtifactId(), archivaDependency.getGroupId(), 0);
            Binary dependency = new Binary(dependencyId);
            dependency.setRevision(archivaDependency.getVersion());
            dependency.setType(archivaDependency.getType());
            dependency = versionResolver.resolve(dependency);
            dependencies.add(dependency);
        }
        return dependencies;
    }

    public void setVersionResolver(CUDFVersionResolver versionResolver) {
        this.versionResolver = versionResolver;
    }
}
