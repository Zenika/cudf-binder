package com.zenika.cudf.adapter;

import com.zenika.cudf.model.CUDFDescriptor;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public interface DescriptorAdapter<X, Y> {

    X fromCUDF(CUDFDescriptor descriptor);

    CUDFDescriptor toCUDF(Y descriptors);
}
