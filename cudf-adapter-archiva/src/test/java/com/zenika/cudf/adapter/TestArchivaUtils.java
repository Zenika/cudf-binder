package com.zenika.cudf.adapter;

import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import org.apache.archiva.metadata.model.Dependency;
import org.apache.archiva.metadata.model.Organization;
import org.apache.archiva.metadata.model.ProjectVersionMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestArchivaUtils {

    public static final Organization ORGANIZATION;

    static {
        ORGANIZATION = new Organization();
        ORGANIZATION.setName("com.zenika");
    }

    public static final BinaryId BINARY_ID_1 = new BinaryId("jar1", ORGANIZATION.getName(), 0);
    public static final BinaryId BINARY_ID_2 = new BinaryId("jar2", ORGANIZATION.getName(), 0);
    public static final BinaryId BINARY_ID_3 = new BinaryId("jar3", ORGANIZATION.getName(), 0);

    public static Binary findBinaryByBinaryId(BinaryId binaryId, Set<Binary> binaries) {
        for (Binary binary : binaries) {
            if (binary.getBinaryId().equals(binaryId)) {
                return binary;
            }
        }
        return null;
    }

    public static ProjectVersionMetadata findProjectVersionMetadata(String name, String version, Organization organization, Set<ProjectVersionMetadata> projectVersionMetadatas) {
        for (ProjectVersionMetadata projectVersionMetadata : projectVersionMetadatas) {
            if (projectVersionMetadata.getName().equals(name) && projectVersionMetadata.getOrganization().getName().equals(organization.getName()) && projectVersionMetadata.getVersion().equals(version)) {
                return projectVersionMetadata;
            }
        }
        return null;
    }

    public static Set<ProjectVersionMetadata> createProjectVersionMetadatas() {
        Set<ProjectVersionMetadata> projectVersionMetadatas = new HashSet<ProjectVersionMetadata>();

        Organization organization = new Organization();
        organization.setName(ORGANIZATION.getName());

        Dependency dependency2 = new Dependency();
        dependency2.setArtifactId("jar2");
        dependency2.setGroupId(ORGANIZATION.getName());
        dependency2.setVersion("1.0.0");
        Dependency dependency3 = new Dependency();
        dependency3.setArtifactId("jar3");
        dependency3.setGroupId(ORGANIZATION.getName());
        dependency3.setVersion("1.2-SNAPSHOT");
        List<Dependency> dependencies = new ArrayList<Dependency>();
        dependencies.add(dependency2);
        dependencies.add(dependency3);

        ProjectVersionMetadata projectVersionMetadata1 = new ProjectVersionMetadata();
        projectVersionMetadata1.setOrganization(organization);
        projectVersionMetadata1.setName("jar1");
        projectVersionMetadata1.setId("1.0");
        projectVersionMetadata1.setDependencies(dependencies);

        ProjectVersionMetadata projectVersionMetadata2 = new ProjectVersionMetadata();
        projectVersionMetadata2.setOrganization(organization);
        projectVersionMetadata2.setName("jar2");
        projectVersionMetadata2.setId("1.0.0");

        ProjectVersionMetadata projectVersionMetadata3 = new ProjectVersionMetadata();
        projectVersionMetadata3.setOrganization(organization);
        projectVersionMetadata3.setName("jar3");
        projectVersionMetadata3.setId("1.2-SNAPSHOT");

        projectVersionMetadatas.add(projectVersionMetadata1);
        projectVersionMetadatas.add(projectVersionMetadata2);
        projectVersionMetadatas.add(projectVersionMetadata3);

        return projectVersionMetadatas;
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
