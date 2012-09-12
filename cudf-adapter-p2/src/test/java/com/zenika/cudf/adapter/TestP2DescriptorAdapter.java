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
import com.zenika.cudf.model.CUDFDescriptor;
import com.zenika.cudf.model.Preamble;
import org.eclipse.equinox.p2.cudf.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.cudf.metadata.InstallableUnit;
import org.eclipse.equinox.p2.cudf.query.QueryableArray;
import org.eclipse.equinox.p2.cudf.solver.ProfileChangeRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zenika.cudf.adapter.TestP2DescriptorUtils.BINARY_ID_1;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.BINARY_ID_2;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.BINARY_ID_3;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.createBinaries;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.createCUDFDescriptor;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.createInstallableUnit;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.createInstallableUnitWithExtrasProperties;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.createP2Descriptor;
import static com.zenika.cudf.adapter.TestP2DescriptorUtils.findBinaryByBinaryId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class TestP2DescriptorAdapter {

    private P2DescriptorAdapter p2DescriptorAdapter;
    private VersionResolverMock versionResolver;

    @Before
    public void setUp() {
        versionResolver = new VersionResolverMock();
        p2DescriptorAdapter = new P2DescriptorAdapter(versionResolver);
    }

    @Test
    public void testFromCUDF() throws Exception {
        CUDFDescriptor descriptor = createCUDFDescriptor();

        ProfileChangeRequest actualP2Descriptor = p2DescriptorAdapter.fromCUDF(descriptor);
        ProfileChangeRequest expectedP2Descriptor = createP2Descriptor();

        assertEquals(expectedP2Descriptor.getInitialState().getSize(), actualP2Descriptor.getInitialState().getSize());

        assertContainsP2Package("com.zenika%3ajar1", 1, actualP2Descriptor.getInitialState());
        assertContainsP2Package("com.zenika%3ajar2", 1, actualP2Descriptor.getInitialState());
        assertContainsP2Package("com.zenika%3ajar3", 2, actualP2Descriptor.getInitialState());

        assertDependencies(expectDependencies("com.zenika%3ajar2", 1, "com.zenika%3ajar3", 2),
                inP2Package("com.zenika%3ajar1", 1, actualP2Descriptor.getInitialState()));

        assertSameP2PackagesToInstall(expectedP2Descriptor.getAllRequests(), actualP2Descriptor.getAllRequests());
    }

    @Test
    public void testToCUDF() throws Exception {
        Collection<InstallableUnit> p2Packages = createInstallableUnit();

        CUDFDescriptor actualDescriptor = p2DescriptorAdapter.toCUDF(p2Packages);
        assertEquals(Preamble.getDefaultPreamble(), actualDescriptor.getPreamble());
        assertSameBinaries(getExpectedBinaries(), actualDescriptor.getBinaries());

        assertDependencies(
                expectDependencies(
                        findBinaryByBinaryId(BINARY_ID_2, getExpectedBinaries().getAllBinaries()),
                        findBinaryByBinaryId(BINARY_ID_3, getExpectedBinaries().getAllBinaries())),
                in(findBinaryByBinaryId(BINARY_ID_1, actualDescriptor.getBinaries().getAllBinaries())));

        assertNull(actualDescriptor.getRequest()); // P2 packages come from resolution, so we don't need request.
    }

    @Test
    public void testToCUDFWithExtrasProperties() {
        Collection<InstallableUnit> p2Packages = createInstallableUnitWithExtrasProperties();

        CUDFDescriptor actualDescriptor = p2DescriptorAdapter.toCUDF(p2Packages);

        assertSameBinaries(getExpectedBinaries(), actualDescriptor.getBinaries());
    }

    private void assertDependencies(Binary[] expectedDependencies, Binary actualBinary) {
        for (Binary actualDependency : actualBinary.getDependencies()) {
            assertContainsBinary(actualDependency, in(expectedDependencies));
        }
    }

    private void assertContainsBinary(Binary actualBinary, Binary[] expectedBinaries) {
        for (Binary expectedBinary : expectedBinaries) {
            if (actualBinary.equals(expectedBinary)) {
                return;
            }
        }
        throw new AssertionError("Unable to find this actual binary: " + actualBinary + System.lineSeparator() + "in expected binaries: " + Arrays.toString(expectedBinaries));
    }

    private Binary[] expectDependencies(Binary... binaries) {
        return binaries;
    }

    private void assertSameBinaries(Binaries expectedBinaries, Binaries actualBinaries) {
        for (Binary actualBinary : actualBinaries) {
            assertContainsBinary(actualBinary, in(expectedBinaries));
        }
    }

    private void assertContainsBinary(Binary actualBinary, Binaries expectedBinaries) {
        assertTrue(expectedBinaries.getAllBinaries().contains(actualBinary));
    }

    private Binaries getExpectedBinaries() {
        return createBinaries(BINARY_ID_1, BINARY_ID_2, BINARY_ID_3, true);
    }

    private void assertContainsP2Package(String name, int version, QueryableArray p2packages) {
        if (findP2Package(name, version, p2packages) == null) {
            throw new AssertionError("Unable to find P2 package: " + name + "\nin list: " + p2packages.getList());
        }
    }

    private InstallableUnit findP2Package(String name, int version, QueryableArray p2packages) {
        for (Object oPackage : p2packages.getList()) {
            InstallableUnit p2package = (InstallableUnit) oPackage;
            if (p2package.getId().equals(name) && p2package.getVersion().getMajor() == version) {
                return p2package;
            }
        }
        return null;
    }

    private void assertDependencies(Map<String, Integer> expect, InstallableUnit installableUnit) {
        installableUnit.getRequiredCapabilities();
        for (Map.Entry<String, Integer> dependency : expect.entrySet()) {
            assertContainsP2Dependency(dependency, installableUnit.getRequiredCapabilities());
        }
    }

    private void assertContainsP2Dependency(Map.Entry<String, Integer> expectedDependency, IRequiredCapability[] actualDependencies) {
        for (IRequiredCapability actualDependency : actualDependencies) {
            // TODO: Find best way to compare versions (maybe use the P2 comparator)
            if (actualDependency.getName().equals(expectedDependency.getKey()) &&
                    actualDependency.getRange().getMaximum().getMajor() == expectedDependency.getValue()) {
                return;
            }
        }
        throw new AssertionError("Unable to find this expected dependency: " + expectedDependency.getKey() +
                " with version: " + expectedDependency.getValue() + System.lineSeparator() + "in actual dependencies: "
                + Arrays.toString(actualDependencies));
    }

    private Map<String, Integer> expectDependencies(String dependency1, int dependencyVersion1, String dependency2, int dependencyVersion2) {
        Map<String, Integer> p2Dependencies = new HashMap<String, Integer>();
        p2Dependencies.put(dependency1, dependencyVersion1);
        p2Dependencies.put(dependency2, dependencyVersion2);
        return p2Dependencies;
    }

    private InstallableUnit inP2Package(String name, int version, QueryableArray p2Package) {
        return findP2Package(name, version, p2Package);
    }

    private void assertSameP2PackagesToInstall(ArrayList expectedP2PackagesToInstall, ArrayList actualP2PackageToInstall) {
        for (Object oActualP2Package : expectedP2PackagesToInstall) {
            IRequiredCapability actualP2Package = (IRequiredCapability) oActualP2Package;
            assertContainsP2Package(actualP2Package, in(expectedP2PackagesToInstall));
        }
    }

    private void assertContainsP2Package(IRequiredCapability actualP2Package, List expectedP2PackagesList) {
        for (Object oExpectedP2Package : expectedP2PackagesList) {
            IRequiredCapability expectedP2Package = (IRequiredCapability) oExpectedP2Package;
            if (actualP2Package.getName().equals(expectedP2Package.getName()) &&
                    actualP2Package.getRange().getMaximum().equals(expectedP2Package.getRange().getMaximum())) {
                return;
            }
        }
        throw new AssertionError("Unable to find this actual package: " + actualP2Package.getName() + " with version: "
                + actualP2Package.getRange().getMaximum().getMajor() + System.lineSeparator() + "in expected package: "
                + expectedP2PackagesList);
    }

    private <T> T in(T t) {
        return t;
    }
}
