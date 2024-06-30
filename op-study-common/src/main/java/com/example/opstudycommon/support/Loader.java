package com.example.opstudycommon.support;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author xxs
 * @Date 2024/6/29 22:48
 */
public interface Loader<T> {

    /**
     * load
     * @param id
     * @return
     */
    UpdateHandler<T> loadById(Serializable id);


    UpdateHandler<T> load(Supplier<T> t);
}
