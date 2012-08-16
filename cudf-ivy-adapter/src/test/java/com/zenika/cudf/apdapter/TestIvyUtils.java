package com.zenika.cudf.apdapter;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import org.apache.ivy.core.module.descriptor.DefaultDependencyArtifactDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyArtifactDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

    public static Set<ModuleDescriptor> createModuleDescriptors() {
        DependencyDescriptor dependencyDescriptor2 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_2, false);
        DependencyDescriptor dependencyDescriptor3 = new DefaultDependencyDescriptor(MODULE_REVISION_ID_3, false);
        DependencyArtifactDescriptor[] dependencyArtifactDescriptors = new DependencyArtifactDescriptor[]{
                new DefaultDependencyArtifactDescriptor(dependencyDescriptor2, "jar2", "jar", "jar", null, null),
                new DefaultDependencyArtifactDescriptor(dependencyDescriptor3, "jar3", "jar", "jar", null, null)};
        Set<ModuleDescriptor> moduleDescriptors = new HashSet<ModuleDescriptor>();
        moduleDescriptors.add(DefaultModuleDescriptor.newDefaultInstance(MODULE_REVISION_ID_1,
                dependencyArtifactDescriptors));
        moduleDescriptors.add(DefaultModuleDescriptor.newDefaultInstance(MODULE_REVISION_ID_2, null));
        moduleDescriptors.add(DefaultModuleDescriptor.newDefaultInstance(MODULE_REVISION_ID_3, null));
        return moduleDescriptors;
    }

    public static CUDFDescriptor createDescriptor() {
        CUDFDescriptor descriptor = new CUDFDescriptor();

        Preamble preamble = createPreamble();
        Set<Binary> binaries = createBinaries(BINARY_ID_1, BINARY_ID_2, BINARY_ID_3);
        Request request = createRequest(findBinaryByBinaryId(BINARY_ID_1, binaries));

        descriptor.setPreamble(preamble);
        descriptor.setPackages(binaries);
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

    private static Set<Binary> createBinaries(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3) {
        Set<Binary> binaries = new LinkedHashSet<Binary>();
        Binary binary1 = createBinary(binaryId1, "1.0", "jar", false);
        Binary binary2 = createBinary(binaryId2, "1.0.0", "jar", false);
        Binary binary3 = createBinary(binaryId3, "1.2-SNAPSHOT", "jar", true);

        binary1.getDependencies().add(binary2);
        binary1.getDependencies().add(binary3);

        binaries.add(binary1);
        binaries.add(binary2);
        binaries.add(binary3);
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