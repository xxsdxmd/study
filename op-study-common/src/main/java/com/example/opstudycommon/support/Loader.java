package com.example.opstudycommon.support;

import domain.iface.Aggregate;
import domain.iface.Identifier;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author xxs
 * @Date 2024/6/29 22:48
 */
public interface Loader<T extends Aggregate<ID>, ID extends Identifier> {

    /**
     * load
     * @param id
     * @return
     */
    UpdateHandler<T,ID> loadById(ID id);


    UpdateHandler<T,ID> load(Supplier<T> t);
}
