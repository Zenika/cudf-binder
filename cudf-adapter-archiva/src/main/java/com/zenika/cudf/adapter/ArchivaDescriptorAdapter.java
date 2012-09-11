package com.zenika.cudf.adapter;

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

import com.zenika.cudf.adapter.cache.Cache;
import com.zenika.cudf.adapter.cache.CachedBinaries;
import com.zenika.cudf.adapter.resolver.CUDFVersionResolver;
import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.DefaultBinaries;
import org.apache.archiva.metadata.model.Dependency;
import org.apache.archiva.metadata.model.Organization;
import org.apache.archiva.metadata.model.ProjectVersionMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class ArchivaDescriptorAdapter
        implements DescriptorAdapter<Collection<ProjectVersionMetadata>, Collection<ProjectVersionMetadata>> {

    private Cache cache;
    private final ArchivaBinaryAdapter archivaBinaryAdapter;

    public ArchivaDescriptorAdapter(CUDFVersionResolver versionResolver, ArchivaBinaryAdapter archivaBinaryAdapter) {
        this.archivaBinaryAdapter = archivaBinaryAdapter;
        this.archivaBinaryAdapter.setVersionResolver(versionResolver);
    }

    @Override
    public Collection<ProjectVersionMetadata> fromCUDF(CUDFDescriptor descriptor) {
        Set<ProjectVersionMetadata> projectVersionMetadatas = new HashSet<ProjectVersionMetadata>();
        Set<Binary> binaries = descriptor.getBinaries().getAllBinaries();
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
    public CUDFDescriptor toCUDF(Collection<ProjectVersionMetadata> descriptors) {
        CUDFDescriptor descriptor = new CUDFDescriptor();
        Binaries binaries = getBinaries();
        for (ProjectVersionMetadata projectVersionMetadata : descriptors) {
            binaries.addBinary(archivaBinaryAdapter.toCUDF(projectVersionMetadata));
        }
        descriptor.setBinaries(binaries);
        return descriptor;
    }

    private Binaries getBinaries() {
        Binaries binaries;
        if (isCacheActive()) {
            binaries = new CachedBinaries(cache);
        } else {
            binaries = new DefaultBinaries();
        }
        return binaries;
    }

    private boolean isCacheActive() {
        return cache != null;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
