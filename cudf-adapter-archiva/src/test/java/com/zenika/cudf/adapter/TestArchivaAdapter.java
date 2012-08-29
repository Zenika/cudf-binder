package com.zenika.cudf.adapter;

import com.zenika.cudf.model.*;
import org.apache.archiva.metadata.model.Dependency;
import org.apache.archiva.metadata.model.ProjectVersionMetadata;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.zenika.cudf.adapter.TestArchivaUtils.*;
import static org.junit.Assert.*;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestArchivaAdapter {

    private ArchivaDescriptorAdapter archivaAdapter;
    private VersionResolverMock mock;

    @Before
    public void setUp() {
        mock = new VersionResolverMock();
        archivaAdapter = new ArchivaDescriptorAdapter(mock, new ArchivaBinaryAdapter());
    }

    @Test
    public void testFromCUDF() {
        CUDFDescriptor descriptor = createDescriptor();

        Collection<ProjectVersionMetadata> actualProjectVersionMetadatas = archivaAdapter.fromCUDF(descriptor);
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
        assertBinaries(expectedDescriptor.getBinaries(), actualDescriptor.getBinaries());
        assertRequest(expectedDescriptor.getRequest(), actualDescriptor.getRequest());
    }

    private void assertPreamble(Preamble expectedPreamble, Preamble actualPreamble) {
        assertNull(actualPreamble);
    }

    private void assertBinaries(Binaries expectedBinaries, Binaries actualBinaries) {
        Binary actualBinary1 = expectedBinaries.getBinaryById(BINARY_ID_1);
        Binary actualBinary2 = expectedBinaries.getBinaryById(BINARY_ID_2);
        Binary actualBinary3 = expectedBinaries.getBinaryById(BINARY_ID_3);

        Binary expectedBinary1 = actualBinaries.getBinaryById(BINARY_ID_1);
        Binary expectedBinary2 = actualBinaries.getBinaryById(BINARY_ID_2);
        Binary expectedBinary3 = actualBinaries.getBinaryById(BINARY_ID_3);

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

    @Test
    public void testVersionResolverCall() {
        Set<ProjectVersionMetadata> projectVersionMetadatas = createProjectVersionMetadatas();
        archivaAdapter.toCUDF(projectVersionMetadatas);
        assertTrue(mock.isCalled());
        // 3 times for the 3 binaries and 2 times for the 2 dependencies of the "jar1" binary
        assertEquals(mock.getNumberOfCall(), 5);
    }
}
