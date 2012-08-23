package com.zenika.cudf.adapter;

import com.zenika.cudf.adapter.resolver.CUDFVersionResolver;
import com.zenika.cudf.model.Binary;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public class VersionResolverMock implements CUDFVersionResolver {

    private int numberOfCall = 0;
    private boolean called = false;

    @Override
    public Binary resolve(Binary binary) {
        called = true;
        numberOfCall++;
        return binary;
    }

    public boolean isCalled() {
        return called;
    }

    public int getNumberOfCall() {
        return numberOfCall;
    }
}
