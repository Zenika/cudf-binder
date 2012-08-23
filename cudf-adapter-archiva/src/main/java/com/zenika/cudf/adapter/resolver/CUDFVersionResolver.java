package com.zenika.cudf.adapter.resolver;

import com.zenika.cudf.model.Binary;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
//TODO: Maybe should be in core ?
public interface CUDFVersionResolver {

    Binary resolve(Binary binary);
}
