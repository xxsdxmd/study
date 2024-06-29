package com.example.opstudycommon.support;

import domain.iface.Aggregate;
import domain.iface.Identifier;
import domain.repository.RepositorySupport;

/**
 * @author xxs
 * @Date 2024/6/29 22:54
 */
public class EntityOperations {

    public static <T extends Aggregate<ID>, ID extends Identifier> EntityUpdater<T,ID> doUpdate(RepositorySupport<T, ID> repositorySupport) {
        return new EntityUpdater(repositorySupport);
    }

    public static <T extends Aggregate<ID>, ID extends Identifier> EntityCreator<T, ID> doCreate(RepositorySupport<T, ID> repositorySupport) {
        return new EntityCreator(repositorySupport);
    }


}
