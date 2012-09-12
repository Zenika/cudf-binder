package com.zenika.cudf.resolver;

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

import com.zenika.cudf.adapter.P2DescriptorAdapter;
import com.zenika.cudf.model.CUDFDescriptor;
import org.eclipse.equinox.p2.cudf.metadata.InstallableUnit;
import org.eclipse.equinox.p2.cudf.solver.ProfileChangeRequest;
import org.eclipse.equinox.p2.cudf.solver.SimplePlanner;
import org.eclipse.equinox.p2.cudf.solver.SolverConfiguration;

import java.util.Collection;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class P2Resolver implements Resolver {

    private VersionResolver versionResolver;

    public P2Resolver(VersionResolver versionResolver) {
        this.versionResolver = versionResolver;
    }

    @Override
    public CUDFDescriptor resolve(CUDFDescriptor cudfDescriptor) {
        P2DescriptorAdapter p2DescriptorAdapter = new P2DescriptorAdapter(versionResolver);
        ProfileChangeRequest p2Descriptor = p2DescriptorAdapter.fromCUDF(cudfDescriptor);
        SolverConfiguration configuration = new SolverConfiguration("p2", "default", false, false);// TODO: Compute the P2 resolver options 
        SimplePlanner planner = new SimplePlanner();
        Object result = planner.getSolutionFor(p2Descriptor, configuration);
        return p2DescriptorAdapter.toCUDF((Collection<InstallableUnit>) result);
    }
}
