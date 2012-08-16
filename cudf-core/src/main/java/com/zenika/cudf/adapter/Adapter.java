package com.zenika.cudf.adapter;

import com.zenika.cudf.model.CUDFDescriptor;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public interface Adapter<T> {

    T fromCUDF(CUDFDescriptor descriptor);

    CUDFDescriptor toCUDF(T descriptors);
}
