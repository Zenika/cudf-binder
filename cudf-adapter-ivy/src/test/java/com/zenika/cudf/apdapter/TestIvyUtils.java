package com.zenika.cudf.apdapter;

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

import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.DefaultBinaries;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestIvyUtils {

    public static final BinaryId BINARY_ID_1 = new BinaryId("jar1", "com.zenika", 0);
    public static final BinaryId BINARY_ID_2 = new BinaryId("jar2", "com.zenika", 0);
    public static final BinaryId BINARY_ID_3 = new BinaryId("jar3", "com.zenika", 0);

    public static final ModuleRevisionId MODULE_REVISION_ID_1 = ModuleRevisionId.newInstance("com.zenika", "jar1", "1.0");
    public static final ModuleRevisionId MODULE_REVISION_ID_2 = ModuleRevisionId.newInstance("com.zenika", "jar2", "1.0.0");
    public static final ModuleRevisionId MODULE_REVISION_ID_3 = ModuleRevisionId.newInstance("com.zenika", "jar3", "1.2-SNAPSHOT");

    public static Binary findBinaryByBinaryId(BinaryId binaryId, Set<Binary> binaries) {
        for (Binary binary : binaries) {
            if (binary.getBinaryId().equals(binaryId)) {
                return binary;
            }
        }
        return null;
    }

    public static ModuleDescriptor findModuleDescriptorByModuleRevisionId(ModuleRevisionId moduleRevisionId,
                                                                          Set<ModuleDescriptor> moduleDescriptors) {
        for (ModuleDescriptor moduleDescriptor : moduleDescriptors) {
            if (moduleDescriptor.getModuleRevisionId().equals(moduleRevisionId)) {
                return moduleDescriptor;
            }
        }
        return null;
    }

    public static Set<DependencyDescriptor> createDependencyDescriptor() {
        DependencyDescriptor dependencyDescriptor1 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_1, false);
        DependencyDescriptor dependencyDescriptor2 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_2, false);
        DependencyDescriptor dependencyDescriptor3 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_3, false);

        Set<DependencyDescriptor> dependencyDescriptors = new HashSet<DependencyDescriptor>();
        dependencyDescriptors.add(dependencyDescriptor1);
        dependencyDescriptors.add(dependencyDescriptor2);
        dependencyDescriptors.add(dependencyDescriptor3);

        return dependencyDescriptors;
    }

    public static Set<ModuleDescriptor> createModuleDescriptors() {
        Set<ModuleDescriptor> moduleDescriptors = new HashSet<ModuleDescriptor>();

        DependencyDescriptor dependencyDescriptor2 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_2, false);
        DependencyDescriptor dependencyDescriptor3 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_3, false);

        moduleDescriptors.add(createModuleDescriptor(MODULE_REVISION_ID_1, dependencyDescriptor2, dependencyDescriptor3));
        moduleDescriptors.add(createModuleDescriptor(MODULE_REVISION_ID_2));
        moduleDescriptors.add(createModuleDescriptor(MODULE_REVISION_ID_3));

        return moduleDescriptors;
    }

    private static DefaultModuleDescriptor createModuleDescriptor(ModuleRevisionId moduleRevisionId) {
        return createModuleDescriptor(moduleRevisionId, new DependencyDescriptor[]{});
    }

    private static DefaultModuleDescriptor createModuleDescriptor(ModuleRevisionId moduleRevisionId, DependencyDescriptor... dependencyDescriptors) {
        DefaultModuleDescriptor moduleDescriptor = new DefaultModuleDescriptor(null, null);
        moduleDescriptor.setModuleRevisionId(moduleRevisionId);
        moduleDescriptor.setResolvedModuleRevisionId(moduleRevisionId);
        for (DependencyDescriptor dependencyDescriptor : dependencyDescriptors) {
            moduleDescriptor.addDependency(dependencyDescriptor);
        }
        return moduleDescriptor;
    }

    public static CUDFDescriptor createDescriptor() {
        CUDFDescriptor descriptor = new CUDFDescriptor();

        Preamble preamble = createPreamble();
        Binaries binaries = createBinaries(BINARY_ID_1, BINARY_ID_2, BINARY_ID_3);
        Request request = createRequest(binaries.getBinaryById(BINARY_ID_1));

        descriptor.setPreamble(preamble);
        descriptor.setBinaries(binaries);
        descriptor.setRequest(request);

        return descriptor;
    }

    private static Preamble createPreamble() {
        Preamble preamble = new Preamble();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "value");
        preamble.setProperties(properties);
        preamble.setReqChecksum("req");
        preamble.setStatusChecksum("status");
        preamble.setUnivChecksum("univ");
        return preamble;
    }

    private static Binaries createBinaries(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3) {
        Binaries binaries = new DefaultBinaries();
        Binary binary1 = createBinary(binaryId1, "1.0", "jar", false);
        Binary binary2 = createBinary(binaryId2, "1.0.0", "jar", false);
        Binary binary3 = createBinary(binaryId3, "1.2-SNAPSHOT", "jar", true);

        binary1.getDependencies().add(binary2);
        binary1.getDependencies().add(binary3);

        binaries.addBinary(binary1);
        binaries.addBinary(binary2);
        binaries.addBinary(binary3);
        return binaries;
    }

    private static Binary createBinary(BinaryId binaryId, String revision, String type, boolean installed) {
        Binary binary = new Binary(binaryId);
        binary.setInstalled(installed);
        binary.setRevision(revision);
        binary.setType(type);
        return binary;
    }

    private static Request createRequest(Binary binary1) {
        Request request = new Request();
        request.getInstall().add(binary1);
        return request;
    }
}