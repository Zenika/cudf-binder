package com.zenika.cudf.apdater;

import com.zenika.cudf.adapter.Adapter;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultDependencyArtifactDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyArtifactDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class IvyAdapter implements Adapter<Set<ModuleDescriptor>> {

    @Override
    public Set<ModuleDescriptor> fromCUDF(CUDFDescriptor descriptor) {
        Set<Binary> binaries = descriptor.getPackages();
        Set<ModuleDescriptor> moduleDescriptors = new HashSet<ModuleDescriptor>();
        for (Binary binary : binaries) {
            BinaryId binaryId = binary.getBinaryId();
            ModuleRevisionId moduleRevisionId = ModuleRevisionId.newInstance(binaryId.getOrganisation(),
                    binaryId.getName(), binary.getRevision());
            Set<Binary> dependencies = binary.getDependencies();
            Set<DependencyArtifactDescriptor> dependencyArtifactDescriptors = convertDependencies(dependencies);
            ModuleDescriptor moduleDescriptor = DefaultModuleDescriptor.newDefaultInstance(moduleRevisionId,
                    dependencyArtifactDescriptors.toArray(
                            new DependencyArtifactDescriptor[dependencyArtifactDescriptors.size()]));
            moduleDescriptors.add(moduleDescriptor);
        }
        return moduleDescriptors;
    }

    private Set<DependencyArtifactDescriptor> convertDependencies(Set<Binary> dependencies) {
        Set<DependencyArtifactDescriptor> dependencyArtifactDescriptors =
                new HashSet<DependencyArtifactDescriptor>();
        for (Binary dependency : dependencies) {
            BinaryId dependencyBinaryId = dependency.getBinaryId();
            ModuleRevisionId dependencyModuleRevisionId = ModuleRevisionId.newInstance(
                    dependencyBinaryId.getOrganisation(), dependencyBinaryId.getName(), dependency.getRevision());
            DependencyDescriptor dependencyDescriptor = new DefaultDependencyDescriptor(dependencyModuleRevisionId,
                    false);
            DependencyArtifactDescriptor dependencyArtifactDescriptor = new DefaultDependencyArtifactDescriptor(
                    dependencyDescriptor, dependencyBinaryId.getName(), dependency.getType(), dependency.getType(),
                    null, null);
            dependencyArtifactDescriptors.add(dependencyArtifactDescriptor);
        }
        return dependencyArtifactDescriptors;
    }

    @Override
    public CUDFDescriptor toCUDF(Set<ModuleDescriptor> descriptors) {
        Set<Binary> binaries = new HashSet<Binary>();
        for (ModuleDescriptor moduleDescriptor : descriptors) {
            ModuleRevisionId moduleRevisionId = moduleDescriptor.getModuleRevisionId();
            //TODO: Find best way to flag unknown CUDF version
            BinaryId binaryId = new BinaryId(moduleRevisionId.getName(), moduleRevisionId.getOrganisation(), 0);
            Binary binary = new Binary(binaryId);
            binary.setRevision(moduleRevisionId.getRevision());
            DependencyDescriptor[] dependencyDescriptors = moduleDescriptor.getDependencies();
            //TODO: I don't known if we need dependencies
            Set<Binary> dependencies = convertDependencyDescriptor(dependencyDescriptors);
            binary.setDependencies(dependencies);
            binaries.add(binary);
        }
        CUDFDescriptor descriptor = new CUDFDescriptor();
        descriptor.setPackages(binaries);
        return descriptor;
    }

    private Set<Binary> convertDependencyDescriptor(DependencyDescriptor[] dependencyDescriptors) {
        Set<Binary> dependencies = new HashSet<Binary>();
        for (DependencyDescriptor dependencyDescriptor : dependencyDescriptors) {
            ModuleRevisionId dependencyModuleRevisionId = dependencyDescriptor.getDependencyRevisionId();
            BinaryId dependencyId = new BinaryId(dependencyModuleRevisionId.getName(), dependencyModuleRevisionId.getOrganisation(), 0);
            Binary dependency = new Binary(dependencyId);
            dependency.setRevision(dependencyModuleRevisionId.getRevision());
            dependencies.add(dependency);
        }
        return dependencies;
    }
}
