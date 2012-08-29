package com.zenika.cudf.adapter.cache;

/**
 * @author Antoine Rouaze <antoine.rouaze@zenika.com>
 */
public interface Cache {

    void put(Object key, Object value);

    Object get(Object key);

}
