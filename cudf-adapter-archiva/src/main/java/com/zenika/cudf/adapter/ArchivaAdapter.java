package com.zenika.cudf.adapter;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import org.apache.archiva.metadata.model.Dependency;
import org.apache.archiva.metadata.model.Organization;
import org.apache.archiva.metadata.model.ProjectVersionMetadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ArchivaAdapter implements Adapter<Set<ProjectVersionMetadata>> {

    @Override
    public Set<ProjectVersionMetadata> fromCUDF(CUDFDescriptor descriptor) {
        Set<ProjectVersionMetadata> projectVersionMetadatas = new HashSet<ProjectVersionMetadata>();
        Set<Binary> binaries = descriptor.getPackages();
        for (Binary binary : binaries) {
            BinaryId binaryId = binary.getBinaryId();
            ProjectVersionMetadata projectVersionMetadata = new ProjectVersionMetadata();
            Organization organization = new Organization();
            organization.setName(binaryId.getOrganisation());
            projectVersionMetadata.setOrganization(organization);
            projectVersionMetadata.setName(binaryId.getName());
            projectVersionMetadata.setId(binary.getRevision());
            List<Dependency> archivaDependencies = convertDependencies(binary.getDependencies());
            projectVersionMetadata.setDependencies(archivaDependencies);
            projectVersionMetadatas.add(projectVersionMetadata);
        }
        return projectVersionMetadatas;
    }

    private List<Dependency> convertDependencies(Set<Binary> dependencies) {
        List<Dependency> archivaDependencies = new ArrayList<Dependency>();
        for (Binary dependency : dependencies) {
            BinaryId dependencyId = dependency.getBinaryId();
            Dependency archivaDependency = new Dependency();
            archivaDependency.setGroupId(dependencyId.getOrganisation());
            archivaDependency.setArtifactId(dependencyId.getName());
            archivaDependency.setVersion(dependency.getRevision());
            archivaDependency.setType(dependency.getType());
            archivaDependencies.add(archivaDependency);
        }
        return archivaDependencies;
    }

    @Override
    public CUDFDescriptor toCUDF(Set<ProjectVersionMetadata> descriptors) {
        CUDFDescriptor descriptor = new CUDFDescriptor();
        Set<Binary> binaries = new HashSet<Binary>();
        for (ProjectVersionMetadata projectVersionMetadata : descriptors) {
            BinaryId binaryId = new BinaryId(projectVersionMetadata.getName(), projectVersionMetadata.getOrganization().getName(), 0);
            Binary binary = new Binary(binaryId);
            binary.setRevision(projectVersionMetadata.getVersion());
            Set<Binary> dependencies = convertArchivaDependencies(projectVersionMetadata.getDependencies());
            binary.setDependencies(dependencies);
            binaries.add(binary);
        }
        descriptor.setPackages(binaries);
        return descriptor;
    }

    private Set<Binary> convertArchivaDependencies(List<Dependency> archivaDependencies) {
        Set<Binary> dependencies = new HashSet<Binary>();
        for (Dependency archivaDependency : archivaDependencies) {
            BinaryId dependencyId = new BinaryId(archivaDependency.getArtifactId(), archivaDependency.getGroupId(), 0);
            Binary dependency = new Binary(dependencyId);
            dependency.setRevision(archivaDependency.getVersion());
            dependencies.add(dependency);
        }
        return dependencies;
    }
}
