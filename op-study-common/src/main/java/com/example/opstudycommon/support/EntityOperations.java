package com.example.opstudycommon.support;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author xxs
 * @Date 2024/6/29 22:54
 */
public class EntityOperations {

    public static <T> EntityUpdater<T> doUpdate(BaseMapper<T> baseMapper) {
        return new EntityUpdater(baseMapper);
    }

    public static <T> EntityCreator<T> doCreate(BaseMapper<T> baseMapper) {
        return new EntityCreator(baseMapper);
    }


}
