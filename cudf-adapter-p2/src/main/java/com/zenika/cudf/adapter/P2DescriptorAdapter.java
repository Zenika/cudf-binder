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
import com.zenika.cudf.resolver.VersionResolver;
import org.eclipse.equinox.p2.cudf.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.cudf.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.cudf.metadata.InstallableUnit;
import org.eclipse.equinox.p2.cudf.metadata.ProvidedCapability;
import org.eclipse.equinox.p2.cudf.metadata.RequiredCapability;
import org.eclipse.equinox.p2.cudf.metadata.Version;
import org.eclipse.equinox.p2.cudf.metadata.VersionRange;
import org.eclipse.equinox.p2.cudf.query.QueryableArray;
import org.eclipse.equinox.p2.cudf.solver.ProfileChangeRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
//TODO: We should add the CUDF keep constraint in model
public class P2DescriptorAdapter implements DescriptorAdapter<ProfileChangeRequest, Collection<InstallableUnit>> {

    private VersionResolver versionResolver;

    public P2DescriptorAdapter(VersionResolver versionResolver) {
        this.versionResolver = versionResolver;
    }

    @Override
    public ProfileChangeRequest fromCUDF(CUDFDescriptor descriptor) {
        ProfileChangeRequest p2Descriptor = convertBinaries(descriptor.getBinaries());
        p2Descriptor = convertRequest(p2Descriptor, descriptor.getRequest());
        return p2Descriptor;
    }

    private ProfileChangeRequest convertBinaries(Binaries binaries) {
        Iterator<Binary> itBinaries = binaries.iterator();
        InstallableUnit[] p2Packages = new InstallableUnit[binaries.getAllBinaries().size()];
        List<InstallableUnit> preInstalledPackages = new ArrayList<InstallableUnit>();
        for (int i = 0; i < p2Packages.length; i++) {
            Binary binary = itBinaries.next();
            InstallableUnit p2Package = new InstallableUnit();

            p2Package.setId(convertIntoId(binary.getBinaryId()));
            p2Package.setInstalled(binary.isInstalled());
            p2Package.setVersion(new Version(binary.getBinaryId().getVersion()));
            p2Package.setRequiredCapabilities(convertBinaryDependencies(binary.getDependencies()));
            p2Package.setCapabilities(createP2Capacities(binary));
            putExtrasProperties(binary, p2Package);

            p2Packages[i] = p2Package;

            if (p2Package.isInstalled()) {
                preInstalledPackages.add(p2Package);
            }
        }

        ProfileChangeRequest p2Descriptor = new ProfileChangeRequest(new QueryableArray(p2Packages));
        p2Descriptor.setPreInstalledIUs(preInstalledPackages);

        return p2Descriptor;
    }

    private IRequiredCapability[] convertBinaryDependencies(Set<Binary> dependencies) {
        Iterator<Binary> itDependencies = dependencies.iterator();
        IRequiredCapability[] p2Dependencies = new RequiredCapability[dependencies.size()];
        for (int j = 0; j < p2Dependencies.length; j++) {
            p2Dependencies[j] = convertIntoP2RequiredPackage(itDependencies.next());
        }
        return p2Dependencies;
    }

    private IRequiredCapability convertIntoP2RequiredPackage(Binary binary) {
        return new RequiredCapability(convertIntoId(binary.getBinaryId()),
                new VersionRange(new Version(binary.getBinaryId().getVersion())), false);
    }

    private void putExtrasProperties(Binary binary, InstallableUnit p2Package) {
        p2Package.addExtraProperty("name", convertIntoId(binary.getBinaryId()));
        p2Package.addExtraProperty("package", convertIntoId(binary.getBinaryId()));
        p2Package.addExtraProperty("number", binary.getRevision());
        p2Package.addExtraProperty("type", binary.getType());
        p2Package.addExtraProperty("version", String.valueOf(binary.getBinaryId().getVersion()));
        if (!binary.getDependencies().isEmpty()) {
            p2Package.addExtraProperty("depends", convertDependenciesIntoCUDF(binary.getDependencies()));
        }
    }

