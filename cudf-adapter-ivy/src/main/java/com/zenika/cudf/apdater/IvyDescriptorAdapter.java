package com.zenika.cudf.apdater;

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

import com.zenika.cudf.adapter.DescriptorAdapter;
import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.DefaultBinaries;
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
