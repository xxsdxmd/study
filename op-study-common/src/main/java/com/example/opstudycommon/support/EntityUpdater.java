package com.example.opstudycommon.support;


import com.example.opstudycommon.validator.UpdateGroup;
import com.google.common.base.Preconditions;
import domain.iface.Aggregate;
import domain.iface.Identifier;
import domain.repository.RepositorySupport;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author xxs
 * @Date 2024/6/29 22:54
 * step builder
 */
@Slf4j
public class EntityUpdater<T extends Aggregate<ID>, ID extends Identifier> extends BaseEntityOperation implements Loader<T, ID>, UpdateHandler<T, ID>, Executor<T> {

    private final RepositorySupport<T,ID> repositorySupport;
    private T entity;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = Throwable::printStackTrace;

    public EntityUpdater(RepositorySupport<T, ID> repositorySupport) {
        this.repositorySupport = repositorySupport;
    }

    @Override
    public Optional<T> executor() {
        doValidator(this.entity, UpdateGroup.class);
        repositorySupport.save(entity);
        return Optional.empty();
    }

    @Override
    public Executor<T> successHook(Consumer<T> successHook) {
        this.successHook = successHook;
        return this;
    }

    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> errorHook) {
        this.errorHook = errorHook;
        return this;
    }

    @Override
    public UpdateHandler<T,ID> loadById(ID id) {
        Preconditions.checkArgument(Objects.nonNull(id), "id is null");
        this.entity = repositorySupport.find(id);
        return this;
    }

    @Override
    public UpdateHandler<T,ID> load(Supplier<T> t) {
        this.entity = t.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Preconditions.checkArgument(Objects.nonNull(entity), "entity is null");
        consumer.accept(this.entity);
        return this;
    }
}
