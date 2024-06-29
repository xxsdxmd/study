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
 * @Date 2024/6/29 23:12
 */
@Slf4j
public class EntityCreator<T extends Aggregate<ID>, ID extends Identifier> extends BaseEntityOperation implements Create<T, ID>, UpdateHandler<T, ID>, Executor<T> {

    private final RepositorySupport<T,ID> repositorySupport;
    private T t;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = Throwable::printStackTrace;

    public EntityCreator(RepositorySupport<T, ID> repositorySupport) {
        this.repositorySupport = repositorySupport;
    }

    @Override
    public UpdateHandler<T, ID> create(Supplier<T> supplier) {
        this.t = supplier.get();
        return this;
    }

    @Override
    public Optional<T> executor() {
        doValidator(this.t, UpdateGroup.class);
        repositorySupport.save(t);
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
    public Executor<T> update(Consumer<T> consumer) {
        Preconditions.checkArgument(Objects.nonNull(t), "entity must supply");
        consumer.accept(this.t);
        return this;
    }
}