    private String convertDependenciesIntoCUDF(Set<Binary> dependencies) {
        StringBuilder stringBuilder = new StringBuilder(100);
        Iterator<Binary> itBinary = dependencies.iterator();
        while (itBinary.hasNext()) {
            Binary dependency = itBinary.next();
            stringBuilder.append(convertIntoId(dependency.getBinaryId()))
                    .append(" = ").append(dependency.getBinaryId().getVersion());
            if (itBinary.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    private IProvidedCapability[] createP2Capacities(Binary binary) {
        return new IProvidedCapability[]{
                new ProvidedCapability(convertIntoId(binary.getBinaryId()),
                        new VersionRange(new Version(binary.getBinaryId().getVersion())))};
    }

    private ProfileChangeRequest convertRequest(ProfileChangeRequest p2Descriptor, Request request) {
        for (Binary binaryToInstall : request.getInstall()) {
            p2Descriptor.addInstallableUnit(convertIntoP2RequiredPackage(binaryToInstall));
        }
        for (Binary binaryToUpdate : request.getUpdate()) {
            p2Descriptor.upgradeInstallableUnit(convertIntoP2RequiredPackage(binaryToUpdate));
        }
        for (Binary binaryToRemove : request.getRemove()) {
            p2Descriptor.removeInstallableUnit(convertIntoP2RequiredPackage(binaryToRemove));
        }
        return p2Descriptor;
    }

    private String convertIntoId(BinaryId binaryId) {
        return binaryId.getOrganisation() + ParsedBinary.SEPARATOR + binaryId.getName();
    }

    @Override
    public CUDFDescriptor toCUDF(Collection<InstallableUnit> p2Packages) {
        CUDFDescriptor descriptor = new CUDFDescriptor();
        descriptor.setPreamble(Preamble.getDefaultPreamble());
        Set<Binary> binaries = new HashSet<Binary>();
        for (InstallableUnit p2Package : p2Packages) {
            BinaryId binaryId = convertIntoBinaryId(p2Package);
            Binary binary = new Binary(binaryId);
            binary.setInstalled(true);
            binary.setDependencies(convertIntoBinaryDependency(p2Package.getRequiredCapabilities()));
            if (checkExtrasProperties(p2Package, "type")) {
                binary.setType(p2Package.getExtraPropertyValue("type"));
            } else {
                binary.setType("jar");
            }
            if (checkExtrasProperties(p2Package, "number")) {
                binary.setRevision(p2Package.getExtraPropertyValue("number"));
            } else {
                binary = versionResolver.resolveFromCUDF(binary);
            }
            binaries.add(binary);
        }
        descriptor.setBinaries(new DefaultBinaries(binaries));
        // No request
        return descriptor;
    }

    private boolean checkExtrasProperties(InstallableUnit p2Package, String propertyKey) {
        return p2Package.getExtraPropertyValue(propertyKey) != null && !p2Package.getExtraPropertyValue(propertyKey).isEmpty();
    }

    private Set<Binary> convertIntoBinaryDependency(IRequiredCapability[] p2PackageDependencies) {
        Set<Binary> dependencies = new HashSet<Binary>();
        for (IRequiredCapability p2PackageDependency : p2PackageDependencies) {
            BinaryId dependencyId = convertIntoBinaryId(p2PackageDependency);
            Binary dependency = new Binary(dependencyId);
            dependency.setInstalled(true); // TODO: to be checked (Find a way to check if the dependency should be installed)
            dependency.setType("jar");
            dependency = versionResolver.resolveFromCUDF(dependency);
            dependencies.add(dependency);
        }
        return dependencies;
    }

    private BinaryId convertIntoBinaryId(InstallableUnit p2Package) {
        String[] binaryInfo = p2Package.getId().split(ParsedBinary.SEPARATOR);
        return new BinaryId(binaryInfo[1], binaryInfo[0], p2Package.getVersion().getMajor());
    }

    private BinaryId convertIntoBinaryId(IRequiredCapability p2Package) {
        String[] binaryInfo = p2Package.getName().split(ParsedBinary.SEPARATOR);
        // Take the version maximum by default 
        return new BinaryId(binaryInfo[1], binaryInfo[0], p2Package.getRange().getMaximum().getMajor());
    }
}
