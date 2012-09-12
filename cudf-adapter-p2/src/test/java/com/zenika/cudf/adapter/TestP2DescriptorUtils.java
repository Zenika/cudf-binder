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

import com.zenika.cudf.model.Binaries;
import com.zenika.cudf.model.Binary;
import com.zenika.cudf.model.BinaryId;
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.DefaultBinaries;
import com.zenika.cudf.model.Preamble;
import com.zenika.cudf.model.Request;
import com.zenika.cudf.parser.model.ParsedBinary;
import org.eclipse.equinox.p2.cudf.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.cudf.metadata.InstallableUnit;
import org.eclipse.equinox.p2.cudf.metadata.RequiredCapability;
import org.eclipse.equinox.p2.cudf.metadata.Version;
import org.eclipse.equinox.p2.cudf.metadata.VersionRange;
import org.eclipse.equinox.p2.cudf.query.QueryableArray;
import org.eclipse.equinox.p2.cudf.solver.ProfileChangeRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestP2DescriptorUtils {

    public static final String DEFAULT_ORGANISATION = "com.zenika";

    public static final BinaryId BINARY_ID_1 = new BinaryId("jar1", DEFAULT_ORGANISATION, 1);
    public static final BinaryId BINARY_ID_2 = new BinaryId("jar2", DEFAULT_ORGANISATION, 1);
    public static final BinaryId BINARY_ID_3 = new BinaryId("jar3", DEFAULT_ORGANISATION, 2);

    //TODO: Maybe we should simplify the test sample creation
    public static ProfileChangeRequest createP2Descriptor() {
        InstallableUnit[] p2Packages = new InstallableUnit[3];
        p2Packages[0] = createP2Package(DEFAULT_ORGANISATION, "jar1", 1);
        p2Packages[1] = createP2Package(DEFAULT_ORGANISATION, "jar2", 1);
        p2Packages[2] = createP2Package(DEFAULT_ORGANISATION, "jar3", 2);

        IRequiredCapability[] p2Dependencies = createDefaultP2Dependencies();

        p2Packages[0].setRequiredCapabilities(p2Dependencies);

        ProfileChangeRequest p2Descriptor = new ProfileChangeRequest(new QueryableArray(p2Packages));

        p2Descriptor.addInstallableUnit(new RequiredCapability("com.zenika%3ajar1", new VersionRange(new Version(1))));
        p2Descriptor.addInstallableUnit(new RequiredCapability("com.zenika%3ajar2", new VersionRange(new Version(1))));
        p2Descriptor.addInstallableUnit(new RequiredCapability("com.zenika%3ajar3", new VersionRange(new Version(2))));

        return p2Descriptor;
    }

    private static InstallableUnit createP2Package(String organisation, String name, int version) {
        InstallableUnit p2Package = new InstallableUnit();
        p2Package.setId(organisation + ParsedBinary.SEPARATOR + name);
        p2Package.setVersion(new Version(version));
        p2Package.setInstalled(false);
        return p2Package;
    }

    private static IRequiredCapability[] createDefaultP2Dependencies() {
        IRequiredCapability[] p2Dependencies = new IRequiredCapability[2];
        p2Dependencies[0] = new RequiredCapability("com.zenika%3ajar2", new VersionRange(new Version(1)), false);
        p2Dependencies[1] = new RequiredCapability("com.zenika%3ajar3", new VersionRange(new Version(2)), false);
        return p2Dependencies;
    }

    public static Collection<InstallableUnit> createInstallableUnit() {
        List<InstallableUnit> p2Packages = new ArrayList<InstallableUnit>();

        InstallableUnit p2Package1 = createP2Package(DEFAULT_ORGANISATION, "jar1", 1);
        p2Package1.setRequiredCapabilities(createDefaultP2Dependencies());

        p2Packages.add(p2Package1);
        p2Packages.add(createP2Package(DEFAULT_ORGANISATION, "jar2", 1));
        p2Packages.add(createP2Package(DEFAULT_ORGANISATION, "jar3", 2));
        return p2Packages;
    }

    public static Collection<InstallableUnit> createInstallableUnitWithExtrasProperties() {
        List<InstallableUnit> p2Packages = new ArrayList<InstallableUnit>();

        InstallableUnit p2Package1 = createP2Package(DEFAULT_ORGANISATION, "jar1", 1);
        p2Package1.setRequiredCapabilities(createDefaultP2Dependencies());
        p2Package1 = addExtrasProperties(p2Package1, "1.0");

        InstallableUnit p2Package2 = createP2Package(DEFAULT_ORGANISATION, "jar2", 1);
        p2Package2 = addExtrasProperties(p2Package2, "1.0.0");

        InstallableUnit p2Package3 = createP2Package(DEFAULT_ORGANISATION, "jar3", 2);
        p2Package3 = addExtrasProperties(p2Package3, "1.2-SNAPSHOT");

        p2Packages.add(p2Package1);
        p2Packages.add(p2Package2);
        p2Packages.add(p2Package3);

        return p2Packages;
    }

    private static InstallableUnit addExtrasProperties(InstallableUnit p2package, String revision) {
        p2package.addExtraProperty("package", p2package.getId());
        p2package.addExtraProperty("name", p2package.getId());
        p2package.addExtraProperty("number", revision);
        p2package.addExtraProperty("version", String.valueOf(p2package.getVersion().getMajor()));
        p2package.addExtraProperty("type", "jar");
        return p2package;
    }

    public static CUDFDescriptor createCUDFDescriptor() {
        CUDFDescriptor descriptor = new CUDFDescriptor();

        Preamble preamble = createPreamble();
        Binaries binaries = createBinaries(BINARY_ID_1, BINARY_ID_2, BINARY_ID_3);
        Request request = new Request();
        request.setInstall(binaries.getAllBinaries());

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

    public static Binaries createBinaries(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3) {
        return createBinaries(binaryId1, binaryId2, binaryId3, false);
    }

    public static Binaries createBinaries(BinaryId binaryId1, BinaryId binaryId2, BinaryId binaryId3, boolean installed) {
        Binaries binaries = new DefaultBinaries();
        Binary binary1 = createBinary(binaryId1, "1.0", "jar", installed);
        Binary binary2 = createBinary(binaryId2, "1.0.0", "jar", installed);
        Binary binary3 = createBinary(binaryId3, "1.2-SNAPSHOT", "jar", installed);

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

    public static Binary findBinaryByBinaryId(BinaryId binaryId, Set<Binary> binaries) {
        for (Binary binary : binaries) {
            if (binary.getBinaryId().equals(binaryId)) {
                return binary;
            }
        }
        return null;
    }
}
