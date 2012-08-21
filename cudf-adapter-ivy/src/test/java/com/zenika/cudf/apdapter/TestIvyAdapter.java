package com.zenika.cudf.apdapter;

import com.zenika.cudf.apdater.IvyAdapter;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.junit.Test;

import java.util.Set;

import static com.zenika.cudf.apdapter.TestIvyUtils.BINARY_ID_1;
import static com.zenika.cudf.apdapter.TestIvyUtils.BINARY_ID_2;
import static com.zenika.cudf.apdapter.TestIvyUtils.BINARY_ID_3;
import static com.zenika.cudf.apdapter.TestIvyUtils.MODULE_REVISION_ID_1;
import static com.zenika.cudf.apdapter.TestIvyUtils.MODULE_REVISION_ID_2;
import static com.zenika.cudf.apdapter.TestIvyUtils.MODULE_REVISION_ID_3;
import static com.zenika.cudf.apdapter.TestIvyUtils.createDependencyDescriptor;
import static com.zenika.cudf.apdapter.TestIvyUtils.createDescriptor;
import static com.zenika.cudf.apdapter.TestIvyUtils.createModuleDescriptors;
import static com.zenika.cudf.apdapter.TestIvyUtils.findBinaryByBinaryId;
import static com.zenika.cudf.apdapter.TestIvyUtils.findModuleDescriptorByModuleRevisionId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestIvyAdapter {

    @Test
    public void testFromCUDF() {
        CUDFDescriptor descriptor = createDescriptor();
        IvyAdapter ivyAdapter = new IvyAdapter();

        Set<ModuleDescriptor> actualModuleDescriptors = ivyAdapter.fromCUDF(descriptor);
        Set<ModuleDescriptor> expectedModuleDescriptors = createModuleDescriptors();

        ModuleDescriptor actualModuleDescriptor1 = findModuleDescriptorByModuleRevisionId(MODULE_REVISION_ID_1,
                actualModuleDescriptors);
        ModuleDescriptor actualModuleDescriptor2 = findModuleDescriptorByModuleRevisionId(MODULE_REVISION_ID_2,
                actualModuleDescriptors);
        ModuleDescriptor actualModuleDescriptor3 = findModuleDescriptorByModuleRevisionId(MODULE_REVISION_ID_3,
                actualModuleDescriptors);

        assertNotNull(actualModuleDescriptor1);
        assertNotNull(actualModuleDescriptor2);
        assertNotNull(actualModuleDescriptor3);

        ModuleDescriptor expectedModuleDescriptor1 = findModuleDescriptorByModuleRevisionId(MODULE_REVISION_ID_1,
                expectedModuleDescriptors);
        ModuleDescriptor expectedModuleDescriptor2 = findModuleDescriptorByModuleRevisionId(MODULE_REVISION_ID_2,
                expectedModuleDescriptors);
        ModuleDescriptor expectedModuleDescriptor3 = findModuleDescriptorByModuleRevisionId(MODULE_REVISION_ID_3,
                expectedModuleDescriptors);

        assertModuleDescriptor(expectedModuleDescriptor1, actualModuleDescriptor1);
        assertModuleDescriptor(expectedModuleDescriptor2, actualModuleDescriptor2);
        assertModuleDescriptor(expectedModuleDescriptor3, actualModuleDescriptor3);
    }

    private void assertModuleDescriptor(ModuleDescriptor expectedModuleDescriptor, ModuleDescriptor actualModuleDescriptor) {
        ModuleRevisionId expectedModuleRevisionId = expectedModuleDescriptor.getModuleRevisionId();
        ModuleRevisionId actualModuleRevisionId = actualModuleDescriptor.getModuleRevisionId();

        assertEquals(expectedModuleRevisionId.getOrganisation(), actualModuleRevisionId.getOrganisation());
        assertEquals(expectedModuleRevisionId.getName(), actualModuleRevisionId.getName());
        assertEquals(expectedModuleRevisionId.getRevision(), actualModuleRevisionId.getRevision());
        assertEquals(expectedModuleDescriptor.getDependencies().length, actualModuleDescriptor.getDependencies().length);

        assertEquals(1, actualModuleDescriptor.getArtifacts("default").length);
    }

    @Test
    public void testToCUDF() {
        Set<DependencyDescriptor> dependencyDescriptors = createDependencyDescriptor();
        IvyAdapter ivyAdapter = new IvyAdapter();

        CUDFDescriptor actualDescriptor = ivyAdapter.toCUDF(dependencyDescriptors);
        CUDFDescriptor expectedDescriptor = createDescriptor();

        assertPreamble(expectedDescriptor.getPreamble(), actualDescriptor.getPreamble());
        assertBinaries(expectedDescriptor.getPackages(), actualDescriptor.getPackages());
        assertRequest(expectedDescriptor.getRequest(), actualDescriptor.getRequest());
    }

    private void assertPreamble(Preamble expectedPreamble, Preamble actualPreamble) {
        assertNull(actualPreamble);
    }

    private void assertBinaries(Set<Binary> expectedBinaries, Set<Binary> actualBinaries) {
        Binary actualBinary1 = findBinaryByBinaryId(BINARY_ID_1, actualBinaries);
        Binary actualBinary2 = findBinaryByBinaryId(BINARY_ID_2, actualBinaries);
        Binary actualBinary3 = findBinaryByBinaryId(BINARY_ID_3, actualBinaries);

        Binary expectedBinary1 = findBinaryByBinaryId(BINARY_ID_1, expectedBinaries);
        Binary expectedBinary2 = findBinaryByBinaryId(BINARY_ID_2, expectedBinaries);
        Binary expectedBinary3 = findBinaryByBinaryId(BINARY_ID_3, expectedBinaries);

        assertBinary(expectedBinary1, actualBinary1);
        assertBinary(expectedBinary2, actualBinary2);
        assertBinary(expectedBinary3, actualBinary3);
    }

    private void assertBinary(Binary expectedBinary, Binary actualBinary) {
        assertEquals(expectedBinary.getBinaryId(), actualBinary.getBinaryId());
        assertEquals(expectedBinary.getRevision(), actualBinary.getRevision());
    }

    private void assertRequest(Request expectedRequest, Request actualRequest) {
        assertNull(actualRequest);
    }
}
