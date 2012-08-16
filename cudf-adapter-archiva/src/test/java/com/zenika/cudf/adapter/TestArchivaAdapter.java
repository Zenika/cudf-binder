package com.zenika.cudf.adapter;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import org.apache.archiva.metadata.model.Dependency;
import org.apache.archiva.metadata.model.ProjectVersionMetadata;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.zenika.cudf.adapter.TestArchivaUtils.BINARY_ID_1;
import static com.zenika.cudf.adapter.TestArchivaUtils.BINARY_ID_2;
import static com.zenika.cudf.adapter.TestArchivaUtils.BINARY_ID_3;
import static com.zenika.cudf.adapter.TestArchivaUtils.ORGANIZATION;
import static com.zenika.cudf.adapter.TestArchivaUtils.createDescriptor;
import static com.zenika.cudf.adapter.TestArchivaUtils.createProjectVersionMetadatas;
import static com.zenika.cudf.adapter.TestArchivaUtils.findBinaryByBinaryId;
import static com.zenika.cudf.adapter.TestArchivaUtils.findProjectVersionMetadata;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestArchivaAdapter {

    private ArchivaAdapter archivaAdapter;

    @Before
    public void setUp() {
        archivaAdapter = new ArchivaAdapter();
    }

    @Test
    public void testFromCUDF() {
        CUDFDescriptor descriptor = createDescriptor();

        Set<ProjectVersionMetadata> actualProjectVersionMetadatas = archivaAdapter.fromCUDF(descriptor);
        Set<ProjectVersionMetadata> expectedProjectVersionMetadatas = createProjectVersionMetadatas();

        ProjectVersionMetadata actualProjectVersionMetadata1 = findProjectVersionMetadata("jar1", "1.0", ORGANIZATION,
                actualProjectVersionMetadatas);
        ProjectVersionMetadata actualProjectVersionMetadata2 = findProjectVersionMetadata("jar2", "1.0.0", ORGANIZATION,
                actualProjectVersionMetadatas);
        ProjectVersionMetadata actualProjectVersionMetadata3 = findProjectVersionMetadata("jar3", "1.2-SNAPSHOT",
                ORGANIZATION, actualProjectVersionMetadatas);

        assertNotNull(actualProjectVersionMetadata1);
        assertNotNull(actualProjectVersionMetadata2);
        assertNotNull(actualProjectVersionMetadata3);

        assertContainsDependency(actualProjectVersionMetadata1.getDependencies(), ORGANIZATION.getName(), "jar2", "1.0.0");
        assertContainsDependency(actualProjectVersionMetadata1.getDependencies(), ORGANIZATION.getName(), "jar3", "1.2-SNAPSHOT");
    }

    private void assertContainsDependency(List<Dependency> actualDependencies, String organisation, String name, String version) {
        Dependency dependency = new Dependency();
        dependency.setArtifactId(name);
        dependency.setGroupId(organisation);
        dependency.setVersion(version);
        assertContainsDependency(actualDependencies, dependency);
    }

    private void assertContainsDependency(List<Dependency> actualDependencies, Dependency dependency) {
        for (Dependency actualDependency : actualDependencies) {
            if (actualDependency.getArtifactId().equals(dependency.getArtifactId()) &&
                    actualDependency.getGroupId().equals(dependency.getGroupId()) &&
                    actualDependency.getVersion().equals(dependency.getVersion())) {
                return;
            }
        }
        throw new AssertionError("Unable to find dependency " + dependency + "\nin list : " + actualDependencies);
    }

    @Test
    public void testToCUDF() {
        Set<ProjectVersionMetadata> projectVersionMetadatas = createProjectVersionMetadatas();

        CUDFDescriptor actualDescriptor = archivaAdapter.toCUDF(projectVersionMetadatas);
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
