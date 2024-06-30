package com.example.opstudycommon.support;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.opstudycommon.validator.UpdateGroup;
import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
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
public class EntityUpdater<T> extends BaseEntityOperation implements Loader<T>, UpdateHandler<T>, Executor<T> {

    private final BaseMapper<T> baseMapper;
    private T entity;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = Throwable::printStackTrace;

    public EntityUpdater(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public Optional<T> executor() {
        doValidator(this.entity, UpdateGroup.class);
        T save = Try.of(() -> {
            baseMapper.updateById(entity);
            return this.entity;
        }).onSuccess(successHook).onFailure(errorHook).getOrNull();
        return Optional.ofNullable(save);
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
    public UpdateHandler<T> loadById(Serializable id) {
        Preconditions.checkArgument(Objects.nonNull(id), "id is null");
        T t = baseMapper.selectById(id);
        if(Objects.isNull(t)){
            throw new RuntimeException("id is null");
        }else {
            this.entity = t;
        }
        return this;
    }

    @Override
    public UpdateHandler<T> load(Supplier<T> t) {
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
