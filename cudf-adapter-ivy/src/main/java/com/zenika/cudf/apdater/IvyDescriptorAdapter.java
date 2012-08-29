package com.zenika.cudf.apdater;

import com.zenika.cudf.adapter.DescriptorAdapter;
import com.zenika.cudf.model.*;
import org.apache.ivy.core.module.descriptor.*;
import org.apache.ivy.core.module.id.ModuleRevisionId;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class IvyDescriptorAdapter
        implements DescriptorAdapter<Set<ModuleDescriptor>, Set<DependencyDescriptor>> {

    @Override
    public Set<ModuleDescriptor> fromCUDF(CUDFDescriptor descriptor) {
        Set<Binary> binaries = descriptor.getBinaries().getAllBinaries();
        Set<ModuleDescriptor> moduleDescriptors = new HashSet<ModuleDescriptor>();
        for (Binary binary : binaries) {
            BinaryId binaryId = binary.getBinaryId();
            ModuleRevisionId moduleRevisionId = ModuleRevisionId.newInstance(binaryId.getOrganisation(),
                    binaryId.getName(), binary.getRevision());
            Set<Binary> dependencies = binary.getDependencies();

            DefaultModuleDescriptor moduleDescriptor = new DefaultModuleDescriptor(moduleRevisionId, "", new Date());
            moduleDescriptor.setResolvedModuleRevisionId(moduleRevisionId);
            moduleDescriptor.addConfiguration(new Configuration("default"));

            Artifact artifact = new DefaultArtifact(moduleRevisionId, new Date(), binaryId.getName(), binary.getType(), binary.getType());
            moduleDescriptor.addArtifact("default", artifact);

            convertDependencies(dependencies, moduleDescriptor);
            moduleDescriptors.add(moduleDescriptor);
        }
        return moduleDescriptors;
    }

    private void convertDependencies(Set<Binary> dependencies, DefaultModuleDescriptor moduleDescriptor) {
        for (Binary dependency : dependencies) {
            BinaryId dependencyBinaryId = dependency.getBinaryId();
            ModuleRevisionId dependencyModuleRevisionId = ModuleRevisionId.newInstance(
                    dependencyBinaryId.getOrganisation(), dependencyBinaryId.getName(), dependency.getRevision());
            DefaultDependencyDescriptor dependencyDescriptor = new DefaultDependencyDescriptor(dependencyModuleRevisionId,
                    false);
            dependencyDescriptor.addDependencyConfiguration("default", "default");
            DefaultDependencyArtifactDescriptor dependencyArtifactDescriptor =
                    new DefaultDependencyArtifactDescriptor(
                            dependencyDescriptor, dependencyDescriptor.getDependencyId().getName(),
                            dependency.getType(), dependency.getType(), null, null);
            dependencyDescriptor.addDependencyArtifact("default", dependencyArtifactDescriptor);
            moduleDescriptor.addDependency(dependencyDescriptor);
        }
    }

    @Override
    public CUDFDescriptor toCUDF(Set<DependencyDescriptor> descriptors) {
        Binaries binaries = new DefaultBinaries();
        for (DependencyDescriptor dependencyDescriptor : descriptors) {
            ModuleRevisionId moduleRevisionId = dependencyDescriptor.getDependencyRevisionId();
            //TODO: Find best way to flag unknown CUDF version
            BinaryId binaryId = new BinaryId(moduleRevisionId.getName(), moduleRevisionId.getOrganisation(), 0);
            Binary binary = new Binary(binaryId);
            binary.setRevision(moduleRevisionId.getRevision());
            binaries.addBinary(binary);
        }
        CUDFDescriptor descriptor = new CUDFDescriptor();
        descriptor.setBinaries(binaries);
        return descriptor;
    }

}
