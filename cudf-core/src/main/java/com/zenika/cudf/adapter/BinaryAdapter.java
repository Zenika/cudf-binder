package com.zenika.cudf.adapter;

import com.zenika.cudf.model.Binary;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public interface BinaryAdapter<T> {

    T fromCUDF(Binary binary);

    Binary toCUDF(T object);

}
