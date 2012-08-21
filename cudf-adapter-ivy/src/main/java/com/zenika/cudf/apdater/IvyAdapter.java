package com.zenika.cudf.apdater;

import com.zenika.cudf.adapter.Adapter;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.Configuration;
import org.apache.ivy.core.module.descriptor.DefaultArtifact;
import org.apache.ivy.core.module.descriptor.DefaultDependencyArtifactDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class IvyAdapter implements Adapter<Set<ModuleDescriptor>, Set<DependencyDescriptor>> {

    @Override
    public Set<ModuleDescriptor> fromCUDF(CUDFDescriptor descriptor) {
        Set<Binary> binaries = descriptor.getPackages();
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
        Set<Binary> binaries = new HashSet<Binary>();
        for (DependencyDescriptor dependencyDescriptor : descriptors) {
            ModuleRevisionId moduleRevisionId = dependencyDescriptor.getDependencyRevisionId();
            //TODO: Find best way to flag unknown CUDF version
            BinaryId binaryId = new BinaryId(moduleRevisionId.getName(), moduleRevisionId.getOrganisation(), 0);
            Binary binary = new Binary(binaryId);
            binary.setRevision(moduleRevisionId.getRevision());
            binaries.add(binary);
        }
        CUDFDescriptor descriptor = new CUDFDescriptor();
        descriptor.setPackages(binaries);
        return descriptor;
    }

}
